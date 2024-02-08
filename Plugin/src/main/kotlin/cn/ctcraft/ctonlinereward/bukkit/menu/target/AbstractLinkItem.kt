package cn.ctcraft.ctonlinereward.bukkit.menu.target

import cn.ctcraft.ctonlinereward.bukkit.menu.MenuItem
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

abstract class AbstractLinkItem : LinkItem {

    override val linkMap: ConcurrentHashMap<String, Function<MenuItem, Unit>> = ConcurrentHashMap()


    fun register(key: String, func: Function<MenuItem, Unit>) {
        linkMap.put(key, func)
    }

    fun register(keys: List<String>, func: Function<MenuItem, Unit>) {
        keys.forEach {
            register(it, func)
        }
    }
}