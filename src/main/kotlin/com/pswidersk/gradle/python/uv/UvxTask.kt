package com.pswidersk.gradle.python.uv

import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import javax.inject.Inject

@CacheableTask
abstract class UvxTask @Inject constructor(
    objects: ObjectFactory
) : UvBaseTask(objects) {

    private val plugin: UvExtension = project.uvExtension

    init {
        executable = plugin.uvxExec.get().asFile.absolutePath
    }

}

