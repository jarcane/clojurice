![clojurice](resources/brand/clojurice-logo.png)

[![Sponsored](https://img.shields.io/badge/chilicorn-sponsored-brightgreen.svg?logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAAA4AAAAPCAMAAADjyg5GAAABqlBMVEUAAAAzmTM3pEn%2FSTGhVSY4ZD43STdOXk5lSGAyhz41iz8xkz2HUCWFFhTFFRUzZDvbIB00Zzoyfj9zlHY0ZzmMfY0ydT0zjj92l3qjeR3dNSkoZp4ykEAzjT8ylUBlgj0yiT0ymECkwKjWqAyjuqcghpUykD%2BUQCKoQyAHb%2BgylkAyl0EynkEzmkA0mUA3mj86oUg7oUo8n0k%2FS%2Bw%2Fo0xBnE5BpU9Br0ZKo1ZLmFZOjEhesGljuzllqW50tH14aS14qm17mX9%2Bx4GAgUCEx02JySqOvpSXvI%2BYvp2orqmpzeGrQh%2Bsr6yssa2ttK6v0bKxMBy01bm4zLu5yry7yb29x77BzMPCxsLEzMXFxsXGx8fI3PLJ08vKysrKy8rL2s3MzczOH8LR0dHW19bX19fZ2dna2trc3Nzd3d3d3t3f39%2FgtZTg4ODi4uLj4%2BPlGxLl5eXm5ubnRzPn5%2Bfo6Ojp6enqfmzq6urr6%2Bvt7e3t7u3uDwvugwbu7u7v6Obv8fDz8%2FP09PT2igP29vb4%2BPj6y376%2Bu%2F7%2Bfv9%2Ff39%2Fv3%2BkAH%2FAwf%2FtwD%2F9wCyh1KfAAAAKXRSTlMABQ4VGykqLjVCTVNgdXuHj5Kaq62vt77ExNPX2%2Bju8vX6%2Bvr7%2FP7%2B%2FiiUMfUAAADTSURBVAjXBcFRTsIwHAfgX%2FtvOyjdYDUsRkFjTIwkPvjiOTyX9%2FAIJt7BF570BopEdHOOstHS%2BX0s439RGwnfuB5gSFOZAgDqjQOBivtGkCc7j%2B2e8XNzefWSu%2BsZUD1QfoTq0y6mZsUSvIkRoGYnHu6Yc63pDCjiSNE2kYLdCUAWVmK4zsxzO%2BQQFxNs5b479NHXopkbWX9U3PAwWAVSY%2FpZf1udQ7rfUpQ1CzurDPpwo16Ff2cMWjuFHX9qCV0Y0Ok4Jvh63IABUNnktl%2B6sgP%2BARIxSrT%2FMhLlAAAAAElFTkSuQmCC)](http://spiceprogram.org/oss-sponsorship) [![license](https://img.shields.io/badge/license-EPL%202.0-brightgreen.svg)](https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt) [![Build Status](https://travis-ci.org/jarcane/clojurice.svg?branch=master)](https://travis-ci.org/jarcane/clojurice)

An opinionated starter app for full-stack web applications in Clojure

## Requirements

You will need [Boot](http://boot-clj.com/) installed, as well as Java 1.8+, and PostgreSQL 9.6+.

The default configuration expects a running PGSQL server with user/password "postgres" containing databases called "app" and "apptest" (for the integration tests), though these settings can be re-configured. See below.

## Instructions

The best way to start a new project is to either clone the repository, or, if you wish to start from a clean slate with git, download the [master .zip](https://github.com/futurice/clojurice/archive/master.zip), extract it locally, and `git init` from there.

To start the dev environment do:

```
boot dev
```

This will start both backend and frontend compilation, with the site hosted on [localhost:7000](http://localhost:7000). API documentation can also be found at [http://localhost:7000/api-docs](http://localhost:7000/api-docs)

To build the project to an uberjar do:

```
boot build <target-dir> 
```

An uberjar called "app-(version)-standalone.jar" will be found in the target directory. The project version number can be set in `build.boot`.

## Configuration

Configuration is handled via EDN files under the `resources/config` directory. `base.edn` provides the base configuration that applies to all environments, while the two profiles, `dev.edn` and `prod.edn` are loaded in their respective environments and take precedence over `base.edn`. In addition, on load time, the config files are checked against the `Config` schema located in `domain.cljc`.

Frontend configuration is provided via the API at `GET /api/config`, and provides a subset of the configuration as defined in the `FrontendConfig` schema in `domain.cljc`.

## Development notes

### Backend 

The main backend API can be found in `api.clj` and is written in [compojure-api](https://github.com/metosin/compojure-api). There is also a [tutorial](https://github.com/metosin/compojure-api/wiki/Tutorial) on working with compojure-api syntax. 

Dependency injection and system component handling is handled via [system](https://github.com/danielsz/system) and the Raamwerk model. This is what enables live reloading of the backend, but also orchestrates all the components of the app (static and API servers, config, DB, etc.). The main constructors for these are found in `app.systems`. There is a base `build-system` function which takes a config profile name and produces the base system map for that profile, and then functions that produce the prod and dev systems.

Of most important note is the `:site-endpoint`, which is the component that handles static routes like the main index and points to `app.routes/site`, and `:api-endpoint`, which is the component for the REST API, and points to `app.api/api-routes`. Each of these functions takes a single argument (called `sys` by convention), which is a subset of the system map, containing the keys listed as dependencies in the vector passed to `component/using`. So in order for a component to be available to the end-point, its key needs to be added to this vector.

Database migrations are handled with a custom [Flyway](https://flywaydb.org/) component, configured to automatically run on server start or reload. Migrations are located in `resources/db/migrations`, which contains `.sql` files for migrations, named according to the scheme described in the [Flyway documentation](https://flywaydb.org/documentation/migrations#sql-based-migrations). The `Flyway` object is available from the system-map as `:flyway` and can thus be called from the REPL or from any component that inherits it as a dependency. This is useful for rolling back migrations in test, etc.

For SQL abstraction, [honeysql](https://github.com/jkk/honeysql) is used on top of [clojure.java.jdbc](https://github.com/clojure/java.jdbc). HoneySQL provides a way of writing SQL queries as maps, which can thus be built and composed as any other Clojure map, and then formatted into SQL to call with the JDBC driver. A helper function, `app.query/make-query` is provided in `query.sql` for wrapping the call to the JDBC driver, so one need only provide the system map and the SQL query map to get a result. 

### Frontend

The frontend is built with [reagent](https://reagent-project.github.io/), using a combination of multimethod dispatch for rendering each view, and client-side routing with [bide](https://github.com/funcool/bide). As such, adding a new sub-view requires a few steps that are important to remember:

1. Create your view under the `app.views` namespace, ie. `app.views.foo` in `cljs/app/views/foo.cljs`
2. Require the `app.views.dispatch/dispatch-view` multimethod, and create your own multimethod to dispatch from a suitable key, ie. `:app.foo`. The method should take two arguments, the first is the key itself, the second is any parameters from the URI.
3. Require the new namespace in `index.cljs`.
4. Add a route to the routes in `router.cljs`.

Main app state is kept in a shared reagent atom at `app.state/app-state`. A `app.api` namespace is also provided for common API calls. 

An important note regarding routing: when linking to another component within the app, it is best to use the `app.router/app-link` function as this hooks into the routing system. Normal hrefs will work, but force a page reload, which will be slower and also reset app-state.

### Common namespaces

In addition to the frontend and backend, there are also included some common namespaces via `.cljc` files in `src/cljc/app`, that allow for key data and functions to be shared by front and back. The most important of these is `app.domain` in `src/cljc/app/domain.cljc`. This provides the common data schemas for the application, including the schema for the configuration files. 

### Dev tools

The default configuration will open nREPL connections to both frontend at port 6809, and backend at port 6502. 

The frontend environment wraps [cljs-devtools](https://github.com/binaryage/cljs-devtools) for a more pleasant browser environment for Chrome. There is also an additional [reagent-dev-tools](https://github.com/metosin/reagent-dev-tools) element added to the page in dev mode that provides reflection to the current app state. You will want to turn on custom formatters in the Chrome Devtools for the cljs-devtools formatters to work.

A `boot cljfmt` task is provided which will run [cljfmt](https://github.com/weavejester/cljfmt) on all files in the src directory. The `check` and `fix` tasks from [boot-cljfmt](https://github.com/siilisolutions/boot-cljfmt) are also available directly, and can be used to run against individual files or directories as needed.

For basic linting a `boot analyse` task is provided, which will check the source files with [kibit](https://github.com/jonase/kibit) and [bikeshed](https://github.com/dakrone/lein-bikeshed) for common style issues.

### Hot Reloading

Both frontend and backend code have been configured to automatically reload on file changes. There's even a helpful audio cue to notify you once a rebuild is done.

Note that the full backend server system will only be restarted completely when certain files change. This is configured through the `build.boot` dev task with the `:files` parameter to the `system` step.

### Testing

Some basic integration tests have been provided. You can run these with `boot test`, or with `boot test-watch`, which will start a watcher and run all tests on file change.

The tests include browser testing via [etaoin](https://github.com/igrishaev/etaoin), and you will also need to install the Chrome webdriver. Information and links on how to do this can be found [here](https://github.com/igrishaev/etaoin#installing-drivers). On Mac it can be installed with `brew install chromedriver`, or on Windows with `scoop install chromedriver`. You will also of course need Chrome. 

## Credits and License

This app is based originally on [system-template](https://github.com/shakdwipeea/system-template) with some further guidance from [tenzing](https://github.com/martinklepsch/tenzing).

Developed by Annaia Berry ([@jarcane](https://github.com/jarcane)). Development made possible by [Futurice](http://www.futurice.com/).

Copyright (C) 2018 Annaia Berry. The code is distributed under the Eclipse Public License v2.0 or any later version. For more information see [`LICENSE`](LICENSE) in the root directory.
