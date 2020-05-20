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

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.openknowledge.sample.customer.domain.Customer;
import de.openknowledge.sample.customer.domain.Gender;

/**
 * An DTO that represents a {@link Customer}.
 */
@Schema(name = "Customer")
public class CustomerResourceType {

    @Schema(readOnly = true)
    private Long id;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private Gender gender;

    public CustomerResourceType() {
    }

    public CustomerResourceType(Customer customer) {
        this.id = customer.getId();
        this.firstName = customer.getName().getFirstName();
        this.lastName = customer.getName().getLastName();
        this.emailAddress = customer.getEmailAddress();
        this.gender = customer.getGender();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    @Override
    public String toString() {
        return "CustomerResourceType{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", emailAddress='" + emailAddress + '\'' +
                ", gender=" + gender +
                '}';
    }
}
