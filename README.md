# Code splitting for Kotlin/JS browser apps

This is a rough proof-of-concept that a Kotlin/JS browser app can be split into multiple chunks that can depend on each
other dynamically. For example a Kotlin React app could be split into multiple smaller JS files that way.

### Architecture

#### Modules

- `/` compiles all submodules into separate JS files.
- [`/modules/dynamic`](https://github.com/fluidsonic/kjs-chunks/tree/main/modules/dynamic) will be
  [loaded dynamically](https://github.com/fluidsonic/kjs-chunks/blob/main/modules/index/src/main/kotlin/index.kt#L11).
- [`/modules/index`](https://github.com/fluidsonic/kjs-chunks/tree/main/modules/index)
  is the entry point with a `main()` function that loads `dynamic` dynamically.
- [`/modules/shared`](https://github.com/fluidsonic/kjs-chunks/tree/main/modules/shared)
  will be shared by `dynamic` and `index` without duplicating code.

### Configuration

- [`/webpack.config.d/configuration.js`](https://github.com/fluidsonic/kjs-chunks/tree/main/webpack.config.d/configuration.js)
  defines the `index` module as the entry point for the library.

### Known limitations

- Only works with the JS IR compiler.
- The setup is fragile and can break with subsequent Kotlin updates.
- Dynamic imports have unstable names (like `kotlin_kjs_chunks_dynamic` in this example).
- `@EagerInitialization` must be used as a workaround to execute logic when loading a module, e.g. the `index`.
