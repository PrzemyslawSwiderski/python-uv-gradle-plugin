package com.pswidersk.gradle.python.uv

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

abstract class UvSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val extension: Extension = project.pythonUvPlugin

    init {
        group = DEFAULT_UV_TASK_GROUP
        description = "Setup uv tool"
        this.onlyIf {
            !extension.uvBinDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup() {
        with(extension) {
            val uvDirFile = uvDir.get().asFile
            val uvInstaller = uvInstallerFile.get().asFile
            logger.lifecycle("Installing ${this.uvInstaller.get()}...")
            if (isWindows) {
                runOnWindows(uvInstaller, uvDirFile)
            } else {
                runOnUnix(uvInstaller, uvDirFile)
            }
        }
    }

    private fun runOnWindows(condaInstaller: File, condaDirFile: File) = execOperations.exec {
        val cmdArgs = listOf(
            "cmd",
            "/c",
            "start",
            "/wait",
            condaInstaller.canonicalPath,
            "/InstallationType=JustMe",
            "/RegisterPython=0",
            "/AddToPath=0",
            "/S",
            "/D=${condaDirFile.canonicalPath}"
        )
        it.commandLine(cmdArgs)
    }

    private fun runOnUnix(uvInstaller: File, uvDirFile: File) = execOperations.exec {
        it.executable = uvInstaller.canonicalPath
        it.environment("UV_UNMANAGED_INSTALL", uvDirFile.canonicalPath)
    }

}
