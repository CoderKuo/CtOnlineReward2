package cn.ctcraft.ctonlinereward.common.database

import cn.hutool.core.date.DateField
import cn.hutool.core.date.DateUtil
import java.util.*

enum class TableMode(val description:String,val offsetField: DateField,val format:String) {
    month("按月分表",DateField.MONTH,"yyyyMM"),
    week("按周分表",DateField.WEEK_OF_YEAR,"yyyyMMdd"),
    day("按天分表",DateField.DAY_OF_YEAR,"yyyyMMdd");

    fun getTableName():String{
        return getTableName(0)
    }

    fun getTableName(offset:Int):String{
        var date = DateUtil.date()
        if (this == week){
            date = DateUtil.beginOfWeek(date)
        }
        if (offset != 0){
            date = date.offset(offsetField,offset)
        }
        return DateUtil.format(date,format)
    }
}