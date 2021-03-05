package hello.restapitests;

import hello.restapiclient.RestAPIClient
import org.json.JSONObject
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class TestServer extends Specification {

    private static final int SERVER_PORT = 8765

    @Shared RestAPIClient caller = new RestAPIClient("localhost", SERVER_PORT)

    // /login: login endpoint
    /*
        the endpoint for user login
    */
    // POST
    def "POST to login"() {

        given:

        String requestBody = "username=bodyParamValue&password=bodyParamValue"


        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.post_to_login(requestBody, headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map



    }
    def "POST to login without mandatory bodyParam: username"() {

        given:

        String requestBody = "password=bodyParamValue"


        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_login(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "POST to login without mandatory bodyParam: password"() {

        given:

        String requestBody = "username=bodyParamValue&"


        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_login(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    // /products: products endpoint
    // GET
    def "GET products"() {

        given:

        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()
        queryParams.computeIfAbsent("start", { k -> new ArrayList<>() } ).add("0")
        queryParams.computeIfAbsent("count", { k -> new ArrayList<>() } ).add("20")
        queryParams.computeIfAbsent("status", { k -> new ArrayList<>() } ).add("ACTIVE")
        queryParams.computeIfAbsent("sort", { k -> new ArrayList<>() } ).add("id%7CDESC")
        when:

        Map<String, Object> result = caller.get_products(headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map

        jsonResult.total instanceof Number
        jsonResult.count instanceof Number
        jsonResult.start instanceof Number
        jsonResult.products instanceof List


    }
    // POST
    def "POST to products"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.post_to_products(requestBody, headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map



    }
    def "POST to products without mandatory bodyParam: name"() {

        given:

        String requestBody = "description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_products(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "POST to products without mandatory bodyParam: description"() {

        given:

        String requestBody = "name=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_products(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "POST to products without mandatory bodyParam: category"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_products(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "POST to products without mandatory bodyParam: tags"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_products(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "POST to products without mandatory bodyParam: withdrawn"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_products(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "POST to products without mandatory header: X-OBSERVATORY-AUTH"() {
        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_products(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    // /products: products endpoint
    // GET
    def "GET products by attributes"() {

        given:

        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()
        when:

        Map<String, Object> result = caller.get_products_by_attributes("2", "2", headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map



    }
    // PUT
    def "PUT to products by attributes"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.put_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map



    }
    def "PUT to products without mandatory bodyParam: name"() {

        given:

        String requestBody = "description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.put_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PUT to products without mandatory bodyParam: description"() {

        given:

        String requestBody = "name=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.put_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PUT to products without mandatory bodyParam: category"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.put_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PUT to products without mandatory bodyParam: tags"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.put_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PUT to products without mandatory bodyParam: withdrawn"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.put_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PUT to products without mandatory header: X-OBSERVATORY-AUTH"() {
        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.put_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    // PATCH
    def "PATCH to products by attributes"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.patch_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map



    }
    def "PATCH to products without mandatory bodyParam: name"() {

        given:

        String requestBody = "description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.patch_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PATCH to products without mandatory bodyParam: description"() {

        given:

        String requestBody = "name=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.patch_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PATCH to products without mandatory bodyParam: category"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.patch_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PATCH to products without mandatory bodyParam: tags"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.patch_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PATCH to products without mandatory bodyParam: withdrawn"() {

        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&"


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.patch_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    def "PATCH to products without mandatory header: X-OBSERVATORY-AUTH"() {
        given:

        String requestBody = "name=bodyParamValue&description=bodyParamValue&category=bodyParamValue&tags=bodyParamValue&withdrawn=false"


        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.patch_to_products_by_attributes("2", "2",  requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    // DELETE
    def "DELETE from products by attributes"() {

        given:

        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.delete_from_products_by_attributes("2", "2",  headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map



    }
    def "DELETE from products by attributes without mandatory header: X-OBSERVATORY-AUTH"() {
        given:

        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.delete_from_products_by_attributes("2", "2",  headers, queryParams)

        then:
        thrown RuntimeException

    }
    // /logout: logout endpoint
    /*
        the endpoint for user logout
    */
    // POST
    def "POST to logout"() {

        given:

        String requestBody = ""


        Map<String, String> headers = new HashMap<>()
        headers.put("X-OBSERVATORY-AUTH", 'headerValue')

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        Map<String, Object> result = caller.post_to_logout(requestBody, headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map



    }
    def "POST to logout without mandatory header: X-OBSERVATORY-AUTH"() {
        given:

        String requestBody = ""


        Map<String, String> headers = new HashMap<>()

        Map<String, List<Object>> queryParams = new HashMap<>()

        when:

        caller.post_to_logout(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
}