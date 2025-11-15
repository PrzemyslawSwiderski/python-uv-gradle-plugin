rootProject.name = "python-uv-gradle-plugin"

include(
    "examples:sample-python-project",
)

pluginManagement {
    val pluginVersionForExamples: String by settings

    plugins {
        id("com.pswidersk.python-uv-plugin") version pluginVersionForExamples
    }

    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
