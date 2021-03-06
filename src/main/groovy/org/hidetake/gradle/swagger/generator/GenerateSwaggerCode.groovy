package org.hidetake.gradle.swagger.generator

import io.swagger.codegen.SwaggerCodegen
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

/**
 * A task to generate a source code from the Swagger specification.
 *
 * @author Hidetake Iwata
 */
class GenerateSwaggerCode extends DefaultTask {

    @Input
    String language

    @InputFile
    File inputFile

    @OutputDirectory
    File outputDir

    @Optional @Input
    String library

    @Optional @InputFile
    File configFile

    @Optional @InputDirectory
    File templateDir

    @Optional @Input
    List<String> components

    def GenerateSwaggerCode() {
        outputDir = new File(project.buildDir, 'swagger-code')
        onlyIf {
            inputFile
        }
    }

    @TaskAction
    void exec() {
        assert language, "language should be set in the task $name"
        assert inputFile, "inputFile should be set in the task $name"
        assert outputDir, "outputDir should be set in the task $name"
        if (components) {
            assert components.every { component ->
                component in ['models', 'apis', 'supportingFiles']
            }
        }

        assert outputDir != project.projectDir, 'Prevent wiping the project directory'

        project.delete(outputDir)
        outputDir.mkdirs()

        def args = buildOptions()
        applySystemProperties {
            SwaggerCodegen.main(*args)
        }
    }

    private List<String> buildOptions() {
        def options = []
        options << 'generate'
        options << '-l' << language
        options << '-i' << inputFile.path
        options << '-o' << outputDir.path
        if (library) {
            options << '--library' << library
        }
        if (configFile) {
            options << '-c' << configFile.path
        }
        if (templateDir) {
            options << '-t' << templateDir.path
        }
        options
    }

    private <T> T applySystemProperties(Closure<T> closure) {
        components?.each { component ->
            System.setProperty(component, '')
        }
        try {
            closure()
        } finally {
            components?.each { component ->
                System.clearProperty(component)
            }
        }
    }

}
