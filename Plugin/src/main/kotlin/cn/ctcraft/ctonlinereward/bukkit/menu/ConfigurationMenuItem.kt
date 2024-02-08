package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.actions.Trigger
import cn.ctcraft.ctonlinereward.common.utils.ConfigUtils
import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection
import java.util.concurrent.CopyOnWriteArrayList

class ConfigurationMenuItem(
    val section: ConfigurationSection, override val originItem: ItemStack,
    override val action: CopyOnWriteArrayList<Trigger>
) : MenuItem {

    companion object {
        @JvmStatic
        fun sectionToActionList(section: ConfigurationSection): CopyOnWriteArrayList<Trigger> {
            return section.let {
                it.getConfigurationSection("action")?.let {
                    val keys = it.getKeys(false)
                    keys.map { key ->
                        Trigger(key, it.getMapList(key))
                    }
                }
            }.let {
                if (it != null) CopyOnWriteArrayList(it) else CopyOnWriteArrayList()
            }
        }

    }

    constructor(section: ConfigurationSection) : this(
        section,
        ConfigUtils.buildItem(section),
        sectionToActionList(section)
    )


    override fun getParam(vararg key: String): Any? {
        key.forEach {
            section.get(it)?.also {
                return it
            }
        }
        return null
    }
}