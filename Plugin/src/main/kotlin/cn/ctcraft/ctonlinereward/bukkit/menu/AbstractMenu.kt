package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.event.*
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Function

abstract class AbstractMenu:Menu {

    private var inventory:Inventory? = null

    override var isOpened: Boolean = false

    private val eventHandlers: ConcurrentHashMap<MenuEvent, CopyOnWriteArrayList<EventHandler<Event>>> =
        ConcurrentHashMap()

    override fun toBukkitInventory():Inventory{
        if (inventory != null) return inventory!!
        return rebuild()
    }

    override fun rebuild(): Inventory {
        return Bukkit.createInventory(MenuHolder(this),type.originType,title.getOrNull(0) ?: "Chest").also {
            build(it)
            inventory = it
            isOpened = true
        }
    }

    override fun closeAll(): List<HumanEntity> {
        val views: MutableList<HumanEntity> = mutableListOf()
        inventory?.also {
            views.addAll(it.viewers)
        }
        views.forEach {
            it.closeInventory()
        }
        return views
    }

    override fun reopen(views: List<HumanEntity>) {
        views.forEach {
            inventory?.let { it1 -> it.openInventory(it1) }
        }
    }

    override fun getSlotChar(slot: Int): Char? {
        return slotMap[slot]
    }

    override fun callEvent(event: Event) {
        eventHandlers.get(MenuEvent.match(event))?.sortedBy {
            it.priority
        }?.forEach {
            it.apply(event)
        }
    }

    override fun onClick(priority: Int, event: Function<ClickEvent, Unit>) {
        eventHandlers.getOrPut(MenuEvent.Click) { CopyOnWriteArrayList() }
            .add(EventHandler(priority, event) as EventHandler<Event>)
    }

    override fun onClose(priority: Int, event: Function<CloseEvent, Unit>) {
        eventHandlers.getOrPut(MenuEvent.Close) { CopyOnWriteArrayList() }
            .add(EventHandler(priority, event) as EventHandler<Event>)
    }

    override fun onOpen(priority: Int, event: Function<OpenEvent, Unit>) {
        eventHandlers.getOrPut(MenuEvent.Open) { CopyOnWriteArrayList() }
            .add(EventHandler(priority, event) as EventHandler<Event>)
    }


}