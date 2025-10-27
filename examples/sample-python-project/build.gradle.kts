import com.pswidersk.gradle.python.uv.UvTask
import com.pswidersk.gradle.python.uv.UvxTask
import java.io.ByteArrayInputStream

plugins {
    id("com.pswidersk.python-uv-plugin")
}

pythonUvPlugin {
    uvVersion = "0.9.5"
}

tasks {

    val sync by registering(UvTask::class) {
        args = listOf("sync")
        finalizedBy("saveSdkImportConfig")
    }

    register<UvTask>("runQuickSort") {
        args = listOf("run", "quicksort.py")
        outputFile = file("output.txt")
        dependsOn(sync)
    }

    register<UvTask>("runAnalyzeData") {
        args = listOf("run", "analyze_data.py")
        dependsOn(sync)
    }

    register<UvTask>("runInlineCode") {
        args = listOf("run", "-")
        // language="Python"
        val pythonCode = """
            a = 2
            b = 3
            c = a + b
            print(f"{c=}")
        """.trimIndent()
        standardInput = ByteArrayInputStream(pythonCode.toByteArray())
    }

    register<UvxTask>("formatCode") {
        args = listOf("black@25.9.0", ".")
    }

    register<UvxTask>("runHello") {
        args = listOf(
            "--from", "cowsay", "cowsay", "-t",
            "\uD83D\uDC2E Hello there from uv Gradle Plugin \uD83D\uDC04"
        )
    }


}
