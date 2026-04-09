package com.aegis.membersync.util;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.util.Base64;

public class HttpUtil {

    public static String get(String url, String username, String password) throws Exception {

        // Create HTTP client
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(url);

            // 🔐 Basic Auth
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            request.setHeader("Authorization", "Basic " + encodedAuth);

            request.setHeader("Accept", "application/json");

            // ⏱ Timeout (optional but recommended)
            request.setConfig(
            	    org.apache.hc.client5.http.config.RequestConfig.custom()
            	        .setConnectTimeout(Timeout.ofSeconds(30))
            	        .setResponseTimeout(Timeout.ofSeconds(30))
            	        .build()
            	);

            // Execute
            try (CloseableHttpResponse response = client.execute(request)) {

                int statusCode = response.getCode();

                if (statusCode == 200) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new RuntimeException("HTTP GET failed with status: " + statusCode);
                }
            }
        }
    }
}