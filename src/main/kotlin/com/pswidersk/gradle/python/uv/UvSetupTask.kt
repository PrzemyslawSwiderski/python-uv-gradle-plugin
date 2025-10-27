package com.pswidersk.gradle.python.uv

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

abstract class UvSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val plugin: UvExtension = project.uvExtension

    init {
        group = DEFAULT_UV_TASK_GROUP
        description = "Setup uv tool"
        this.onlyIf {
            !plugin.uvDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup() {
        with(plugin) {
            val uvDirFile = uvDir.get().asFile
            val uvInstaller = uvInstallerFile.get().asFile
            logger.lifecycle("Installing uv...")
            if (isWindows) {
                runOnWindows(uvInstaller, uvDirFile)
            } else {
                runOnUnix(uvInstaller, uvDirFile)
            }
        }
    }

    private fun runOnWindows(uvInstaller: File, uvDirFile: File) = execOperations.exec {
        it.commandLine(
            "powershell",
            "-ExecutionPolicy", "Bypass",
            "-File", uvInstaller.canonicalPath
        )
        it.environment("UV_UNMANAGED_INSTALL", uvDirFile.canonicalPath)
    }

    private fun runOnUnix(uvInstaller: File, uvDirFile: File) = execOperations.exec {
        it.executable = uvInstaller.canonicalPath
        it.environment("UV_UNMANAGED_INSTALL", uvDirFile.canonicalPath)
    }

}
