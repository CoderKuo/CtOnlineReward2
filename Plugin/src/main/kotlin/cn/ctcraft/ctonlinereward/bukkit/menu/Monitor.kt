package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.event.ClickEvent
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.event.SubscribeEvent

object Monitor {

    @SubscribeEvent
    fun click(e: InventoryClickEvent) {
        e.clickedInventory?.also {
            MenuHolder.formInventory(it)?.also {
                it.menu.callEvent(ClickEvent(e))
            }
        }
    }


}