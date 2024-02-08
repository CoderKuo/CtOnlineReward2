package cn.ctcraft.ctonlinereward.bukkit.cmd

import taboolib.common.platform.command.CommandContext
import taboolib.module.chat.colored

object CommandUtil {


    fun unknow(context: CommandContext<*>, msg: String, drop: Int = 0): String {
        val args = context.args()
        return unkonw(context.name, args.dropLast(drop).toTypedArray(), msg)
    }

    private fun unkonw(name: String, args: Array<String>, msg: String): String {
        args.set(args.lastIndex, "&c&n" + args.last())
        val sb = StringBuilder().apply {
            append("&7/${name} ${args.joinToString(" ")}&r&c&o<---[$msg]")
        }
        if (sb.length > 10) {
            sb.deleteRange(2, 10)
            sb.insert(2, "...")
        }
        return sb.toString().colored()
    }

}