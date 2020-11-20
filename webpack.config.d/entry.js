+function () {
    const path = require("path")

    // Change entry from kjs-chunks.js (imports dependencies statically) to kotlin_kjs_chunks_index.js (imports dynamically)
    config.entry = path.resolve(__dirname, "kotlin/kotlin_kjs_chunks_index.js")
}()
