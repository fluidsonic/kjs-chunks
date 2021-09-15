import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

plugins {
    kotlin("js") version "1.5.30"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(project(":dynamic"))
    implementation(project(":index"))

    implementation(devNpm("html-webpack-plugin", "~5.3.1"))
}

// https://youtrack.jetbrains.com/issue/KT-48273
rootProject.plugins.withType(NodeJsRootPlugin::class.java) {
    rootProject.the<NodeJsRootExtension>().versions.webpackDevServer.version = "4.0.0"
}

kotlin {
    js(KotlinJsCompilerType.IR) {
        val mainCompilation = compilations.getByName(KotlinCompilation.MAIN_COMPILATION_NAME)
        val copyTaskName = "copy${mainCompilation.compileKotlinTaskName.removePrefix("compile")}ToRootProject"

        mainCompilation.apply {
            val compilation = this
            val compileKotlinTask = compileKotlinTask
            val npmModuleIndex = compilation.npmProject.dir.resolve(compilation.npmProject.main)
            val npmModuleDir = npmModuleIndex.parentFile

            compileKotlinTask
                .doFirst {
                    // Disable klib creation.
                    // Instead simply create separate JS files per module & dependency (in /build/classes/main/)

                    kotlinOptions {
                        outputFile = checkNotNull(outputFile) + "/index.js"

                        freeCompilerArgs = freeCompilerArgs
                            .filter { it != "-Xir-produce-klib-dir" }
                            .plus(listOf("-Xir-per-module", "-Xir-produce-js"))
                    }
                }
                .finalizedBy(tasks.create<Copy>(copyTaskName) {
                    // Copy the separate JS files to the expected location (from /build/classes/main/ to /build/js/packages/kjs-chunks/kotlin/)

                    duplicatesStrategy = DuplicatesStrategy.INCLUDE

                    from(compileKotlinTask) {
                        exclude {
                            println(it.file.toPath().toString() + " .. " + npmModuleIndex.name)
                            ;it.file == compileKotlinTask.outputFileProperty.get()
                        }
                    }
                    from(compileKotlinTask.outputFileProperty) {
                        rename { npmModuleIndex.name }
                    }
                    into(npmModuleDir)
                })
        }

        browser {
            webpackTask {
                dependsOn(copyTaskName)

                outputFileName = "assets/[name].[contenthash].js"
                sourceMaps = false
            }

            runTask {
                dependsOn(copyTaskName)

                outputFileName = "assets/index.js"
            }
        }

        binaries.executable().forEach { binary ->
            // Looks like we don't need this task anymore?

            binary as JsIrBinary
            binary.linkTask.configure {
                isEnabled = false
            }
        }
    }
}
