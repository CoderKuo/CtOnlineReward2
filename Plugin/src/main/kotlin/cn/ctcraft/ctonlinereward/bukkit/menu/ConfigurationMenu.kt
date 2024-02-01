package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.actions.Trigger
import cn.ctcraft.ctonlinereward.bukkit.menu.actions.TriggerType
import cn.ctcraft.ctonlinereward.bukkit.menu.target.LinkTarget
import cn.ctcraft.ctonlinereward.bukkit.menu.target.Linked
import cn.ctcraft.ctonlinereward.common.logger.Logging
import cn.hutool.core.lang.Dict
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.module.configuration.ConfigSection
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.*
import taboolib.platform.util.buildItem
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class ConfigurationMenu(val id:String,config:Configuration): AbstractMenu() {

    override val type: MenuType = config.getEnum("type",MenuType::class.java) ?: MenuType.chest

    override val params: Dict = Dict(config.getMap("param"))

    override val title: String = config.getStringColored("title") ?: "Chest"

    override val layout: CopyOnWriteArrayList<List<Char>> = config.getStringList("layout").map {
        it.toCharArray().toList()
    }.let {
        CopyOnWriteArrayList(it)
    }

    override val items: ConcurrentHashMap<Char, ItemStack> = config.get("items")?.let {
        buildLayoutItems(it as ConfigSection)
    } ?: ConcurrentHashMap()

    override val target:LinkTarget = Linked.match(config.getString("link")).clazz.newInstance()

    private fun buildLayoutItems(section:ConfigSection):ConcurrentHashMap<Char,ItemStack>{
        section.mapSection {
            val name = it.getStringColored("name")
            val lore = it.getStringListColored("lore")
            val material = it.getString("material","AIR")
            val cmd = it.getInt("cmd")
            val action = it.getConfigurationSection("action")?.let {
                val keys = it.getKeys(false)
                keys.map { key->
                    Trigger(key,it.getMapList(key))
                }
            }
            buildItem(XMaterial.matchXMaterial(material!!).get()){
                this.name = name
                this.lore.addAll(lore)
                this.customModelData = cmd
            }.also {
                MenuItem(it).apply {
                    this.action = action
                }
            }
        }.also {
            return ConcurrentHashMap(it.mapKeys { it.key[0] })
        }
    }

    override fun build(inventory: Inventory){
        kotlin.runCatching {
            val slotMap = type.slotMapping.get(layout)
            slotMap.forEach {
                if (it.value != ' ' && items.containsKey(it.value)){
                    inventory.setItem(it.key,items[it.value])
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