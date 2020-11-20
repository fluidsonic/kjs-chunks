import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType

plugins {
    id("org.jetbrains.kotlin.js")
}

dependencies {
    implementation(project(":shared"))
}

kotlin {
    js(KotlinJsCompilerType.IR).browser()
}
