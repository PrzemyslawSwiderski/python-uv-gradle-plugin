@file:JvmName("PythonPluginConstants")

package com.pswidersk.gradle.python.uv

/**
 * Name of python plugin extension in projects.
 */
const val PYTHON_PLUGIN_EXTENSION_NAME = "pythonUvPlugin"

/**
 * Directory where gradle specific files are stored.
 */
const val GRADLE_FILES_DIR = ".gradle"

/**
 * Name of directory where python environments will be stored.
 */
const val PYTHON_ENVS_DIR = "python"

/**
 * Plugin tasks group name.
 */
const val PLUGIN_TASKS_GROUP_NAME = "python"

/**
 * Default Python version. [List of available releases](https://anaconda.org/conda-forge/python/).
 */
const val DEFAULT_PYTHON_VERSION = "3.14.0"

/**
 * Default uv installer.
 */
const val DEFAULT_UV_INSTALLER = "uv"

/**
 * Default uv version. [List of available releases](https://github.com/astral-sh/uv/releases).
 */
const val DEFAULT_UV_VERSION = "0.9.5"

/**
 * Default uv repository URL.
 */
const val DEFAULT_UV_REPO_URL = "https://github.com/astral-sh/uv/releases/download/"

/**
 * Default Intellij Idea config directory.
 */
const val DEFAULT_IDEA_DIR = ".idea"

/**
 * SDK Import Intellij plugin config file name.
 */
const val SDK_IMPORT_FILE_NAME = "sdk-import.yml"

/**
 * Default group for uv tasks.
 */
const val DEFAULT_UV_TASK_GROUP = "uv tool"
