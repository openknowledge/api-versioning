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
package de.openknowledge.sample.infrastructure.openapi;

import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.media.Content;
import org.eclipse.microprofile.openapi.models.parameters.RequestBody;
import org.eclipse.microprofile.openapi.models.responses.APIResponse;

import de.openknowledge.sample.customer.application.CustomMediaType;

public class OpenApiFilter implements OASFilter {

    @Override
    public RequestBody filterRequestBody(RequestBody requestBody) {
        requestBody.setContent(filterContent(requestBody.getContent()));
        return requestBody;
    }

    @Override
    public APIResponse filterAPIResponse(APIResponse apiResponse) {
        apiResponse.setContent(filterContent(apiResponse.getContent()));
        return apiResponse;
    }

    public Content filterContent(Content content) {
        content.removeMediaType(MediaType.APPLICATION_JSON);
        content.removeMediaType(CustomMediaType.CUSTOMER_V1);
        return content;
    }
}
