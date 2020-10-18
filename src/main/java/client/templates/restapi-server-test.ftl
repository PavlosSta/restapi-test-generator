package restapiserver

import client.RestAPIClient
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
class TestServer extends Specification {

    private static final int MOCK_SERVER_PORT = 8766

    @Shared WireMockServer wms
    @Shared RestAPIClient caller = new RestAPIClient("localhost", MOCK_SERVER_PORT)

    def setupSpec() {
        wms = new WireMockServer(WireMockConfiguration.options().httpsPort(MOCK_SERVER_PORT))
        wms.start()
    }

    def cleanupSpec() {
        wms.stop()
    }

    <#-- @ftlvariable name="api" type="interfaces.APISpec" -->
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
        ObjectMapper objectMapper = new ObjectMapper()

        <#if endpoint.attributes??>
        Map<String, Object> agencyMap = Map.of("id", "2", "name", "prod2")
        ObjectNode productJSON = objectMapper.valueToTree(agencyMap)
        <#else>
        Map<String, Object> agencyMap = Map.of(
                "products",
                List.of(Map.of("id", '1', "name", "prod1"),
                        Map.of("id", '2', "name", "prod2"),
                        Map.of("id", '3', "name", "prod3"),
                        Map.of("id", '4', "name", "prod4"))
        )
        ObjectNode productJSON = objectMapper.valueToTree(agencyMap)
        </#if>

        wms.givenThat(
                get(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>/.*</#if><#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("${header.value}"))
                </#list>
                <#list method.request.queryParams as queryParam>
                .withQueryParam("${queryParam.name}", equalTo("${queryParam.value}"))
                </#list>
                .willReturn(aResponse()
                        .withStatus(200)
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "${responseHeader.value}")
                        </#list>
                        .withJsonBody(productJSON)
                )
        )

