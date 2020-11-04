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
                for (int i = 0; i < entry.getValue().size(); i++) {
                    queryParamString.append("&").append(entry.getKey()).append("=").append(entry.getValue().get(i));
                }
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
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}(String ${endpoint.attributes?first}) {

        <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}),
                ClientHelper::parseJsonObject
        );
        <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}));
        <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}));
        </#if>
    }
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers(String ${endpoint.attributes?first}, Map<String, String> headers) {

        <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers),
                ClientHelper::parseJsonObject
        );
        <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers));
        <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers));
        </#if>
    }
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams(String ${endpoint.attributes?first}, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}));
        }
        </#if>
        else {
            String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString));
        }
        </#if>
    }
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams(String ${endpoint.attributes?first}, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers));
        }
        </#if>
        else {
            String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, headers));
        }
        </#if>
    }
    <#else>
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> get_${endpoint.path?keep_after("/")}() {

    <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "${endpoint.path}"),
                ClientHelper::parseJsonObject
        );
    }
    <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newGetRequest(urlPrefix + "${endpoint.path}"));
    }
    <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newGetRequest(urlPrefix + "${endpoint.path}"));
    }
    </#if>
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> get_${endpoint.path?keep_after("/")}_with_headers(Map<String, String> headers) {

    <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newGetRequest(urlPrefix + "${endpoint.path}", headers),
                ClientHelper::parseJsonObject
        );
    }
    <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newGetRequest(urlPrefix + "${endpoint.path}", headers));
    }
    <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newGetRequest(urlPrefix + "${endpoint.path}", headers));
    }
    </#if>
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> get_${endpoint.path?keep_after("/")}_with_queryParams(Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}"),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}"));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}"));
        }
        </#if>
        else {
            String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString));
        }
        </#if>
    }
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> get_${endpoint.path?keep_after("/")}_with_headers_and_queryParams(Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}", headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}", headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
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
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString, headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newGetRequest(urlPrefix + "${endpoint.path}" + queryParamString, headers));
        }
        </#if>
    }
    </#if>

    </#if>
    <#if method.type == "POST">
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> post_to_${endpoint.path?keep_after("/")}(String input) {

    <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                ClientHelper::parseJsonObject
        );
    }
    <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
    }
    <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
    }
    </#if>

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> post_to_${endpoint.path?keep_after("/")}_with_headers(String input, Map<String, String> headers) {

    <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                ClientHelper::parseJsonObject
        );
    }
    <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
    }
    <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
    }
    </#if>

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> post_to_${endpoint.path?keep_after("/")}_with_queryParams(String input, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        </#if>
    }

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> post_to_${endpoint.path?keep_after("/")}_with_headers_and_queryParams(String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}", URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPostRequest(urlPrefix + "${endpoint.path}" + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
    }

    </#if>
    <#if method.type == "PUT">
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}(String ${endpoint.attributes?first}, String input) {

    <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                ClientHelper::parseJsonObject
        );
    }
    <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
    }
    <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
    }
    </#if>

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers(String ${endpoint.attributes?first}, String input, Map<String, String> headers) {

    <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                ClientHelper::parseJsonObject
        );
    }
    <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
    }
    <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
    }
    </#if>

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams(String ${endpoint.attributes?first}, String input, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        </#if>
    }

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams(String ${endpoint.attributes?first}, String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPutRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
    }

    </#if>
    <#if method.type == "PATCH">
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}(String ${endpoint.attributes?first}, String input) {

    <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                ClientHelper::parseJsonObject
        );
    }
    <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
    }
    <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
    }
    </#if>

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers(String ${endpoint.attributes?first}, String input, Map<String, String> headers) {

    <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                ClientHelper::parseJsonObject
        );
    }
    <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
    }
    <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
    }
    </#if>

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams(String ${endpoint.attributes?first}, String input, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input)));
        }
        </#if>
    }

    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams(String ${endpoint.attributes?first}, String input, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
        else {
        String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newPatchRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, URL_ENCODED, HttpRequest.BodyPublishers.ofString(input), headers));
        }
        </#if>
    }
    </#if>
    <#if method.type == "DELETE">
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}(String ${endpoint.attributes?first}) {

        <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}),
                ClientHelper::parseJsonObject
        );
        <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}));
        <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}));
        </#if>
    }
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers(String ${endpoint.attributes?first}, Map<String, String> headers) {

        <#if method.response.responseBodySchema == "JSON">
        return sendRequestAndParseResponseBodyAsUTF8Text(
                () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers),
                ClientHelper::parseJsonObject
        );
        <#elseif method.response.responseBodySchema == "String">
        return sendRequestAndParseResponseBodyAsString(
                () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers));
        <#else>
        return sendRequestAndParseResponseBodyAsInteger(
                () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers));
        </#if>
    }
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams(String ${endpoint.attributes?first}, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}));
        }
        </#if>
        else {
            String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString));
        }
        </#if>
    }
    public <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams(String ${endpoint.attributes?first}, Map<String, String> headers, Map<String, List<String>> queryParams) {

        if(queryParams.isEmpty()) {
        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first}, headers));
        }
        </#if>
        else {
            String queryParamString = queryParamsToString(queryParams);

        <#if method.response.responseBodySchema == "JSON">
            return sendRequestAndParseResponseBodyAsUTF8Text(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, headers),
                    ClientHelper::parseJsonObject
            );
        }
        <#elseif method.response.responseBodySchema == "String">
            return sendRequestAndParseResponseBodyAsString(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, headers));
        }
        <#else>
            return sendRequestAndParseResponseBodyAsInteger(
                    () -> newDeleteRequest(urlPrefix + "${endpoint.path}/" + ${endpoint.attributes?first} + queryParamString, headers));
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
}