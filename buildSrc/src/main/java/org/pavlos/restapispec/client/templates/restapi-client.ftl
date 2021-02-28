<#-- @ftlvariable name="api" type="org.pavlos.restapispec.interfaces.APISpec" -->
<#-- @ftlvariable name="clientPackage" type="String" -->
<#-- @ftlvariable name="clientName" type="String" -->
package ${clientPackage};

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

public class ${clientName} {

    public static final String BASE_URL = "${api.baseUrl}";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

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

    public ${clientName}(String host, int port) throws RuntimeException {

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

    <#list api.endpoints as endpoint>
    // ${endpoint.path}<#if endpoint.label??>: ${endpoint.label}</#if>
    <#if endpoint.description??>
    /*
        ${endpoint.description}
    */
    </#if>
    <#list endpoint.methods as method>

    // ${method.type}
    <#if method.type == "GET">
    <#if endpoint.attributes?first??>
    public Map<String, Object> get_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>String ${attr}, </#list>Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list>, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list>, headers));
        }
        </#if>
        else {
            String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list> + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list> + queryParamString, headers));
        }
        </#if>
    }
    <#else>
    public Map<String, Object> get_${endpoint.path?keep_after("/")?replace("/", "_")}(Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}", headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}", headers));
        }
        </#if>
        else {
            String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString, headers));
        }
        </#if>
    }
    </#if>

    </#if>
    <#if method.type == "POST">
    public Map<String, Object> post_to_${endpoint.path?keep_after("/")?replace("/", "_")}(String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
    }

    </#if>
    <#if method.type == "PUT">
    public Map<String, Object> put_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>String ${attr}, </#list>String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list>, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list>, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list> + queryParamString, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list> + queryParamString, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
    }

    </#if>
    <#if method.type == "PATCH">
    public Map<String, Object> patch_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>String ${attr}, </#list>String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list>, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list>, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list> + queryParamString, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list> + queryParamString, "${method.request.contentType}", HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
    }

    </#if>
    <#if method.type == "DELETE">
    public Map<String, Object> delete_from_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>String ${attr}, </#list>Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list>, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list>, headers));
        }
        </#if>
        else {
            String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list> + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "Text">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + <#list endpoint.attributes as attr>${attr}<#sep> + "/" + </#sep></#list> + queryParamString, headers));
        }
        </#if>
    }
    </#if>
    </#list>
    </#list>

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private HttpRequest newPostRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher, Map<String, String> headers) {

        return newRequest("POST", url, contentType, bodyPublisher, headers);
    }

    private HttpRequest newGetRequest(String url, Map<String, String> headers) {

        return newRequest("GET", url, "application/x-www-form-urlencoded", HttpRequest.BodyPublishers.noBody(), headers);
    }

    private HttpRequest newPutRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher, Map<String, String> headers) {

        return newRequest("PUT", url, contentType, bodyPublisher, headers);
    }

    private HttpRequest newPatchRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher, Map<String, String> headers) {

        return newRequest("PATCH", url, contentType, bodyPublisher, headers);
    }

    private HttpRequest newDeleteRequest(String url, Map<String, String> headers) {

        return newRequest("DELETE", url, "application/x-www-form-urlencoded", HttpRequest.BodyPublishers.noBody(), headers);
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

        Map<String, Object> bodyHeaders = new HashMap<>();

        try {
            System.out.println("Sending " + request.method() + " to " + request.uri());
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            int statusCode = response.statusCode();
            if (statusCode == 200 || statusCode == 201) {
                try {
                    if (bodyProcessor != null) {
                        bodyHeaders.put("headers", response.headers().map());
                        bodyHeaders.put("body", bodyProcessor.apply(new InputStreamReader(response.body(), StandardCharsets.UTF_8)));
                        return bodyHeaders;
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

    private Map<String, Object> sendRequestAndParseResponseBodyAsString(Supplier<HttpRequest> requestSupplier) {

        HttpRequest request = requestSupplier.get();

        Map<String, Object> bodyHeaders = new HashMap<>();

        try {
            System.out.println("Sending " + request.method() + " to " + request.uri());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 200 || statusCode == 201) {
                try {
                    bodyHeaders.put("headers", response.headers().map());
                    bodyHeaders.put("body", response.body());
                    return bodyHeaders;
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