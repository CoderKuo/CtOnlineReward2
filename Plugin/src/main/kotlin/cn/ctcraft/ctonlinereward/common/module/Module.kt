package cn.ctcraft.ctonlinereward.common.module

import cn.ctcraft.ctonlinereward.CtOnlineReward
import cn.ctcraft.ctonlinereward.common.logger.Logging
import taboolib.platform.BukkitPlugin

abstract class Module {


    val plugin = CtOnlineReward

    val logging = Logging

    abstract val description:ModuleDescription

    fun load(){
        Logging.info("${description.name} 模块加载中...")
        onLoad()
    }

    fun enable(){
        Logging.info("模块名: ${description.name} 作者: ${description.author} 模块已加载")
        onEnable()
    }

    fun disable(){
        Logging.info("模块名: ${description.name} 作者: ${description.author} 模块已卸载")
        onDisable()
    }


    abstract fun onLoad()

    abstract fun onEnable()

    abstract fun onDisable()

}

data class ModuleDescription(val name:String,val author:String,val version:String,val description: List<String> = listOf()){

    constructor(name:String,author:String,version:String):this(name, author, version, listOf())

}