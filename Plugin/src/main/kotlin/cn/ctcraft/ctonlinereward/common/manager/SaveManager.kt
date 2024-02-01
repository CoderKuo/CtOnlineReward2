package cn.ctcraft.ctonlinereward.common.manager

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.ctcraft.ctonlinereward.common.logger.debug
import cn.ctcraft.ctonlinereward.common.service.SaveService
import java.util.concurrent.CopyOnWriteArrayList

object SaveManager {

    private val cache = CopyOnWriteArrayList<SaveService>()



    fun addSaveService(player: CtOnlineRewardProxyPlayer){
        cache.add(SaveService(player).also {
            debug("玩家${it.uuid}已登出,登入时间为:${it.startTime} 登出时间为:${it.endTime}")
        })
    }



}