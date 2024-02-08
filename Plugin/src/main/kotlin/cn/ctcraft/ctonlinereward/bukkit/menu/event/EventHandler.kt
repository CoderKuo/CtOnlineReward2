package cn.ctcraft.ctonlinereward.bukkit.menu.event

import java.util.function.Function

class EventHandler<T : Event>(val priority: Int, val function: Function<T, Unit>) : Function<T, Unit> {


    override fun apply(t: T) {
        function.apply(t)
    }


}