package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.event.ClickEvent
import cn.ctcraft.ctonlinereward.bukkit.menu.event.CloseEvent
import cn.ctcraft.ctonlinereward.bukkit.menu.event.OpenEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import taboolib.common.platform.event.SubscribeEvent

object Monitor {

    @SubscribeEvent
    fun open(e: InventoryOpenEvent) {
        e.inventory.also {
            MenuHolder.formInventory(it)?.also {
                it.menu.callEvent(OpenEvent(e))
            }
        }
    }

    @SubscribeEvent
    fun click(e: InventoryClickEvent) {
        e.clickedInventory?.also {
            MenuHolder.formInventory(it)?.also {
                it.menu.callEvent(ClickEvent(e))
            }
        }
    }

    @SubscribeEvent
    fun close(e: InventoryCloseEvent) {
        e.inventory.also {
            MenuHolder.formInventory(it)?.also {
                it.menu.callEvent(CloseEvent(e))
            }
        }
    }


}