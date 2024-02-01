package cn.ctcraft.ctonlinereward.common.database.filedata.sqlite

import cn.ctcraft.ctonlinereward.common.database.AbstractDatabaseSQL
import cn.ctcraft.ctonlinereward.common.database.DatabaseUtils
import cn.ctcraft.ctonlinereward.common.pojo.OnlineData
import cn.hutool.db.Entity

import taboolib.module.configuration.ConfigSection
import java.io.File
import java.util.*
import javax.sql.DataSource


class SQLiteDatabase(private val config: ConfigSection) : AbstractDatabaseSQL(config) {


    private val directory: File = DatabaseUtils.getDirectory(config)

    val database = connectDatabase()

    override val dataSource: DataSource = setupHikariCP(
        "jdbc:sqlite:${directory}/data_sqlite.db",
        null,
        null,
        "org.sqlite.JDBC"
    )

    fun createTable(){
            val sql = """
                CREATE INDEX idx_uuid on online_data(uuid);
                CREATE INDEX idx_startTime on online_data(startTime);
                CREATE INDEX idx_endTime on online_data(endTime);
            """.trimIndent()
        database.connection.use {
            it.prepareStatement(sql).use {
                it.executeUpdate()
            }
        }
    }

    init {
        createTable()
    }

    override fun select(entity: Entity): Entity {
        TODO("Not yet implemented")
    }

    override fun initData(uuid: UUID) {
    }

    override fun save(uuid: UUID) {

    }

    override fun save() {
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

    override fun getOtherData(uuid: UUID, key: String): Any? {
        error("不能用")
    }

    override fun setOtherData(uuid: UUID, key: String, any: Any) {
        error("不能用")
    }

    override fun onDisable() {

    }


}