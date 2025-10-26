package com.pswidersk.gradle.python.uv

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ListPropertiesTask : DefaultTask() {

    private val extension: Extension = project.pythonUvPlugin

    init {
        group = DEFAULT_UV_TASK_GROUP
        description = "List basic plugin properties."
    }

    @TaskAction
    fun setup() {
        with(extension) {
            logger.lifecycle(
                """
                Operating system: $os
                Arch: ${systemArch.get()}
                Use home directory: ${useHomeDir.get()}
                Install directory: ${installDir.get()}
                Python: ${pythonEnvName.get()}
                Python environment: ${pythonEnvDir.get()}
                Conda repo URL: ${uvRepoUrl.get()}
                ${uvInstaller.get()} version: ${uvVersion.get()}
                ${uvInstaller.get()} directory: ${uvDir.get()}
                Conda activate path: ${condaActivatePath.get()}
                Conda exec location: ${condaExec.get()}
            """.trimIndent()
            )
        }
    }
}
