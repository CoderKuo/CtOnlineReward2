package cn.ctcraft.ctonlinereward.bukkit.menu

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class MenuHolder(val menu: Menu):InventoryHolder {

    companion object {

        fun formInventory(inventory: Inventory): MenuHolder? {
            val holder = inventory.holder
            if (holder is MenuHolder) {
                return holder
            }
            return null
        }

    }

    override fun getInventory(): Inventory {
        return menu.toBukkitInventory()
    }

}