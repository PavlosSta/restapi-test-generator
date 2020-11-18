<#-- @ftlvariable name="api" type="org.pavlos.restapispec.interfaces.APISpec" -->
<#-- @ftlvariable name="clientPackage" type="String" -->
<#-- @ftlvariable name="clientName" type="String" -->
<#-- @ftlvariable name="mockPackage" type="String" -->
<#-- @ftlvariable name="mockName" type="String" -->
package ${mockPackage};

import ${clientPackage}.${clientName}
import org.json.JSONObject
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static com.github.tomakehurst.wiremock.client.WireMock.*

@Stepwise
class ${mockName} extends Specification {

    private static final int MOCK_SERVER_PORT = 8766

    @Shared WireMockServer wms
    @Shared ${clientName} caller = new ${clientName}("localhost", MOCK_SERVER_PORT)

    def setupSpec() {
        wms = new WireMockServer(WireMockConfiguration.options().httpsPort(MOCK_SERVER_PORT))
        wms.start()
    }

    def cleanupSpec() {
        wms.stop()
    }

    <#list api.endpoints as endpoint>
    // ${endpoint.path}: ${endpoint.label}
    <#list endpoint.methods as method>
    // ${method.type}
    <#if method.type == "GET">
    def "GET ${endpoint.path?keep_after("/")?replace("/", "_")} <#if endpoint.attributes?first??>by ${endpoint.attributes?first} </#if>with headers and queryParams"() {

        given:

        <#if method.response.responseBodySchema == "JSON">
        ObjectMapper responseMapper = new ObjectMapper()

        <#if endpoint.attributes??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> agencyMap = Map.of("id", "2", "name", "prod2")
        ObjectNode resultJSON = responseMapper.valueToTree(agencyMap)
        <#else>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> agencyMap = Map.of(
                "result",
                List.of(Map.of("id", '1', "name", "prod1"),
                        Map.of("id", '2', "name", "prod2"),
                        Map.of("id", '3', "name", "prod3"),
                        Map.of("id", '4', "name", "prod4"))
        )
        ObjectNode resultJSON = responseMapper.valueToTree(agencyMap)
        </#if>
        <#elseif method.response.responseBodySchema == "String">
        <#if endpoint.attributes??>
        String resultString = "prod2"
        <#else>
        String resultString = "prod1, prod2, prod3"
        </#if>
        <#else> <#-- Integer -->
        Integer resultInteger = 42
        </#if>

