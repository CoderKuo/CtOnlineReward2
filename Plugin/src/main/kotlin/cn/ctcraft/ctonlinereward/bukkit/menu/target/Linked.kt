package cn.ctcraft.ctonlinereward.bukkit.menu.target

enum class Linked(val clazz: Class<out LinkTarget>) {
    basic(Basic::class.java),
    signin(SignIn::class.java);

    companion object{


        @JvmStatic
        fun match(name:String?):Linked{
            return entries.find {
                it.name == name?.lowercase()
            } ?: basic
        }
    }


}