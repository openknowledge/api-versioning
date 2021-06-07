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

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
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
    public void updateCustomerWithoutEtagFails() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(1))
                .request(APPLICATION_JSON)
                .put(entity(getClass().getResourceAsStream("customer_v1_0.json"), APPLICATION_JSON));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.PRECONDITION_REQUIRED);
    }

    @Test
    public void updateCustomerWithWrongEtagFails() throws Exception {
        Response response = customerListTarget
                .path(Long.toString(1))
                .request(APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, "wrong")
                .put(entity(getClass().getResourceAsStream("customer_v1_0.json"), APPLICATION_JSON));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.PRECONDITION_FAILED);
    }

    @Test
    public void updateCustomerWithCorrectEtag() throws Exception {
        Response oldCustomer = customerListTarget
                .path(Long.toString(1))
                .request(APPLICATION_JSON)
                .get();
        EntityTag oldCustomerTag = oldCustomer.getEntityTag();

        Response response = customerListTarget
                .path(Long.toString(1))
                .request(APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, oldCustomerTag)
                .put(entity(getClass().getResourceAsStream("customer_v1_0.json"), APPLICATION_JSON));

        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Status.NO_CONTENT);
    }
}
