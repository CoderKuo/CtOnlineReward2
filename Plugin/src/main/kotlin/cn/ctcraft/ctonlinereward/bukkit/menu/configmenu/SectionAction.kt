package cn.ctcraft.ctonlinereward.bukkit.menu.configmenu

import cn.ctcraft.ctonlinereward.bukkit.menu.actions.ActionUtil
import cn.ctcraft.ctonlinereward.bukkit.menu.actions.JavascriptFunctionInterface
import cn.hutool.core.lang.Dict


class SectionAction(val id: String, listMap: List<Map<*, *>>) : JavascriptFunctionInterface {

    val actions = ActionUtil.buildAction(listMap)

    val defaultArguments = Dict()

    override fun apply(args: Map<String, Any>): Any? {
        var result: Any? = null
        actions.forEach {
            result = it.call(null, args.toMutableMap().apply { putAll(defaultArguments) })
        }
        return result
    }

    override fun apply(vararg args: Any): Any? {
        return apply(mapOf("args" to args))
    }

    override fun apply(): Any? {
        return apply(mapOf())
    }

}

