package cn.ctcraft.ctonlinereward.bukkit.compat

import cn.ctcraft.ctonlinereward.common.manager.PlayerManager
import cn.ctcraft.ctonlinereward.onlinetime.CommonOnlineTime
import cn.ctcraft.ctonlinereward.onlinetime.SelectOnlineTime
import org.bukkit.OfflinePlayer
import taboolib.platform.compat.PlaceholderExpansion

object PlaceholderCompat:PlaceholderExpansion {
    override val identifier: String
        get() = "CtOnlineReward"


    override fun onPlaceholderRequest(player: OfflinePlayer?, args: String): String {
        val uuid = player?.uniqueId ?: return "错误"
        if (args == "today"){
            return PlayerManager.getPlayerOnlineTime(uuid,CommonOnlineTime.day).toString()
        }else if(args == "week"){
            return PlayerManager.getPlayerOnlineTime(uuid,CommonOnlineTime.week).toString()
        }

        return "error"
    }
}