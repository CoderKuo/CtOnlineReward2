package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.hutool.core.lang.Dict
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

interface Session {

    val store: Dict

    val startTime: Date

    val menu:Menu

    var status: SessionStatus

    val players: CopyOnWriteArrayList<CtOnlineRewardProxyPlayer>

    fun add(player: CtOnlineRewardProxyPlayer): Boolean

    fun remove(player: CtOnlineRewardProxyPlayer): Boolean

}