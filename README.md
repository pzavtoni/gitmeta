# GitHub Repositories

Service that expose REST API for user's repo metadata retrieval

## 1. How do I run locally

`./gradlew clean` # this will clean any generated resources from previous build

`./gradlew build ` # this will build the project

`./gradlew bootJar ` # this will create a jar file

`java -jar .\build\libs\GitMeta-1.0-SNAPSHOT.jar com.homework.task.HomeworkApplication` # This will start a local server that will expose the API on port `9000`

`NOTE: GitHub allows to query their API for very few times if the calls are not authorized. An access token can be retrieved from 'Developer Setting(https://github.com/settings/tokens)' 
and set as environment variable 'GIT_ACCESS_TOKEN'`

## 2. How do I run in Docker

`./gradlew clean` # this will clean any generated resources from previous build

`./gradlew build ` # this will build the project

`./gradlew bootJar ` # this will create a jar file

`docker-compose up --build` # this will create docker image and it will create a docker container that exposes the API at the port `9001`

`NOTE: In 'docker-compose.yml file we can set as environment variables 'GIT_ACCESS_TOKEN' and 'GIT_API_BASE_URL' values`

### REST APIs documentation
To have a look on the most recent version of the API and its documentation, please visit:
```    
        {host}:{port}/swagger-ui/
```

To export the apis as an openAPI specifications in JSON format, please visit:
```    
        {host}:{port}/v3/api-docs
```