        when:

        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "${header.value}")
        </#list>
        </#if>

        <#if method.request.queryParams??>
        Map<String, String> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        queryParams.put("${queryParam.name}", "${queryParam.value}")
        </#list>
        </#if>

        <#if method.request.headers?? && method.request.queryParams??>
        Map product = caller.get_${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>_with_headers_and_queryParams(<#if endpoint.attributes?first??>"2", </#if>headers, queryParams)
        <#elseif method.request.headers??>
        Map product = caller.get_${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>_with_headers(<#if endpoint.attributes?first??>"2", </#if>headers)
        <#elseif method.request.queryParams??>
        Map product = caller.get_${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>_with_queryParams(<#if endpoint.attributes?first??>"2", </#if>queryParams)
        <#else>
        Map product = caller.get_${endpoint.path?keep_after("/")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>(<#if endpoint.attributes?first??>"2"</#if>)
        </#if>

        then:
        <#if endpoint.attributes??>
        product.get("id") == "2"
        product.get("name") == "prod2"
        <#else>
        products.get("products").toString() == "[[id:1, name:prod1], [id:2, name:prod2], [id:3, name:prod3], [id:4, name:prod4]]" ||
            products.get("products").toString() == "[[name:prod1, id:1], [name:prod2, id:2], [name:prod3, id:3], [name:prod4, id:4]]"
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
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}<#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("${header.value}"))
                </#list>
                <#list method.request.queryParams as queryParam>
                .withQueryParam("${queryParam.name}", equalTo("${queryParam.value}"))
                </#list>
                .willReturn(aResponse()
                        .withStatus(201)
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "${responseHeader.value}")
                        </#list>
                        .withJsonBody(jsonBody)
                )
        )

        when:
        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "${header.value}")
        </#list>
        </#if>

        <#if method.request.queryParams??>
        Map<String, String> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        queryParams.put("${queryParam.name}", "${queryParam.value}")
        </#list>
        </#if>

        <#if method.request.headers?? && method.request.queryParams??>
        Map<String, Object> products = caller.post_to_${endpoint.path?keep_after("/")}_with_headers_and_queryParams("test", headers, queryParams)
        <#elseif method.request.headers??>
        Map<String, Object> products = caller.post_to_${endpoint.path?keep_after("/")}_with_headers("test", headers)
        <#elseif method.request.queryParams??>
        Map<String, Object> products = caller.post_to_${endpoint.path?keep_after("/")}_with_queryParams("test", queryParams)
        <#else>
        Map<String, Object> products = caller.post_to_${endpoint.path?keep_after("/")}("test")
        </#if>

        then:
        products.get("value") == "ok"

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
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                put(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}/.*<#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("${header.value}"))
                </#list>
                <#list method.request.queryParams as queryParam>
                .withQueryParam("${queryParam.name}", equalTo("${queryParam.value}"))
                </#list>
                .willReturn(aResponse()
                        .withStatus(200)
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "${responseHeader.value}")
                        </#list>
                        .withJsonBody(jsonBody)
                )
        )

        when:
        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "${header.value}")
        </#list>
        </#if>

        <#if method.request.queryParams??>
        Map<String, String> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        queryParams.put("${queryParam.name}", "${queryParam.value}")
        </#list>
        </#if>

        <#if method.request.headers?? && method.request.queryParams??>
        Map<String, Object> products = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("test", "2", headers, queryParams)
        <#elseif method.request.headers??>
        Map<String, Object> products = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers("test", "2", headers)
        <#elseif method.request.queryParams??>
        Map<String, Object> products = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams("test", "2", queryParams)
        <#else>
        Map<String, Object> products = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("test", "2")
        </#if>

        then:
        products.get("value") == "ok"

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
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                patch(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}/.*<#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("${header.value}"))
                </#list>
                <#list method.request.queryParams as queryParam>
                .withQueryParam("${queryParam.name}", equalTo("${queryParam.value}"))
                </#list>
                .willReturn(aResponse()
                        .withStatus(200)
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "${responseHeader.value}")
                        </#list>
                        .withJsonBody(jsonBody)
                )
        )

        when:
        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "${header.value}")
        </#list>
        </#if>

        <#if method.request.queryParams??>
        Map<String, String> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        queryParams.put("${queryParam.name}", "${queryParam.value}")
        </#list>
        </#if>

        <#if method.request.headers?? && method.request.queryParams??>
        Map<String, Object> products = caller.patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("test", "2", headers, queryParams)
        <#elseif method.request.headers??>
        Map<String, Object> products = caller.patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers("test", "2", headers)
        <#elseif method.request.queryParams??>
        Map<String, Object> products = caller.patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams("test", "2", queryParams)
        <#else>
        Map<String, Object> products = caller.patch_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("test", "2")
        </#if>

        then:
        products.get("value") == "ok"

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
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                delete(urlMatching("${api.baseUrl}/${endpoint.path?keep_after("/")}/.*<#if method.request.queryParams??>\\\\?.*</#if>"))
                <#list method.request.headers as header>
                .withHeader("${header.name}", equalTo("${header.value}"))
                </#list>
                <#list method.request.queryParams as queryParam>
                .withQueryParam("${queryParam.name}", equalTo("${queryParam.value}"))
                </#list>
                .willReturn(aResponse()
                        .withStatus(200)
                        <#list method.response.headers as responseHeader>
                        .withHeader("${responseHeader.name}", "${responseHeader.value}")
                        </#list>
                        .withJsonBody(jsonBody)
                )
        )

        when:
        <#if method.request.headers??>
        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", "${header.value}")
        </#list>
        </#if>

        <#if method.request.queryParams??>
        Map<String, String> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        queryParams.put("${queryParam.name}", "${queryParam.value}")
        </#list>
        </#if>

        <#if method.request.headers?? && method.request.queryParams??>
        Map<String, Object> products = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("2", headers, queryParams)
        <#elseif method.request.headers??>
        Map<String, Object> products = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_headers("2", headers)
        <#elseif method.request.queryParams??>
        Map<String, Object> products = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}_with_queryParams("2", queryParams)
        <#else>
        Map<String, Object> products = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("2")
        </#if>

        then:
        products.get("value") == "ok"

    }
    </#if>
    </#list>
    </#list>
}