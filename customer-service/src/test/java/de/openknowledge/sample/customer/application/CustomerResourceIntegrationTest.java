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

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.johnzon.jaxrs.jsonb.jaxrs.JsonbJaxrsProvider;
import org.apache.meecrowave.Meecrowave;
import org.apache.meecrowave.junit5.MonoMeecrowaveConfig;
import org.apache.meecrowave.testing.ConfigurationInject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test class for the rest resource {@link CustomerResource}.
 */
@MonoMeecrowaveConfig
public class CustomerResourceIntegrationTest {

    @ConfigurationInject
    private Meecrowave.Builder config;
    private WebTarget customerListTarget;

    @BeforeEach
    public void initializeClient() {
        customerListTarget = ClientBuilder.newClient()
                .register(JsonbJaxrsProvider.class)
                .target("http://localhost:" + config.getHttpPort())
                .path("api/customers");
    }
    
    @Test
    public void createCustomer() throws Exception {
        Response response = customerListTarget
                .request(APPLICATION_JSON)
                .post(entity(getClass().getResourceAsStream("customer_v1_0.json"), APPLICATION_JSON));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.CREATED);
        assertThat(response.getLocation()).isEqualTo(customerListTarget.path(Long.toString(2)).getUri());
    }

    @Test
    public void deleteCustomerV1() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(1))
                .request(APPLICATION_JSON)
                .delete();

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NO_CONTENT);
    }

    @Test
    public void deleteCustomerV1ShouldFailUnknownCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(-1))
                .request(APPLICATION_JSON)
                .delete();

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NOT_FOUND);
    }

    @Test
    public void getCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(1))
                .request(APPLICATION_JSON)
                .get();

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.OK);

        JsonObject customer = parse(response.readEntity(String.class));
        assertThat(customer).isNotNull();
        assertThat(customer).containsAllEntriesOf(parse(getClass().getResourceAsStream("customer_v1_0.json")));
    }

    @Test
    public void getCustomerV1ShouldFailForUnknownCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(-1))
                .request(APPLICATION_JSON)
                .get(Response.class);

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NOT_FOUND);
    }

    @Test
    public void getCustomers() throws Exception {
        Response response = customerListTarget
                .request(APPLICATION_JSON)
                .get(Response.class);

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.OK);

        List<JsonValue> customers = Json.createReader(new StringReader(response.readEntity(String.class))).readArray();
        
        assertThat(customers).hasSize(1);
        assertThat(customers.get(0)).isInstanceOf(JsonObject.class);
        JsonObject customer = (JsonObject)customers.get(0);
        assertThat(customer).containsAllEntriesOf(parse(getClass().getResourceAsStream("customer_v1_0.json")));
    }

    @Test
    public void updateCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(1))
                .request(APPLICATION_JSON)
                .put(entity(getClass().getResourceAsStream("customer_v1_0.json"), APPLICATION_JSON));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NO_CONTENT);
    }

    @Test
    public void updateCustomerShouldFailForUnknownCustomer() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(-1))
                .request(APPLICATION_JSON)
                .put(entity(getClass().getResourceAsStream("customer_v1_0.json"), APPLICATION_JSON));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NOT_FOUND);
    }

    private JsonObject parse(String json) {
        return Json.createReader(new StringReader(json)).readObject();
    }

    private JsonObject parse(InputStream json) {
        return Json.createReader(json).readObject();
    }
}
