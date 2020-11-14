package org.pavlos.restapispec.client;

import com.google.gson.Gson;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RestAPIClient {

        public static final String BASE_URL = "/observatory/api";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String URL_ENCODED = "application/x-www-form-urlencoded";

    private static final TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            }
    };

    static {
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
    }

    private final String urlPrefix;
    private final HttpClient client;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public RestAPIClient(String host, int port) throws RuntimeException {

        try {
            this.client = newHttpClient();
        }
        catch(NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e.getMessage());
        }

        this.urlPrefix = "https://" + host + ":" + port + BASE_URL;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String queryParamsToString(Map<String, List<String>> queryParams) {

        StringBuilder queryParamString = new StringBuilder();

        boolean first = true;

        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            if (first) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    queryParamString.append("?").append(entry.getKey()).append("=").append(entry.getValue().get(i));
                }
                first = false;
            } else {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    queryParamString.append("&").append(entry.getKey()).append("=").append(entry.getValue().get(i));
                }
            }
        }

        return queryParamString.toString();
    }

    // Methods for each Endpoint:

    // /login: login endpoint

    // POST
    public Map<String, Object> post_to_login(String input) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "/login", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                ClientHelper::parseJsonObject
        );
    }

    public Map<String, Object> post_to_login_with_headers(String input, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "/login", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                ClientHelper::parseJsonObject
        );
    }

    public Map<String, Object> post_to_login_with_queryParams(String input, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "/login", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "/login" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
    }

    public Map<String, Object> post_to_login_with_headers_and_queryParams(String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "/login", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "/login" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
    }

    // /logout: logout endpoint

    // POST
    public Map<String, Object> post_to_logout(String input) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "/logout", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                ClientHelper::parseJsonObject
        );
    }

    public Map<String, Object> post_to_logout_with_headers(String input, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "/logout", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                ClientHelper::parseJsonObject
        );
    }

    public Map<String, Object> post_to_logout_with_queryParams(String input, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "/logout", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "/logout" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
    }

    public Map<String, Object> post_to_logout_with_headers_and_queryParams(String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "/logout", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "/logout" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
    }

    // /products: endpoint for products with attribute

    // GET
    public Map<String, Object> get_products_by_id(String id) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "/products/" + id),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> get_products_by_id_with_headers(String id, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "/products/" + id, headers),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> get_products_by_id_with_queryParams(String id, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "/products/" + id),
                    ClientHelper::parseJsonObject
            );
        }
        else {
            String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "/products/" + id + queryParamString),
                    ClientHelper::parseJsonObject
            );
        }
    }
    public Map<String, Object> get_products_by_id_with_headers_and_queryParams(String id, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "/products/" + id, headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
            String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "/products/" + id + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
    }


    // PUT
    public Map<String, Object> put_to_products_by_id(String id, String input) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPutRequest(urlPrefix + "/products/" + id, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                ClientHelper::parseJsonObject
        );
    }

    public Map<String, Object> put_to_products_by_id_with_headers(String id, String input, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPutRequest(urlPrefix + "/products/" + id, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                ClientHelper::parseJsonObject
        );
    }

    public Map<String, Object> put_to_products_by_id_with_queryParams(String id, String input, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "/products/" + id, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "/products/" + id + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
    }

    public Map<String, Object> put_to_products_by_id_with_headers_and_queryParams(String id, String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "/products/" + id, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "/products/" + id + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
    }


    // PATCH
    public Map<String, Object> patch_to_products_by_id(String id, String input) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPatchRequest(urlPrefix + "/products/" + id, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                ClientHelper::parseJsonObject
        );
    }

    public Map<String, Object> patch_to_products_by_id_with_headers(String id, String input, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPatchRequest(urlPrefix + "/products/" + id, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                ClientHelper::parseJsonObject
        );
    }

    public Map<String, Object> patch_to_products_by_id_with_queryParams(String id, String input, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "/products/" + id, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "/products/" + id + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
    }

    public Map<String, Object> patch_to_products_by_id_with_headers_and_queryParams(String id, String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "/products/" + id, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "/products/" + id + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
    }

    // DELETE
    public Map<String, Object> delete_from_products_by_id(String id) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newDeleteRequest(urlPrefix + "/products/" + id),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> delete_from_products_by_id_with_headers(String id, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newDeleteRequest(urlPrefix + "/products/" + id, headers),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> delete_from_products_by_id_with_queryParams(String id, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "/products/" + id),
                    ClientHelper::parseJsonObject
            );
        }
        else {
            String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "/products/" + id + queryParamString),
                    ClientHelper::parseJsonObject
            );
        }
    }
    public Map<String, Object> delete_from_products_by_id_with_headers_and_queryParams(String id, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "/products/" + id, headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
            String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "/products/" + id + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
    }
    // /products: endpoint for products without attribute

    // GET
    public String get_products() {

        return sendRequestAndParseResponseBodyAsString(
                () -> newGetRequest(urlPrefix + "/products"));
    }
    public String get_products_with_headers(Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsString(
                () -> newGetRequest(urlPrefix + "/products", headers));
    }
    public String get_products_with_queryParams(Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "/products"));
        }
        else {
            String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "/products" + queryParamString));
        }
    }
    public String get_products_with_headers_and_queryParams(Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "/products", headers));
        }
        else {
            String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "/products" + queryParamString, headers));
        }
    }


    // POST
    public Integer post_to_products(String input) {

        return sendRequestAndParseResponseBodyAsInteger(
                () -> newPostRequest(urlPrefix + "/products", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
    }

    public Integer post_to_products_with_headers(String input, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsInteger(
                () -> newPostRequest(urlPrefix + "/products", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
    }

    public Integer post_to_products_with_queryParams(String input, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPostRequest(urlPrefix + "/products", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPostRequest(urlPrefix + "/products" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
    }

    public Integer post_to_products_with_headers_and_queryParams(String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPostRequest(urlPrefix + "/products", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPostRequest(urlPrefix + "/products" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private HttpRequest newPostRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher, Map<String, String> headers) {

        return newRequest("POST", url, contentType, bodyPublisher, headers);
    }

    private HttpRequest newGetRequest(String url, Map<String, String> headers) {

        return newRequest("GET", url, URL_ENCODED, HttpRequest.BodyPublishers.noBody(), headers);
    }

    private HttpRequest newPutRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher, Map<String, String> headers) {

        return newRequest("PUT", url, contentType, bodyPublisher, headers);
    }

    private HttpRequest newPatchRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher, Map<String, String> headers) {

        return newRequest("PATCH", url, contentType, bodyPublisher, headers);
    }

    private HttpRequest newDeleteRequest(String url, Map<String, String> headers) {

        return newRequest("DELETE", url, URL_ENCODED, HttpRequest.BodyPublishers.noBody(), headers);
    }

    private HttpRequest newPostRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher) {

        return newRequest("POST", url, contentType, bodyPublisher);
    }

    private HttpRequest newGetRequest(String url) {

        return newRequest("GET", url, URL_ENCODED, HttpRequest.BodyPublishers.noBody());
    }

    private HttpRequest newPutRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher) {

        return newRequest("PUT", url, contentType, bodyPublisher);
    }

    private HttpRequest newPatchRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher) {

        return newRequest("PATCH", url, contentType, bodyPublisher);
    }

    private HttpRequest newDeleteRequest(String url) {

        return newRequest("DELETE", url, URL_ENCODED, HttpRequest.BodyPublishers.noBody());
    }

    private HttpRequest newRequest(String method, String url, String contentType,
                                   HttpRequest.BodyPublisher bodyPublisher) {

        HttpRequest.Builder builder = HttpRequest.newBuilder();

        return builder
                .method(method, bodyPublisher)
                .header(CONTENT_TYPE_HEADER, contentType)
                .uri(URI.create(url))
                .build();
    }

    private HttpRequest newRequest(String method, String url, String contentType,
                                   HttpRequest.BodyPublisher bodyPublisher, Map<String, String> headers) {

        HttpRequest.Builder builder = HttpRequest.newBuilder();

        builder.method(method, bodyPublisher)
                .header(CONTENT_TYPE_HEADER, contentType);

        for (Map.Entry<String,String> entry : headers.entrySet())
            builder.header(entry.getKey(), entry.getValue());

        return builder
                .uri(URI.create(url))
                .build();
    }

    private Map<String, Object> sendRequestAndParseResponseBodyAsUTF8Text(Supplier<HttpRequest> requestSupplier,
                                                            Function<Reader, Map<String, Object>> bodyProcessor) {

        HttpRequest request = requestSupplier.get();

        try {
            System.out.println("Sending " + request.method() + " to " + request.uri());
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            int statusCode = response.statusCode();
            if (statusCode == 200 || statusCode == 201) {
                try {
                    if (bodyProcessor != null) {
                        return bodyProcessor.apply(new InputStreamReader(response.body(), StandardCharsets.UTF_8));
                    }
                    else {
                        return null;
                    }
                }
                catch(Exception e) {
                    throw new ResponseProcessingException(e.getMessage(), e);
                }
            }
            else {
                throw new ServerResponseException(statusCode, ClientHelper.readContents(response.body()));
            }
        }
        catch(IOException | InterruptedException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    private String sendRequestAndParseResponseBodyAsString(Supplier<HttpRequest> requestSupplier) {

        HttpRequest request = requestSupplier.get();

        try {
            System.out.println("Sending " + request.method() + " to " + request.uri());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 200 || statusCode == 201) {
                try {
                    return response.body();
                }
                catch(Exception e) {
                    throw new ResponseProcessingException(e.getMessage(), e);
                }
            }
            else {
                throw new ServerResponseException(statusCode, response.body());
            }
        }
        catch(IOException | InterruptedException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    private Integer sendRequestAndParseResponseBodyAsInteger(Supplier<HttpRequest> requestSupplier) {

        HttpRequest request = requestSupplier.get();

        try {
            System.out.println("Sending " + request.method() + " to " + request.uri());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 200 || statusCode == 201) {
                try {
                    return Integer.parseInt(response.body());
                }
                catch(Exception e) {
                    throw new ResponseProcessingException(e.getMessage(), e);
                }
            }
            else {
                throw new ServerResponseException(statusCode, response.body());
            }
        }
        catch(IOException | InterruptedException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    /**
     * Helper method to create a new http client that can tolerate self-signed or improper ssl certificates.
     */
    private static HttpClient newHttpClient() throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        return HttpClient.newBuilder().sslContext(sslContext).build();
    }

    private static HttpRequest.BodyPublisher ofUrlEncodedFormData(Map<String, Object> data) {

        return HttpRequest.BodyPublishers.ofString(ClientHelper.encode(data));
    }

    public static class ClientHelper {

        public static String encode(Map<String, Object> data) {

            var builder = new StringBuilder();

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
            }

            return builder.toString();
        }

        static Map parseJsonObject(Reader reader) {

            Gson gson = new Gson();
            Map map = gson.fromJson(reader, Map.class);

            return map;
        }

        static String readContents(InputStream inputStream) {

            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).
                    lines().collect(Collectors.joining("\n"));
        }

        private static String parseJsonAndGetValueOfField(Reader reader, String field) {

            Gson gson = new Gson();
            Map map = gson.fromJson(reader, Map.class);

            return (String) map.get(field);
        }

    }

    public static class ConnectionException extends RuntimeException {

        public ConnectionException(String message) {

            super(message);
        }

        public ConnectionException(String message, Throwable cause) {

            super(message, cause);
        }

    }

    public static class ResponseProcessingException extends RuntimeException {

        public ResponseProcessingException(String message) {

            super(message);
        }

        public ResponseProcessingException(String message, Throwable cause) {

            super(message, cause);
        }

    }

    public static class ServerResponseException extends RuntimeException {

        private final int statusCode;

        public ServerResponseException(int statusCode, String message) {

            super(message);

            this.statusCode = statusCode;
        }

        public int getStatusCode() {

            return statusCode;
        }
    }
}