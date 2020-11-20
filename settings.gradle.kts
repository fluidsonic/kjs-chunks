rootProject.name = "kjs-chunks"

file("modules")
    .listFiles()!!
    .filter(File::isDirectory)
    .forEach { directory ->
        val name = directory.name

        include(name)

        project(":$name").apply {
            this.projectDir = directory
        }
    }
