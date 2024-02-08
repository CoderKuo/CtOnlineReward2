package cn.ctcraft.ctonlinereward

import cn.ctcraft.ctonlinereward.common.database.DatabaseHandler
import cn.ctcraft.ctonlinereward.common.logger.Logging
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object CtOnlineReward : Plugin() {

    @Config(value = "config.yml")
    lateinit var config: ConfigFile

    private val logging = Logging

    override fun onEnable() {
        logging.info("在线奖励加载成功!")
    }

    override fun onActive() {
        DatabaseHandler.getInnerDatabase().also {
            logging.info(it::class.java)
        }
        DatabaseHandler.getInnerDatabase().getTableName().also {
            logging.info(it)
        }

    }
}