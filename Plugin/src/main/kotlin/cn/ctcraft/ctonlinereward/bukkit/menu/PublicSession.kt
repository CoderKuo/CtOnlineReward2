package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import java.util.concurrent.CopyOnWriteArrayList

class PublicSession(override val menu: Menu) : AbstractSession() {

    override val players = CopyOnWriteArrayList<CtOnlineRewardProxyPlayer>()

    init {
        menu.onClose {
            players.remove(it.player)
        }
        status = SessionStatus.active
    }

    override fun add(player: CtOnlineRewardProxyPlayer): Boolean {
        if (players.contains(player)) return false
        player.openMenu(menu)
        return players.add(player)
    }


    override fun remove(player: CtOnlineRewardProxyPlayer): Boolean {
        return players.remove(player).also {
            if (it) {
                player.closeMenu()
            }
        }
    }
}