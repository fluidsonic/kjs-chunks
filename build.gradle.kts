import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

plugins {
    id("org.jetbrains.kotlin.js") version "1.4.20-RC"
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

dependencies {
    implementation(project(":dynamic"))
    implementation(project(":index"))
}

kotlin {
    js(KotlinJsCompilerType.IR) {
        compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME) {
            val compilation = this
            val compileKotlinTask = compileKotlinTask
            val npmModuleIndex = compilation.npmProject.dir.resolve(compilation.npmProject.main)
            val npmModuleDir = npmModuleIndex.parentFile

            compileKotlinTask
                .doFirst {
                    // Disable klib creation.
                    // Instead simply create separate JS files per module & dependency (in /build/classes/main/)

                    kotlinOptions {
                        freeCompilerArgs = freeCompilerArgs
                            .filter { it != "-Xir-produce-klib-dir" }
                            .plus(listOf("-Xir-per-module", "-Xir-produce-js"))
                    }
                }
                .finalizedBy(tasks.create<Copy>("copy${compileKotlinTaskName.removePrefix("compile")}ToRootProject") {
                    // Copy the separate JS files to the expected location (from /build/classes/main/ to /build/js/packages/kjs-chunks/kotlin/)

                    from(compileKotlinTask) {
                        exclude { it.file == compileKotlinTask.outputFile }
                    }
                    from(compileKotlinTask.outputFile) {
                        rename { npmModuleIndex.name }
                    }
                    into(npmModuleDir)
                })
        }

        browser()

        binaries.executable().forEach { binary ->
            // Looks like we don't need this task anymore?

            binary as JsIrBinary
            binary.linkTask.configure {
                isEnabled = false
            }
        }
    }
}
