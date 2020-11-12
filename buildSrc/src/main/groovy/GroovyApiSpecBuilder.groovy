import com.github.tomakehurst.wiremock.http.Request
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import org.apache.http.client.methods.RequestBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.file.Stat
import org.pavlos.client.generator.FreeMarkerJavaCodeGenerator
import org.pavlos.implementations.*
import org.pavlos.interfaces.*

import java.nio.file.Files
import java.nio.file.Paths

class GroovyApiSpecBuilder implements Plugin<Project> {

    void apply(Project project) {
        def extension = project.extensions.create('api', GroovyApiSpecExtension)
        project.task('generator') {
            doLast {
                api {apiBuilder}
                api_baseUrl "$extension.api_baseUrl"
                api_label "$extension.api_label"

                endpoint {endpointBuilder}
                endpoint_path "/login"
                endpoint_label "login endpoint"

                method {methodBuilder}
                method_type "POST"

                requestURL {requestURLBuilder}

                parameter {parameterBuilder}
                parameter_name "username"
                parameter_type "String"
                parameter_mandatory true
                requestURL_bodyParam(parameter_build {parameterBuilder})

                parameter {parameterBuilder}
                parameter_name "password"
                parameter_type "String"
                parameter_mandatory true
                requestURL_bodyParam(parameter_build {parameterBuilder})

                method_request ( requestURL_build {requestURLBuilder} )

                response {responseBuilder}
                response_schema "JSON"

                header {headerBuilder}
                header_name "X-OBSERVATORY-AUTH"
                header_mandatory true
                response_header( header_build {headerBuilder} )

                status {statusBuilder}
                status_code "201"
                status_body "Created"
                response_status( status_build {statusBuilder} )

                method_response ( response_build {responseBuilder} )

                endpoint_method ( method_build {methodBuilder} )

                api_endpoint ( endpoint_build {endpointBuilder} )

                generate_tests ( build {apiBuilder} )
            }
        }
    }

    // Builders
    private APISpecBuilder apiBuilder
    private EndpointSpecBuilder endpointBuilder
    private HeaderSpecBuilder headerBuilder
    private MethodSpecBuilder methodBuilder
    private ParamSpecBuilder parameterBuilder
    private RequestJSONSpecBuilder requestJSONBuilder
    private RequestURLSpecBuilder requestURLBuilder
    private ResponseSpecBuilder responseBuilder
    private StatusSpecBuilder statusBuilder
    private FreeMarkerJavaCodeGenerator javaGenerator

    // Freemarker Configuration
    private Configuration cfg

