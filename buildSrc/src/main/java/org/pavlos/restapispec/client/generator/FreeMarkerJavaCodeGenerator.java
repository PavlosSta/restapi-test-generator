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
    protected void generateClientCode(File dest, String clientPackage, String clientName) {

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();
        root.put("api", apiSpec);
        root.put("clientPackage", clientPackage);
        root.put("clientName", clientName);

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
    protected void generateTestServerCode(File dest, String clientPackage, String clientName, String testPackage, String testName, String serverPort) {

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();
        root.put("api", apiSpec);
        root.put("clientPackage", clientPackage);
        root.put("clientName", clientName);
        root.put("testPackage", testPackage);
        root.put("testName", testName);
        root.put("serverPort", serverPort);

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

    @Override
    protected void generateTestClientCode(File dest, String clientPackage, String clientName, String mockPackage, String mockName) {

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();
        root.put("api", apiSpec);
        root.put("clientPackage", clientPackage);
        root.put("clientName", clientName);
        root.put("mockPackage", mockPackage);
        root.put("mockName", mockName);

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
}