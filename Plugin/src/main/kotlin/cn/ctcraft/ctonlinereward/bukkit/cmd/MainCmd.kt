package cn.ctcraft.ctonlinereward.bukkit.cmd

import cn.ctcraft.ctonlinereward.bukkit.listener.PlayerChatWrapper
import cn.ctcraft.ctonlinereward.common.script.ScriptManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.severe
import taboolib.platform.type.BukkitPlayer
import java.io.File


@CommandHeader("ctonlinereward",["cor","cto","ctor"])
object MainCmd {

    @CommandBody
    val script = subCommand {
        literal("run"){
            dynamic("文件名") {
                suggest {
                    ScriptManager.scripts.map {
                        it.key
                    }
                }
                execute<ProxyCommandSender>{ sender, context, argument ->
                   ScriptManager.runScriptFile(argument)
                }

                dynamic("函数名") {
                    execute<ProxyCommandSender>{ sender, context, argument ->
                       ScriptManager.runScriptFile(context["文件名"],argument)
                    }
                }
            }
        }
        literal("reload"){
            dynamic {
                suggest {
                    File(getDataFolder(),"script").listFiles().map { it.name }
                }
                execute<ProxyCommandSender>{ sender, context, argument ->
                    ScriptManager.loadScript(File(getDataFolder(),"script/$argument"))
                }

            }
        }
    }

    @CommandBody
    val test = subCommand {
        execute<ProxyPlayer>{ sender, context, argument ->
           PlayerChatWrapper.addCallback((sender as BukkitPlayer).player){
               it
           }
        }
    }

}