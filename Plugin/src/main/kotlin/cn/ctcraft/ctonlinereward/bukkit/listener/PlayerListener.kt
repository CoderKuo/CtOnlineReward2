package cn.ctcraft.ctonlinereward.bukkit.listener

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.ctcraft.ctonlinereward.common.annotation.AutoClearPlayerTask
import cn.ctcraft.ctonlinereward.common.manager.PlayerManager
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent

object PlayerListener {

    @SubscribeEvent
    fun joinMonitor(e:PlayerJoinEvent){
        val player = CtOnlineRewardProxyPlayer(e.player)
        PlayerManager.addPlayerData(player.uniqueId,player)
    }

    @SubscribeEvent
    fun quit(e:PlayerQuitEvent){
        PlayerManager.stopTiming(e.player.uniqueId)

        AutoClearPlayerTask.remove(e.player)
        AutoClearPlayerTask.remove(e.player.uniqueId)
        AutoClearPlayerTask.remove(e.player.name)
    }

    @SubscribeEvent
    fun chat(e:AsyncPlayerChatEvent){
        PlayerChatWrapper.call(e)
    }


}