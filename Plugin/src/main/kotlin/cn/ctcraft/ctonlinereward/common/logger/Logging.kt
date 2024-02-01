package cn.ctcraft.ctonlinereward.common.logger

import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.chat.colored
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.io.File
import java.util.Objects

object Logging {

    fun info(vararg message: Any?){
        Bukkit.getConsoleSender().sendMessage(("&f[&aCtOnlineReward&f] "+message.joinToString(" ")).colored())
    }

    fun warning(vararg message: Any?){
        taboolib.common.platform.function.warning(*message)
    }

    fun severe(vararg message: Any?){
        taboolib.common.platform.function.severe(*message)
    }

    fun debug(vararg message:Any?){
        cn.ctcraft.ctonlinereward.common.logger.debug(*message)
    }


}

var isDebug = false

@Awake(LifeCycle.LOAD)
fun onLoad(){
    isDebug = Configuration.loadFromFile(File(BukkitPlugin.getInstance().dataFolder,"config.yml")).getBoolean("debug") ?: false
    debug("&c调试模式已开启")
}

inline fun debug(vararg message: Any?) {
    if (isDebug) {
        Bukkit.getConsoleSender().sendMessage("&f[&6CtOnlineReward 调试信息&f] ${message.joinToString(" ")}".colored())
    }
}
