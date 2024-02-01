package cn.ctcraft.ctonlinereward.common.database.filedata.h2

import cn.ctcraft.ctonlinereward.common.database.AbstractDatabaseSQL
import cn.ctcraft.ctonlinereward.common.database.DatabaseUtils
import cn.ctcraft.ctonlinereward.common.pojo.OnlineData
import cn.hutool.db.Entity
import cn.hutool.db.dialect.DialectFactory
import com.zaxxer.hikari.HikariDataSource
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.module.configuration.ConfigSection
import java.time.Instant
import java.util.*
import javax.sql.DataSource

@RuntimeDependencies(
    RuntimeDependency(
        "!com.h2database:h2:2.2.224",
        test = "!org.h2.Driver"
    )
)
class H2Database(private val config: ConfigSection) : AbstractDatabaseSQL(config) {

    override val dataSource: DataSource = setupHikariCP()

    val database = connectDatabase()

    val TABLE_NAME = "online_data"

    val dialect = DialectFactory.getDialect(dataSource)

    fun createTable() {
        val sql = """
            create table IF NOT EXISTS online_data (id INT AUTO_INCREMENT PRIMARY KEY, uuid UUID not null
                ,startTime TIMESTAMP not null 
                ,endTime TIMESTAMP not null 
                , key idx_uuid(uuid)
                ,key idx_startTime(startTime)
                ,key idx_endTime(endTime));
            """.trimIndent()
        database.connection.use {
            it.prepareStatement(sql).use {
                it.executeUpdate()
            }
        }
    }

    init {
        createTable()
        UUID.randomUUID().also {
            database.insert(Entity(TABLE_NAME).set("uuid",it).set("startTime",Instant.now()).set("endTime",Instant.ofEpochMilli(1704900696)))
            getData(it).forEach {
                println(it)
            }
        }
    }

    override fun onDisable() {

    }

    override fun initData(uuid: UUID) {
    }

    override fun save(uuid: UUID) {
    }

    override fun save() {
    }

    override fun getData(uuid: UUID): List<OnlineData> {
        return database.findAll(Entity(TABLE_NAME).set("uuid",uuid)).map {
            OnlineData(UUID.fromString(it.get("uuid").toString()),it.getTimestamp("startTime"),it.getTimestamp("endTime"))
        }
    }

    override fun getDataByDateRange(uuid: UUID, start: Date, end: Date): List<OnlineData> {
        TODO()
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

    override fun select(entity: Entity): Entity {
        TODO("Not yet implemented")
    }

    private fun setupHikariCP(): HikariDataSource {
        val directory = DatabaseUtils.getDirectory(config.get("h2") as ConfigSection).let {
            if (!it.isAbsolute) {
                "./${it.path}"
            } else {
                it.path
            }
        }

        return setupHikariCP(
            "jdbc:h2:${directory}/${this.config.getString("h2.database")};MODE=MYSQL;AUTO_RECONNECT=true;",
            this.config.getString("h2.user"),
            this.config.getString("h2.password"),
            "org.h2.Driver"
        )
    }
}