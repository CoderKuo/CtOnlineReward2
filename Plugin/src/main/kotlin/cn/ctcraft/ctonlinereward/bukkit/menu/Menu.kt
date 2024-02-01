package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.target.LinkTarget
import cn.hutool.core.lang.Dict
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

interface Menu {

    val type:MenuType

    val params:Dict

    val title:List<String>

    val layout:CopyOnWriteArrayList<List<Char>>

    val items:ConcurrentHashMap<Char,ItemStack>

    val target:LinkTarget

    fun toBukkitInventory():Inventory

    fun build(inventory: Inventory)

}