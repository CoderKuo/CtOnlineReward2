package cn.ctcraft.ctonlinereward.bukkit.menu.event

import cn.ctcraft.ctonlinereward.bukkit.menu.MenuHolder
import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.ctcraft.ctonlinereward.common.manager.PlayerManager
import org.bukkit.event.inventory.InventoryClickEvent

class ClickEvent(override val origin: InventoryClickEvent) : Event {

    val holder = (origin.clickedInventory!!.holder as MenuHolder)

    val slotChar: Char? = holder.menu.getSlotChar(origin.slot)

    val currentItem = origin.currentItem

    val clickType = origin.click

    val whoClicked = origin.whoClicked

    val player: CtOnlineRewardProxyPlayer? = PlayerManager.getPlayer(whoClicked.uniqueId)

    fun setCancelled(boolean: Boolean) {
        origin.setCancelled(boolean)
    }

    fun isCancelled(): Boolean {
        return origin.isCancelled
    }

}