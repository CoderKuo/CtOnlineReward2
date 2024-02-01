package cn.ctcraft.ctonlinereward.bukkit.menu

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class MenuHolder(val menu: Menu):InventoryHolder {


    private val inventory:Inventory = menu.toBukkitInventory()

    override fun getInventory(): Inventory {
        return inventory
    }

}