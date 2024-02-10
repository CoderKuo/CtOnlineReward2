package cn.ctcraft.ctonlinereward.bukkit.menu.actions

@FunctionalInterface
interface JavascriptFunctionInterface {

    fun apply(args: Map<String, Any>): Any?

    fun apply(vararg args: Any): Any?


    fun apply(): Any?

}