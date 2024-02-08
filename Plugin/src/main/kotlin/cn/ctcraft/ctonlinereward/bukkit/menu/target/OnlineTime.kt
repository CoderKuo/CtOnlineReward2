package cn.ctcraft.ctonlinereward.bukkit.menu.target

import cn.ctcraft.ctonlinereward.bukkit.menu.Menu

class OnlineTime : LinkTarget {


    override fun link(menu: Menu) {

    }

    class rewardItem : AbstractLinkItem() {
        init {
            register(listOf("reward", "item")) {
                val reward = (it.getParam("reward") as? String) ?: throw RuntimeException("没有获取到reward参数")

            }
        }
    }

}