Gradle Swagger Generator Plugin [![Build Status](https://travis-ci.org/int128/gradle-swagger-generator-plugin.svg?branch=master)](https://travis-ci.org/int128/gradle-swagger-generator-plugin)
=============================

This is a Gradle plugin to generate server code, client code and API document, using [Swagger Codegen](https://github.com/swagger-api/swagger-codegen).

See also following examples:

- [Generated Swagger UI](https://int128.github.io/gradle-swagger-generator-plugin/swagger-ui/)
- [Generated HTML document](https://int128.github.io/gradle-swagger-generator-plugin/swagger-html/)
- [Example Gradle Projects](acceptance-test/)

Add the plugin into a build script as follows:

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '2.2.0'
}
```


Getting Started: Swagger YAML Validation
----------------------------------------

Add following into a build script.

```groovy
validateSwagger {
  inputFile = file('petstore.yaml')
}
```

The task validates a Swagger YAML against the JSON schema of Swagger specification.

```
% ./gradlew validateSwagger
:validateSwagger
```


Getting Started: Code Generation
--------------------------------

Add following into a build script.

```groovy
repositories {
  jcenter()
}

generateSwaggerCode {
  language = 'spring'
  inputFile = file('petstore.yaml')
  configFile = file('config.json')
  components = ['models', 'apis']
}
```

The task generates source code into `build/swagger-code`.

```
% ./gradlew generateSwaggerCode
:generateSwaggerCode
[main] INFO io.swagger.parser.Swagger20Parser - reading from ...
```

Run the task with `Help` postfix to show available JSON configuration.

```
% ./gradlew generateSwaggerCodeHelp
:generateSwaggerCodeHelp
Available JSON configuration for task ':generateSwaggerCode':

CONFIG OPTIONS
	sortParamsByRequiredFlag
  ...
```


Getting Started: Swagger UI Generation
--------------------------------------

Add following into a build script.

```groovy
repositories {
  jcenter()
}

dependencies {
  // Add dependency for Swagger UI
  swaggerUI 'org.webjars:swagger-ui:2.2.6'
}

generateSwaggerUI {
  inputFile = file('petstore.yaml')
  options.docExpansion = 'full'
}
```

The task generates an API document as `build/swagger-ui`.

```
% ./gradlew generateSwaggerUI
:generateSwaggerUI
```


Custom generation
-----------------

The plugin adds `validateSwagger`, `generateSwaggerCode` and `generateSwaggerUI` tasks.
A task will be skipped if no input file is given.

See projects under [acceptance-test](acceptance-test) for more.


### Task type `ValidateSwagger`

The task accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`reportFile`  | File              | File to write validation report.        | `$buildDir/tmp/validateSwagger/report.yaml`


### Task type `GenerateSwaggerCode`

The task accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`language`    | String            | Language to generate.                   | Mandatory
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`outputDir`   | File              | Directory to write generated files, wiped before generation. Do not specify the project directory. | `$buildDir/swagger-code`
`library`     | String            | Library type.                           | None
`configFile`  | File              | [JSON configuration file](https://github.com/swagger-api/swagger-codegen#customizing-the-generator). | None
`templateDir` | File              | Directory containing the template.      | None
`components`  | List of Strings   | [Components to generate](https://github.com/swagger-api/swagger-codegen#selective-generation) that is a list of `models`, `apis` and `supportingFiles`. | All components


### Task type `GenerateSwaggerUI`

The task accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`outputDir`   | File              | Directory to write Swagger UI files, wiped before generation. Do not specify the project directory. | `$buildDir/swagger-ui`
`options`     | Map of Objects    | [Swagger UI options](https://github.com/swagger-api/swagger-ui#parameters). | Empty map
`header`      | String            | Custom tags before loading Swagger UI.  | None

The plugin replaces the Swagger UI loader with custom one containing following and given `options`:

```json
{
  "url":"",
  "validatorUrl":null,
  "spec":{"swagger":"2.0","info":"... converted from YAML input ..."}
}
```


Contributions
-------------

This is an open source software licensed under the Apache License Version 2.0.
Feel free to open issues or pull requests.

Travis CI builds the plugin continuously.
Following variables should be set.

Environment Variable        | Purpose
----------------------------|--------
`$GRADLE_PUBLISH_KEY`       | Publish the plugin to Gradle Plugins
`$GRADLE_PUBLISH_SECRET`    | Publish the plugin to Gradle Plugins
`$GITHUB_TOKEN`             | Publish the example of API document to GitHub Pages
