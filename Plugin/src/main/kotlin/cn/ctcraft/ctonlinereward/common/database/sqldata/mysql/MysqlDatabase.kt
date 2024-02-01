package cn.ctcraft.ctonlinereward.common.database.sqldata.mysql

import cn.ctcraft.ctonlinereward.common.database.AbstractDatabaseSQL
import cn.ctcraft.ctonlinereward.common.database.DatabaseHandler
import cn.ctcraft.ctonlinereward.common.pojo.OnlineData
import cn.hutool.db.Entity
import taboolib.module.configuration.ConfigSection
import java.util.*
import javax.sql.DataSource


class MysqlDatabase(private val config:ConfigSection):AbstractDatabaseSQL(config) {

    val database = connectDatabase()


    override val dataSource: DataSource = setupHikariCP(
        "jdbc:mysql://${config.getString("setting.host")}:${config.getInt("setting.port")}/${config.getString("setting.database")}?useUnicode=true&characterEncoding=utf-8&useSSL=false",
        config.getString("setting.user"),
        config.getString("setting.password"),
        DatabaseHandler.dataSourceSetting.getString("DefaultSettings.DriverClassName", "com.mysql.jdbc.Driver")!!
    )

    override fun select(entity: Entity): Entity {
        TODO("Not yet implemented")
    }


    override fun onDisable() {
        TODO("Not yet implemented")
    }

    override fun getData(uuid: UUID): List<OnlineData> {
        TODO("Not yet implemented")
    }


    override fun getDataByDateRange(uuid: UUID, start: Date, end: Date): List<OnlineData> {
        TODO()
    }

    override fun addData(uuid: UUID, onlineData: OnlineData) {
        TODO("Not yet implemented")
    }

    override fun addData(onlineData: List<OnlineData>) {
        TODO("Not yet implemented")
    }

    override fun initData(uuid: UUID) {
    }

    override fun save(uuid: UUID) {
    }

    override fun save() {
    }

    override fun getOtherData(uuid: UUID, key: String): Any? {
        error("不能用")
    }

    override fun setOtherData(uuid: UUID, key: String, any: Any) {
        error("不能用")
    }

}