package client.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import interfaces.APISpec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerJavaCodeGenerator extends CodeGenerator {

    private Configuration cfg;

    public FreeMarkerJavaCodeGenerator(APISpec apiSpec) {

        super(apiSpec);

        // Creates a Configuration instance
        cfg = new Configuration(Configuration.VERSION_2_3_30);

        cfg.setClassForTemplateLoading(FreeMarkerJavaCodeGenerator.class, "templates");

        // Specifies the source where the template files come from.
        try {
            cfg.setDirectoryForTemplateLoading(new File("src/main/java/client/templates"));
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
    }

    @Override
    protected void validateForTargetLanguage() {

    }

    @Override
    protected void generateClientCode(File dest) {

        /* Create a data-model */
        Map<String, APISpec> root = new HashMap<>();
        root.put("api", apiSpec);

        // Gets the template
        try {
            Template temp = cfg.getTemplate("restapi.ftl");

            // Writes to console
            Writer fileWriter = new FileWriter(dest);
            try {
                temp.process(root, fileWriter);
            } finally {
                fileWriter.close();
            }

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void generateTestClientCode(File dest) {

        /* Create a data-model */
        Map<String, APISpec> root = new HashMap<>();
        root.put("api", apiSpec);

        // Gets the template
        try {
            Template temp = cfg.getTemplate("client-test.ftl");

            // Writes to console
            Writer fileWriter = new FileWriter(dest);
            try {
                temp.process(root, fileWriter);
            } finally {
                fileWriter.close();
            }

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

    }
};