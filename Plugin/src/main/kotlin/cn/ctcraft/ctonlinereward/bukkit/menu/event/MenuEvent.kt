package cn.ctcraft.ctonlinereward.bukkit.menu.event

enum class MenuEvent(val originClass: Class<out Event>) {


    Click(ClickEvent::class.java),
    Close(CloseEvent::class.java),
    Open(OpenEvent::class.java);

    companion object {
        @JvmStatic
        fun match(event: Event): MenuEvent? {
            values().forEach {
                if (event::class.java == it.originClass) {
                    return it
                }
            }
            return null
        }
    }
}