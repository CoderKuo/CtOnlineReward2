package cn.ctcraft.ctonlinereward.bukkit.menu

import org.bukkit.event.inventory.InventoryType

enum class MenuType(val originType:InventoryType,val slotMapping: SlotMapping) {

    chest(InventoryType.CHEST,SlotMapping.ChestMapping),
    hopper(InventoryType.HOPPER,SlotMapping.Hopper)


}