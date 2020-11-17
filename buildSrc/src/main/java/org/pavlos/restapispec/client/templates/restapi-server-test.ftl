<#-- @ftlvariable name="api" type="org.pavlos.restapispec.interfaces.APISpec" -->
<#-- @ftlvariable name="clientPackage" type="String" -->
<#-- @ftlvariable name="clientName" type="String" -->
<#-- @ftlvariable name="testPackage" type="String" -->
<#-- @ftlvariable name="testName" type="String" -->
<#-- @ftlvariable name="serverPort" type="String" -->
package ${testPackage};

import ${clientPackage}.${clientName}
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
class ${testName} extends Specification {

    private static final int SERVER_PORT = ${serverPort}

    @Shared ${clientName} caller = new ${clientName}("localhost", SERVER_PORT)

    <#list api.endpoints as endpoint>
    // ${endpoint.path}: ${endpoint.label}
    <#list endpoint.methods as method>

    // ${method.type}
    <#if method.type == "GET">
    <#if method.request.headers?? && method.request.queryParams??>
    def "GET ${endpoint.path?keep_after("/")} <#if endpoint.attributes?first??>by ${endpoint.attributes?first} </#if>with headers and queryParams"() {
    <#elseif method.request.headers??>
    def "GET ${endpoint.path?keep_after("/")} <#if endpoint.attributes?first??>by ${endpoint.attributes?first} </#if>with headers"() {
    <#elseif method.request.queryParams??>
    def "GET ${endpoint.path?keep_after("/")} <#if endpoint.attributes?first??>by ${endpoint.attributes?first} </#if>with queryParams"() {
    <#else>
    def "GET ${endpoint.path?keep_after("/")} <#if endpoint.attributes?first??>by ${endpoint.attributes?first} </#if>"() {
    </#if>

        given:

        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>
        </#if>

        <#if method.request.queryParams??>
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
        </#if>

        when:

        <#if method.request.headers?? && method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.get_${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>_with_headers_and_queryParams(<#if endpoint.attributes?first??>"2", </#if>headers, queryParams)
        <#elseif method.request.headers??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.get_${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>_with_headers(<#if endpoint.attributes?first??>"2", </#if>headers)
        <#elseif method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.get_${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>_with_queryParams(<#if endpoint.attributes?first??>"2", </#if>queryParams)
        <#else>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.get_${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>(<#if endpoint.attributes?first??>"2"</#if>)
        </#if>

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
    <#if method.request.headers?? && method.request.queryParams??>
    def "POST to ${endpoint.path?keep_after("/")} with headers and queryParams"() {
    <#elseif method.request.headers??>
    def "POST to ${endpoint.path?keep_after("/")} with headers"() {
    <#elseif method.request.queryParams??>
    def "POST to ${endpoint.path?keep_after("/")} with queryParams"() {
    <#else>
    def "POST to ${endpoint.path?keep_after("/")}"() {
    </#if>

        given:

        ObjectMapper responseMapper = new ObjectMapper()

        String jsonBodyRequest = responseMapper.readTree("{\"name\":\"prod1\"}")

        <#if method.response.responseBodySchema == "JSON">
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")
        <#elseif method.response.responseBodySchema == "String">
        String resultString = "ok"
        <#else>
        Integer resultInteger = 42
        </#if>

        wms.givenThat(
                post(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}<#if method.request.queryParams??>\\\\?.*</#if>"))
                .withRequestBody(containing(jsonBodyRequest))
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

        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>
        </#if>

        <#if method.request.queryParams??>
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
        </#if>

        when:

        <#if method.request.headers?? && method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.post_to_${endpoint.path?keep_after("/")}_with_headers_and_queryParams(jsonBodyRequest, headers, queryParams)
        <#elseif method.request.headers??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.post_to_${endpoint.path?keep_after("/")}_with_headers(jsonBodyRequest, headers)
        <#elseif method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.post_to_${endpoint.path?keep_after("/")}_with_queryParams(jsonBodyRequest, queryParams)
        <#else>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.post_to_${endpoint.path?keep_after("/")}(jsonBodyRequest)
        </#if>

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
    <#if method.request.headers?? && method.request.queryParams??>
    def "PUT to ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with headers and queryParams"() {
    <#elseif method.request.headers??>
    def "PUT to ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with headers"() {
    <#elseif method.request.queryParams??>
    def "PUT to ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with queryParams"() {
    <#else>
    def "PUT to ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first}"() {
    </#if>

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

        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>
        </#if>

        <#if method.request.queryParams??>
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
        </#if>

        when:

        <#if method.request.headers?? && method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("test", "2", headers, queryParams)
        <#elseif method.request.headers??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers("test", "2", headers)
        <#elseif method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams("test", "2", queryParams)
        <#else>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("test", "2")
        </#if>

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
    <#if method.request.headers?? && method.request.queryParams??>
    def "PATCH to ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with headers and queryParams"() {
    <#elseif method.request.headers??>
    def "PATCH to ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with headers"() {
    <#elseif method.request.queryParams??>
    def "PATCH to ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with queryParams"() {
    <#else>
    def "PATCH to ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first}"() {
    </#if>

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

        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>
        </#if>

        <#if method.request.queryParams??>
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
        </#if>

        when:

        <#if method.request.headers?? && method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("test", "2", headers, queryParams)
        <#elseif method.request.headers??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers("test", "2", headers)
        <#elseif method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams("test", "2", queryParams)
        <#else>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("test", "2")
        </#if>

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
    <#if method.request.headers?? && method.request.queryParams??>
    def "DELETE from ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with headers and queryParams"() {
    <#elseif method.request.headers??>
    def "DELETE from ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with headers"() {
    <#elseif method.request.queryParams??>
    def "DELETE from ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first} with queryParams"() {
    <#else>
    def "DELETE from ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first}"() {
    </#if>

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
        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "headerValue")
        </#list>
        </#if>

        <#if method.request.queryParams??>
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
        </#if>

        <#if method.request.headers?? && method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("2", headers, queryParams)
        <#elseif method.request.headers??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers("2", headers)
        <#elseif method.request.queryParams??>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams("2", queryParams)
        <#else>
        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("2")
        </#if>

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