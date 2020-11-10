

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.pavlos.implementations.APISpecBuilder
import org.pavlos.interfaces.APISpec

class GroovyApiSpecBuilder implements Plugin<Project> {

    void apply(Project project) {
        def extension = project.extensions.create('api', GroovyApiSpecExtension)
        project.task('generator') {
            doLast {
                api({builder})
                baseUrl("$extension.baseUrl")
                build({builder})
            }
        }
    }

    private APISpecBuilder builder;

    void api(Closure c) {
        builder = new APISpecBuilder();
        c.call(this)
    }

    void baseUrl(String url) {
        builder.setBaseUrl(url);
    }

    void endpoint(String path, Closure c) {

    }

    void methodMissing(String name) {

    }

    APISpec build(Closure c) {
        builder.build()
        c.call(this)
    }


}