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

        // /products: endpoint for products with attribute

    // GET
    def "GET products by id with headers and queryParams"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        Map<String, Object> agencyMap = Map.of("id", "2", "name", "prod2")

        ObjectNode productJSON = objectMapper.valueToTree(agencyMap)

        wms.givenThat(
                get(urlMatching("/rest/api/products/.*"))
                .withHeader("headerName", equalTo("headerBody"))
                .withQueryParam("queryName", equalTo("queryValue"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("headerName", "headerBody")
                        .withJsonBody(productJSON)
                )
        )

        when:

        Map<String, String> headers = new HashMap<>();
        headers.put("headerName", "headerBody")

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("queryName", "queryValue")

        Map product = caller.get_products_by_id_with_headers_and_queryParams("2", headers, queryParams)

        then:
        product.get("id") == "2"
        product.get("name") == "prod2"

    }

    // PUT
    def "PUT to products"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                put(urlMatching("/rest/api/products/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, Object> products = caller.put_to_products_by_id("test", "2")

        then:
        products.get("value") == "ok"

    }

    // PATCH
    def "PATCH to products"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                patch(urlMatching("/rest/api/products/.*"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, Object> products = caller.put_to_products_by_id("test", "2")

        then:
        products.get("value") == "ok"

    }

    // DELETE
    def "DELETE from products"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                delete(urlMatching("/rest/api/products/.*"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, Object> products = caller.delete_from_products_by_id("2")

        then:
        products.get("value") == "ok"
    }
    // /products: endpoint for products without attribute

    // GET
    def "GET products"() {
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
                get(urlPathEqualTo("/rest/api/products"))
                .withHeader("headerName1", equalTo("headerValue1"))
                .withHeader("headerName2", equalTo("headerValue2"))
                .withQueryParam("queryName1", equalTo("queryValue1"))
                .withQueryParam("queryName2", equalTo("queryValue2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("responseHeaderName", "responseHeaderValue")
                        .withJsonBody(productJSON)
                )
        )

        when:
        Map<String, String> headers = new HashMap<>()
        headers.put("headername1", "headerValue1")
        headers.put("headername2", "headerValue2")

        Map<String, String> queryParams = new HashMap<>()
        queryParams.put("queryName1", "queryValue1")
        queryParams.put("queryName2", "queryValue2")

        Map products = caller.get_products_with_headers_and_queryParams(headers, queryParams)

        then:
        products.get("products").toString() == "[[id:1, name:prod1], [id:2, name:prod2], [id:3, name:prod3], [id:4, name:prod4]]" ||
                products.get("products").toString() == "[[name:prod1, id:1], [name:prod2, id:2], [name:prod3, id:3], [name:prod4, id:4]]"

    }

    // POST
    def "POST to products"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlEqualTo("/rest/api/products"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, Object> products = caller.post_to_products("test")

        then:
        products.get("value") == "ok"

    }
}