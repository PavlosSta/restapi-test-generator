<#-- @ftlvariable name="api" type="org.pavlos.restapispec.interfaces.APISpec" -->
<#-- @ftlvariable name="clientPackage" type="String" -->
<#-- @ftlvariable name="clientName" type="String" -->
<#-- @ftlvariable name="testPackage" type="String" -->
<#-- @ftlvariable name="testName" type="String" -->
<#-- @ftlvariable name="serverPort" type="String" -->
package ${testPackage};

import ${clientPackage}.${clientName}
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
class ${testName} extends Specification {

    private static final int SERVER_PORT = ${serverPort}

    @Shared ${clientName} caller = new ${clientName}("localhost", SERVER_PORT)

    <#list api.endpoints as endpoint>
    // ${endpoint.path}<#if endpoint.label??>: ${endpoint.label}</#if>
    <#if endpoint.description??>
    /*
        ${endpoint.description}
    */
    </#if>
    <#list endpoint.methods as method>
    // ${method.type}
    <#if method.type == "GET">
    def "GET ${endpoint.path?keep_after("/")?replace("/", "_")}<#if endpoint.attributes?first??> by attributes</#if>"() {

        given:

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>
        when:

        Map<String, Object> result = caller.get_${endpoint.path?keep_after("/")?replace("/", "_")}<#if endpoint.attributes?first??>_by_attributes</#if>(<#list endpoint.attributes as attr>"2", </#list>headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        <#if method.response.responseBodySchema == "JSON">
        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map

        <#list method.response.bodyAttributes as key,value>
        <#if value == "null">
        jsonResult.key == null
        <#else>
        jsonResult.${key} instanceof ${value}
        </#if>
        </#list>
        <#elseif method.response.responseBodySchema == "Text">
        result.get("body").toString().matches("\\w+")
        </#if>

        <#list method.response.headers as responseHeader>
        resultHeaders.containsKey("${responseHeader.name}")
        </#list>

    }
    <#list method.request.queryParams as queryParamMandatory>
    <#if queryParamMandatory.mandatory>
    def "GET ${endpoint.path?keep_after("/")?replace("/", "_")}<#if endpoint.attributes?first??> by attributes</#if> without mandatory queryParam: ${queryParamMandatory.name}"() {
        given:

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.name != queryParamMandatory.name>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#if>
        </#list>

        when:

        caller.get_${endpoint.path?keep_after("/")?replace("/", "_")}<#if endpoint.attributes?first??>_by_attributes</#if>(<#list endpoint.attributes as attr>"2", </#list>headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    <#list method.request.headers as headerMandatory>
    <#if headerMandatory.mandatory>
    def "GET ${endpoint.path?keep_after("/")?replace("/", "_")}<#if endpoint.attributes?first??> by attributes</#if> without mandatory header: ${headerMandatory.name}"() {
        given:

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        <#if header.name != headerMandatory.name>
        headers.put("${header.name}", 'headerValue')
        </#if>
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        caller.get_${endpoint.path?keep_after("/")?replace("/", "_")}<#if endpoint.attributes?first??>_by_attributes</#if>(<#list endpoint.attributes as attr>"2", </#list>headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    </#if>
    <#if method.type == "POST" && !endpoint.attributes?first??>
    def "POST to ${endpoint.path?keep_after("/")?replace("/", "_")}"() {

        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        Map<String, Object> result = caller.post_to_${endpoint.path?keep_after("/")?replace("/", "_")}(requestBody, headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        <#if method.response.responseBodySchema == "JSON">
        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map

        <#list method.response.bodyAttributes as key,value>
        jsonResult.${key} instanceof ${value}
        </#list>
        <#elseif method.response.responseBodySchema == "Text">
        result.get("body").toString().matches("\\w+")
        </#if>

        <#list method.response.headers as responseHeader>
        resultHeaders.containsKey("${responseHeader.name}")
        </#list>

    }
    <#list method.request.bodyParams as bodyParamMandatory>
    <#if bodyParamMandatory.mandatory>
    def "POST to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory bodyParam: ${bodyParamMandatory.name}"() {

        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.name != bodyParamMandatory.name><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#if></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.name != bodyParamMandatory.name>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        caller.post_to_${endpoint.path?keep_after("/")?replace("/", "_")}(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    <#list method.request.queryParams as queryParamMandatory>
    <#if queryParamMandatory.mandatory>
    def "POST to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory queryParam: ${queryParamMandatory.name}"() {
        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.name != queryParamMandatory.name>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#if>
        </#list>

        when:

        caller.post_to_${endpoint.path?keep_after("/")?replace("/", "_")}(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    <#list method.request.headers as headerMandatory>
    <#if headerMandatory.mandatory>
    def "POST to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory header: ${headerMandatory.name}"() {
        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        <#if header.name != headerMandatory.name>
        headers.put("${header.name}", 'headerValue')
        </#if>
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        caller.post_to_${endpoint.path?keep_after("/")?replace("/", "_")}(requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    </#if>
    <#if method.type == "PUT" && endpoint.attributes?first??>
    def "PUT to ${endpoint.path?keep_after("/")?replace("/", "_")} by attributes"() {

        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        Map<String, Object> result = caller.put_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> requestBody, headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        <#if method.response.responseBodySchema == "JSON">
        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map

        <#list method.response.bodyAttributes as key,value>
        jsonResult.${key} instanceof ${value}
        </#list>
        <#elseif method.response.responseBodySchema == "Text">
        result.get("body").toString().matches("\\w+")
        </#if>

        <#list method.response.headers as responseHeader>
        resultHeaders.containsKey("${responseHeader.name}")
        </#list>

    }
    <#list method.request.bodyParams as bodyParamMandatory>
    <#if bodyParamMandatory.mandatory>
    def "PUT to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory bodyParam: ${bodyParamMandatory.name}"() {

        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.name != bodyParamMandatory.name><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#if></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.name != bodyParamMandatory.name>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        caller.put_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    <#list method.request.queryParams as queryParamMandatory>
    <#if queryParamMandatory.mandatory>
    def "PUT to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory queryParam: ${queryParamMandatory.name}"() {
        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.name != queryParamMandatory.name>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#if>
        </#list>

        when:

        caller.put_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    <#list method.request.headers as headerMandatory>
    <#if headerMandatory.mandatory>
    def "PUT to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory header: ${headerMandatory.name}"() {
        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        <#if header.name != headerMandatory.name>
        headers.put("${header.name}", 'headerValue')
        </#if>
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        caller.put_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    </#if>
    <#if method.type == "PATCH">
    def "PATCH to ${endpoint.path?keep_after("/")?replace("/", "_")} by attributes"() {

        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        Map<String, Object> result = caller.patch_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> requestBody, headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        <#if method.response.responseBodySchema == "JSON">
        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map

        <#list method.response.bodyAttributes as key,value>
        jsonResult.${key} instanceof ${value}
        </#list>
        <#elseif method.response.responseBodySchema == "Text">
        result.get("body").toString().matches("\\w+")
        </#if>

        <#list method.response.headers as responseHeader>
        resultHeaders.containsKey("${responseHeader.name}")
        </#list>

    }
    <#list method.request.bodyParams as bodyParamMandatory>
    <#if bodyParamMandatory.mandatory>
    def "PATCH to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory bodyParam: ${bodyParamMandatory.name}"() {

        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.name != bodyParamMandatory.name><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#if></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.name != bodyParamMandatory.name>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        caller.patch_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    <#list method.request.queryParams as queryParamMandatory>
    <#if queryParamMandatory.mandatory>
    def "PATCH to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory queryParam: ${queryParamMandatory.name}"() {
        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.name != queryParamMandatory.name>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#if>
        </#list>

        when:

        caller.patch_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    <#list method.request.headers as headerMandatory>
    <#if headerMandatory.mandatory>
    def "PATCH to ${endpoint.path?keep_after("/")?replace("/", "_")} without mandatory header: ${headerMandatory.name}"() {
        given:

        <#if method.request.contentType == "application/x-www-form-urlencoded">
        String requestBody = "<#list method.request.bodyParams as bodyParam><#if bodyParam.type == "String">${bodyParam.name}=bodyParamValue<#elseif bodyParam.type == "Integer">${bodyParam.name}=42<#elseif bodyParam.type == "float">${bodyParam.name}=42.5<#else>${bodyParam.name}=false</#if><#sep>&</#sep></#list>"

        <#else>
        String requestBody = new JSONObject()
            <#list method.request.bodyParams as bodyParam>
            <#if bodyParam.type == "String">
            .put("${bodyParam.name}", "bodyParamValue")
            <#elseif bodyParam.type == "Integer">
            .put("${bodyParam.name}", "42")
            <#elseif bodyParam.type == "float">
            .put("${bodyParam.name}", "42.5")
            <#else>
            .put("${bodyParam.name}", true)
            </#if>
            </#list>
            .toString();

        </#if>

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        <#if header.name != headerMandatory.name>
        headers.put("${header.name}", 'headerValue')
        </#if>
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        caller.patch_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> requestBody, headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    </#if>
    <#if method.type == "DELETE">
    def "DELETE from ${endpoint.path?keep_after("/")?replace("/", "_")} by attributes"() {

        given:

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        Map<String, Object> result = caller.delete_from_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> headers, queryParams)

        Map<String, Object> resultHeaders = result.get("headers") as Map<String, Object>

        then:

        println("Body:")
        println(result.get("body"))
        println("Headers:")
        println(resultHeaders)

        <#if method.response.responseBodySchema == "JSON">
        def json = JsonOutput.toJson(result.get("body"))
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX);
        def jsonResult = jsonSlurper.parseText(json)

        noExceptionThrown()

        jsonResult instanceof Map

        <#list method.response.bodyAttributes as key,value>
        jsonResult.${key} instanceof ${value}
        </#list>
        <#elseif method.response.responseBodySchema == "Text">
        result.get("body").toString().matches("\\w+")
        </#if>

        <#list method.response.headers as responseHeader>
        resultHeaders.containsKey("${responseHeader.name}")
        </#list>

    }
    <#list method.request.queryParams as queryParamMandatory>
    <#if queryParamMandatory.mandatory>
    def "DELETE from ${endpoint.path?keep_after("/")?replace("/", "_")} by attributes without mandatory queryParam: ${queryParamMandatory.name}"() {
        given:

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.name != queryParamMandatory.name>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#if>
        </#list>

        when:

        caller.delete_from_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    <#list method.request.headers as headerMandatory>
    <#if headerMandatory.mandatory>
    def "DELETE from ${endpoint.path?keep_after("/")?replace("/", "_")} by attributes without mandatory header: ${headerMandatory.name}"() {
        given:

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        <#if header.name != headerMandatory.name>
        headers.put("${header.name}", 'headerValue')
        </#if>
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>queryValue<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>42.5<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>false<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>

        when:

        caller.delete_from_${endpoint.path?keep_after("/")?replace("/", "_")}_by_attributes(<#list endpoint.attributes as attr>"2", </#list> headers, queryParams)

        then:
        thrown RuntimeException

    }
    </#if>
    </#list>
    </#if>
    </#list>
    </#list>
}