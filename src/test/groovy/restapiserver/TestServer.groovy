package restapiserver

import client.RestAPIClient
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
    def "GET products by id"() {
        given:
        ObjectMapper objectMapper = new ObjectMapper()

        Map<String, Object> agencyMap = Map.of("id", '2', "name", "prod2")

        ObjectNode productJSON = objectMapper.valueToTree(agencyMap)

        wms.givenThat(
                get(
                        urlEqualTo("/rest/api/products/id")
                ).willReturn(
                        aResponse().withJsonBody(productJSON)
                )
        )

        when:
        Map product = caller.get_products_by_id("2")

        then:
        product.get("id") == '2'
        product.get("name") == "prod2"

    }

    // PUT

    // PATCH

    // DELETE
    // /products: endpoint for products without attribute

    // GET
    def "GET products}"() {
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
                        urlEqualTo("/rest/api/products")
                ).willReturn(
                        aResponse().withJsonBody(productJSON)
                )
        )

        when:
        Map products = caller.get_products()

        then:
        products.get("products").toString() == "[[id:1, name:prod1], [id:2, name:prod2], [id:3, name:prod3], [id:4, name:prod4]]" ||
                products.get("products").toString() == "[[name:prod1, id:1], [name:prod2, id:2], [name:prod3, id:3], [name:prod4, id:4]]"

    }

    // POST
}