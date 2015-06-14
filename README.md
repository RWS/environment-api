## Environment API

Environment API provides a simple way to collect and access different type of input (e.g. environment values, system properties and properties files) for a Java application for its runtime. 

A *default* environment can be created as:

```java
Environment env = Environment.createEnvironment();
```

the instance respectively contains:

* all the entries from `System.getenv()` if not over-written.
* all the entries from `System.getProperties()` if not over-written.
* all the entires from an environment file if provided by `-Denvironment.file=/full/path/to`. 

An instance of environment can be created starting from a set of *default* values:

```java
Map<String, String> myDefaultEnv = ...
Environment env = Environment.createEnvironment(myDefaultEnv);
```

that allows to provide default values for the environment if not provided in the system environment, system properties or the environment file.

It is recommended to use environment file as the main approach. An environment file is a standard Java properties file:

```
environment.name=development
application.name=mysdlapp
```

To use the environment file, start the Java application with one system property:

```
$ java -Denvironment.file=/path/to/env/file.properties -jar myapp.jar
```

An example of environment file is available at `src/main/resources/env.properties-dpkg`.

## LifeCycle API

LifeCycle API provides:

* high-level abstractions to manage an object's life cycle through different states
* thread management of a life cycle object

Consider a simple HTTP server. Apart from the usuage, it is common that there needs to be boiler-plate code to manage the HTTP server in different states of the application:

```java
class LifeCycleHttpServer extends AbstractLifeCycle {

  private HttpServer server;

  LifeCycleHttpServer() {}

  @Override
  protected void doInitLifeCycle() throws Exception {
    try {
      this.server = HttpServer.create(new InetSocketAddress(0), 2);
    } catch (IOException e) {
      throw new Exception(e);
    }
  }

  @Override
  protected void doStartLifeCycle() throws Exception {
   this.server.start();
  }

  @Override
  protected void doStopLifeCycle() throws Exception {
   this.server.stop(0);
  }
```

Having `LifeCycleHttpServer`, then it can be managed through a `Container`:

```java
Container httpContainer = new Container (new LifeCycleHttpServer());
httpContainer.init();
httpContainer.start();
...
httpContainer.stop();
```

**Note** that a `LifeCycle` implementation should avoid blocking operations if it is managed by a `Container`. 

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[Complete License][1]

[1]: LICENSE.txt
