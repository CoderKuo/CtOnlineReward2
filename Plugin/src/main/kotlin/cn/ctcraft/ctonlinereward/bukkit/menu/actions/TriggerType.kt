package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import org.bukkit.event.inventory.ClickType

enum class TriggerType(val origin: ClickType) {

    click(ClickType.LEFT),
    double_click(ClickType.DOUBLE_CLICK),
    right_click(ClickType.RIGHT),
    shift_click(ClickType.SHIFT_LEFT),
    shift_right_click(ClickType.SHIFT_RIGHT),
    drop(ClickType.DROP),
    all(ClickType.UNKNOWN);

    companion object{
        @JvmStatic
        fun match(type:String):TriggerType{
            return entries.toTypedArray().find {
                it.name == type.lowercase()
            } ?: click
        }
    }

}