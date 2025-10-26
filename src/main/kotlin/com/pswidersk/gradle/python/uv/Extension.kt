package com.pswidersk.gradle.python.uv

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property
import java.io.File
import javax.inject.Inject

abstract class Extension @Inject constructor(
    project: Project,
    providerFactory: ProviderFactory,
    objects: ObjectFactory,
    fileFactory: FileFactory
) {

    val pythonVersion: Property<String> = objects.property<String>().convention(DEFAULT_PYTHON_VERSION)

    val uvVersion: Property<String> = objects.property<String>().convention(DEFAULT_UV_VERSION)

    val uvInstaller: Property<String> = objects.property<String>().convention(DEFAULT_UV_INSTALLER)

    val uvRepoUrl: Property<String> = objects.property<String>().convention(DEFAULT_UV_REPO_URL)

    val uvRepoUsername: Property<String> = objects.property()

    val uvRepoPassword: Property<String> = objects.property()

    val uvRepoHeaders: MapProperty<String, String> = objects.mapProperty()

    val useHomeDir: Property<Boolean> = objects.property<Boolean>().convention(false)

    val installDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            baseInstallDirectory.get().dir(GRADLE_FILES_DIR).dir(PYTHON_ENVS_DIR)
        }
    )

    val systemArch: Property<String> = objects.property<String>().convention(arch)

    val ideaDir: DirectoryProperty = objects.directoryProperty()
        .convention(fileFactory.dir(project.rootDir.resolve(DEFAULT_IDEA_DIR)))

    internal val uvDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            installDir.get()
                .dir("${uvInstaller.get()}-${uvVersion.get()}")
        }
    )

    internal val uvInstallerFile: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            val installerDirFile = installDir.get().asFile
            installerDirFile.mkdirs()
            installDir.get().file("${uvInstaller.get()}-${uvVersion.get()}-$os-${systemArch.get()}.$exec")
        }
    )

    internal val pythonEnvName: Property<String> = objects.property<String>().convention(
        providerFactory.provider { "python-${pythonVersion.get()}" }
    )

    internal val pythonEnvDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { uvDir.get().dir("envs").dir(pythonEnvName.get()) }
    )

    internal val uvBinDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { uvDir.get().dir("condabin") }
    )

    internal val condaActivatePath: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                uvBinDir.get().file("activate.bat")
            else
                uvDir.get().dir("bin").file("activate")
        }
    )

    internal val condaExec: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                this.uvBinDir.get().file("conda.bat")
            else
                this.uvBinDir.get().file("conda")
        }
    )

    internal val intellijModuleName: Property<String> = objects.property<String>().convention(
        providerFactory.provider {
            project.intellijModuleName()
        }
    )

    internal val baseInstallDirectory: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            if (useHomeDir.get()) {
                val homePath = providerFactory.systemProperty("user.home").get()
                fileFactory.dir(File(homePath))
            } else {
                fileFactory.dir(project.rootDir)
            }
        }
    )

}
