package cn.ctcraft.ctonlinereward.onlinetime

import cn.hutool.core.date.DateUtil
import java.util.*

enum class CommonOnlineTime: SelectOnlineTime {

    day{
        override fun setStartTime(): Long {
            return DateUtil.beginOfDay(Date()).time
        }

        override fun setEndTime(): Long {
            return DateUtil.endOfDay(Date()).time
        }
    },
    week{

        override fun setStartTime(): Long {
            return DateUtil.beginOfWeek(Date()).time
        }

        override fun setEndTime(): Long {
            return DateUtil.endOfWeek(Date()).time
        }

    },
    month{
        override fun setStartTime(): Long {
            return DateUtil.beginOfMonth(Date()).time
        }

        override fun setEndTime(): Long {
            return DateUtil.endOfMonth(Date()).time
        }
    }

}