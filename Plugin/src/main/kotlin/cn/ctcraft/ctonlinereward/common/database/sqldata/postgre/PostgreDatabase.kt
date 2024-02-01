package cn.ctcraft.ctonlinereward.common.database.sqldata.postgre

import cn.ctcraft.ctonlinereward.common.database.AbstractDatabaseSQL
import cn.ctcraft.ctonlinereward.common.database.DatabaseHandler
import cn.ctcraft.ctonlinereward.common.pojo.OnlineData
import cn.hutool.db.Entity
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.env.RuntimeResources
import taboolib.module.configuration.ConfigSection
import java.util.*
import javax.sql.DataSource

@RuntimeDependencies(
    RuntimeDependency("!org.postgresql:postgresql:42.7.1", test = "!org.postgresql_42_7_1.Driver",
        relocate = ["!org.postgresql","!org.postgresql_42_7_1"])
)
class PostgreDatabase(config: ConfigSection) :AbstractDatabaseSQL(config) {

    override val dataSource: DataSource = setupHikariCP(
        "jdbc:postgresql://${config.getString("setting.host")}:${config.getInt("setting.port")}/${config.getString("setting.database")}?useUnicode=true&characterEncoding=utf-8&useSSL=false",
        config.getString("setting.user"),
        config.getString("setting.password"),
        "org.postgresql_42_7_1.Driver"
    )


    override fun select(entity: Entity): Entity {
        TODO("Not yet implemented")
    }

    override fun initData(uuid: UUID) {
        TODO("Not yet implemented")
    }

    override fun save(uuid: UUID) {
        TODO("Not yet implemented")
    }

    override fun save() {
        TODO("Not yet implemented")
    }

    override fun getData(uuid: UUID): List<OnlineData> {
        TODO("Not yet implemented")
    }

    override fun getDataByDateRange(uuid: UUID, start: Date, end: Date): List<OnlineData> {
        TODO("Not yet implemented")
    }

    override fun getOtherData(uuid: UUID, key: String): Any? {
        TODO("Not yet implemented")
    }

    override fun addData(uuid: UUID, onlineData: OnlineData) {
        TODO("Not yet implemented")
    }

    override fun addData(onlineData: List<OnlineData>) {
        TODO("Not yet implemented")
    }

    override fun setOtherData(uuid: UUID, key: String, any: Any) {
        TODO("Not yet implemented")
    }

    override fun onDisable() {
        TODO("Not yet implemented")
    }
}