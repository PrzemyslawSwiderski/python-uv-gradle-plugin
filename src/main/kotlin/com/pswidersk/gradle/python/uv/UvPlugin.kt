package com.pswidersk.gradle.python.uv

import com.pswidersk.gradle.python.sdkimport.registerSdkImportTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class UvPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        val uvExtension = extensions.create(PYTHON_UV_PLUGIN_EXTENSION_NAME, UvExtension::class.java, this)

        tasks.register<ListPropertiesTask>("listPluginProperties")
        val uvDownloadTask = tasks.register<UvDownloadTask>("uvDownload")
        tasks.register<UvSetupTask>(UV_SETUP_TASK) {
            dependsOn(uvDownloadTask)
        }
        registerSdkImportTasks(uvExtension)
    }


}
