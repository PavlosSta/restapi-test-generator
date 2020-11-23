import org.pavlos.restapispec.client.generator.FreeMarkerJavaCodeGenerator
import org.pavlos.restapispec.implementations.APISpecBuilder
import org.pavlos.restapispec.implementations.EndpointSpecBuilder
import org.pavlos.restapispec.implementations.HeaderSpecBuilder
import org.pavlos.restapispec.implementations.MethodSpecBuilder
import org.pavlos.restapispec.implementations.ParamSpecBuilder
import org.pavlos.restapispec.implementations.RequestJSONSpecBuilder
import org.pavlos.restapispec.implementations.RequestURLSpecBuilder
import org.pavlos.restapispec.implementations.ResponseSpecBuilder
import org.pavlos.restapispec.implementations.StatusSpecBuilder

class ApiExtension {

    String clientFolder
    String clientPackage
    String clientName

    String testFolder
    String testPackage
    String testName

    String mockFolder
    String mockPackage
    String mockName

    String serverPort

    APISpecBuilder apiBuilder = new APISpecBuilder()
    private EndpointSpecBuilder endpointBuilder
    private HeaderSpecBuilder headerBuilder
    private MethodSpecBuilder methodBuilder
    private ParamSpecBuilder parameterBuilder
    private RequestJSONSpecBuilder requestJSONBuilder
    private RequestURLSpecBuilder requestURLBuilder
    private ResponseSpecBuilder responseBuilder
    private StatusSpecBuilder statusBuilder

    private String whoseLabel = "API"
    private String whoCalls

    void baseUrl(String baseUrl) {
        apiBuilder.setBaseUrl(baseUrl)
    }

    void label(String label) {
        if (whoseLabel == "API") {
            apiBuilder.setLabel(label)
        }
        else if (whoseLabel == "Endpoint") {
            endpointBuilder.setLabel(label)
            whoseLabel = "API"
        }
    }

    void endpoint(String path, Closure configuration) {
        endpointBuilder = new EndpointSpecBuilder()
        endpointBuilder.setPath(path)
        whoseLabel = "Endpoint"
        configuration.call(this)
        apiBuilder.addEndpoint(endpointBuilder.build())
    }

    void endpoint(String path, String attribute, Closure configuration) {
        endpointBuilder = new EndpointSpecBuilder()
        endpointBuilder.setPath(path)
        endpointBuilder.addAttribute(attribute)
        whoseLabel = "Endpoint"
        configuration.call(this)
        apiBuilder.addEndpoint(endpointBuilder.build())
    }

    void endpointLabel(String label) {
        endpointBuilder.setLabel(label)
    }

    void description(String description) {
        endpointBuilder.addDescription(description)
    }

    void method(String type, Closure configuration) {
        methodBuilder = new MethodSpecBuilder()
        methodBuilder.setType(type)
        configuration.call(this)
        endpointBuilder.addMethod(methodBuilder.build())
    }

    void request(String type, Closure configuration) {
        if (type == "JSON") {
            whoCalls = "reqJSON"
            requestJSONBuilder = new RequestJSONSpecBuilder()
            configuration.call(this)
            methodBuilder.setRequest(requestJSONBuilder.build())
        }
        else if (type == "URL") {
            whoCalls = "reqURL"
            requestURLBuilder = new RequestURLSpecBuilder()
            configuration.call(this)
            methodBuilder.setRequest(requestURLBuilder.build())
        }
    }

    void response(String schema, Closure configuration) {
        whoCalls = "response"
        responseBuilder = new ResponseSpecBuilder()
        responseBuilder.addResponseBodySchema(schema)
        configuration.call(this)
        methodBuilder.setResponse(responseBuilder.build())
    }

    void withBodyParameter(String name, String type) {
        parameterBuilder = new ParamSpecBuilder()
        parameterBuilder.setName(name)
        parameterBuilder.setType(type)
        parameterBuilder.setMandatory(true)

        if (whoCalls == "reqURL") {
            requestURLBuilder.addBodyParam(parameterBuilder.build())
        }
        else if (whoCalls == "reqJSON") {
            requestJSONBuilder.addBodyParam(parameterBuilder.build())
        }

    }

    void withBodyParameter(String name, String type, Object defaultValueIfOptionalAndMissing) {
        parameterBuilder = new ParamSpecBuilder()
        parameterBuilder.setName(name)
        parameterBuilder.setType(type)
        parameterBuilder.setMandatory(false)
        parameterBuilder.setDefaultValue(defaultValueIfOptionalAndMissing.toString())

        if (whoCalls == "reqURL") {
            requestURLBuilder.addBodyParam(parameterBuilder.build())
        }
        else if (whoCalls == "reqJSON") {
            requestJSONBuilder.addBodyParam(parameterBuilder.build())
        }

    }

    void withQueryParameter(String name, String type) {
        parameterBuilder = new ParamSpecBuilder()
        parameterBuilder.setName(name)
        parameterBuilder.setType(type)
        parameterBuilder.setMandatory(true)

        if (whoCalls == "reqURL") {
            requestURLBuilder.addQueryParam(parameterBuilder.build())
        }
        else if (whoCalls == "reqJSON") {
            requestJSONBuilder.addQueryParam(parameterBuilder.build())
        }

    }

    void withQueryParameter(String name, String type, Object defaultValueIfOptionalAndMissing) {
        parameterBuilder = new ParamSpecBuilder()
        parameterBuilder.setName(name)
        parameterBuilder.setType(type)
        parameterBuilder.setMandatory(false)
        parameterBuilder.setDefaultValue(defaultValueIfOptionalAndMissing.toString())

        if (whoCalls == "reqURL") {
            requestURLBuilder.addQueryParam(parameterBuilder.build())
        }
        else if (whoCalls == "reqJSON") {
            requestJSONBuilder.addQueryParam(parameterBuilder.build())
        }

    }

    void withHeader(String name) {
        headerBuilder = new HeaderSpecBuilder()
        headerBuilder.setName(name)
        headerBuilder.setMandatory(true)

        if (whoCalls == "reqURL") {
            requestURLBuilder.addHeader(headerBuilder.build())
        }
        else if (whoCalls == "reqJSON") {
            requestJSONBuilder.addHeader(headerBuilder.build())
        }
        else if (whoCalls == "response") {
            responseBuilder.addHeader(headerBuilder.build())
        }

    }

    void withHeader(String name, String defaultValueIfOptionalAndMissing) {
        headerBuilder = new HeaderSpecBuilder()
        headerBuilder.setName(name)
        headerBuilder.setMandatory(false)
        headerBuilder.setDefaultValueIfOptionalAndMissing(defaultValueIfOptionalAndMissing)

        if (whoCalls == "reqURL") {
            requestURLBuilder.addHeader(headerBuilder.build())
        }
        else if (whoCalls == "reqJSON") {
            requestJSONBuilder.addHeader(headerBuilder.build())
        }
        else if (whoCalls == "response") {
            responseBuilder.addHeader(headerBuilder.build())
        }

    }

    void withStatus(Integer code, Closure configuration) {
        statusBuilder = new StatusSpecBuilder()
        statusBuilder.setCode(code.toString())
        configuration.call(this)
        responseBuilder.addStatus(statusBuilder.build())
    }

    void body(String body) {
        statusBuilder.setBody(body)
    }

}
