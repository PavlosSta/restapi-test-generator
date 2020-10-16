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

public class RestAPIClient {

    public static final String BASE_URL = "/rest/api";
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

    // Methods for each Endpoint:

    // /products: endpoint for products with attribute

    // GET
    public Map<String, Object> get_products_by_id(String id, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "/products/id", headers),
                ClientHelper::parseJsonObject
        );
    }


    // PUT
    public Map<String, Object> put_to_products_by_id(String input, String id, Map<String, String> headers) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPutRequest(urlPrefix + "/products/id", URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                ClientHelper::parseJsonObject
        );

    }

    // PATCH
    public Map<String, Object> patch_to_products_by_id(String input, String id, Map<String, String> headers) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPatchRequest(urlPrefix + "/products/id", URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                ClientHelper::parseJsonObject
        );

    }

    // DELETE
    public Map<String, Object> delete_from_products_by_id(String id, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newDeleteRequest(urlPrefix + "/products/id", headers),
                ClientHelper::parseJsonObject
        );
    }
    // /products: endpoint for products without attribute

    // GET
    public Map<String, Object> get_products(Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "/products", headers),
                ClientHelper::parseJsonObject
        );
    }


    // POST
    public Map<String, Object> post_to_products(String input, Map<String, String> headers) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "/products", URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                ClientHelper::parseJsonObject
        );

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