    // Generate Tests
    void generate_tests(APISpec api) {

        String projectPath = System.getProperty("user.dir")

        // Creates a Configuration instance
        cfg = new Configuration(Configuration.VERSION_2_3_30);

        cfg.setClassForTemplateLoading(FreeMarkerJavaCodeGenerator.class, "templates");

        // Specifies the source where the template files come from.
        try {
            System.out.println(projectPath + "/buildSrc/src/main/java/org/pavlos/client/templates")
            cfg.setDirectoryForTemplateLoading(new File(projectPath + "/buildSrc/src/main/java/org/pavlos/client/templates"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);

        // Wrap unchecked exceptions thrown during template processing into TemplateException-s:
        cfg.setWrapUncheckedExceptions(true);

        // Do not fall back to higher scopes when reading a null loop variable:
        cfg.setFallbackOnNullLoopVariable(false);

        javaGenerator = new FreeMarkerJavaCodeGenerator(api, cfg)

        String clientFolder = "/buildSrc/src/test/groovy/"
        String clientPackage = "restapiclient"
        String clientName = "TestClient"
        String clientPath = projectPath + clientFolder + clientPackage.replaceAll("\\.","/")
        String clientFile = clientPath + "/" + clientName + ".groovy"

        String serverFolder = "/buildSrc/src/test/groovy/"
        String serverPackage = "restapiserver"
        String serverName = "TestServer"
        String serverPath = projectPath + serverFolder + serverPackage.replaceAll("\\.","/")
        String serverFile = serverPath + "/" + serverName + ".groovy"

        try {
            Files.createDirectories(Paths.get(serverPath))
            Files.createDirectories(Paths.get(clientPath))
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        javaGenerator.generateClient(new File(projectPath + "/buildSrc/src/main/java/org/pavlos/client/RestAPIClient.java"), new File(clientFile))
        javaGenerator.generateServer(new File(serverFile))

        System.out.println("Client tests saved at: " + clientPath)
        System.out.println("Server tests saved at: " + serverPath)

    }

    // API
    void api(Closure c) {
        apiBuilder = new APISpecBuilder()
        c.call(this)
    }

    void api_baseUrl(String url) {
        apiBuilder.setBaseUrl(url);
    }

    void api_label(String label) {
        apiBuilder.setLabel(label);
    }

    void api_endpoint(EndpointSpec endpoint) {
        apiBuilder.addEndpoint(endpoint)
    }

    // Endpoint
    void endpoint(Closure c) {
        endpointBuilder = new EndpointSpecBuilder()
        c.call(this)
    }

    void endpoint_path(String path) {
        endpointBuilder.setPath(path)
    }

    void endpoint_label(String label) {
        endpointBuilder.setLabel(label)
    }

    void endpoint_method(MethodSpec method) {
        endpointBuilder.addMethod(method)
    }

    // Method
    void method(Closure c) {
        methodBuilder = new MethodSpecBuilder()
        c.call(this)
    }

    void method_type(String type) {
        methodBuilder.setType(type)
    }

    void method_request(RequestSpec request) {
        methodBuilder.setRequest(request)
    }

    void method_response(ResponseSpec response) {
        methodBuilder.setResponse(response)
    }

    // Header
    void header(Closure c) {
        headerBuilder = new HeaderSpecBuilder()
        c.call(this)
    }

    void header_name(String name) {
        headerBuilder.setName(name)
    }

    void header_mandatory(boolean mandatory) {
        headerBuilder.setMandatory(mandatory)
    }

    // RequestJSON
    void requestJSON(Closure c) {
        requestJSONBuilder = new RequestJSONSpecBuilder()
        c.call(this)
    }

    // RequestURL
    void requestURL(Closure c) {
        requestURLBuilder = new RequestURLSpecBuilder()
        c.call(this)
    }

    void requestURL_bodyParam(ParameterSpec bodyParam) {
        requestURLBuilder.addBodyParam(bodyParam)
    }

    // Response
    void response(Closure c) {
        responseBuilder = new ResponseSpecBuilder()
        c.call(this)
    }

    void response_status(StatusSpec status) {
        responseBuilder.addStatus(status)
    }

    void response_header(HeaderSpec header) {
        responseBuilder.addHeader(header)
    }

    void response_schema(String schema) {
        responseBuilder.addResponseBodySchema(schema)
    }

    // Parameter
    void parameter(Closure c) {
        parameterBuilder = new ParamSpecBuilder()
        c.call(this)
    }

    void parameter_name(String name) {
        parameterBuilder.setName(name)
    }

    void parameter_type(String type) {
        parameterBuilder.setType(type)
    }

    void parameter_mandatory(boolean mandatory) {
        parameterBuilder.setMandatory(mandatory)
    }

    // Status
    void status(Closure c) {
        statusBuilder = new StatusSpecBuilder()
        c.call(this)
    }

    void status_code(String code) {
        statusBuilder.setCode(code)
    }

    void status_body(String body) {
        statusBuilder.setBody(body)
    }

    // Misc
    void methodMissing(String name) {

    }

    // Build Specs

    APISpec build(Closure c) {
        apiBuilder.build()
    }

    EndpointSpec endpoint_build(Closure c) {
        endpointBuilder.build()
    }

    MethodSpec method_build(Closure c) {
        methodBuilder.build()
    }

    HeaderSpec header_build(Closure c) {
        headerBuilder.build()
    }

    ParameterSpec parameter_build(Closure c) {
        parameterBuilder.build()
    }

    RequestJSONSpec requestJSON_build(Closure c) {
        requestJSONBuilder.build()
    }

    RequestURLSpec requestURL_build(Closure c) {
        requestURLBuilder.build()
    }

    ResponseSpec response_build(Closure c) {
        responseBuilder.build()
    }

    StatusSpec status_build(Closure c) {
        statusBuilder.build()
    }

}