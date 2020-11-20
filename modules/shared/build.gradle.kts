import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType

plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    js(KotlinJsCompilerType.IR).browser()
}
