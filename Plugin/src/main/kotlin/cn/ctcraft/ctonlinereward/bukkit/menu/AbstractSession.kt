package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.hutool.core.lang.Dict
import java.util.*

abstract class AbstractSession : Session {

    override var status: SessionStatus = SessionStatus.created

    override val startTime: Date = Date()


    override val store: Dict = Dict()


}