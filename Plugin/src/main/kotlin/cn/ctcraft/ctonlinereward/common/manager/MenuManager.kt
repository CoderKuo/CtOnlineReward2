package cn.ctcraft.ctonlinereward.common.manager

import cn.ctcraft.ctonlinereward.bukkit.menu.ConfigurationMenu
import cn.ctcraft.ctonlinereward.bukkit.menu.Menu
import cn.ctcraft.ctonlinereward.common.logger.Logging
import cn.ctcraft.ctonlinereward.common.utils.ConfigUtils
import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.watch.SimpleWatcher
import cn.hutool.core.io.watch.WatchMonitor
import cn.hutool.core.io.watch.watchers.WatcherChain
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.common.platform.function.submit
import taboolib.module.configuration.Configuration
import java.io.File
import java.nio.file.Path
import java.nio.file.WatchEvent
import java.util.concurrent.ConcurrentHashMap

object MenuManager {

    val menu = ConcurrentHashMap<String, Menu>()

    val originFileValue = ConcurrentHashMap<String, String>()

    val watcher = WatcherChain()
    lateinit var monitor: WatchMonitor

    @Awake(LifeCycle.ENABLE)
    fun loadFileMenu() {
        val file = File(getDataFolder(), "menu")
        if (!file.exists()) {
            file.mkdir()
            releaseResourceFile("menu/Basic.yml")
            releaseResourceFile("menu/Craft.yml")
            releaseResourceFile("menu/OnlineReward.yml")
            releaseResourceFile("menu/SignIn.yml")
        }


        menu.clear()

        FileUtil.walkFiles(file) {
            if (FileUtil.extName(it) == "yml") {
                FileUtil.mainName(it).also { fileName ->
                    val loadFromFile = Configuration.loadFromFile(it)
                    menu[fileName] = ConfigurationMenu(fileName, loadFromFile)
                    originFileValue[fileName] = ConfigUtils.md5(it.readText())

                    watcher.addChain(object : SimpleWatcher() {
                        override fun onModify(event: WatchEvent<*>?, currentPath: Path?) {
                            ConfigUtils.md5(it.readText()).also { md5 ->
                                if (originFileValue[fileName] != md5) {
                                    Logging.info("&6 检测到 &a$fileName &6菜单文件已修改,正在重新构建...")
                                    var oldMenu = menu[fileName]
                                    menu[fileName] =
                                        ConfigurationMenu(fileName, Configuration.loadFromFile(it)).also { newMenu ->
                                            oldMenu?.also {
                                                if (it.isOpened) {
                                                    submit {
                                                        it.closeAll().forEach {
                                                            it.openInventory(newMenu.toBukkitInventory())
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    oldMenu = null
                                    Logging.info("&a✓ &a$fileName 菜单已构建完毕.")
                                    originFileValue[fileName] = md5
                                }
                            }
                        }
                    })

                    Logging.info("&a✓ &6$fileName &a菜单已加载.")

                }
            }
        }
        monitor = WatchMonitor.create(file).apply {
            setWatcher(watcher)
            start()
        }

    }


    operator fun get(name: String): Menu? {
        return menu[name]
    }

    fun contains(name: String): Boolean {
        return menu.containsKey(name)
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        monitor.close()
    }


}