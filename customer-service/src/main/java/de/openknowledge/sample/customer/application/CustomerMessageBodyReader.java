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

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

/**
 * Message body reader that transforms media type 'application/vnd.de.openknowledge.sample.customer.v1+json'
 * to an entity of {@link CustomerResourceType}.
 */
@Provider
@RequestScoped
@Consumes(CustomMediaType.CUSTOMER_V1)
public class CustomerMessageBodyReader implements MessageBodyReader<CustomerResourceType> {

    @Context
    private Providers providers;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == CustomerResourceType.class;
    }

    @Override
    public CustomerResourceType readFrom(
            Class<CustomerResourceType> type,
            Type genericType,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream
    ) throws IOException, WebApplicationException {

        MessageBodyReader<CustomerResourceType> jsonReader = providers.getMessageBodyReader(
                CustomerResourceType.class,
                CustomerResourceType.class,
                annotations,
                MediaType.APPLICATION_JSON_TYPE);

        return jsonReader.readFrom(
                CustomerResourceType.class,
                CustomerResourceType.class,
                annotations,
                MediaType.APPLICATION_JSON_TYPE,
                httpHeaders,
                entityStream
        );
    }
}
