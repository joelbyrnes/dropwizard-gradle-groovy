# Chat experiments

Send events to users on channel, including sounds

# Dropwizard + Gradle = &hearts;

Based on https://github.com/joelbyrnes/dropwizard-gradle-groovy which is a fork and update of an example project.

Also based on https://github.com/andershedstrom/dropwizard-with-sse for EventSource stuff

## Gotchas

You need Gradle 1.1 or higher, otherwise you'll run into a [dependency resolution bug](http://issues.gradle.org/browse/GRADLE-2285).

Just use gradlew script to avoid any such problems.

## FatJar

This example is using the [Gradle FatJar Plugin](https://github.com/musketyr/gradle-fatjar-plugin) which will create
a JAR file of the project including all dependencies, similar to the [Maven Assembly Plugin](http://maven.apache.org/plugins/maven-assembly-plugin/)
or the [Maven Shade Plugin](http://maven.apache.org/plugins/maven-shade-plugin/).

To create a fat JAR just run `gradle fatJar`. The resulting JAR will be saved as `./build/libs/dropwizard-gradle-fat.jar`.

## Shadow

Shadow is a rewrite of the maven shade plugin for Gradle, which is faster than fatJar.
I am trying to get this to work instead.

## Gradle Application Plugin

An alternative to creating a fat JAR is using the [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html).

To create a distributable ZIP archive including all dependencies for your application just run `gradle distZip`. The
resulting archive will be saved as `./build/distributions/dropwizard-gradle.zip`.

You can also use the `run` task to start the application.

## Server-sent Events

Copied from https://github.com/andershedstrom/dropwizard-with-sse

This is a example project using [Dropwizard](github.com/codahale/dropwizard) with [Server-sent Events support](https://github.com/jetty-project/jetty-eventsource-servlet)

__Usage__

* Run the app in IDE, or in terminal execute:

```
$ gradlew run
```

* Open a new terminal and execute:

```
$ curl localhost:8080/sse -H"Accept: text/event-stream"
```

* Open yet another terminal and execute:

```
$ curl localhost:8080/publish?msg=HelloWorld
```

* You should see the following i terminal 2:

```
~ $ curl localhost:8080/sse -H"Accept: text/event-stream"

data: HelloWorld
```

## Problems

* shadowJar and fatJar have the wav in the jar, and this apparently can't be resolved so sounds can't be accessed? so only distZip works.
* shadowJar has some kind of class resolution problem, might be fixed later.

## Potential improvements

* make sounds its own dir so shaded jars can be just run in the same root dir, and will serve custom sounds out of it
* will also automatically find the wavs and list them for playing.
* add other event types to the channel, eg chat - essentially a basic IRC server with sounds. what more?

