[![🥁 Build](https://github.com/PrzemyslawSwiderski/python-uv-gradle-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/PrzemyslawSwiderski/python-uv-gradle-plugin/actions/workflows/build.yml)
[![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/PrzemyslawSwiderski/python-uv-gradle-plugin?label=Plugin%20Version&sort=semver&style=plastic)](https://plugins.gradle.org/plugin/com.pswidersk.python-uv-plugin)

# Python uv Gradle Plugin

Gradle plugin that wraps the **[uv tool](https://docs.astral.sh/uv/)**.
It automatically downloads and installs it. Developer can use helpful tasks to integrate Python projects into
Gradle build system.

---

## Requirements

* Java `11` or higher
* Gradle `7.0` or higher project

## Quick start

1. Apply a plugin to a project as described
   in [gradle portal](https://plugins.gradle.org/plugin/com.pswidersk.python-uv-plugin).

2. Add sample task.

    ```kotlin
    import com.pswidersk.gradle.python.uv.UvTask
    import com.pswidersk.gradle.python.uv.UvxTask
    
    plugins {
        id("com.pswidersk.python-uv-plugin")
    }
    
    tasks {
        register<UvxTask>("runHello") {
            args = listOf(
                "--from", "cowsay", "cowsay", "-t",
                "\uD83D\uDC2E Hello there from uv Gradle Plugin \uD83D\uDC04"
            )
        }
    }
    ```

3. Exec it with: `./gradlew runHello` 😼

Notes:

- `pythonUvPlugin` extension configuration (version, repo settings, installDir etc.).
- `UvTask` is used to execute `uv` commands (e.g., `sync`, `run <script>`).
- `UvxTask` is used to execute `uvx` (for running packages/tools by name: `black@25.9.0`, `ruff`, `pytest` etc.).
- You can set `inputFile` or `outputFile` on tasks to redirect standard in/out with files.
- Use Gradle `dependsOn` to ensure environment sync/install runs before other tasks.

### Additional examples can be found in `examples` module in this project.

---

## Extension properties

Below are the key extension properties you can configure in `pythonUvPlugin { ... }`. Types are Kotlin Gradle types (
Property/MapProperty/DirectoryProperty/etc).

- `uvVersion: Property<String>`
    - Type: `Property<String>`
    - Default: `DEFAULT_UV_VERSION` (plugin default). Example override: `uvVersion = "0.9.9"`.
    - Purpose: The `uv` release/version to install and use.

- `uvRepoUrl: Property<String>`
    - Type: `Property<String>`
    - Default: `DEFAULT_UV_REPO_URL`
    - Purpose: Custom repository URL for fetching `uv` artifacts/packages.

- `uvRepoUsername: Property<String>`
    - Type: `Property<String>`
    - Purpose: Optional username for private repo authentication.

- `uvRepoPassword: Property<String>`
    - Type: `Property<String>`
    - Purpose: Optional password for private repo authentication.

- `uvRepoHeaders: MapProperty<String, String>`
    - Type: `MapProperty<String, String>`
    - Purpose: Optional custom HTTP headers for repository requests (e.g., auth tokens, special headers).

- `installDir: DirectoryProperty`
    - Type: `DirectoryProperty`
    - Default: `baseInstallDirectory -> GRADLE_FILES_DIR -> PYTHON_ENVS_DIR` (constructed via provider)
    - Purpose: Base directory where `uv` runtimes and installers will be placed. Override when you need a specific
      install location.

- `ideaDir: DirectoryProperty`
    - Type: `DirectoryProperty`
    - Default: `project.rootDir/DEFAULT_IDEA_DIR` (via `fileFactory.dir(project.rootDir.resolve(DEFAULT_IDEA_DIR))`)
    - Purpose: Location used for IntelliJ/IDE integration files when the plugin writes IDE-specific output.

## Edge cases & tips

- If you use a private package repo, set `uvRepoUrl` and `uvRepoUsername`/`uvRepoPassword` or `uvRepoHeaders` for
  token-based auth.
- If you need the uv files in a specific place (CI or container), set `installDir` or `baseInstallDirectory`.
- On Windows, the plugin expects `uv.exe` / `uvx.exe` equivalents and will locate them under a `bin/` directory if
  needed.
- Ensure Gradle can write to `installDir` (permissions) — CI agents sometimes use read-only workspaces.
