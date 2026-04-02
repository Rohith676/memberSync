package com.aegis.membersync.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class HttpUtil {

    public static String get(String url, String username, String password) throws Exception {

        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP Error: " + response.statusCode());
        }

        return response.body();
    }
    
    public static String put(String urlStr, String body, String username, String password) throws Exception {

        java.net.URL url = new java.net.URL(urlStr);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // 🔐 Basic Auth
        String auth = username + ":" + password;
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

        // 📤 Send body
        try (java.io.OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();

        java.io.InputStream is;
        if (responseCode >= 200 && responseCode < 300) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }

        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        System.out.println("HTTP Response Code: " + responseCode);

        return response.toString();
    }
}