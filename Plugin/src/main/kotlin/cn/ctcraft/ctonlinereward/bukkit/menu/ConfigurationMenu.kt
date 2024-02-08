package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.actions.TriggerType
import cn.ctcraft.ctonlinereward.bukkit.menu.target.LinkTarget
import cn.ctcraft.ctonlinereward.bukkit.menu.target.Linked
import cn.ctcraft.ctonlinereward.common.logger.Logging
import cn.hutool.core.lang.Dict
import org.bukkit.inventory.Inventory
import taboolib.module.configuration.ConfigSection
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.getMap
import taboolib.module.configuration.util.getStringColored
import taboolib.module.configuration.util.getStringListColored
import taboolib.module.configuration.util.mapSection
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class ConfigurationMenu(val id:String,config:Configuration): AbstractMenu() {

    override val type: MenuType = config.getEnum("type",MenuType::class.java) ?: MenuType.chest

    override val params: Dict = Dict(config.getMap("param"))

    override val title: List<String> = config.getStringListColored("title").let {
        if (it.size == 0) {
            listOf(config.getStringColored("title") ?: "Chest")
        } else {
            it
        }
    }

    override val layout: CopyOnWriteArrayList<List<Char>> = config.getStringList("layout").map {
        it.toCharArray().toList()
    }.let {
        CopyOnWriteArrayList(it)
    }


    override val items: ConcurrentHashMap<Char, MenuItem> = config.get("items")?.let {
        buildLayoutItems(it as ConfigSection)
    } ?: ConcurrentHashMap()

    override val slotMap = type.slotMapping.get(layout)


    override val target:LinkTarget = Linked.match(config.getString("link")).clazz.newInstance()


    private fun buildLayoutItems(section: ConfigSection): ConcurrentHashMap<Char, MenuItem> {
        section.mapSection { section ->
            kotlin.runCatching {
                ConfigurationMenuItem(section)
            }.onFailure {
                when (it.message) {
                    "物品材质配置错误或为空" -> {
                        Logging.severe("解析 $id 菜单文件时,因为 ${section.name} 物品配置的材质配置错误或为空")
                    }

                    else -> it.printStackTrace()
                }
            }.getOrNull()
        }.also {

            return ConcurrentHashMap<Char, MenuItem>().apply {
                it.entries.forEach {
                    if (it.value != null) {
                        val item = it.value!!
                        val slotChar = it.key[0]
                        put(slotChar, item)
                    }
                }

                onClick { event ->
                    if (null != event.slotChar && containsKey(event.slotChar)) {
                        get(event.slotChar)?.also { item ->
                            val type = event.clickType
                            event.player?.also { player ->

                                val map = mapOf(
                                    "player" to player,
                                    "event" to event
                                )

                                item.action.filter {
                                    it.type.origin == type || it.type == TriggerType.all
                                }.forEach {

                                    it.call(player, map)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun build(inventory: Inventory){
        kotlin.runCatching {
            slotMap.forEach {
                if (it.value != ' ' && items.containsKey(it.value)){
                    inventory.setItem(it.key, items[it.value]!!.originItem)
                }
            }
        }.onFailure {
            when (it.message) {
                "行越界" -> {
                    Logging.severe("解析 $id 菜单文件时,因为布局错误出现行越界异常  请参考文档进行修改 不同的type会有不同的布局")
                }
                "列越界" -> {
                    Logging.severe("解析 $id 菜单文件时,因为布局错误出现列越界异常  请参考文档进行修改 不同的type会有不同的布局")
                }
                else -> {
                    it.printStackTrace()
                }
            }
        }


    }

}