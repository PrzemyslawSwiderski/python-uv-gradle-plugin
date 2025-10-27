package com.pswidersk.gradle.python.uv

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

abstract class UvBaseTask(
    objects: ObjectFactory
) : AbstractExecTask<UvBaseTask>(UvBaseTask::class.java) {

    /**
     * Input file to be passed into standard input.
     */
    @Internal
    val inputFile: RegularFileProperty = objects.fileProperty()

    /**
     * Output file to be passed into standard output.
     */
    @Internal
    val outputFile: RegularFileProperty = objects.fileProperty()


    init {
        group = PLUGIN_TASKS_GROUP_NAME
        dependsOn(UV_SETUP_TASK)
    }

    @TaskAction
    fun execute() {
        if (inputFile.isPresent)
            standardInput = inputFile.get().asFile.inputStream()
        if (outputFile.isPresent)
            standardOutput = outputFile.get().asFile.outputStream()
        logger.lifecycle("Executing command: '$commandLine'")
    }
}

