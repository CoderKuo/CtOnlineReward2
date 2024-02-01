package cn.ctcraft.ctonlinereward.common.logger

import cn.hutool.core.text.StrFormatter
import cn.hutool.log.Log
import cn.hutool.log.level.Level

class BukkitLog:Log {
    val logging = Logging

    override fun isTraceEnabled(): Boolean {
        return false
    }

    override fun trace(t: Throwable?) {
        logging.severe(t)
    }

    override fun trace(format: String?, vararg arguments: Any?) {
        logging.severe(StrFormatter.format(format,arguments))
    }

    override fun trace(t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun trace(fqcn: String?, t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun isDebugEnabled(): Boolean {
        return isDebug
    }

    override fun debug(t: Throwable?) {
        logging.debug(t)
    }

    override fun debug(format: String?, vararg arguments: Any?) {
        logging.debug(StrFormatter.format(format,arguments))
    }

    override fun debug(t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun debug(fqcn: String?, t: Throwable?, format: String?, vararg arguments: Any?) {
        logging.debug(StrFormatter.format(format,arguments))
    }

    override fun isInfoEnabled(): Boolean {
        return true
    }

    override fun info(t: Throwable?) {
        logging.info(t)
    }

    override fun info(format: String?, vararg arguments: Any?) {
        logging.info(format,arguments)
    }

    override fun info(t: Throwable?, format: String?, vararg arguments: Any?) {
        logging.info(t,format,arguments)
    }

    override fun info(fqcn: String?, t: Throwable?, format: String?, vararg arguments: Any?) {
        logging.info(fqcn,t,format,arguments)
    }

    override fun isWarnEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun warn(t: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun warn(format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun warn(t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun warn(fqcn: String?, t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun isErrorEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun error(t: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun error(format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun error(t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun error(fqcn: String?, t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun isEnabled(level: Level?): Boolean {
        TODO("Not yet implemented")
    }

    override fun log(level: Level?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun log(level: Level?, t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun log(fqcn: String?, level: Level?, t: Throwable?, format: String?, vararg arguments: Any?) {
        TODO("Not yet implemented")
    }
}