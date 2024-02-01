package cn.ctcraft.ctonlinereward.onlinetime

import cn.ctcraft.ctonlinereward.common.database.DatabaseHandler
import cn.ctcraft.ctonlinereward.common.logger.debug
import cn.ctcraft.ctonlinereward.common.logger.isDebug
import cn.ctcraft.ctonlinereward.common.manager.PlayerManager
import cn.ctcraft.ctonlinereward.common.pojo.OnlineData
import cn.ctcraft.ctonlinereward.common.service.SaveService
import cn.hutool.core.date.DateUnit
import cn.hutool.core.date.DateUtil
import taboolib.common5.cint
import java.util.*

interface SelectOnlineTime {

    fun setStartTime():Long

    fun setEndTime():Long

    fun getOnlineTime(uuid: UUID):Int{
        val start = Date(setStartTime())
        val end = Date(setEndTime())
        if (isDebug){
            val format = "yyyy-MM-dd HH-mm-ss"
            debug("正在获取玩家数据 请求参数为: startTime{${DateUtil.format(start,format)}} endTime{${DateUtil.format(end,format)}}")
        }
        val cache = mutableListOf<OnlineData>().apply {
            SaveService.saveCache.get(uuid)?.also {
                it.filter {
                    DateUtil.isIn(it.startTime,start,end) && DateUtil.isIn(it.endTime,start,end)
                }.forEach {
                    add(OnlineData(it.uuid,it.startTime,it.endTime))
                }
            }
        }.also {
            debug("从缓存中获取到 ${it.joinToString(";")}")
        }

        val noSave = PlayerManager.getStartTime(uuid)?.let {
            if (DateUtil.isIn(it,start,end)){
                if (DateUtil.isIn(Date(),start,end)){
                    return@let PlayerManager.getPlayerOnlineTimeNow(uuid)
                }else{
                    return@let DateUtil.between(it,end,DateUnit.MINUTE).cint
                }
            }else{
                0
            }
        }.also {
            debug("从未保存数据中读取到 $it")
        }


        val inDatabase = DatabaseHandler.getInnerDatabase().getDataByDateRange(uuid,start,end).also {
            debug("从数据库中获取到 ${it.joinToString(";")}")
        }
        return inDatabase.toMutableList().apply {
            addAll(cache)
        }.sumOf { DateUtil.between(Date(it.getDao().startTime),Date(it.getDao().endTime)
            ,DateUnit.MINUTE).cint } + noSave.cint
    }

}