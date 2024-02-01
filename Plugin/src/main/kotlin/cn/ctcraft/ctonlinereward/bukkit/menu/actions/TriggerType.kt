package cn.ctcraft.ctonlinereward.bukkit.menu.actions

enum class TriggerType {

    click,
    double_click,
    right_click,
    right_double_click,
    shift_click,
    drop;

    companion object{
        @JvmStatic
        fun match(type:String):TriggerType{
            return entries.toTypedArray().find {
                it.name == type.lowercase()
            } ?: click
        }
    }

}