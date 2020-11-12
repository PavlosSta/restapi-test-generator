package restapiserver

import org.pavlos.client.RestAPIClient
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

        ObjectMapper responseMapper = new ObjectMapper()

        String jsonBodyRequest = responseMapper.readTree("{\"name\":\"prod1\"}")

        JsonNode resultJSON = responseMapper.readTree("{\"value\":\"ok\"}")

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
}