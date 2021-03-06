package org.hidetake.gradle.swagger.generator

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class GenerateSwaggerCodeSpec extends Specification {

    def "plugin should provide task class"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.GenerateSwaggerCode == GenerateSwaggerCode
    }

    def "plugin should add default task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.tasks.findByName('generateSwaggerCode')
    }

    def "exception should be thrown if projectDir is given"() {
        given:
        def path = GenerateSwaggerCodeSpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.generateSwaggerCode.language = 'java'
            tasks.generateSwaggerCode.inputFile = file(path)
            tasks.generateSwaggerCode.outputDir = projectDir
            tasks.generateSwaggerCode.exec()
        }

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
    }

}
