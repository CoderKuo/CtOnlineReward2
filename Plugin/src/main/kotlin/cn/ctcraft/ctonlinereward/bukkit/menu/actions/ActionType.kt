package cn.ctcraft.ctonlinereward.bukkit.menu.actions

enum class ActionType(val alias:Array<String>,val clazz: Class<out Action>) {
    js(arrayOf("javascript"),JavascriptAction::class.java),
    kether(arrayOf("ke"),KetherAction::class.java);

    companion object{


        @JvmStatic
        fun match(str:String?):ActionType{
            return entries.find {
                it.name == str?.lowercase() || it.alias.contains(str?.lowercase())
            } ?: js
        }
    }

}