package cn.ctcraft.ctonlinereward.bukkit.menu.actions

object ActionUtil {

    fun buildAction(list: List<Map<*, *>>): List<Action<*>> {
        return list.map {
            val type = it.get("type")
            var call = it.get("call") ?: it.get("script") ?: it.get("value") ?: it.get("val")
            if (call == null) {
                call = it.entries.toList().let {
                    it.get(it.indexOfFirst { it.key != "type" })
                }
            }
            if (call is List<*>) {
                ActionType.match(type as? String).clazz.getConstructor(List::class.java).newInstance(
                    call
                )

            } else {
                ActionType.match(type as? String).clazz.getConstructor(String::class.java)
                    .newInstance(call.toString())
            }
        }
    }

}