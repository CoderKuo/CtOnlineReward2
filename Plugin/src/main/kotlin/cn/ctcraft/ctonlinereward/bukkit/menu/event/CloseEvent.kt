package cn.ctcraft.ctonlinereward.bukkit.menu.event

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.ctcraft.ctonlinereward.common.manager.PlayerManager
import org.bukkit.event.inventory.InventoryCloseEvent

class CloseEvent(override val origin: InventoryCloseEvent) : Event {

    val player: CtOnlineRewardProxyPlayer? = PlayerManager.getPlayer(origin.player.uniqueId)


}