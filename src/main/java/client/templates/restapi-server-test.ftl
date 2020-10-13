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

    <#list api.endpoints as endpoint>
    // ${endpoint.path}: ${endpoint.label}
    <#list endpoint.methods as method>

    // ${method.type}
    <#if method.type == "GET">
    <#if endpoint.attributes?first??>
    def "GET ${endpoint.path?keep_after("/")} by ${endpoint.attributes?first}"() {
        given:
        ObjectMapper objectMapper = new ObjectMapper()

        Map<String, Object> agencyMap = Map.of("id", '2', "name", "prod2")

        ObjectNode productJSON = objectMapper.valueToTree(agencyMap)

        wms.givenThat(
                get(
                        urlEqualTo("${api.baseUrl}/${endpoint.path?keep_after("/")}/${endpoint.attributes?first}")
                ).willReturn(
                        aResponse().withJsonBody(productJSON)
                )
        )

        when:
        Map product = caller.get_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("2")

        then:
        product.get("id") == '2'
        product.get("name") == "prod2"

    }
    <#else>
    def "GET ${endpoint.path?keep_after("/")}"() {
        given:
        ObjectMapper objectMapper = new ObjectMapper()

        Map<String, Object> agencyMap = Map.of(
                "products",
                List.of(Map.of("id", '1', "name", "prod1"),
                        Map.of("id", '2', "name", "prod2"),
                        Map.of("id", '3', "name", "prod3"),
                        Map.of("id", '4', "name", "prod4"))
        )

        ObjectNode productJSON = objectMapper.valueToTree(agencyMap)

        wms.givenThat(
                get(
                        urlEqualTo("${api.baseUrl}/${endpoint.path?keep_after("/")}")
                ).willReturn(
                        aResponse().withJsonBody(productJSON)
                )
        )

        when:
        Map products = caller.get_${endpoint.path?keep_after("/")}()

        then:
        products.get("products").toString() == "[[id:1, name:prod1], [id:2, name:prod2], [id:3, name:prod3], [id:4, name:prod4]]" ||
                products.get("products").toString() == "[[name:prod1, id:1], [name:prod2, id:2], [name:prod3, id:3], [name:prod4, id:4]]"

    }
    </#if>
    </#if>
    <#if method.type == "POST">
    def "POST to ${endpoint.path?keep_after("/")}"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(
                        urlEqualTo("${api.baseUrl}/${endpoint.path?keep_after("/")}")
                ).willReturn(
                        aResponse().withStatus(200).withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, Object> products = caller.post_to_${endpoint.path?keep_after("/")}("test");

        then:
        products.get("value") == "ok"

    }
    </#if>
    <#if method.type == "PUT">
    def "PUT to ${endpoint.path?keep_after("/")}"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                put(
                        urlEqualTo("${api.baseUrl}/${endpoint.path?keep_after("/")}/${endpoint.attributes?first}")
                ).willReturn(
                        aResponse().withStatus(200).withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, Object> products = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("test", "2");

        then:
        products.get("value") == "ok"

    }
    </#if>
    <#if method.type == "PATCH">
    def "PATCH to ${endpoint.path?keep_after("/")}"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                patch(
                        urlEqualTo("${api.baseUrl}/${endpoint.path?keep_after("/")}/${endpoint.attributes?first}")
                ).willReturn(
                        aResponse().withStatus(200).withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, Object> products = caller.put_to_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("test", "2");

        then:
        products.get("value") == "ok"

    }
    </#if>
    <#if method.type == "DELETE">
    def "DELETE from ${endpoint.path?keep_after("/")}"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                delete(
                        urlEqualTo("${api.baseUrl}/${endpoint.path?keep_after("/")}/${endpoint.attributes?first}")
                ).willReturn(
                        aResponse().withStatus(200).withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, Object> products = caller.delete_from_${endpoint.path?keep_after("/")}_by_${endpoint.attributes?first}("2");

        then:
        products.get("value") == "ok"
    }
    </#if>
    </#list>
    </#list>
}