package cn.ctcraft.ctonlinereward.common.annotation

import cn.ctcraft.ctonlinereward.bukkit.listener.PlayerChatWrapper
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.io.getInstance
import taboolib.common.io.runningClasses
import taboolib.common.io.runningClassesWithoutLibrary
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule
import taboolib.common.platform.function.getProxyPlayer
import taboolib.common.platform.function.isListened
import taboolib.common.reflect.Reflex
import taboolib.common5.cint
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object AutoClearPlayerTask {

    val fields = ConcurrentHashMap<Any,CopyOnWriteArrayList<Field>>()

    @Awake(LifeCycle.ACTIVE)
    fun load(){
        fields.clear()
        runningClassesWithoutLibrary.forEach {
            kotlin.runCatching {
                it.getInstance()?.get()?.also {
                    it::class.java.declaredFields.forEach { field->
                        field.getAnnotationsByType(AutoClearPlayer::class.java).firstOrNull()?.also {
                            fields.getOrPut(it) { CopyOnWriteArrayList() }.addIfAbsent(field)
                        }
                    }
                }
            }
        }

    }

    @Schedule(false,0,20)
    fun task(){
        fields.forEach { (obj,fieldList)->
            fieldList.forEach{
                val field = it.get(obj)
                when{
                    (field is MutableMap<*,*>)->{
                        with(field.iterator()){
                            while (hasNext()){
                                val next = next()
                                if (!checkOnline(next.key)){
                                    remove()
                                }
                            }
                        }
                    }
                    (field is MutableList<*>)->{
                        with(field.iterator()) {
                            while (hasNext()){
                                val next= next()
                                if (!checkOnline(next)){
                                    remove()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun remove(any: Any){
        fields.forEach {(obj,fieldList)->
            fieldList.forEach {
                val field = it.get(obj)
                remove(any,field)
            }
        }
    }

    fun remove(any:Any,container:Any){
        when{
            (container is MutableMap<*,*>)->{
                container.remove(any)
            }
            (container is MutableList<*>)->{
                container.removeAt(any.cint)
            }
        }
    }


    private fun checkOnline(uuidOrPlayer:Any?):Boolean{
        if (uuidOrPlayer == null) return false

        return when{
            (uuidOrPlayer is UUID)-> getProxyPlayer(uuidOrPlayer as java.util.UUID)?.isOnline() == true
            (uuidOrPlayer is Player)-> uuidOrPlayer.isOnline
            // 玩家名 不建议用
            (uuidOrPlayer is String)-> getProxyPlayer(uuidOrPlayer)?.isOnline() == true
            else->{
                true
            }
        }
    }


}