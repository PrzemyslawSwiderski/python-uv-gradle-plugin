package com.pswidersk.gradle.python.uv

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.getByType

/**
 * Gets the [UvExtension] that is installed on the project.
 */
internal val Project.uvExtension: UvExtension
    get() = extensions.getByType()

/**
 * Returns if operating system is Windows
 */
internal val isWindows: Boolean
    get() = OperatingSystem.current().isWindows

/**
 * Returns simplified operating system name
 */
internal val os: String
    get() {
        return when {
            OperatingSystem.current().isMacOsX -> "MacOSX"
            isWindows -> "Windows"
            else -> "Linux"
        }
    }

/**
 * Returns exec extensions
 */
internal val exec: String
    get() {
        return when {
            OperatingSystem.current().isLinux -> "sh"
            isWindows -> "ps1"
            else -> "sh"
        }
    }

/**
 * Converts Gradle project path to Intellij one
 * For example: :nestedModule:someOtherModule -> parent.nestedModule.someOtherModule
 */
internal fun Project.intellijModuleName(): String = this.path
    .replace(':', '.')
    .replaceFirst(".", this.rootProject.name + '.')
    .trimEnd('.')

