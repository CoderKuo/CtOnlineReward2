package cn.ctcraft.ctonlinereward.bukkit.listener

import cn.ctcraft.ctonlinereward.common.annotation.AutoClearPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

object PlayerChatWrapper {

    @JvmField
    @AutoClearPlayer
    val callbacks = ConcurrentHashMap<Player, Function<String, String>>()

    fun call(event: Event) {
        (event as AsyncPlayerChatEvent).also {
            callbacks[it.player]?.apply(it.message)?.apply{
                it.message = this
            }
        }
    }

    fun addCallback(player: Player,function: Function<String,String>){
        callbacks[player] = function
    }

}