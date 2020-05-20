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
package de.openknowledge.sample.customer.domain;

import static de.openknowledge.sample.customer.domain.Gender.MALE;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Test data builder for the entity {@link Customer}.
 */
@ApplicationScoped
public class TestCustomers {

    @Inject
    private CustomerRepository repository;

    public static Customer newDefaultCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName(new Name("Max", "Mustermann"));
        customer.setEmailAddress("max.mustermann@openknowledge.de");
        customer.setGender(MALE);
        return customer;
    }

    public void initCustomer(@Observes @Initialized(RequestScoped.class) Object event) {
        for (Customer customer: repository.findAll()) {
            repository.delete(customer);
        }
        repository.create(newDefaultCustomer());
    }
}
