# clojurice

A base template for full-stack web applications in Clojure

## Requirements

You will need [Boot](http://boot-clj.com/) installed, as well as Java of at least version 1.6+ (1.8+ is recommended).

## Instructions

To start the dev environment do:

```
boot dev
```

This will start both backend and frontend compilation, with the site hosted on [localhost:7000](http://localhost:7000). API documentation can also be found at [http://localhost:7000/api-docs](http://localhost:7000/api-docs)

To build the project to an uberjar do:

```
boot build <target-dir> 
```

An uberjar called "clojurice-(version)-standalone.jar" will be found in the target directory. The project version number can be set in `build.boot`.

## Configuration

Configuration is handled via `config.cljc`. `config.cljc` defines a namespace, `clojurice.config` available to both front-end and backend, and provides a base, dev, and prod profile, with the latter two being derived from base. The config is a simple map.

## Development notes

### Backend 

The main backend API can be found in `api.clj` and is written in [compojure-api](https://github.com/metosin/compojure-api). There is also a [tutorial](https://github.com/metosin/compojure-api/wiki/Tutorial) on working with compojure-api syntax. 

Dependency injection and system component handling is handled via [system](https://github.com/danielsz/system) and the Raamwerk model. This is what enables live reloading of the backend, but also orchestrates all the components of the app (static and API servers, config, DB, etc.). The main constructors for these are found in `app.systems`. There is a base `build-system` function which takes a config from `config.cljc` and produces the base system map for that profile, and then functions that produce the prod and dev systems.

Of most important note is the `:site-endpoint`, which is the component that handles static routes like the main index and points to `app.routes/site`, and `:api-endpoint`, which is the component for the REST API, and points to `app.api/api-routes`. Each of these functions takes a single argument, which is a subset of the system map, containing the keys listed as dependencies in the vector passed to `component/using`. So in order for a component to be available to the end-point, its key needs to be added to this vector.

### Frontend

The frontend design is based around a combination of multimethod dispatch for rendering each view, and client-side routing with [bide](https://github.com/funcool/bide). As such, adding a new sub-view requires a few steps that are important to remember:

1. Create your view under the `clojurice.views` namespace, ie. `clojurice.views.foo` in `cljs/clojurice/views/foo.cljs`
2. Require the `clojurice.views.dispatch/dispatch-view` multimethod, and create your own multimethod to dispatch from a suitable key, ie. `:clojurice.foo`. The method should take two arguments, the first is the key itself, the second is any parameters from the URI.
3. Require the new namespace in `index.cljs`.
4. Add a route to the routes in `router.cljs`.

Main app state is kept in a shared reagent atom at `clojurice.state/app-state`. A `clojurice.api` namespace is also provided for common API calls. 

An important note regarding routing: when linking to another component within the app, it is best to use the `clojurice.router/app-link` function as this hooks into the routing system. Normal hrefs will work, but force a page reload, which will be slower and also reset app-state.

### Dev tools

The default configuration will open nREPL connections to both frontend and backend, each on their own port. Check the console logs from `boot dev` to find them.

The frontend environment also wraps [cljs-devtools](https://github.com/binaryage/cljs-devtools) and [Dirac](https://github.com/binaryage/dirac) for a more pleasant browser environment for Chrome. 

Dirac allows for a CLJS REPL in a Chrome devtools window, but requires some additional setup. You'll need to set up the Dirac extension with Chrome Canary, as described [here](https://github.com/binaryage/dirac/blob/master/docs/installation.md#setup-dirac-chrome-extension). The remaining setup (installing the runtime, nREPL, and agent) is handled automatically by boot.

If you are not using Chrome Canary, you will also want to turn on custom formatters in the Chrome Devtools for the cljs-devtools formatters to work.

### Hot Reloading

Both frontend and backend code have been configured to automatically reload on file changes. There's even a helpful audio cue to notify you once a rebuild is done.

Note that the full backend server system will only be restarted completely when certain files change. This is configured through the `build.boot` dev task with the `:files` parameter to the `system` step.

## Credits and License

This app is based originally on [system-template](https://github.com/shakdwipeea/system-template) with some further guidance from [tenzing](https://github.com/martinklepsch/tenzing).

Development has been made possible by [Futurice](http://www.futurice.com/).

The code is distributed under the Eclipse Public License v1.0 or any later version. For more information see `LICENSE` in the root directory.