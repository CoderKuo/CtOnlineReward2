package cn.ctcraft.ctonlinereward.common.database

import cn.ctcraft.ctonlinereward.common.database.filedata.h2.H2Database
import cn.ctcraft.ctonlinereward.common.database.filedata.sqlite.SQLiteDatabase
import cn.ctcraft.ctonlinereward.common.database.filedata.yaml.YamlDatabase
import cn.ctcraft.ctonlinereward.common.database.sqldata.mysql.MysqlDatabase
import cn.ctcraft.ctonlinereward.common.database.sqldata.postgre.PostgreDatabase
import taboolib.module.configuration.util.withComment

enum class DatabaseType(val database:Class<out AbstractDatabase>) {
    yaml(YamlDatabase::class.java),
    mysql(MysqlDatabase::class.java),
    sqlite(SQLiteDatabase::class.java),
    h2(H2Database::class.java),
    postgre(PostgreDatabase::class.java),
    disable(Nothing::class.java);

    fun getSetting(): Map<String,Any> {
        when (name){
            "sqlite",
            "yaml"->{
                mapOf("directory" to "./data".withComment("存储数据文件的文件夹"))
            }
            "h2"->{
                mapOf("h2" to mapOf(
                    "directory" to "./data".withComment("存储数据文件的文件夹"),
                        "user" to "sa".withComment("用户名"),
                        "password" to "123456".withComment("密码"),
                        "database" to "data".withComment("数据库名")))

            }
            "mysql","postgre"->{
                mapOf("setting" to mapOf(
                    "host" to "localhost".withComment("主机名或ip地址"),
                    "port" to 3306.withComment("端口号 默认3306"),
                    "user" to "root".withComment("用户名"),
                    "password" to "root".withComment("密码"),
                    "database" to "minecraft".withComment("数据库名")
                ))
            }
            else -> {
                mapOf()
            }
        }.also {
            return it
        }
    }




}