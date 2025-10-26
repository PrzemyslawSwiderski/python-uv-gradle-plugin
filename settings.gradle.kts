rootProject.name = "python-uv-gradle-plugin"

include(
//    "examples:sample-python-project",
)

pluginManagement {
    val pythonPluginVersionForExamples: String by settings

    plugins {
        id("com.pswidersk.python-uv-plugin") version pythonPluginVersionForExamples
    }

    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
