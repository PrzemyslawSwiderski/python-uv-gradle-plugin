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
//        val unblockCmd = "Unblock-File -Path '${uvInstaller.canonicalPath}' -ErrorAction SilentlyContinue"
        val unblockCmd2 = "Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope CurrentUser"
        val installCmd = "& '${uvInstaller.canonicalPath}'"

        // Combine both commands in PowerShell with &&
        val psCommand = "$unblockCmd2; $installCmd"

        it.commandLine(
            "powershell",
            "-NoProfile",
            "-WindowStyle", "Hidden",
            "-ExecutionPolicy", "Bypass",
            "-Command", psCommand
        )
        it.environment("UV_UNMANAGED_INSTALL", uvDirFile.canonicalPath)
    }

    private fun runOnUnix(uvInstaller: File, uvDirFile: File) = execOperations.exec {
        it.executable = uvInstaller.canonicalPath
        it.environment("UV_UNMANAGED_INSTALL", uvDirFile.canonicalPath)
    }

}
