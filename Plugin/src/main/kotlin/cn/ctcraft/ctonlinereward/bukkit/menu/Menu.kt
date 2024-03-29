package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.event.ClickEvent
import cn.ctcraft.ctonlinereward.bukkit.menu.event.CloseEvent
import cn.ctcraft.ctonlinereward.bukkit.menu.event.Event
import cn.ctcraft.ctonlinereward.bukkit.menu.event.OpenEvent
import cn.ctcraft.ctonlinereward.bukkit.menu.target.LinkTarget
import cn.hutool.core.lang.Dict
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Function

interface Menu {

    val type:MenuType

    val params:Dict

    val title:List<String>

    val layout:CopyOnWriteArrayList<List<Char>>

    val items: ConcurrentHashMap<Char, MenuItem>

    val target:LinkTarget

    val slotMap: ConcurrentHashMap<Int, Char>

    var isOpened: Boolean

    fun getSlotChar(slot: Int): Char?

    fun getMenuItem(slot: Int): MenuItem? {
        return items[getSlotChar(slot)]
    }

    fun toBukkitInventory():Inventory

    fun build(inventory: Inventory)

    fun rebuild(): Inventory

    fun closeAll(): List<HumanEntity>

    fun reopen(views: List<HumanEntity>)

    fun callEvent(event: Event)

    fun onClick(priority: Int = 10, event: Function<ClickEvent, Unit>)
    fun onClose(priority: Int = 10, event: Function<CloseEvent, Unit>)
    fun onOpen(priority: Int = 10, event: Function<OpenEvent, Unit>)

}