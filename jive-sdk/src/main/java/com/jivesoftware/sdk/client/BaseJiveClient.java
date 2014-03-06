/*
 *
 *  * Copyright 2013 Jive Software
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package com.jivesoftware.sdk.client;

import com.google.common.collect.Maps;
import com.jivesoftware.sdk.client.filter.DebugClientResponseFilter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.HttpHeaders;
import java.util.Map;

/**
 * Created by rrutan on 2/13/14.
 */
public class BaseJiveClient {
    private static final Logger log = LoggerFactory.getLogger(BaseJiveClient.class);

    enum HttpMethods {
        GET, POST, UPDATE, DELETE, PATCH, PUT, HEAD;
    } // end HttpMethods

    public static final String HEADER_JIVE_RUN_AS = "X-Jive-Run-As";

    protected Client buildClient() {
        Client client = ClientBuilder.newClient();

        if (log.isDebugEnabled()) {
            client.register(DebugClientResponseFilter.class);
        } // end if

        client.register(JacksonFeature.class);

        return client;
    } // end buildClient

    protected AsyncInvoker getAsyncInvoker(WebTarget target, String requestContentType,
                              JiveAuthorizationSupport authorizationSupport, JiveRunAs runAs) {

        if (target == null) {
            return null;
        } // end if

        Invocation.Builder builder = target.request(requestContentType);
        builder.header(HttpHeaders.AUTHORIZATION, authorizationSupport.getAuthorizationHeader());

        if (runAs != null) {
            if (log.isDebugEnabled()) { log.trace("Adding "+HEADER_JIVE_RUN_AS+" to Request ..."); }
            builder.header(HEADER_JIVE_RUN_AS,runAs.getKey());
        } // end if

        return builder.async();
    } // end initTarget

    class DataBlock {
        @JsonProperty("data")
        private Object data;
        @JsonProperty("message")
        private Map<String,Object> message;
        @JsonProperty("status")
        private Map<String,Object> status;

        DataBlock() {
            this.message = Maps.newHashMap();
            this.status = Maps.newHashMap();
        }

        DataBlock(Object data) {
            this();
            this.data = data;
        } // end constructor

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Map<String, Object> getMessage() {
            return message;
        }

        public void setMessage(Map<String, Object> message) {
            this.message = message;
        }

        public Map<String, Object> getStatus() {
            return status;
        }

        public void setStatus(Map<String, Object> status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "DataBlock{" +
                    "data=" + data +
                    ", message=" + message +
                    ", status=" + status +
                    '}';
        }
    } // end class



} // end class