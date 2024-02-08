package cn.ctcraft.ctonlinereward.common.utils

import cn.hutool.core.lang.Dict
import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XMaterial
import taboolib.module.configuration.ConfigSection
import taboolib.module.configuration.util.getStringColored
import taboolib.module.configuration.util.getStringListColored
import java.security.MessageDigest
import kotlin.experimental.and

object ConfigUtils {

    fun parseCmd(section: ConfigSection): Dict {
        return Dict().apply {
            put("default", section.getStringListColored("default"))
            put("op", section.getStringListColored("op"))
            put("console", section.getStringListColored("console"))
        }
    }

    fun buildItem(section: ConfigurationSection): ItemStack {
        return section.let {
            val name = it.getStringColored("name")
            val lore = it.getStringListColored("lore")
            val material = it.getString("material", "AIR")
            if (material == "AIR") {
                throw RuntimeException("物品材质配置错误或为空")
            }
            val amount = it.getInt("amount", 1)
            val cmd = it.getInt("cmd")
            taboolib.platform.util.buildItem(XMaterial.matchXMaterial(material!!).get()) {
                this.name = name
                this.lore.addAll(lore)
                this.customModelData = cmd
                this.amount = amount
            }
        }
    }

    fun md5(origin: String): String {
        val md = MessageDigest.getInstance("MD5");
        md.update(origin.toByteArray())
        val digest = md.digest()
        val stringBuffer = StringBuffer()
        digest.forEach {
            stringBuffer.append(String.format("%02x", it and 0xff.toByte()))
        }
        return stringBuffer.toString()
    }

}