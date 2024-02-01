package cn.ctcraft.ctonlinereward.common.database

import cn.ctcraft.ctonlinereward.common.logger.BukkitLog
import cn.ctcraft.ctonlinereward.common.logger.MyLogFactory
import cn.hutool.db.Db
import cn.hutool.db.Entity
import cn.hutool.log.LogFactory
import cn.hutool.log.dialect.console.ConsoleColorLogFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.module.configuration.ConfigSection
import java.io.PrintWriter
import javax.sql.DataSource


@RuntimeDependencies(
    RuntimeDependency("!com.zaxxer:HikariCP:4.0.3",
        test = "!com.zaxxer.hikari_4_0_3.HikariDataSource",
        relocate = ["!com.zaxxer.hikari", "!com.zaxxer.hikari_4_0_3", "!org.slf4j", "!org.slf4j_2_0_8"],
        transitive = false),
    RuntimeDependency("!cn.hutool:hutool-db:5.8.24", test = "!cn.hutool_5_8_24.db.Db",
        relocate = ["!cn.hutool","!cn.hutool_5_8_24"])
)
abstract class AbstractDatabaseSQL(config: ConfigSection) :AbstractDatabase(config) {

    abstract val dataSource:DataSource

    fun connectDatabase():Db{
        return Db.use(dataSource)
    }

    init {
        LogFactory.setCurrentLogFactory(MyLogFactory(""))
    }


    fun setupHikariCP(jdbcURL:String,user:String?,password:String?,driver:String): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = driver
        config.username = user
        config.password = password
        config.jdbcUrl = jdbcURL

        config.isAutoCommit = DatabaseHandler.dataSourceSetting.getBoolean("DefaultSettings.AutoCommit", true)
        config.minimumIdle = DatabaseHandler.dataSourceSetting.getInt("DefaultSettings.MinimumIdle", 1)
        config.maximumPoolSize = DatabaseHandler.dataSourceSetting.getInt("DefaultSettings.MaximumPoolSize", 10)
        config.validationTimeout = DatabaseHandler.dataSourceSetting.getLong("DefaultSettings.ValidationTimeout", 5000)
        config.connectionTimeout = DatabaseHandler.dataSourceSetting.getLong("DefaultSettings.ConnectionTimeout", 30000)
        config.idleTimeout = DatabaseHandler.dataSourceSetting.getLong("DefaultSettings.IdleTimeout", 600000)
        config.maxLifetime = DatabaseHandler.dataSourceSetting.getLong("DefaultSettings.MaxLifetime", 1800000)
        if (DatabaseHandler.dataSourceSetting.contains("DefaultSettings.ConnectionTestQuery")) {
            config.connectionTestQuery = DatabaseHandler.dataSourceSetting.getString("DefaultSettings.ConnectionTestQuery")
        }
        if (DatabaseHandler.dataSourceSetting.contains("DefaultSettings.DataSourceProperty")) {
            DatabaseHandler.dataSourceSetting.getConfigurationSection("DefaultSettings.DataSourceProperty")?.getKeys(false)?.forEach { key ->
                config.addDataSourceProperty(key, DatabaseHandler.dataSourceSetting.getString("DefaultSettings.DataSourceProperty.$key"))
            }
        }

        return HikariDataSource(config)
    }

    abstract fun select(entity: Entity):Entity




}