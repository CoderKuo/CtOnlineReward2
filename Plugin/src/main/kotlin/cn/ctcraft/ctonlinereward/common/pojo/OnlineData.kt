package cn.ctcraft.ctonlinereward.common.pojo

import cn.ctcraft.ctonlinereward.common.pojo.dao.OnlineDataDao
import java.time.Instant
import java.util.Date
import java.util.UUID

data class OnlineData(val uuid: UUID,val startTime:Date,val endTime:Date) {

    fun getDao(): OnlineDataDao {
        return OnlineDataDao(uuid.toString(), startTime.time,endTime.time)
    }

}