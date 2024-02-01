package cn.ctcraft.ctonlinereward.common.service

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.ctcraft.ctonlinereward.common.database.DatabaseHandler
import cn.ctcraft.ctonlinereward.common.logger.debug
import cn.ctcraft.ctonlinereward.common.pojo.OnlineData
import cn.hutool.core.date.DateUnit
import cn.hutool.core.date.DateUtil
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class SaveService(player:CtOnlineRewardProxyPlayer) {

    companion object{
        val saveCache = ConcurrentHashMap<UUID,CopyOnWriteArrayList<SaveService>>()

        var saveTask: PlatformExecutor.PlatformTask? = null

        @Awake(LifeCycle.ENABLE)
        fun enable(){
            saveTask = submitAsync(period = 20) {
                saveCache.entries.flatMap {
                    it.value
                }.filterNot {
                    it.saved
                }.forEach {
                    if (DateUtil.between(it.cacheTime,Date(),DateUnit.SECOND,false) >= DatabaseHandler.saveTime) {
                        DatabaseHandler.getInnerDatabase()
                            .addData(it.uuid, OnlineData(it.uuid, it.startTime, it.endTime))
                        it.saved = true
                    }
                }
            }
        }

        @Awake(LifeCycle.DISABLE)
        fun save(){
            saveCache.entries.flatMap{
                it.value.filterNot { it.saved }.map {saveService->
                    OnlineData(it.key,saveService.startTime,saveService.endTime)
                }
            }.also {
                DatabaseHandler.getInnerDatabase().addData(it)
            }
            DatabaseHandler.getInnerDatabase().save()
            saveCache.clear()
            saveTask?.cancel()
            saveTask = null
        }


    }

    val uuid = player.uniqueId

    val startTime = player.startTime

    val endTime = Date()

    val cacheTime = Date()

    var saved = false

    init {
        if (DateUtil.between(player.startTime,endTime,DateUnit.MINUTE,false) >=1){
            saveCache.getOrPut(player.uniqueId){ CopyOnWriteArrayList() }.add(this)
            debug("${player.name}玩家数据 已存入存储队列 在线时长 ${DateUtil.between(player.startTime,endTime,DateUnit.MINUTE,false)}")
        }else{
            debug("由于在线时间不足一分钟,则丢弃数据")
        }
    }


}