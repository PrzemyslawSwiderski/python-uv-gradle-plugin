package com.pswidersk.gradle.python.uv

import com.pswidersk.gradle.python.uv.pythonUvPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class EnvSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val extension: com.pswidersk.gradle.python.uv.Extension = project.pythonUvPlugin

    init {
        group = _root_ide_package_.com.pswidersk.gradle.python.uv.DEFAULT_UV_TASK_GROUP
        description = "Setup python env"
        this.onlyIf { !extension.pythonEnvDir.get().asFile.exists() }
    }

    @TaskAction
    fun setup() {
        with(extension) {
            val condaExec = condaExec.get().asFile.absolutePath
            val condaArgs = listOf(
                "create",
                "--name",
                pythonEnvName.get(),
                "python=${pythonVersion.get()}",
                "--yes"
            )
            logger.lifecycle("Executing command: $condaExec ${condaArgs.joinToString(" ")}")
            execOperations.exec {
                it.executable = condaExec
                it.args(condaArgs)
            }
        }
    }
}
