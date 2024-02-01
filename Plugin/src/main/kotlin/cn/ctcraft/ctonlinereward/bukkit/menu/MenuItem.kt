package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.bukkit.menu.actions.Action
import cn.ctcraft.ctonlinereward.bukkit.menu.actions.Trigger
import org.bukkit.inventory.ItemStack

class MenuItem(item:ItemStack):ItemStack(item){

    var action:List<Trigger>? = null

}