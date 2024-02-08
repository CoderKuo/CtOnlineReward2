package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.actions.Trigger
import org.bukkit.inventory.ItemStack
import java.util.concurrent.CopyOnWriteArrayList

interface MenuItem {

    val originItem: ItemStack

    val action: CopyOnWriteArrayList<Trigger>


    fun getParam(vararg key: String): Any?
}