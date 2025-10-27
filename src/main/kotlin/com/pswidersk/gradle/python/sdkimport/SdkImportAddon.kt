package com.pswidersk.gradle.python.sdkimport

import com.pswidersk.gradle.python.uv.UvExtension
import com.pswidersk.gradle.python.uv.SDK_IMPORT_FILE_NAME
import com.pswidersk.gradle.python.uv.UvTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

private const val SDK_IMPORT_TASK_GROUP = "sdk import"

fun Project.registerSdkImportTasks(pythonUvExtension: UvExtension) {
    val locatePythonTask = tasks.register<UvTask>("locatePython") {
        group = SDK_IMPORT_TASK_GROUP
        val pythonEnvFileName = "${project.name}-pythonEnv.txt"
        val pythonEnvFile = temporaryDir.resolve(pythonEnvFileName)
        description = "Saves Python SDK reference to a temporary file"
        args = listOf("python", "find")
        outputFile.set(pythonEnvFile)
        outputs.file(pythonEnvFile)
    }

    tasks.register<SaveSdkImportConfigTask>("saveSdkImportConfig") {
        group = SDK_IMPORT_TASK_GROUP
        sdkConfigFile = pythonUvExtension.ideaDir.file(SDK_IMPORT_FILE_NAME).get().asFile
        inputFile = locatePythonTask.get().outputFile.get().asFile
        dependsOn(locatePythonTask)
        onlyIf { pythonUvExtension.ideaDir.get().asFile.exists() }
    }
}
