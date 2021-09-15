+function () {
    const HtmlWebpackPlugin = require('html-webpack-plugin')
    const path = require("path")

    // Change entry from kjs-chunks.js (imports dependencies statically) to kjs-chunks-index.js (imports dynamically)
    config.entry = path.resolve(__dirname, "kotlin/kjs-chunks-index.js")

    config.plugins.push(new HtmlWebpackPlugin({template: '../../../processedResources/js/main/index.ejs'}))
}()
