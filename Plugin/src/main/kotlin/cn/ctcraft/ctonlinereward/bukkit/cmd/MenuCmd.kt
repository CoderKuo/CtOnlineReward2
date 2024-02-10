package cn.ctcraft.ctonlinereward.bukkit.cmd

import cn.ctcraft.ctonlinereward.bukkit.menu.SessionStatus
import cn.ctcraft.ctonlinereward.common.manager.MenuManager
import cn.ctcraft.ctonlinereward.common.manager.MenuSessionManager
import cn.ctcraft.ctonlinereward.common.manager.PlayerManager
import cn.hutool.core.date.DateUtil
import cn.hutool.core.io.FileUtil
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.onlinePlayers
import taboolib.common5.cint
import taboolib.expansion.createHelper
import taboolib.module.chat.component
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FilenameFilter

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
                            sender.sendMessage(CommandUtil.unknow(context, "玩家未在线或输入错误", 1))
                        } else {
                            PlayerManager[context["player"]]?.openMenu(MenuManager[argument]!!)
                        }
                    } else {
                        sender.sendMessage(CommandUtil.unknow(context, "菜单不存在"))
                    }
                }
            }
        }
    }

    @CommandBody
    val reload = subCommand {
        dynamic("文件名") {
            suggestUncheck {
                File(getDataFolder(), "menu").listFiles(FilenameFilter { dir, name -> name.endsWith("yml") }).map {
                    FileUtil.mainName(it)
                }
            }
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, argument: String ->
                val file = File(getDataFolder(), "menu/$argument.yml")
                if (!file.exists()) {
                    sender.sendMessage(CommandUtil.unknow(context, "该文件不存在"))
                } else {
                    MenuManager.loadMenu(file)
                }
            }
        }
    }

    @CommandBody
    val session = subCommand {
        createHelper()
        literal("open") {
            dynamic("菜单") {
                suggestUncheck {
                    MenuManager.menu.map { it.key }
                }

                dynamic("player") {
                    suggestUncheck {
                        onlinePlayers().map { it.name }
                    }
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, argument: String ->
                        if (MenuManager.contains(context["菜单"])) {
                            if (PlayerManager[context["player"]] == null) {
                                sender.sendMessage(CommandUtil.unknow(context, "玩家未在线或输入错误"))
                            } else {
                                PlayerManager[context["player"]]?.also {
                                    MenuManager[context["菜单"]]?.let { it1 ->

                                        MenuSessionManager.openSession(
                                            it1, PlayerManager[context["player"]]!!
                                        ).also {
                                            sender.asLangText("cmd-session-open", it.toString()).component()
                                                .buildColored().sendTo(sender)
                                        }

                                    }
                                }
                            }
                        } else {
                            sender.sendMessage(CommandUtil.unknow(context, "菜单不存在", 1))
                        }
                    }
                }

                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, argument: String ->
                    if (MenuManager.contains(context["菜单"])) {
                        MenuManager[context["菜单"]]?.let { it1 ->
                            MenuSessionManager.openSession(
                                it1
                            ).also {
                                sender.asLangText("cmd-session-open", it.toString()).component().buildColored()
                                    .sendTo(sender)
                            }
                        }
                    } else {
                        sender.sendMessage(CommandUtil.unknow(context, "菜单不存在", 1))
                    }
                }


            }
        }

        literal("info") {
            dynamic("会话标识") {
                execute<ProxyCommandSender> { sender, context, argument ->
                    MenuSessionManager[argument]?.also {
                        val players = it.players.map { it.name }
                        val store = it.store.map { "{${it.key}:${it.value}}" }
                        sender.sendLang(
                            "cmd-session-info",
                            argument,
                            players.joinToString(" "),
                            store.joinToString(" ")
                        )
                    }
                }

            }
        }

        literal("list") {
            execute<ProxyCommandSender> { sender, context, argument ->
                sender.sendLang("cmd-session-list", MenuSessionManager.size())
                if (MenuSessionManager.size() >= 10) {
                    sender.sendMessage("由于会话过多，请使用 [/cor menu session list 页码] 进行分页查看")
                    MenuSessionManager.toList().subList(0, 10)
                } else {
                    MenuSessionManager
                }.forEach {
                    sender.sendLang(
                        "cmd-session-list-item", it.value.menu.title.first(), it.key.toString(), it.value.players.size,
                        DateUtil.format(it.value.startTime, "yyyy-MM-dd HH:mm:ss"),
                        sender.asLangText("session-status-${it.value.status.name.lowercase()}")
                    )
                }

            }
            int {
                execute<ProxyCommandSender> { sender, context, argument ->
                    sender.sendLang("cmd-session.list", MenuSessionManager.size())
                    MenuSessionManager.toList().subList(argument.cint - 1 * 10, argument.cint * 10).forEach {
                        sender.sendLang(
                            "cmd-session-list-item",
                            it.value.menu.title.first(),
                            it.key.toString(),
                            it.value.players.size,
                            DateUtil.format(it.value.startTime, "yyyy-MM-dd HH:mm:ss"),
                            sender.asLangText("session-status-${it.value.status.name.lowercase()}")
                        )
                    }
                }
            }


        }



        literal("status") {
            dynamic("会话标识") {
                suggestUncheck {
                    MenuSessionManager.map { it.key.toString() }
                }
                dynamic("状态") {
                    suggest {
                        listOf("created", "active", "closed", "0", "1", "2")
                    }
                    execute<ProxyCommandSender> { sender, context, argument ->
                        MenuSessionManager[context["会话标识"]]?.apply {
                            when (argument) {
                                "created", "0" -> SessionStatus.created
                                "active", "1" -> SessionStatus.active
                                "closed", "2" -> SessionStatus.closed
                                else -> {
                                    status
                                }
                            }.also {
                                status = it
                            }
                            sender.sendLang(
                                "cmd-session-status",
                                sender.asLangText("session-status-${status.name.lowercase()}")
                            )
                        }
                    }

                }
            }

        }

        literal("player") {
            dynamic("会话标识") {
                suggestUncheck {
                    MenuSessionManager.map { it.key.toString() }
                }
                literal("add") {
                    dynamic("玩家名") {
                        suggestUncheck {
                            onlinePlayers().map { it.name }
                        }
                        execute<ProxyCommandSender> { sender, context, argument ->
                            MenuSessionManager[context["会话标识"]]?.apply {
                                val player = PlayerManager[argument]
                                    ?: run { sender.sendMessage("该玩家不在线或输入错误");return@apply }
                                if (status == SessionStatus.closed) {
                                    sender.sendLang("cmd-session-player-closed")
                                    return@apply
                                }
                                if (add(player)) {
                                    sender.sendMessage("已经将 ${argument} 加入会话")
                                } else {
                                    sender.sendMessage("该玩家不在此会话中")
                                }
                            }
                        }
                    }
                }
                literal("remove") {
                    dynamic("玩家名") {
                        suggestUncheck {
                            onlinePlayers().map { it.name }
                        }
                        execute<ProxyCommandSender> { sender, context, argument ->
                            MenuSessionManager[context["会话标识"]]?.apply {
                                if (status == SessionStatus.closed) {
                                    sender.sendLang("cmd-session-player-closed")
                                    return@apply
                                }
                                if (remove(
                                        PlayerManager[argument]
                                            ?: run { sender.sendMessage("该玩家不在线或输入错误");return@apply })
                                ) {
                                    sender.sendMessage("已经将 ${argument} 移出会话")
                                } else {
                                    sender.sendMessage("该玩家不在此会话中")
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}