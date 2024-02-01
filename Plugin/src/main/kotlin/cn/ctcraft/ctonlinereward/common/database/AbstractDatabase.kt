package cn.ctcraft.ctonlinereward.common.database

import cn.ctcraft.ctonlinereward.common.pojo.OnlineData
import cn.ctcraft.ctonlinereward.common.pojo.dao.OnlineDataDao
import taboolib.module.configuration.ConfigSection
import java.util.Date
import java.util.UUID

/**
 * AbstractDatabase 抽象数据库类
 * @author 大阔
 * @param config config.yml中的database配置
 */
abstract class AbstractDatabase(config: ConfigSection) {

    abstract fun initData(uuid: UUID)

    abstract fun save(uuid: UUID)

    abstract fun save()

    abstract fun getData(uuid: UUID): List<OnlineData>

    abstract fun getDataByDateRange(uuid: UUID,start:Date,end:Date):List<OnlineData>


    abstract fun getOtherData(uuid:UUID, key:String):Any?

    abstract fun addData(uuid: UUID,onlineData: OnlineData)

    abstract fun addData(onlineData: List<OnlineData>)

    abstract fun setOtherData(uuid: UUID,key: String,any: Any)

    abstract fun onDisable()

    fun getTableName():String{
        return getTableName(0)
    }

    fun getTableName(offset:Int):String{
        return DatabaseHandler.tableMode.getTableName(offset)
    }

}