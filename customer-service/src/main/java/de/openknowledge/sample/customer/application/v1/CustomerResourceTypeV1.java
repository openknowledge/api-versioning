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
package de.openknowledge.sample.customer.application.v1;

import java.util.Optional;

import de.openknowledge.sample.customer.application.CustomerResourceType;
import de.openknowledge.sample.customer.domain.Customer;
import de.openknowledge.sample.customer.domain.Gender;
import de.openknowledge.sample.customer.domain.Name;

/**
 * An DTO that represents a {@link Customer}.
 */
public class CustomerResourceTypeV1 {

    private Long id;

    private Name name;

    private String emailAddress;

    private Gender gender;

    public CustomerResourceTypeV1() {
    }

    public CustomerResourceTypeV1(CustomerResourceType customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.emailAddress = customer.getEmailAddress();
        this.gender = customer.getGender();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return Optional.ofNullable(name).map(Name::getFirstName).orElse(null);
    }

    public void setFirstName(String firstName) {
        (name = Optional.ofNullable(name).orElse(new Name())).setFirstName(firstName);
    }

    public String getLastName() {
        return Optional.ofNullable(name).map(Name::getLastName).orElse(null);
    }

    public void setLastName(String lastName) {
        (name = Optional.ofNullable(name).orElse(new Name())).setLastName(lastName);
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public CustomerResourceType toV2() {
        return new CustomerResourceType(id, name, emailAddress, gender);
    }

    @Override
    public String toString() {
        return "CustomerResourceType{" +
                "id=" + id +
                ", name=" + name +
                ", emailAddress='" + emailAddress + '\'' +
                ", gender=" + gender +
                '}';
    }
}
