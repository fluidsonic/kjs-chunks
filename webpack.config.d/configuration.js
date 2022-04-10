;+function () {
    const HtmlWebpackPlugin = require('html-webpack-plugin')
    const path = require("path")

    // Change entry from 'kjs-chunks.js' (imports dependencies statically) to 'kotlin_kjs_chunks_index.js' (imports dynamically).
    config.entry = path.resolve(__dirname, "kotlin/kotlin_kjs_chunks_index.js")

    // Dynamically add correct <script> tag to 'index.ejs'.
    config.plugins.push(new HtmlWebpackPlugin({template: '../../../processedResources/js/main/index.ejs'}))

    // Easier debugging.
    if (config.mode === 'development')
        config.devtool = false
}()
