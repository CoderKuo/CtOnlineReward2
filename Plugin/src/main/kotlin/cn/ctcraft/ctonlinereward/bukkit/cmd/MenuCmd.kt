package cn.ctcraft.ctonlinereward.bukkit.cmd

import cn.ctcraft.ctonlinereward.common.manager.MenuManager
import cn.ctcraft.ctonlinereward.common.manager.PlayerManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.platform.function.onlinePlayers
import taboolib.expansion.createHelper

object MenuCmd {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val open = subCommand {
        dynamic("player") {
            suggestUncheck {
                onlinePlayers().map { it.name }
            }
            dynamic("菜单") {
                suggestUncheck {
                    MenuManager.menu.map { it.key }
                }

                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, argument: String ->
                    if (MenuManager.contains(argument)) {
                        if (PlayerManager[context["player"]] == null) {
                            sender.sendMessage("${CommandUtil.unknow(context, "玩家未在线或输入错误", 1)}")
                        } else {
                            PlayerManager[context["player"]]?.openMenu(MenuManager[argument]!!)
                        }
                    } else {
                        sender.sendMessage("${CommandUtil.unknow(context, "菜单不存在")}")
                    }
                }
            }
        }
    }

}