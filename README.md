# Code splitting for Kotlin/JS browser apps

This is a rough proof-of-concept that a Kotlin/JS browser app can be split into multiple chunks that can depend on
each other dynamically. For example a Kotlin React app could be split into multiple smaller JS files that way.

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

- [`/webpack.config.d/entry.js`](https://github.com/fluidsonic/kjs-chunks/tree/main/webpack.config.d/entry.js)
    defines the `index` module as the entry point for the library.
- [`/build.gradle.kts`](https://github.com/fluidsonic/kjs-chunks/tree/main/build.gradle.kts)
    makes various changes to the compilation:
    - Disable `klib` production (`-Xir-produce-klib-dir`) that would typically result in just a single JS file.
    - Create per-module JS files `-Xir-per-module` and `-Xir-produce-js`.
    - Manually copy these created JS files to the appropriate NPM project directory.
    - Disable IR link tasks that don't seem to be needed anymore.

### Known limitations

- Only works with the JS IR compiler.
- The setup is fragile and can break with subsequent Kotlin updates.
- Dynamic imports have weird names (like `kotlin_kjs_chunks_dynamic` in this example).
- Only `@JsExport` exports of dynamic modules can be used. Adding `dynamic` as a Gradle dependency for `index`
  for example to make its symbols accessible would turn it into a static import.
