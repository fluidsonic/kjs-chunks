import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType

plugins {
    kotlin("js") version "1.6.20"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(project(":dynamic"))
    implementation(project(":index"))

    implementation(devNpm("html-webpack-plugin", "~5.5.0"))
}

kotlin {
    js(KotlinJsCompilerType.IR) {
        browser {
            webpackTask {
                outputFileName = "assets/[name].[contenthash].js"
            }

            runTask {
                outputFileName = "assets/index.js"
            }
        }

        binaries.executable()
    }
}
