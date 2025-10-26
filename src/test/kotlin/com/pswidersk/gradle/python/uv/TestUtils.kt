package com.pswidersk.gradle.python.uv

import java.io.File

fun loadResource(location: String): File = File(object {}.javaClass.classLoader.getResource(location)!!.file)
