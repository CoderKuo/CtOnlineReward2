package cn.ctcraft.ctonlinereward.bukkit.menu.target

import cn.ctcraft.ctonlinereward.bukkit.menu.MenuItem
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

interface LinkItem {

    val linkMap: ConcurrentHashMap<String, Function<MenuItem, Unit>>

}