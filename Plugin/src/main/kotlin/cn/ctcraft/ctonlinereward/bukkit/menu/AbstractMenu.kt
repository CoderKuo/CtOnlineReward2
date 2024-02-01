package cn.ctcraft.ctonlinereward.bukkit.menu

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory

abstract class AbstractMenu:Menu {

    private var inventory:Inventory? = null

    override fun toBukkitInventory():Inventory{
        if (inventory != null) return inventory!!
        return Bukkit.createInventory(MenuHolder(this),type.originType,title).also {
            build(it)
            inventory = it
        }
    }



}