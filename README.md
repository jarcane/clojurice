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

An uberjar called "clojurice-(version)-standalone.jar" will be found in the target director. The project version number can be set in `build.boot`.

## Development notes

### Backend 

The main backend API can be found in `api.clj` and is written in [compojure-api](https://github.com/metosin/compojure-api).

### Frontend

The frontend design is based around a combination of multimethod dispatch for rendering each view, and client-side routing with [bide](https://github.com/funcool/bide). As such, adding a new sub-view requires a few steps that are important to remember:

1. Create your view under the `clojurice.views` namespace, ie. `clojurice.views.foo` in `cljs/clojurice/views/foo.cljs`
2. Require the `clojurice.views.dispatch/dispatch-view` multimethod, and create your own multimethod to dispatch from a suitable key, ie. `:clojurice.foo`. The method should take two arguments, the first is the key itself, the second is any parameters from the URI.
3. Require the new namespace in `index.cljs`.
4. Add a route to the routes in `router.cljs`.

Main app state is kept in a shared reagent atom at `clojurice.state/app-state`. A `clojurice.api` namespace is also provided for common API calls. 

An important note regarding routing: when linking to another component within the app, it is best to use the `clojurice.router/app-link` function as this hooks into the routing system. Normal hrefs will work, but force a page reload, which will be slower and also reset app-state.

### Hot Reloading

Both frontend and backend code have been configured to automatically reload on file changes, but note that the full backend server system will only be restarted completely when certain files change. This is configured through the `build.boot` dev task with the `:files` parameter to the `system` step.
