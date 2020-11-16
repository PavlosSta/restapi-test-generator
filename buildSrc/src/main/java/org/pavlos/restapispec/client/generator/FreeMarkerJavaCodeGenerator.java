package org.pavlos.restapispec.client.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.pavlos.restapispec.interfaces.APISpec;
import org.pavlos.restapispec.interfaces.EndpointSpec;
import org.pavlos.restapispec.interfaces.MethodSpec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerJavaCodeGenerator extends CodeGenerator {

    public FreeMarkerJavaCodeGenerator(APISpec apiSpec, Configuration cfg) {

        super(apiSpec, cfg);

        /*
        // Creates a Configuration instance
        cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setClassForTemplateLoading(FreeMarkerJavaCodeGenerator.class, "templates");
        // Specifies the source where the template files come from.
        try {
            System.out.println(System.getProperty("user.dir"));
            cfg.setDirectoryForTemplateLoading(new File("buildSrc/src/main/java/org/pavlos/client/templates"));
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
         */

    }

    protected void validateForTargetLanguage(APISpec newAPI) {

        // validate Endpoints with Attributes

        for (EndpointSpec endpoint: newAPI.getEndpoints()) {

            if (endpoint.getAttributes() == null || endpoint.getAttributes().isEmpty()) {
                for (MethodSpec method: endpoint.getMethods()) {
                    if (method.getType().name().equals("PUT") || method.getType().name().equals("PATCH") || method.getType().name().equals("DELETE")) {
                        throw new RuntimeException("Endpoint bad input: incompatible methods with attributes");
                    }
                }
            }
            else {
                for (MethodSpec method: endpoint.getMethods()) {
                    if (method.getType().name().equals("POST")) {
                        throw new RuntimeException("Endpoint bad input: incompatible methods with attributes");
                    }
                }
            }
        }

    }

    @Override
    protected void generateClientCode(File dest) {

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();
        root.put("api", apiSpec);
        root.put("clientPackage", "org.pavlos.testproject.client");

        // Gets the template
        try {
            Template temp = cfg.getTemplate("restapi-client.ftl");

            // Writes to console
            try (Writer fileWriter = new FileWriter(dest)) {
                temp.process(root, fileWriter);
            }

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void generateTestClientCode(File dest) {

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();
        root.put("api", apiSpec);
        root.put("clientPackage", "org.pavlos.testproject.client");

        // Gets the template
        try {
            Template temp = cfg.getTemplate("restapi-client-test.ftl");

            // Writes to console
            try (Writer fileWriter = new FileWriter(dest)) {
                temp.process(root, fileWriter);
            }

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void generateTestServerCode(File dest) {

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();
        root.put("api", apiSpec);
        root.put("clientPackage", "org.pavlos.testproject.client");
        root.put("serverPackage", "testproject.restapiserver");

        // Gets the template
        try {
            Template temp = cfg.getTemplate("restapi-server-test.ftl");

            // Writes to console
            try (Writer fileWriter = new FileWriter(dest)) {
                temp.process(root, fileWriter);
            }

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

    }
}