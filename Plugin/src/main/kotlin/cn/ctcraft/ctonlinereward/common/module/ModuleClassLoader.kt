package cn.ctcraft.ctonlinereward.common.module

import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile


class ModuleClassLoader(private var file:File): ClassLoader() {

    @Throws(ClassNotFoundException::class)
    public override fun findClass(name: String): Class<*> {
        try {
            val jarFile = JarFile(file)
            val entries: Enumeration<JarEntry> = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry: JarEntry = entries.nextElement()
                if (entry.getName().endsWith(".class") && entry.getName().equals("$name.class")) {
                    val inputStream = jarFile.getInputStream(entry)
                    val byteArrayInputStream = ByteArrayInputStream(inputStream.readBytes())
                    val bytes: ByteArray = byteArrayInputStream.readBytes()
                    return defineClass(name, bytes, 0, bytes.size)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return super.findClass(name)
    }

}