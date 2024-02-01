package cn.ctcraft.ctonlinereward.common.module

import cn.ctcraft.ctonlinereward.common.logger.debug
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.severe
import taboolib.common5.cint
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.concurrent.ConcurrentHashMap
import java.util.jar.JarEntry
import java.util.jar.JarInputStream


object ModuleLoader {

    private val moduleDirectory = File(getDataFolder(),"module").also {
        it.mkdirs()
    }

    private val moduleMap:ConcurrentHashMap<String,Module> = ConcurrentHashMap()

    @Awake(LifeCycle.ACTIVE)
    fun load(){
        val moduleFiles = moduleDirectory.listFiles { dir, name -> name?.endsWith(".jar") == true }
        if (moduleFiles != null && moduleFiles.isNotEmpty()) {
            moduleFiles.forEach {
                debug("正在从 ${it} 加载模块")
                findMainClass(it)?.getConstructor()?.newInstance()?.also {
                    it.load()
                    val description = it.description
                    if (moduleMap.containsKey(description.name)){
                        if (extractVersion(moduleMap[description.name]!!.description.version) < extractVersion(description.version)){
                            moduleMap[description.name] = it
                            it.enable()
                        }
                    }else{
                        moduleMap[description.name] = it
                        it.enable()
                    }
                }
            }
        }
    }

    fun disableModule(name:String){
        moduleMap[name]?.disable()
        moduleMap.remove(name)
    }

    @Awake(LifeCycle.DISABLE)
    fun onPluginDisable(){
        moduleMap.forEach { (_, module) ->
            module.disable()
        }
        moduleMap.clear()
    }

    fun findMainClass(file:File):Class<out Module>?{
        if (!file.exists()) {
            return null
        }

        val jar: URL = file.toURI().toURL()
        val loader = URLClassLoader(arrayOf<URL>(jar), Module::class.java.getClassLoader())
        val matches: MutableList<String> = ArrayList()
        val classes: MutableList<Class<out Module?>> = ArrayList<Class<out Module?>>()

        JarInputStream(jar.openStream()).use { stream ->
            var entry: JarEntry? = null
            while (stream.nextJarEntry.also { entry = it } != null) {
                val name: String = entry!!.getName()
                if (name.isEmpty() || !name.endsWith(".class")) {
                    continue
                }
                matches.add(name.substring(0, name.lastIndexOf('.')).replace('/', '.'))
            }
            for (match in matches) {
                try {
                    val loaded: Class<*> = loader.loadClass(match)
                    if (Module::class.java.isAssignableFrom(loaded)) {
                        classes.add(loaded.asSubclass(Module::class.java))
                    }
                } catch (ignored: NoClassDefFoundError) {
                }
            }
        }
        if (classes.isEmpty()) {
            loader.close()
            return null
        }
        return classes[0] as Class<Module>
    }

    fun extractVersion(inputString:String):Int{
        val regex = Regex("\\d+")
        val matchResults = regex.findAll(inputString)
        return matchResults.joinToString("") { it.value }.cint
    }

}