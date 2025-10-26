package com.pswidersk.gradle.python.uv

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@WireMockTest
class UvDownloadTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test if request is correct for custom repo`(wmRuntimeInfo: WireMockRuntimeInfo) {
        // given
        // Mocking 404 return to stop setup task (we only care about the request)
        stubFor(
            get(urlMatching("/uv/.*"))
                .willReturn(aResponse().withStatus(404))
        )

        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("com.pswidersk.python-uv-plugin")
            }
            
            pythonUvPlugin {
                uvVersion.set("9.4.0")
                uvRepoUrl.set("${wmRuntimeInfo.httpBaseUrl}/uv")
                uvRepoUsername.set("user")
                uvRepoPassword.set("pass")
                uvRepoHeaders.set(mapOf(
                    "SOME_HEADER_1" to "testValue1",
                    "SOME_HEADER_2" to "testValue2"
                ))
            }
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments(":uvSetup")

        // when
        runner.buildAndFail()

        // then
        verify(
            getRequestedFor(urlMatching("/conda/.*"))
                .withUrl("/uv/9.4.0/uv-installer.$exec")
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader("SOME_HEADER_1", equalTo("testValue1"))
                .withHeader("SOME_HEADER_2", equalTo("testValue2"))
        )
    }
}
