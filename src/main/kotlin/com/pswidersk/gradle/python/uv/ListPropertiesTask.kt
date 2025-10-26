package com.pswidersk.gradle.python.uv

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ListPropertiesTask : DefaultTask() {

    private val uvExtension: UvExtension = project.uvExtension

    init {
        group = DEFAULT_UV_TASK_GROUP
        description = "List basic plugin properties."
    }

    @TaskAction
    fun setup() {
        with(uvExtension) {
            logger.lifecycle(
                """
                Operating system: $os
                Install directory: ${installDir.get()}
                uv repo URL: ${uvRepoUrl.get()}
                uv version: ${uvVersion.get()}
                uv directory: ${uvDir.get()}
                uv exec location: ${uvExec.get()}
            """.trimIndent()
            )
        }
    }
}
