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
import javax.inject.Inject

abstract class UvExtension @Inject constructor(
    project: Project,
    providerFactory: ProviderFactory,
    objects: ObjectFactory,
    fileFactory: FileFactory
) {

    val uvVersion: Property<String> = objects.property<String>().convention(DEFAULT_UV_VERSION)

    val uvRepoUrl: Property<String> = objects.property<String>().convention(DEFAULT_UV_REPO_URL)

    val uvRepoUsername: Property<String> = objects.property()

    val uvRepoPassword: Property<String> = objects.property()

    val uvRepoHeaders: MapProperty<String, String> = objects.mapProperty()

    val installDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            baseInstallDirectory.get().dir(GRADLE_FILES_DIR).dir(PYTHON_ENVS_DIR)
        }
    )

    val ideaDir: DirectoryProperty = objects.directoryProperty()
        .convention(fileFactory.dir(project.rootDir.resolve(DEFAULT_IDEA_DIR)))

    internal val uvDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            installDir.get()
                .dir("uv-${uvVersion.get()}")
        }
    )

    internal val uvInstallerFile: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            val installerDirFile = installDir.get().asFile
            installerDirFile.mkdirs()
            installDir.get().file("uv-${uvVersion.get()}.$exec")
        }
    )

    internal val uvExec: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                this.uvDir.get().dir("bin").file("uv.exe")
            else
                this.uvDir.get().file("uv")
        }
    )

    internal val uvxExec: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                this.uvDir.get().dir("bin").file("uvx.exe")
            else
                this.uvDir.get().file("uvx")
        }
    )

    internal val intellijModuleName: Property<String> = objects.property<String>().convention(
        providerFactory.provider {
            project.intellijModuleName()
        }
    )

    internal val baseInstallDirectory: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            fileFactory.dir(project.rootDir)
        }
    )

}
