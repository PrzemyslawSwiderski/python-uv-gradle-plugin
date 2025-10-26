package com.pswidersk.gradle.python.uv

import org.apache.commons.io.input.ObservableInputStream
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URI
import java.net.URLConnection
import java.util.*

abstract class UvDownloadTask : DefaultTask() {

    private val plugin: Extension = project.pythonUvPlugin
    private val progressLogger = ProgressLogger(consumer = { msg -> logger.lifecycle(msg) })

    init {
        group = DEFAULT_UV_TASK_GROUP
        description = "Download uv tool"
        this.onlyIf {
            !plugin.uvBinDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup() {
        with(plugin) {
            val condaInstaller = uvInstallerFile.get()
            downloadUv(condaInstaller.asFile)
        }
    }

    private fun downloadUv(destinationFile: File) {
        val uvRepoUrl = plugin.uvRepoUrl.get()
        val uvInstaller = plugin.uvInstaller.get()
        logger.lifecycle("Downloading $uvInstaller to: ${destinationFile.canonicalPath} from: $uvRepoUrl (please wait, it can take a while)")
        val downloadUri = "${uvRepoUrl}/${plugin.uvVersion.get()}/uv-installer.$exec"
        val connection = URI.create(downloadUri).toURL().openConnection()
        addBasicAuth(connection)
        addCustomHeaders(connection)
        getExecutable(connection, destinationFile)
    }

    private fun addBasicAuth(connection: URLConnection) {
        if (plugin.uvRepoUsername.isPresent) {
            val uvRepoUsername = plugin.uvRepoUsername.get()
            val uvRepoPassword = plugin.uvRepoPassword.get()
            logger.lifecycle("Adding basic authorization headers for '$uvRepoUsername' user.")
            val userAndPass = "$uvRepoUsername:$uvRepoPassword"
            val basicAuth = "Basic ${String(Base64.getEncoder().encode(userAndPass.toByteArray()))}"
            connection.setRequestProperty("Authorization", basicAuth)
        }
    }

    private fun addCustomHeaders(connection: URLConnection) {
        val uvRepoHeaders = plugin.uvRepoHeaders.get()
        uvRepoHeaders.forEach { connection.addRequestProperty(it.key, it.value) }
    }

    private fun getExecutable(connection: URLConnection, destinationFile: File) {
        val progressObserver = ProgressObserver(progressLogger, connection)
        val urlInputStream = ObservableInputStream(connection.getInputStream().buffered(), progressObserver)

        urlInputStream.use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val res = destinationFile.setExecutable(true)
        if (!res) {
            logger.warn("Uv exec file could not be changed to executable.")
        }
    }

}
