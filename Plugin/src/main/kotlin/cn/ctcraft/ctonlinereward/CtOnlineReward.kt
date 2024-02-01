package cn.ctcraft.ctonlinereward

import cn.ctcraft.ctonlinereward.common.database.AbstractDatabaseSQL
import cn.ctcraft.ctonlinereward.common.database.DatabaseHandler
import cn.ctcraft.ctonlinereward.common.logger.Logging
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.buildMenu

object CtOnlineReward : Plugin() {

    @Config
    lateinit var config:Configuration

    private val logging = Logging

    override fun onEnable() {
        logging.info("在线奖励加载成功! com.electronwill.nightconfig")
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