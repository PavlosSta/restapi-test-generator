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

    <#-- @ftlvariable name="api" type="interfaces.APISpec" -->
    public static final String BASE_URL = "${api.baseUrl}";

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
                queryParamString.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        return queryParamString.toString();
    }

    // Methods for each Endpoint:

    <#list api.endpoints as endpoint>
    // ${endpoint.path}: ${endpoint.label}
    <#list endpoint.methods as method>

    // ${method.type}
    <#if method.type == "GET">
    <#if endpoint.attributes?first??>
    public Map<String, Object> get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}(String ${endpoint.attributes?first}) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers(String ${endpoint.attributes?first}, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams(String ${endpoint.attributes?first}, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString),
                    ClientHelper::parseJsonObject
            );
        }
    }
    public Map<String, Object> get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams(String ${endpoint.attributes?first}, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
    }
    <#else>
    public Map<String, Object> get_${endpoint.path?keep_after("/")}() {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "${endpoint.path}"),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> get_${endpoint.path?keep_after("/")}_with_headers(Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "${endpoint.path}", headers),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> get_${endpoint.path?keep_after("/")}_with_queryParams(Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}"),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString),
                    ClientHelper::parseJsonObject
            );
        }
    }
    public Map<String, Object> get_${endpoint.path?keep_after("/")}_with_headers_and_queryParams(Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}", headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
    }
    </#if>

    </#if>
    <#if method.type == "POST">
    public Map<String, Object> post_to_${endpoint.path?keep_after("/")}(String input) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, ofUrlEncodedFormData(formData)),
                ClientHelper::parseJsonObject
        );

    }
    public Map<String, Object> post_to_${endpoint.path?keep_after("/")}_with_headers(String input, Map<String, String> headers) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                ClientHelper::parseJsonObject
        );

    }
    public Map<String, Object> post_to_${endpoint.path?keep_after("/")}_with_queryParams(String input, Map<String, List<String>> queryParams) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, ofUrlEncodedFormData(formData)),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, URL_ENCODED, ofUrlEncodedFormData(formData)),
                    ClientHelper::parseJsonObject
            );
        }
    }
    public Map<String, Object> post_to_${endpoint.path?keep_after("/")}_with_headers_and_queryParams(String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                    ClientHelper::parseJsonObject
            );
        }
    }

    </#if>
    <#if method.type == "PUT">
    public Map<String, Object> put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}(String input, String ${endpoint.attributes?first}) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, ofUrlEncodedFormData(formData)),
                ClientHelper::parseJsonObject
        );

    }
    public Map<String, Object> put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers(String input, String ${endpoint.attributes?first}, Map<String, String> headers) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                ClientHelper::parseJsonObject
        );

    }
    public Map<String, Object> put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams(String input, String ${endpoint.attributes?first}, Map<String, List<String>> queryParams) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, ofUrlEncodedFormData(formData)),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, ofUrlEncodedFormData(formData)),
                    ClientHelper::parseJsonObject
            );
        }
    }
    public Map<String, Object> put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams(String input, String ${endpoint.attributes?first}, Map<String, String> headers, Map<String, List<String>> queryParams) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                    ClientHelper::parseJsonObject
            );
        }
    }
    </#if>
    <#if method.type == "PATCH">
    public Map<String, Object> patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}(String input, String ${endpoint.attributes?first}) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, ofUrlEncodedFormData(formData)),
                ClientHelper::parseJsonObject
        );

    }
    public Map<String, Object> patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers(String input, String ${endpoint.attributes?first}, Map<String, String> headers) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                ClientHelper::parseJsonObject
        );

    }
    public Map<String, Object> patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams(String input, String ${endpoint.attributes?first}, Map<String, List<String>> queryParams) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, ofUrlEncodedFormData(formData)),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, ofUrlEncodedFormData(formData)),
                    ClientHelper::parseJsonObject
            );
        }
    }
    public Map<String, Object> patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams(String input, String ${endpoint.attributes?first}, Map<String, String> headers, Map<String, List<String>> queryParams) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("input", input);

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, ofUrlEncodedFormData(formData), headers),
                    ClientHelper::parseJsonObject
            );
        }
    }
    </#if>
    <#if method.type == "DELETE">
    public Map<String, Object> delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}(String ${endpoint.attributes?first}) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers(String ${endpoint.attributes?first}, Map<String, String> headers) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers),
                ClientHelper::parseJsonObject
        );
    }
    public Map<String, Object> delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams(String ${endpoint.attributes?first}, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString),
                    ClientHelper::parseJsonObject
            );
        }
    }
    public Map<String, Object> delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams(String ${endpoint.attributes?first}, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers),
                    ClientHelper::parseJsonObject
            );
        }
        else {
        String queryParamString = queryParamsToString(queryParams);

        return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
    }
    </#if>
    </#list>
    </#list>

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