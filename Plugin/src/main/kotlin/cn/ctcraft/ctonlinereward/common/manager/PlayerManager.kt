package cn.ctcraft.ctonlinereward.common.manager

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.ctcraft.ctonlinereward.common.logger.debug
import cn.ctcraft.ctonlinereward.onlinetime.SelectOnlineTime
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.platform.util.onlinePlayers
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerManager {

    private val cache:ConcurrentHashMap<UUID,CtOnlineRewardProxyPlayer> = ConcurrentHashMap()

    @Awake(LifeCycle.ENABLE)
    fun initPlayer(){
        onlinePlayers.forEach {
            addPlayerData(it.uniqueId, CtOnlineRewardProxyPlayer(it))
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable(){
        cache.forEach {
            stopTiming(it.key)
        }
        cache.clear()
    }

    fun addPlayerData(uuid: UUID,player: CtOnlineRewardProxyPlayer){
        cache[uuid] = player
        debug("${player.name} 玩家数据已记录, 登入时间为:${getStartTime(uuid)}")
    }

    fun getPlayerData(uuid: UUID): CtOnlineRewardProxyPlayer? {
        return cache[uuid]
    }

    fun getStartTime(uuid: UUID) = cache[uuid]?.startTime

    fun stopTiming(uuid: UUID){
        cache[uuid]?.also {
            SaveManager.addSaveService(it)
            cache.remove(uuid)
        }
    }

    fun getPlayerOnlineTime(uuid:UUID,select: SelectOnlineTime):Int{
        return select.getOnlineTime(uuid)
    }

    fun getPlayerOnlineTimeNow(uuid: UUID):Int{
        return if (cache.containsKey(uuid)){
            cache[uuid]!!.getOnlineTimeNow()
        }else{
            0
        }
    }

}
