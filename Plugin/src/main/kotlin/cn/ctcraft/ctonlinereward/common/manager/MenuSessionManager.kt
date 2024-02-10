package cn.ctcraft.ctonlinereward.common.manager

import cn.ctcraft.ctonlinereward.bukkit.menu.Menu
import cn.ctcraft.ctonlinereward.bukkit.menu.PublicSession
import cn.ctcraft.ctonlinereward.bukkit.menu.Session
import cn.ctcraft.ctonlinereward.bukkit.menu.SessionStatus
import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object MenuSessionManager : Iterable<Map.Entry<UUID, PublicSession>> {

    private val session = ConcurrentHashMap<UUID, PublicSession>()


    fun openSession(menu: Menu): UUID {
        UUID.randomUUID().also {
            session[it] = PublicSession(menu)
            return it
        }
    }

    fun openSession(menu: Menu, player: CtOnlineRewardProxyPlayer): UUID {
        UUID.randomUUID().also {
            session[it] = PublicSession(menu).apply {
                add(player)
            }
            return it
        }
    }

    fun closeSession(uuid: UUID) {
        session[uuid]?.status = SessionStatus.closed
    }

    operator fun get(uuid: String): Session? {
        return get(UUID.fromString(uuid))
    }

    operator fun get(uuid: UUID): Session? {
        return session[uuid]
    }

    fun size(): Int {
        return session.size
    }

    override fun iterator(): Iterator<Map.Entry<UUID, PublicSession>> {
        return session.iterator()
    }


}