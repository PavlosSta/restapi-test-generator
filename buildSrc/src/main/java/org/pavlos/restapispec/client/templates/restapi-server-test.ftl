<#-- @ftlvariable name="api" type="org.pavlos.restapispec.interfaces.APISpec" -->
<#-- @ftlvariable name="clientPackage" type="String" -->
<#-- @ftlvariable name="clientName" type="String" -->
<#-- @ftlvariable name="testPackage" type="String" -->
<#-- @ftlvariable name="testName" type="String" -->
<#-- @ftlvariable name="serverPort" type="String" -->
package ${testPackage};

import ${clientPackage}.${clientName}
import org.json.JSONObject
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
class ${testName} extends Specification {

    private static final int SERVER_PORT = ${serverPort}

    @Shared ${clientName} caller = new ${clientName}("localhost", SERVER_PORT)

    <#list api.endpoints as endpoint>
    // ${endpoint.path}: ${endpoint.label}
    <#list endpoint.methods as method>
    // ${method.type}
    <#if method.type == "GET">
    def "GET ${endpoint.path?keep_after("/")?replace("/", "_")} <#if endpoint.attributes?first??>by ${endpoint.attributes?first} </#if>with headers and queryParams"() {

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
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("<#if queryParam.mandatory>true<#else>${queryParam.defaultBodyIfOptionalAndMissing}</#if>")
        </#if>
        </#list>
        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.get_${endpoint.path?keep_after("/")?replace("/", "_")}<#if endpoint.attributes?first??>_by_${endpoint.attributes?first}</#if>_with_headers_and_queryParams(<#if endpoint.attributes?first??>"2", </#if>headers, queryParams)

        then:

        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("\\w+")
        <#else>
        result.toString().matches("\\d+")
        </#if>

    }
    </#if>
    <#if method.type == "POST" && !endpoint.attributes?first??>
    def "POST to ${endpoint.path?keep_after("/")?replace("/", "_")} with headers and queryParams"() {

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
        <#if queryParam.mandatory>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5"))
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#if>
        </#list>

        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.post_to_${endpoint.path?keep_after("/")?replace("/", "_")}_with_headers_and_queryParams(requestBody, headers, queryParams)

        then:

        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("\\w+")
        <#else>
        result.toString().matches("\\d+")
        </#if>

    }
    </#if>
    <#if method.type == "PUT" && endpoint.attributes?first??>
    def "PUT to ${endpoint.path?keep_after("/")?replace("/", "_")} by ${endpoint.attributes?first} with headers and queryParams"() {

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
        <#if queryParam.mandatory>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5"))
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#if>
        </#list>

        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.put_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("2", requestBody, headers, queryParams)

        then:

        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("[\\{\\[].*[\\}\\]]")
        <#else>
        result.matches("\\d+")
        </#if>

    }
    </#if>
    <#if method.type == "PATCH">
    def "PATCH to ${endpoint.path?keep_after("/")?replace("/", "_")} by ${endpoint.attributes?first} with headers and queryParams"() {

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
        <#if queryParam.mandatory>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5"))
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#if>
        </#list>

        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.patch_to_${endpoint.path?keep_after("/")?replace("/", "_")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("2", requestBody, headers, queryParams)

        then:
        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("[\\{\\[].*[\\}\\]]")
        <#else>
        result.matches("\\d+")
        </#if>

    }
    </#if>
    <#if method.type == "DELETE">
    def "DELETE from ${endpoint.path?keep_after("/")?replace("/", "_")} by ${endpoint.attributes?first} with headers and queryParams"() {

        given:

        Map<String, String> headers = new HashMap<>()
        <#list method.request.headers as header>
        headers.put("${header.name}", 'headerValue')
        </#list>

        Map<String, List<Object>> queryParams = new HashMap<>()
        <#list method.request.queryParams as queryParam>
        <#if queryParam.mandatory>
        <#if queryParam.type == "String">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("queryValue")
        <#elseif queryParam.type == "Integer">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42")
        <#elseif queryParam.type == "float">
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add("42.5"))
        <#else>
        queryParams.computeIfAbsent("${queryParam.name}", { k -> new ArrayList<>() } ).add(true)
        </#if>
        </#if>
        </#list>

        when:

        <#if method.response.responseBodySchema == "JSON">Map<String, Object><#elseif method.response.responseBodySchema == "String">String<#else>Integer</#if> result = caller.delete_from_${endpoint.path?keep_after("/")?replace("/", "_")}_by_${endpoint.attributes?first}_with_headers_and_queryParams("2", headers, queryParams)

        then:
        <#if method.response.responseBodySchema == "JSON">
        result.toString().matches("[\\{\\[].*[\\}\\]]")
        <#elseif method.response.responseBodySchema == "String">
        result.matches("[\\{\\[].*[\\}\\]]")
        <#else>
        result.matches("\\d+")
        </#if>

    }
    </#if>
    </#list>
    </#list>
}