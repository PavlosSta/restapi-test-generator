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

        // /login: login endpoint

    // POST
    def "POST to login with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("/observatory/api/login\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(jsonBody)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> products = caller.post_to_login_with_headers_and_queryParams("test", headers, queryParams)

        then:

        products.get("value") == "ok"

    }
    // /logout: logout endpoint

    // POST
    def "POST to logout with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("/observatory/api/logout\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(jsonBody)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> products = caller.post_to_logout_with_headers_and_queryParams("test", headers, queryParams)

        then:

        products.get("value") == "ok"

    }
    // /products: endpoint for products with attribute

    // GET
    def "GET products by id with headers and queryParams"() {

        given:
 
        Integer productInteger = 42

        wms.givenThat(
                get(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(productInteger.toString())

                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map product = caller.get_products_by_id_with_headers_and_queryParams("2", headers, queryParams)

        then:

        product.get("id") == "2"
        product.get("name") == "prod2"

    }

    // PUT
    def "PUT to products by id with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                put(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(jsonBody)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> products = caller.put_to_products_by_id_with_headers_and_queryParams("test", "2", headers, queryParams)

        then:

        products.get("value") == "ok"

    }

    // PATCH
    def "PATCH to products by id with headers and queryParams"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                patch(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(jsonBody)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> products = caller.patch_to_products_by_id_with_headers_and_queryParams("test", "2", headers, queryParams)

        then:
        products.get("value") == "ok"

    }

    // DELETE
    def "DELETE from products by id with headers and queryParams"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                delete(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(jsonBody)
                )
        )

        when:
        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        Map<String, Object> products = caller.delete_from_products_by_id_with_headers_and_queryParams("2", headers, queryParams)

        then:
        products.get("value") == "ok"

    }
    // /products: endpoint for products without attribute

    // GET
    def "GET products with headers and queryParams"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        Map<String, Object> agencyMap = Map.of("id", "2", "name", "prod2")
        ObjectNode productJSON = objectMapper.valueToTree(agencyMap)

        wms.givenThat(
                get(urlMatching("/observatory/api/products\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withJsonBody(productJSON)

                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map product = caller.get_products_with_headers_and_queryParams(headers, queryParams)

        then:

        product.get("id") == "2"
        product.get("name") == "prod2"

    }

    // POST
    def "POST to products with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()

        JsonNode jsonBody = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("/observatory/api/products\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(jsonBody)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> products = caller.post_to_products_with_headers_and_queryParams("test", headers, queryParams)

        then:

        products.get("value") == "ok"

    }
}