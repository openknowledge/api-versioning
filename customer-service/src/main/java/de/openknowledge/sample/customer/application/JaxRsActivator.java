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

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

/**
 * JAX-RS Activator
 */
@ApplicationPath("api")
@ApplicationScoped
@OpenAPIDefinition(info = @Info(
        title = "Customer Service",
        contact = @Contact(
                name = "open knowledge GmbH",
                email = "info@openknowledge.de",
                url = "http://www.openknowledge.de"),
        license = @License(
                name = "Apache License, Version 2.0",
                url = "http://www.apache.org/licenses/LICENSE-2.0"),
        version = "2",
        description = "A customer service"),
        servers = @Server(url = "http://localhost:8080/api"),
    components = @Components(requestBodies = @RequestBody(name = "Customer", content = @Content(schema = @Schema(implementation = CustomerResourceType.class)))))
public class JaxRsActivator extends Application {

}
