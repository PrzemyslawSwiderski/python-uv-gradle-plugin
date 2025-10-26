package com.pswidersk.gradle.python.uv

import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class UvxTask @Inject constructor(
    objects: ObjectFactory
) : UvBaseTask(objects) {

    private val plugin: UvExtension = project.uvExtension

    init {
        executable = plugin.uvxExec.get().asFile.absolutePath
    }

}

