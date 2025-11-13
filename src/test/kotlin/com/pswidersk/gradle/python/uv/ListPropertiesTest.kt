package com.pswidersk.gradle.python.uv

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.internal.PluginUnderTestMetadataReading
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.File.separatorChar

class ListPropertiesTest {
    @TempDir
    lateinit var tempDir: File

    @Test
    fun `default properties were correctly set`() {
        // given
        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("com.pswidersk.python-uv-plugin")
            }
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments(":listPluginProperties")
            .withDebug(true)

        // when
        val runResult = runner.build()

        // then
        with(runResult) {
            assertThat(task(":listPluginProperties")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains(
                ".gradle${separatorChar}python",
                "uv-0.9.9",
                "Operating system: $os",
                "uv version: 0.9.9",
                "uv repo URL: https://github.com/astral-sh/uv/releases/download/"
            )
        }
    }

    @Test
    fun `test if defaults are overridden by user`() {
        // given
        val customInstallDir = tempDir.resolve(".gradleCustomPath").resolve("python").invariantSeparatorsPath
        val buildFile = File(tempDir, "build.gradle.kts")

        var pluginClasspath = PluginUnderTestMetadataReading.readImplementationClasspath()
        pluginClasspath = pluginClasspath.filter { !it.path.contains("java") }
        buildFile.writeText(
            """
            plugins {
                id("com.pswidersk.python-uv-plugin")
            }
            pythonUvPlugin {
                uvVersion.set("0.9.9")
                installDir.set(file("$customInstallDir"))
            }
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath(pluginClasspath)
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments(":listPluginProperties")
//            .withDebug(true)

        // when
        val runResult = runner.build()

        // then
        with(runResult) {
            assertThat(task(":listPluginProperties")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains(
                "uv-0.9.9",
                ".gradleCustomPath${separatorChar}python",
            )
        }
    }

}
