package cn.ctcraft.ctonlinereward.bukkit.menu.target

import cn.ctcraft.ctonlinereward.bukkit.menu.MenuItem
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

object LinkItemManager {

    val linkItemFunctions = ConcurrentHashMap<String, Function<MenuItem, Unit>>()

    fun registerLinkItem(linkItem: LinkItem) {
        linkItemFunctions.putAll(linkItem.linkMap)
    }


}