        wms.givenThat(
                get(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>/.*</#if><#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("headerValue"))
                </#list>
                <#list method.request.queryParams as queryParam>
                <#if queryParam.type == "String">
                .withQueryParam("${queryParam.name}", containing("queryValue"))
                <#elseif queryParam.type == "Integer">
                .withQueryParam("${queryParam.name}", containing("42"))
                <#elseif queryParam.type == "float">
                .withQueryParam("${queryParam.name}", containing("42.5")))
                <#else>
                .withQueryParam("${queryParam.name}", containing(true))
                </#if>
                </#list>
                .willReturn(aResponse()
                        .withStatus(${method.response.status.code})
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "headerValue")
                        </#list>
                        <#if method.response.responseBodySchema == "JSON">
                        .withJsonBody(resultJSON)
                        <#elseif method.response.responseBodySchema == "String">
                        .withBody(resultString)
                        <#else>
                        .withBody(resultInteger.toString())
                        </#if>
                )
        )

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5"))
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#list>

        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.get_${endpoint.path?keep_after("/")?replace("/", "_")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>_with_headers_and_queryParams(<#if endpoint.attributes?first??>"2", </#if>headers, queryParams)

        then:

        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("\\w+")
        <#else>
        result.toString().matches("\\d+")
        </#if>

    }
    </#if>
    <#if method.type == "POST" && !endpoint.attributes?first??>
    def "POST to ${endpoint.path?keep_after("/")?replace("/", "_")} with headers and queryParams"() {

        given:

        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        <#if method.response.responseBodySchema == "JSON">
        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")
        <#elseif method.response.responseBodySchema == "String">
        String resultString = "ok"
        <#else>
        Integer resultInteger = 42
        </#if>

        wms.givenThat(
                post(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}<#if method.request.queryParams??>\\\\?.*</#if>"))
                .withRequestBody(containing(requestBody))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("headerValue"))
                </#list>
                <#list method.request.queryParams as queryParam>
                <#if queryParam.type == "String">
                .withQueryParam("${queryParam.name}", containing("queryValue"))
                <#elseif queryParam.type == "Integer">
                .withQueryParam("${queryParam.name}", containing("42"))
                <#elseif queryParam.type == "float">
                .withQueryParam("${queryParam.name}", containing("42.5")))
                <#else>
                .withQueryParam("${queryParam.name}", containing(true))
                </#if>
                </#list>
                .willReturn(aResponse()
                        .withStatus(${method.response.status.code})
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "headerValue")
                        </#list>
                        <#if method.response.responseBodySchema == "JSON">
                        .withJsonBody(resultJSON)
                        <#elseif method.response.responseBodySchema == "String">
                        .withBody(resultString)
                        <#else>
                        .withBody(resultInteger.toString())
                        </#if>
                )
        )

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>

        Map<String, List<String>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5"))
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#list>

        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.post_to_${endpoint.path?keep_after("/")?replace("/", "_")}_with_headers_and_queryParams(requestBody, headers, queryParams)

        then:

        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("\\w+")
        <#else>
        result.toString().matches("\\d+")
        </#if>

    }
    </#if>
    <#if method.type == "PUT" && endpoint.attributes?first??>
    def "PUT to ${endpoint.path?keep_after("/")?replace("/", "_")} by ${endpoint.attributes?first} with headers and queryParams"() {

        given:

        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        <#if method.response.responseBodySchema == "JSON">
        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")
        <#elseif method.response.responseBodySchema == "String">
        String resultString = "ok"
        <#else>
        Integer resultInteger = 42
        </#if>

        wms.givenThat(
                put(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}/.*<#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("headerValue"))
                </#list>
                <#list method.request.queryParams as queryParam>
                <#if queryParam.type == "String">
                .withQueryParam("${queryParam.name}", containing("queryValue"))
                <#elseif queryParam.type == "Integer">
                .withQueryParam("${queryParam.name}", containing("42"))
                <#elseif queryParam.type == "float">
                .withQueryParam("${queryParam.name}", containing("42.5")))
                <#else>
                .withQueryParam("${queryParam.name}", containing(true))
                </#if>
                </#list>
                .willReturn(aResponse()
                        .withStatus(${method.response.status.code})
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "headerValue")
                        </#list>
                        <#if method.response.responseBodySchema == "JSON">
                        .withJsonBody(resultJSON)
                        <#elseif method.response.responseBodySchema == "String">
                        .withBody(resultString)
                        <#else>
                        .withBody(resultInteger.toString())
                        </#if>
                )
        )

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>

        Map<String, List<String>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#list>

        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.put_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("2", requestBody, headers, queryParams)

        then:

        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("[\\{\\[].*[\\}\\]]")
        <#else>
        result.matches("\\d+")
        </#if>

    }
    </#if>
    <#if method.type == "PATCH">
    def "PATCH to ${endpoint.path?keep_after("/")?replace("/", "_")} by ${endpoint.attributes?first} with headers and queryParams"() {

        given:

        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        <#if method.response.responseBodySchema == "JSON">
        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")
        <#elseif method.response.responseBodySchema == "String">
        String resultString = "ok"
        <#else>
        Integer resultInteger = 42
        </#if>

        wms.givenThat(
                patch(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}/.*<#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("headerValue"))
                </#list>
                <#list method.request.queryParams as queryParam>
                <#if queryParam.type == "String">
                .withQueryParam("${queryParam.name}", containing("queryValue"))
                <#elseif queryParam.type == "Integer">
                .withQueryParam("${queryParam.name}", containing("42"))
                <#elseif queryParam.type == "float">
                .withQueryParam("${queryParam.name}", containing("42.5"))
                <#else>
                .withQueryParam("${queryParam.name}", containing(true))
                </#if>
                </#list>
                .willReturn(aResponse()
                        .withStatus(${method.response.status.code})
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "headerValue")
                        </#list>
                        <#if method.response.responseBodySchema == "JSON">
                        .withJsonBody(resultJSON)
                        <#elseif method.response.responseBodySchema == "String">
                        .withBody(resultString)
                        <#else>
                        .withBody(resultInteger.toString())
                        </#if>
                )
        )

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>

        Map<String, List<String>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5"))
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#list>

        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.patch_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("2", requestBody, headers, queryParams)

        then:
        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("[\\{\\[].*[\\}\\]]")
        <#else>
        result.matches("\\d+")
        </#if>

    }
    </#if>
    <#if method.type == "DELETE">
    def "DELETE from ${endpoint.path?keep_after("/")?replace("/", "_")} by ${endpoint.attributes?first} with headers and queryParams"() {

        given:

        <#if method.response.responseBodySchema == "JSON">
        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")
        <#elseif method.response.responseBodySchema == "String">
        String resultString = "ok"
        <#else>
        Integer resultInteger = 42
        </#if>

        wms.givenThat(
                delete(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}/.*<#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("headerValue"))
                </#list>
                <#list method.request.queryParams as queryParam>
                <#if queryParam.type == "String">
                .withQueryParam("${queryParam.name}", containing("queryValue"))
                <#elseif queryParam.type == "Integer">
                .withQueryParam("${queryParam.name}", containing("42"))
                <#elseif queryParam.type == "float">
                .withQueryParam("${queryParam.name}", containing("42.5")))
                <#else>
                .withQueryParam("${queryParam.name}", containing(true))
                </#if>
                </#list>
                .willReturn(aResponse()
                        .withStatus(${method.response.status.code})
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "headerValue")
                        </#list>
                        <#if method.response.responseBodySchema == "JSON">
                        .withJsonBody(resultJSON)
                        <#elseif method.response.responseBodySchema == "String">
                        .withBody(resultString)
                        <#else>
                        .withBody(resultInteger.toString())
                        </#if>
                )
        )

        when:

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>

        Map<String, List<String>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5"))
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#list>

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.delete_from_${endpoint.path?keep_after("/")?replace("/", "_")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("2", headers, queryParams)

        then:
        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("[\\{\\[].*[\\}\\]]")
        <#else>
        result.matches("\\d+")
        </#if>

    }
    </#if>
    </#list>
    </#list>
}