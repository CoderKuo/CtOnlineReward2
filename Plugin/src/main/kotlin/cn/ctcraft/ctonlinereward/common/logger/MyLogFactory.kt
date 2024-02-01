package cn.ctcraft.ctonlinereward.common.logger

import cn.hutool.log.Log
import cn.hutool.log.LogFactory

class MyLogFactory(name: String?) : LogFactory(name) {

    override fun createLog(name: String?): Log {
        return BukkitLog()
    }

    override fun createLog(clazz: Class<*>?): Log {
        return BukkitLog()
    }

}