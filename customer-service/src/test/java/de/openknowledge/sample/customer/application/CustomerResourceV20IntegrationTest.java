/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package de.openknowledge.sample.customer.application;

import static de.openknowledge.sample.customer.application.CustomMediaType.CUSTOMER_V2;
import static javax.ws.rs.client.Entity.entity;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.johnzon.jaxrs.jsonb.jaxrs.JsonbJaxrsProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.openknowledge.sample.customer.application.v1.CustomerResourceTypeV1;
import de.openknowledge.sample.customer.domain.Customer;
import de.openknowledge.sample.customer.domain.CustomerNotFoundException;
import de.openknowledge.sample.customer.domain.CustomerRepository;
import de.openknowledge.sample.customer.domain.Gender;
import de.openknowledge.sample.customer.domain.Name;
import de.openknowledge.sample.customer.domain.TestCustomers;

/**
 * Arquillian test class for the rest resource {@link CustomerResource}.
 */
@RunAsClient
@RunWith(Arquillian.class)
public class CustomerResourceV20IntegrationTest {

    private static final Logger LOG = Logger.getLogger(CustomerResourceV20IntegrationTest.class.getName());

    @Deployment
    public static WebArchive createDeployment() {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(pomFile.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile())
                .addPackage(CustomerResourceTypeV1.class.getPackage())
                .addClasses(CustomerResource.class, CustomerResourceType.class, CustomMediaType.class, JaxRsActivator.class)
                .addClasses(CustomerMessageBodyReader.class, CustomerMessageBodyWriter.class, CustomerListMessageBodyWriter.class)
                .addClasses(Customer.class, Name.class, CustomerRepository.class, CustomerNotFoundException.class, Gender.class)
                .addClass(TestCustomers.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        LOG.log(Level.FINE, () -> archive.toString(true));
        return archive;
    }

    @ArquillianResource
    private URL baseURI;
    private WebTarget customerListTarget;

    @Before
    public void initializeClient() {
        customerListTarget = ClientBuilder.newClient()
                .register(JsonbJaxrsProvider.class)
                .target(baseURI.toString())
                .path("api/customers");
    }
    
    @Test
    public void createCustomer() throws Exception {
        Response response = customerListTarget
                .request(CUSTOMER_V2)
                .post(entity(getClass().getResourceAsStream("customer_v2_0.json"), CUSTOMER_V2));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.CREATED);
        assertThat(response.getLocation()).isEqualTo(customerListTarget.path(Long.toString(2)).getUri());
    }

    @Test
    public void deleteCustomerV1() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(1))
                .request(CUSTOMER_V2)
                .delete();

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NO_CONTENT);
    }

    @Test
    public void deleteCustomerV1ShouldFailUnknownCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(-1))
                .request(CUSTOMER_V2)
                .delete();

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NOT_FOUND);
    }

    @Test
    public void getCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(1))
                .request(CUSTOMER_V2)
                .get();

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.OK);

        JsonObject customer = parse(response.readEntity(String.class));
        assertThat(customer).isNotNull();
        assertThat(customer).containsAllEntriesOf(parse(getClass().getResourceAsStream("customer_v2_0.json")));
    }

    @Test
    public void getCustomerV1ShouldFailForUnknownCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(-1))
                .request(CUSTOMER_V2)
                .get(Response.class);

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NOT_FOUND);
    }

    @Test
    public void getCustomers() throws Exception {
        Response response = customerListTarget
                .request(CUSTOMER_V2)
                .get(Response.class);

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.OK);

        List<JsonValue> customers = Json.createReader(new StringReader(response.readEntity(String.class))).readArray();
        
        assertThat(customers).hasSize(1);
        assertThat(customers.get(0)).isInstanceOf(JsonObject.class);
        JsonObject customer = (JsonObject)customers.get(0);
        assertThat(customer).containsAllEntriesOf(parse(getClass().getResourceAsStream("customer_v2_0.json")));
    }

    @Test
    public void updateCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(1))
                .request(CUSTOMER_V2)
                .put(entity(getClass().getResourceAsStream("customer_v2_0.json"), CUSTOMER_V2));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NO_CONTENT);
    }

    @Test
    public void updateCustomerShouldFailForUnknownCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(-1))
                .request(CUSTOMER_V2)
                .put(entity(getClass().getResourceAsStream("customer_v2_0.json"), CUSTOMER_V2));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NOT_FOUND);
    }

    private JsonObject parse(String json) {
        return Json.createReader(new StringReader(json)).readObject();
    }

    private JsonObject parse(InputStream json) {
        return Json.createReader(json).readObject();
    }
}
