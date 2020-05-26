package client;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URI;
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


public class RestAPI {

    public static final String BASE_URL = "https://www.myapi.gr";
    public static final String CUSTOM_HEADER = "X-CONTROL-CENTER-AUTH";

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

    private String token = null; // User is not logged in.

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public RestAPI() throws RuntimeException {
        this(null);
    }

    public RestAPI(String token) throws RuntimeException {
        this("localhost", 9000, token);
    }

    public RestAPI(String host, int port, String token) throws RuntimeException {

        try {
            this.client = newHttpClient();
        }
        catch(NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e.getMessage());
        }

        this.urlPrefix = "https://" + host + ":" + port + BASE_URL;
        this.token = token;
    }

    public boolean isLoggedIn() {

        return token != null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Methods for each Endpoint:

    // /test: test

    // GET
    public String testGET() {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest("/test"),
                ClientHelper::parseJsonObject
        );
    }

    // POST
    public String testPOST(String input) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest("/test", URL_ENCODED, ofUrlEncodedFormData(formData)),
                ClientHelper::parseJsonObject
        );

    }

    // PUT
    public String testPUT(String input) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPutRequest("/test", URL_ENCODED, ofUrlEncodedFormData(formData)),
                ClientHelper::parseJsonObject
        );

    }

    // PATCH
    public String testPATCH(String input) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPatchRequest("/test", URL_ENCODED, ofUrlEncodedFormData(formData)),
                ClientHelper::parseJsonObject
        );

    }

    // DELETE
    public String testDELETE(String input) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newDeleteRequest("/test"),
                ClientHelper::parseJsonObject
        );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

        if (token != null) {
            builder.header(CUSTOM_HEADER, token);
        }

        return builder
                .method(method, bodyPublisher)
                .header(CONTENT_TYPE_HEADER, contentType)
                .uri(URI.create(url))
                .build();
    }

    private <T> T sendRequestAndParseResponseBodyAsUTF8Text(Supplier<HttpRequest> requestSupplier,
                                                            Function<Reader, T> bodyProcessor) {

        HttpRequest request = requestSupplier.get();

        try {
            System.out.println("Sending " + request.method() + " to " + request.uri());
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            int statusCode = response.statusCode();
            if (statusCode == 200) {
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
}