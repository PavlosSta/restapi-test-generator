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

        String jsonBodyRequest = objectMapper.readTree("{\"name\":\"prod1\"}")

        JsonNode resultJSON = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("/observatory/api/login\\\\?.*"))
                .withRequestBody(containing(jsonBodyRequest))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("X-OBSERVATORY-AUTH", "headerValue")
                        .withJsonBody(resultJSON)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.post_to_login_with_headers_and_queryParams(jsonBodyRequest, headers, queryParams)

        then:

        result.toString().matches("[\\{\\[].*[\\}\\]]")

    }
    // /logout: logout endpoint

    // POST
    def "POST to logout with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()

        String jsonBodyRequest = objectMapper.readTree("{\"name\":\"prod1\"}")

        JsonNode resultJSON = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("/observatory/api/logout\\\\?.*"))
                .withRequestBody(containing(jsonBodyRequest))
                .withHeader("X-OBSERVATORY-AUTH", equalTo("headerValue"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(resultJSON)
                )
        )

        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", "headerValue")

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.post_to_logout_with_headers_and_queryParams(jsonBodyRequest, headers, queryParams)

        then:

        result.toString().matches("[\\{\\[].*[\\}\\]]")

    }
    // /products: endpoint for products with attribute

    // GET
    def "GET products by id with headers and queryParams"() {

        given:
        ObjectMapper objectMapper = new ObjectMapper()

        Map<String, Object> agencyMap = Map.of("id", "2", "name", "prod2")
        ObjectNode resultJSON = objectMapper.valueToTree(agencyMap)

        wms.givenThat(
                get(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withJsonBody(resultJSON)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.get_products_by_id_with_headers_and_queryParams("2", headers, queryParams)

        then:

        result.toString().matches("[\\{\\[].*[\\}\\]]")

    }

    // PUT
    def "PUT to products by id with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()
        JsonNode resultJSON = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                put(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(resultJSON)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.put_to_products_by_id_with_headers_and_queryParams("test", "2", headers, queryParams)

        then:

        result.toString().matches("[\\{\\[].*[\\}\\]]")

    }

    // PATCH
    def "PATCH to products by id with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()
        JsonNode resultJSON = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                patch(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(resultJSON)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.patch_to_products_by_id_with_headers_and_queryParams("test", "2", headers, queryParams)

        then:
        result.toString().matches("[\\{\\[].*[\\}\\]]")

    }

    // DELETE
    def "DELETE from products by id with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()
        JsonNode resultJSON = objectMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                delete(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .withHeader("X-OBSERVATORY-AUTH", equalTo("headerValue"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(resultJSON)
                )
        )

        when:
        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", "headerValue")

        Map<String, List<String>> queryParams = new HashMap<>()

        Map<String, Object> result = caller.delete_from_products_by_id_with_headers_and_queryParams("2", headers, queryParams)

        then:
        result.toString().matches("[\\{\\[].*[\\}\\]]")

    }
    // /products: endpoint for products without attribute

    // GET
    def "GET products with headers and queryParams"() {

        given:
        String resultString = "prod2"
        
        wms.givenThat(
                get(urlMatching("/observatory/api/products\\\\?.*"))
                .withQueryParam("start", containing("42"))
                .withQueryParam("count", containing("42"))
                .withQueryParam("status", containing("queryValue"))
                .withQueryParam("sort", containing("queryValue"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(resultString)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()
        queryParams.computeIfAbsent("start", { k -> new ArrayList<>() } ).add("42")
        queryParams.computeIfAbsent("count", { k -> new ArrayList<>() } ).add("42")
        queryParams.computeIfAbsent("status", { k -> new ArrayList<>() } ).add("queryValue")
        queryParams.computeIfAbsent("sort", { k -> new ArrayList<>() } ).add("queryValue")

        when:

        String result = caller.get_products_with_headers_and_queryParams(headers, queryParams)

        then:

        result.matches("\\w+")

    }

    // POST
    def "POST to products with headers and queryParams"() {

        given:

        ObjectMapper objectMapper = new ObjectMapper()

        String jsonBodyRequest = objectMapper.readTree("{\"name\":\"prod1\"}")

        Integer resultInteger = 42

        wms.givenThat(
                post(urlMatching("/observatory/api/products\\\\?.*"))
                .withRequestBody(containing(jsonBodyRequest))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody(resultInteger.toString())
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Integer result = caller.post_to_products_with_headers_and_queryParams(jsonBodyRequest, headers, queryParams)

        then:

        result.toString().matches("\\d+")

    }
}