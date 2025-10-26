import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.pluginVersions)
    alias(libs.plugins.axiom.release)
}

version = scmVersion.version

repositories {
    mavenLocal()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation(libs.bundles.compile)
    testImplementation(libs.bundles.test)
}

java {
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    test {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    named<DependencyUpdatesTask>("dependencyUpdates").configure {
        rejectVersionIf {
            fun isNonStable(version: String): Boolean {
                val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
                val regex = "^[0-9,.v-]+(-r)?$".toRegex()
                val isStable = stableKeyword || regex.matches(version)
                return isStable.not()
            }
            isNonStable(candidate.version)
        }
    }
}



gradlePlugin {
    website = "https://github.com/PrzemyslawSwiderski/python-uv-gradle-plugin"
    vcsUrl = "https://github.com/PrzemyslawSwiderski/python-uv-gradle-plugin"
    plugins {
        create("python-uv-gradle-plugin") {
            id = "com.pswidersk.python-uv-plugin"
            implementationClass = "com.pswidersk.gradle.python.PythonUvPlugin"
            displayName = "Gradle Python Plugin"
            description = "Gradle Plugin for Python projects with uv tool."
            tags = listOf(
                "python",
                "venv",
                "numpy",
                "uv",
                "scipy",
                "pandas",
                "ollama",
                "flask",
                "matplotlib",
                "sklearn"
            )
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}
