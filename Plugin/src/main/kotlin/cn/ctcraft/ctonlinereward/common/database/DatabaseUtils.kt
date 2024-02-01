package cn.ctcraft.ctonlinereward.common.database

import taboolib.common.platform.function.getDataFolder
import taboolib.common5.util.replace
import taboolib.module.configuration.ConfigSection
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.PreparedStatement
import java.sql.Time
import java.sql.Timestamp
import java.sql.Types
import java.util.*
import java.util.concurrent.ConcurrentHashMap


object DatabaseUtils {



    private fun getTypeOfNull(ps: PreparedStatement,index: Int): Int {
        var type = Types.VARCHAR

        runCatching {
            type = ps.parameterMetaData.getParameterType(index)
        }

        return type
    }

    fun setParam(ps:PreparedStatement, index:Int, param:Any?, nullTypeCache:ConcurrentHashMap<Int,Int>){
        if (param == null){
            var type:Int? = nullTypeCache[index]
            if (type == null){
                type = getTypeOfNull(ps,index)
                if (type != null){
                    nullTypeCache[index] = type
                }
            }
            ps.setNull(index,type)
        }
        if (param is Date){
            if (param is java.sql.Date){
                ps.setDate(index,param)
            }else if(param is Time){
                ps.setTime(index,param)
            }else {
                ps.setTimestamp(index, Timestamp(param.time))
            }
            return
        }

        if (param is Number) {
            if (param is BigDecimal) {
                ps.setBigDecimal(index, param as BigDecimal?)
                return
            }
            if (param is BigInteger) {
                ps.setBigDecimal(index, BigDecimal(param as BigInteger?))
                return
            }
        }

        ps.setObject(index, param)

    }

    fun getDirectory(config:ConfigSection):File{
        return config.getString("directory").let {
            if (it == null){
                error("请配置config.yml配置文件中的database.directory配置为数据存储文件夹")
            }
            File(it.replace("." to getDataFolder().path))
        }.also {
            if (!it.exists()){
                it.mkdirs()
            }
        }
    }

}