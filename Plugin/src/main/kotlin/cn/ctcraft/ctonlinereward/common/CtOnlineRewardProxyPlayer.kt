package cn.ctcraft.ctonlinereward.common

import cn.ctcraft.ctonlinereward.bukkit.menu.Menu
import cn.hutool.core.date.DateUnit
import cn.hutool.core.date.DateUtil
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.cint
import taboolib.expansion.fakeOp
import taboolib.platform.type.BukkitPlayer
import java.util.*

class CtOnlineRewardProxyPlayer(val player: ProxyPlayer):ProxyPlayer by (player) {

    constructor(player:Player):this(adaptPlayer(player))

    constructor(player: BukkitPlayer):this(adaptPlayer(player))

    val bukkitPlayer = (player as? BukkitPlayer)?.player

    val startTime = Date()

    /**
     * 获取本次在线时间 单位: 分钟
     */
    fun getOnlineTimeNow():Int{
        return DateUtil.between(startTime,Date(),DateUnit.MINUTE).cint
    }

    fun sudoCmd(cmd: List<String>) {
        (player as BukkitPlayer).player.fakeOp().also { player ->
            cmd.forEach {
                player.performCommand(it)
            }
        }
    }

    fun sudoCmd(cmd: String) {
        sudoCmd(listOf(cmd))
    }

    fun cmd(cmd:List<String>){
        cmd.forEach {
            cmd(it)
        }
    }

    fun cmd(cmd:String){
       performCommand(cmd)
    }

    fun closeMenu() {
        bukkitPlayer?.closeInventory()
    }

    fun openMenu(menu: Menu){
        bukkitPlayer?.openInventory(menu.toBukkitInventory())
    }

}