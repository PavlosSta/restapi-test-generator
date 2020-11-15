import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.pavlos.restapispec.client.generator.FreeMarkerJavaCodeGenerator
import org.pavlos.restapispec.implementations.*
import org.pavlos.restapispec.interfaces.*

import java.nio.file.Files
import java.nio.file.Paths

class GroovyApiSpecBuilder implements Plugin<Project> {

    void apply(Project project) {
        def apiExtension = project.extensions.create('api', ApiExtension)
        def endpointExtension = project.extensions.create('endpoint', EndpointExtension)
        def methodExtension = project.extensions.create('method', MethodExtension)
        def parameterExtension = project.extensions.create('parameter', ParameterExtension)
        def requestURLExtension = project.extensions.create('requestURL', RequestURLExtension)
        def requestJSONExtension = project.extensions.create('requestJSON', RequestJSONExtension)
        def responseExtension = project.extensions.create('response', ResponseExtension)
        def headerExtension = project.extensions.create('header', HeaderExtension)
        def statusExtension = project.extensions.create('httpStatus', StatusExtension)

        project.task('generate') {
            doLast {
                api {apiBuilder}
                api_baseUrl "$apiExtension.baseUrl"
                api_label "$apiExtension.label"

                List<String> list = "$apiExtension.numbers".getValues()[0]

                println(list)
                list.each {
                    println "Item: $it" // `it` is an implicit parameter corresponding to the current element
                }
                
                endpoint {endpointBuilder}
                endpoint_path "$endpointExtension.path"
                endpoint_label "$endpointExtension.label"

                method {methodBuilder}
                method_type "$methodExtension.type"

                requestURL {requestURLBuilder}

                parameter {parameterBuilder}
                parameter_name "$parameterExtension.name"
                parameter_type "$parameterExtension.type"
                parameter_mandatory Boolean.parseBoolean("$parameterExtension.mandatory")
                requestURL_bodyParam(parameter_build {parameterBuilder})

                /*

                parameter {parameterBuilder}
                parameter_name "password"
                parameter_type "String"
                parameter_mandatory true
                requestURL_bodyParam(parameter_build {parameterBuilder})

                */

                method_request ( requestURL_build {requestURLBuilder} )

                response {responseBuilder}
                response_schema "$responseExtension.schema"

                header {headerBuilder}
                header_name "$headerExtension.name"
                header_mandatory Boolean.parseBoolean("$headerExtension.mandatory")
                response_header( header_build {headerBuilder} )

                status {statusBuilder}
                status_code "$statusExtension.code"
                status_body "$statusExtension.body"
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
            cfg.setDirectoryForTemplateLoading(new File(projectPath + "/buildSrc/src/main/java/org/pavlos/restapispec/client/templates"))
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

        String clientFolder = "/ApiSpecTestProject/src/test/groovy/"
        String clientPackage = "testproject/restapiclient"
        String clientName = "TestClient"
        String clientPath = projectPath + clientFolder + clientPackage
        String clientFile = clientPath + "/" + clientName + ".groovy"

        String serverFolder = "/ApiSpecTestProject/src/test/groovy/"
        String serverPackage = "testproject/restapiserver"
        String serverName = "TestServer"
        String serverPath = projectPath + serverFolder + serverPackage
        String serverFile = serverPath + "/" + serverName + ".groovy"

        try {
            Files.createDirectories(Paths.get(projectPath + "/ApiSpecTestProject/src/main/java/org/pavlos/testproject/client"))
            Files.createDirectories(Paths.get(serverPath))
            Files.createDirectories(Paths.get(clientPath))
        } catch (IOException e) {
            System.out.println("Cannot create directories");
            e.printStackTrace();
        }

        javaGenerator.generateClient(new File(projectPath + "/ApiSpecTestProject/src/main/java/org/pavlos/testproject/client/RestAPIClient.java"), new File(clientFile))
        javaGenerator.generateServer(new File(serverFile))

        System.out.println("RestAPI Client saved at: " + projectPath + "/ApiSpecTestProject/src/main/java/org/pavlos/testproject/client")
        System.out.println("Client tests saved at:   " + clientPath)
        System.out.println("Server tests saved at:   " + serverPath)

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