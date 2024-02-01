package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer

class PrivateSession(override val menu: Menu,val player:CtOnlineRewardProxyPlayer):Session {

    init {
        menu.target.link(menu)
        player.openMenu(menu)
    }

}