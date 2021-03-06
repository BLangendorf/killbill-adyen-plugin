/*
 * Copyright 2014 Groupon, Inc
 *
 * Groupon licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.plugin.adyen.client.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public enum PaymentServiceProviderResult {

    INITIALISED("Initialised"), // be careful with this state, it's only here to enable orders from OfflineFundsTransfer to be able to expire in every case after 7 days
    AUTHORISED("Authorised"),
    REDIRECT_SHOPPER("RedirectShopper"), // authorize return code when using 3D-Secure
    RECEIVED(new String[]{"Received", "Pending", "[capture-received]", "[cancel-received]", "[cancelOrRefund-received]", "[refund-received]", "[all-details-successfully-disabled]", "[detail-successfully-disabled]"}), // direct debit, ideal payment response
    REFUSED("Refused"),
    PENDING("Pending"),
    ERROR(new String[]{"Error", "[error]"}),
    CANCELLED("Cancelled");

    private static final Map<String, PaymentServiceProviderResult> REVERSE_LOOKUP = new HashMap<String, PaymentServiceProviderResult>();

    static {
        for (final PaymentServiceProviderResult providerResult : PaymentServiceProviderResult.values()) {
            for (final String response : providerResult.getResponses()) {
                REVERSE_LOOKUP.put(response, providerResult);
            }
        }
    }

    private final String[] responses;

    private PaymentServiceProviderResult(final String response) {
        this(new String[]{response});
    }

    private PaymentServiceProviderResult(final String[] responses) {
        this.responses = responses;
    }

    public static PaymentServiceProviderResult getPaymentResultForId(@Nullable final String id) {
        if (id == null) {
            return ERROR;
        }

        final PaymentServiceProviderResult result = REVERSE_LOOKUP.get(id);
        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("Unknown PaymentResultType id: " + id);
        }
    }

    public String[] getResponses() {
        return responses;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.responses);
    }
}
