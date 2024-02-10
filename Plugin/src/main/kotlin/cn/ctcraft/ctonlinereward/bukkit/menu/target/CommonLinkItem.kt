package cn.ctcraft.ctonlinereward.bukkit.menu.target

import cn.ctcraft.ctonlinereward.bukkit.menu.actions.FunctionAction
import cn.ctcraft.ctonlinereward.bukkit.menu.actions.Trigger
import cn.ctcraft.ctonlinereward.bukkit.menu.actions.TriggerType
import cn.ctcraft.ctonlinereward.common.utils.ConfigUtils
import org.bukkit.Bukkit
import taboolib.module.configuration.ConfigSection

object CommonLinkItem : AbstractLinkItem() {

    init {
        register(arrayListOf("cmd", "command")) {
            (it.getParam("cmd", "command") as? ConfigSection)?.let { section ->
                val parseCmd = ConfigUtils.parseCmd(section)
                it.action.add(Trigger.create(TriggerType.click, listOf(
                    FunctionAction().addFunc { (player, param) ->
                        (parseCmd.get("default") as List<String>).also {
                            player?.cmd(it)
                        }
                        (parseCmd.get("op") as List<String>).also {
                            player?.sudoCmd(it)
                        }
                        (parseCmd.get("console") as List<String>).forEach {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it)
                        }
                    }
                )))
            }
        }
    }


}