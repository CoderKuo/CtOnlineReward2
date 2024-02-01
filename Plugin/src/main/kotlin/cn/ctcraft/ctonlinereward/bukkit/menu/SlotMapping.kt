package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.common.logger.Logging
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

abstract class SlotMapping {

    abstract val mapping:LinkedHashMap<Int, IntRange>

    fun get(list:List<List<Char>>):ConcurrentHashMap<Int,Char>{
        if (list.size > mapping.size){
            throw RuntimeException("行越界")
        }
        val map = ConcurrentHashMap<Int,Char>()
        list.forEachIndexed { index, chars ->
            chars.forEachIndexed { indexc, c ->
                if (indexc >= (mapping[index]?.toList()?.size ?: (indexc + 1))){
                    throw RuntimeException("列越界")
                }
                mapping[index]?.toList()?.get(indexc)?.also {
                    map[it] = c
                }
            }
        }
        return map
    }

    object ChestMapping: SlotMapping() {
        override val mapping: LinkedHashMap<Int, IntRange> = linkedMapOf(
            0 to 0..8,
            1 to 1*9..<2*9,
            2 to 2*9..<3*9,
            3 to 3*9..<4*9,
            4 to 4*9..<5*9,
            5 to 5*9..<6*9,
        )

    }

    object CraftMapping:SlotMapping(){
        override val mapping: LinkedHashMap<Int, IntRange> = linkedMapOf(
            0 to 1..3,
            1 to 4..6,
            2 to 7..9,
            3 to 0..0
        )
    }

    object FurnaceMapping:SlotMapping(){
        override val mapping: LinkedHashMap<Int, IntRange> = linkedMapOf(
            0 to 0..0,
            1 to 1..1,
            2 to 2..2
        )
    }

    object Hopper:SlotMapping(){
        override val mapping: LinkedHashMap<Int, IntRange> = linkedMapOf(
            0 to 0..4
        )

    }


}