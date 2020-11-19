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

        project.task('generate') {
            doLast {
                api {apiBuilder}
                api_baseUrl "$apiExtension.baseUrl"
                api_label "$apiExtension.label"

                apiExtension.endpoints.each {

                    endpoint { endpointBuilder }
                    endpoint_path(it.path)
                    endpoint_label(it.label)
                    endpoint_description(it.description)

                    if (it.attribute) {
                        endpoint_attribute(it.attribute)
                    }

                    it.methods.each {

                        method { methodBuilder }
                        method_type(it.type)

                        if (it.requestJSON) {
                            requestJSON { requestJSONBuilder }

                            it.requestJSON.bodyParameters.each {
                                parameter { parameterBuilder }
                                parameter_name(it.name)
                                parameter_type(it.type)
                                parameter_mandatory(it.mandatory)
                                if (!it.mandatory) {
                                    parameter_defaultValue(it.defaultValueIfOptionalAndMissing)
                                }
                                requestJSON_bodyParameter(parameter_build { parameterBuilder })
                            }

                            it.requestJSON.headers.each {
                                header { headerBuilder }
                                header_name(it.name)
                                header_mandatory(it.mandatory)
                                if (!it.mandatory) {
                                    header_defaultValue(it.defaultValueIfOptionalAndMissing)
                                }
                                requestJSON_header(header_build { headerBuilder })
                            }

                            it.requestJSON.queryParameters.each {
                                parameter { parameterBuilder }
                                parameter_name(it.name)
                                parameter_type(it.type)
                                parameter_mandatory(it.mandatory)
                                if (!it.mandatory) {
                                    parameter_defaultValue(it.defaultValueIfOptionalAndMissing)
                                }
                                requestJSON_queryParameter(parameter_build { parameterBuilder })
                            }

                            method_request(requestJSON_build { requestJSONBuilder })

                        }
                        else if (it.requestURL) {
                            requestURL { requestURLBuilder }

                            it.requestURL.bodyParameters.each {
                                parameter { parameterBuilder }
                                parameter_name(it.name)
                                parameter_type(it.type)
                                parameter_mandatory(it.mandatory)
                                if (!it.mandatory) {
                                    parameter_defaultValue(it.defaultValueIfOptionalAndMissing)
                                }
                                requestURL_bodyParameter(parameter_build { parameterBuilder })
                            }

                            it.requestURL.headers.each {
                                header { headerBuilder }
                                header_name(it.name)
                                header_mandatory(it.mandatory)
                                if (!it.mandatory) {
                                    header_defaultValue(it.defaultValueIfOptionalAndMissing)
                                }
                                requestURL_header(header_build { headerBuilder })
                            }

                            it.requestURL.queryParameters.each {
                                parameter { parameterBuilder }
                                parameter_name(it.name)
                                parameter_type(it.type)
                                parameter_mandatory(it.mandatory)
                                if (!it.mandatory) {
                                    parameter_defaultValue(it.defaultValueIfOptionalAndMissing)
                                }
                                requestURL_queryParameter(parameter_build { parameterBuilder })
                            }

                            method_request(requestURL_build { requestURLBuilder })
                    
                        }

                        response {responseBuilder}

                        response_schema(it.response.schema)

                        status {statusBuilder}
                        status_code(it.response.status.code)
                        status_body(it.response.status.body)
                        response_status(status_build {statusBuilder})

                        it.response.headers.each {
                            header { headerBuilder }
                            header_name(it.name)
                            header_mandatory(it.mandatory)
                            if (!it.mandatory) {
                                header_defaultValue(it.defaultValueIfOptionalAndMissing)
                            }
                            response_header(header_build { headerBuilder })
                        }

                        method_response(response_build {responseBuilder})

                        endpoint_method(method_build {methodBuilder})

                    }
                    api_endpoint ( endpoint_build {endpointBuilder} )
                }

                /* Generate Rest API Client and Tests */

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

                javaGenerator = new FreeMarkerJavaCodeGenerator(build {apiBuilder}, cfg)

                String clientFolder = "$apiExtension.clientFolder"
                String clientPackage = "$apiExtension.clientPackage"
                String clientName = "$apiExtension.clientName"
                String clientPath = projectPath + clientFolder + clientPackage
                String clientFile = clientPath + "/" + clientName + ".java"

                String testFolder = "$apiExtension.testFolder"
                String testPackage = "$apiExtension.testPackage"
                String testName = "$apiExtension.testName"
                String testPath = projectPath + testFolder + testPackage
                String testFile = testPath + "/" + testName + ".groovy"

                String mockFolder = "$apiExtension.mockFolder"
                String mockPackage = "$apiExtension.mockPackage"
                String mockName = "$apiExtension.mockName"
                String mockPath = projectPath + mockFolder + mockPackage
                String mockFile = mockPath + "/" + mockName + ".groovy"

                try {
                    Files.createDirectories(Paths.get(clientPath))
                    Files.createDirectories(Paths.get(testPath))
                    Files.createDirectories(Paths.get(mockPath))
                } catch (IOException e) {
                    System.out.println("Cannot create directories");
                    e.printStackTrace();
                }

                javaGenerator.generateClient(new File(clientFile), new File(mockFile), clientPackage.replaceAll("/","."), clientName, mockPackage.replaceAll("/","."), mockName)
                javaGenerator.generateTests(new File(testFile), clientPackage.replaceAll("/","."), clientName, testPackage.replaceAll("/","."), testName, apiExtension.serverPort)

                System.out.println("RestAPI Client saved at: " + clientPath)
                System.out.println("Client tests saved at:   " + testPath)
                System.out.println("Mock Server tests saved at:   " + mockPath)
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

    void endpoint_description(String description) {
        endpointBuilder.addDescription(description)
    }

    void endpoint_attribute(String attribute) {
        endpointBuilder.addAttribute(attribute)
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

    void header_defaultValue(String defaultValue) {
        headerBuilder.setDefaultValueIfOptionalAndMissing(defaultValue)
    }

    // RequestJSON
    void requestJSON(Closure c) {
        requestJSONBuilder = new RequestJSONSpecBuilder()
        c.call(this)
    }

    void requestJSON_bodyParameter(ParameterSpec bodyParam) {
        requestJSONBuilder.addBodyParam(bodyParam)
    }

    void requestJSON_header(HeaderSpec header) {
        requestJSONBuilder.addHeader(header)
    }

    void requestJSON_queryParameter(ParameterSpec queryParameter) {
        requestJSONBuilder.addQueryParam(queryParameter)
    }

    // RequestURL
    void requestURL(Closure c) {
        requestURLBuilder = new RequestURLSpecBuilder()
        c.call(this)
    }

    void requestURL_bodyParameter(ParameterSpec bodyParam) {
        requestURLBuilder.addBodyParam(bodyParam)
    }

    void requestURL_header(HeaderSpec header) {
        requestURLBuilder.addHeader(header)
    }

    void requestURL_queryParameter(ParameterSpec bodyParam) {
        requestURLBuilder.addQueryParam(bodyParam)
    }

    // Response
    void response(Closure c) {
        responseBuilder = new ResponseSpecBuilder()
        c.call(this)
    }

    void response_schema(String schema) {
        responseBuilder.addResponseBodySchema(schema)
    }

    void response_status(StatusSpec status) {
        responseBuilder.addStatus(status)
    }

    void response_header(HeaderSpec header) {
        responseBuilder.addHeader(header)
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

    void parameter_defaultValue(String defaultValue) {
        parameterBuilder.setDefaultValue(defaultValue)
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