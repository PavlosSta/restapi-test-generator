import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.pavlos.restapispec.client.generator.FreeMarkerJavaCodeGenerator

import java.nio.file.Files
import java.nio.file.Paths

class GroovyApiSpecBuilder implements Plugin<Project> {

    void apply(Project project) {

        def apiExtension = project.extensions.create('api', ApiExtension)

        project.task('generate') {

            doFirst {

                /* Generate Rest API Client and Tests */

                String projectPath = System.getProperty("user.dir")

                // Creates a Configuration instance
                cfg = new Configuration(Configuration.VERSION_2_3_30)

                cfg.setClassForTemplateLoading(FreeMarkerJavaCodeGenerator.class, "templates")

                // Specifies the source where the template files come from.
                try {
                    cfg.setDirectoryForTemplateLoading(new File(projectPath + "/buildSrc/src/main/java/org/pavlos/restapispec/client/templates"))
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cfg.setDefaultEncoding("UTF-8")

                // Sets how errors will appear.
                cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER)

                // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
                cfg.setLogTemplateExceptions(false)

                // Wrap unchecked exceptions thrown during template processing into TemplateException-s:
                cfg.setWrapUncheckedExceptions(true)

                // Do not fall back to higher scopes when reading a null loop variable:
                cfg.setFallbackOnNullLoopVariable(false)

                javaGenerator = new FreeMarkerJavaCodeGenerator(apiExtension.apiBuilder.build(), cfg)

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

    // Freemarker Generator
    private FreeMarkerJavaCodeGenerator javaGenerator
    private Configuration cfg

}