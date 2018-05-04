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

An uberjar called "clojurice-(version)-standalone.jar" will be found in the target director.

## Development notes

The main backend API can be found in `api.clj` and is written in [compojure-api](https://github.com/metosin/compojure-api).


Both frontend and backend code have been configured to automatically reload on file changes, but note that the full backend server system will only be restarted completely when certain files change. This is configured through the `build.boot` dev task with the `:files` parameter to the `system` step.
