package com.pswidersk.gradle.python.uv

import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File


internal class UvPluginTest {

    @Test
    fun `plugin was applied`() {
        val project: Project = ProjectBuilder.builder().build()
        project.apply<UvPlugin>()

        assertThat(project.plugins.size).isEqualTo(1)
    }

    @Test
    fun `uv setup was successful`(@TempDir tempDir: File) {
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
            .withArguments(":uvSetup")
            .withDebug(true)

        // when
        val firstRunResult = runner.build()

        // then
        with(firstRunResult) {
            assertThat(task(":uvDownload")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":uvSetup")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }

    @Test
    fun `test if test python script was run successfully`(@TempDir tempDir: File) {
        // given
        val ideaDir = tempDir.resolve(".idea")
        ideaDir.mkdir()
        val pythonMessage = "Hello world from Gradle Python Plugin :)"
        val buildFile = File(tempDir, "build.gradle.kts")
        val testScriptFile = File(tempDir, "testScript.py")
        buildFile.writeText(
            """
            import com.pswidersk.gradle.python.VenvTask
            
            plugins {
                id("com.pswidersk.python-uv-plugin")
            }
            
            tasks {
                register<VenvTask>("runTestScript") {
                    args = listOf("testScript.py")
                }
            }
        """.trimIndent()
        )
        testScriptFile.writeText(
            """
            print("$pythonMessage")
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments("--configuration-cache", "--warning-mode", "all", "--info", ":runTestScript")

        // when
        val firstRunResult = runner.build()
        val secondRunResult = runner.build()

        // then
        with(firstRunResult) {
            assertThat(task(":condaSetup")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":envSetup")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":locatePython")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":saveSdkImportConfig")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":runTestScript")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains(pythonMessage)
        }
        with(secondRunResult) {
            assertThat(task(":condaSetup")!!.outcome).isEqualTo(TaskOutcome.SKIPPED)
            assertThat(task(":envSetup")!!.outcome).isEqualTo(TaskOutcome.SKIPPED)
            assertThat(task(":locatePython")!!.outcome).isEqualTo(TaskOutcome.UP_TO_DATE)
            assertThat(task(":saveSdkImportConfig")!!.outcome).isEqualTo(TaskOutcome.UP_TO_DATE)
            assertThat(task(":runTestScript")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains(pythonMessage)
        }
        assertThat(ideaDir.resolve("sdk-import.yml")).exists()
    }
}
