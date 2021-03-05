package hello.restapimocktests;

import hello.restapiclient.RestAPIClient
import org.json.JSONObject
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
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
class MockServer extends Specification {

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
    /*
        the endpoint for user login
    */
    // POST
    def "POST to login"() {

        given:

        String requestBody = "username=bodyParamValue&password=bodyParamValue"


        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("/observatory/api/login\\\\?.*"))
                .withRequestBody(containing(requestBody))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withJsonBody(resultJSON)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<String>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.post_to_login(requestBody, headers, queryParams)


        then:

        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        jsonSlurper.parseText(result.get("body").toString())

        noExceptionThrown()


    }
    // /products: products endpoint
    // GET
    def "GET products"() {

        given:

        ObjectMapper responseMapper = new ObjectMapper()

        Map<String, Object> agencyMap = Map.of("id", "2", "name", "prod2")
        ObjectNode resultJSON = responseMapper.valueToTree(agencyMap)

        wms.givenThat(
                get(urlMatching("/observatory/api/products\\\\?.*"))
                .withQueryParam("start", containing("42"))
                .withQueryParam("count", containing("42"))
                .withQueryParam("status", containing("queryValue"))
                .withQueryParam("sort", containing("queryValue"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withJsonBody(resultJSON)
                )
        )

        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()
        queryParams.computeIfAbsent("start", { k -> new ArrayList<>() } ).add("42")
        queryParams.computeIfAbsent("count", { k -> new ArrayList<>() } ).add("42")
        queryParams.computeIfAbsent("status", { k -> new ArrayList<>() } ).add("queryValue")
        queryParams.computeIfAbsent("sort", { k -> new ArrayList<>() } ).add("queryValue")

        when:

        Map<String, Object> result = caller.get_products(headers, queryParams)


        then:

        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        jsonSlurper.parseText(result.get("body").toString())

        noExceptionThrown()



    }
    // POST
    def "POST to products"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("/observatory/api/products\\\\?.*"))
                .withRequestBody(containing(requestBody))
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

        Map<String, Object> result = caller.post_to_products(requestBody, headers, queryParams)


        then:

        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        jsonSlurper.parseText(result.get("body").toString())

        noExceptionThrown()


    }
    // /products: products endpoint
    // GET
    def "GET products by attributes"() {

        given:

        ObjectMapper responseMapper = new ObjectMapper()

        Map<String, Object> agencyMap = Map.of("id", "2", "name", "prod2")
        ObjectNode resultJSON = responseMapper.valueToTree(agencyMap)

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

        Map<String, Object> result = caller.get_products_by_attributes("2", "2", headers, queryParams)


        then:

        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        jsonSlurper.parseText(result.get("body").toString())

        noExceptionThrown()



    }
    // PUT
    def "PUT to products by attributes"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                put(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .withRequestBody(containing(requestBody))
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

        Map<String, Object> result = caller.put_to_products_by_attributes("2", "2", requestBody, headers, queryParams)


        then:

        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        jsonSlurper.parseText(result.get("body").toString())

        noExceptionThrown()


    }
    // PATCH
    def "PATCH to products by attributes"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                patch(urlMatching("/observatory/api/products/.*\\\\?.*"))
                .withRequestBody(containing(requestBody))
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

        Map<String, Object> result = caller.patch_to_products_by_attributes("2", "2", requestBody, headers, queryParams)


        then:
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        jsonSlurper.parseText(result.get("body").toString())

        noExceptionThrown()


    }
    // DELETE
    def "DELETE from products by attributes"() {

        given:

        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                delete(urlMatching("/observatory/api/products/.*\\\\?.*"))
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

        Map<String, Object> result = caller.delete_from_products_by_attributes("2", "2", headers, queryParams)


        then:
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        jsonSlurper.parseText(result.get("body").toString())

        noExceptionThrown()


    }
    // /logout: logout endpoint
    /*
        the endpoint for user logout
    */
    // POST
    def "POST to logout"() {

        given:

        String requestBody = ""


        ObjectMapper responseMapper = new ObjectMapper()
        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")

        wms.givenThat(
                post(urlMatching("/observatory/api/logout\\\\?.*"))
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

        Map<String, Object> result = caller.post_to_logout(requestBody, headers, queryParams)


        then:

        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        jsonSlurper.parseText(result.get("body").toString())

        noExceptionThrown()


    }
}