package cn.ctcraft.ctonlinereward.common.database.filedata.yaml

import cn.ctcraft.ctonlinereward.common.database.AbstractDatabase
import cn.ctcraft.ctonlinereward.common.database.DatabaseUtils
import cn.ctcraft.ctonlinereward.common.logger.debug
import cn.ctcraft.ctonlinereward.common.pojo.OnlineData
import cn.ctcraft.ctonlinereward.common.pojo.dao.OnlineDataDao
import cn.hutool.core.bean.BeanDesc
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import cn.hutool.core.date.DateUtil
import taboolib.module.configuration.ConfigSection
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Configuration.Companion.getObject
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class YamlDatabase(private val config: ConfigSection) : AbstractDatabase(config) {


    private val directory: File = DatabaseUtils.getDirectory(config)

    private val cache = ConcurrentHashMap<UUID,Configuration>()

    private fun getYaml(uuid: UUID): Configuration {
        if (cache.containsKey(uuid)){
            return cache[uuid]!!
        }else{
            getFile(uuid).also {
                return Configuration.loadFromFile(it).also {
                    cache[uuid] = it
                }
            }
        }
    }

    private fun getFile(uuid: UUID):File{
        return File(directory, "$uuid.yml").apply {
            if (!this.exists()){
                this.createNewFile()
            }
        }.also {
            debug("已读入 $it yaml数据文件")
        }
    }

    override fun onDisable() {
        save()
    }

    override fun getData(uuid: UUID): List<OnlineData> {
        return (getYaml(uuid).getMapList("online")).map {
            val map = it.toMutableMap()
            map.put("uuid",uuid)
            BeanUtil.mapToBean(it,OnlineDataDao::class.java,true,CopyOptions.create()).let {
                OnlineData(uuid,Date(it.startTime),Date(it.endTime))
            }
        }
    }

    override fun getOtherData(uuid:UUID, key:String):Any? {
        return getYaml(uuid).getObject(key)
    }

    override fun getDataByDateRange(uuid: UUID, start: Date, end: Date): List<OnlineData> {
        getYaml(uuid).also {
            return it.getMapList("online").filter {
                val startTime = it.get("startTime") as Long ?: 0
                val endTime = it.get("endTime") as Long ?: 0
                DateUtil.isIn(Date(startTime),start,end) && DateUtil.isIn(Date(endTime),start,end)
            }.map {
                val map = it.toMutableMap().apply {
                    put("uuid",uuid.toString())
                }
                BeanUtil.toBean(map,OnlineDataDao::class.java).let {
                    OnlineData(UUID.fromString(it.uuid),Date(it.startTime),Date(it.endTime))
                }
            }
        }
    }

    override fun addData(onlineData: List<OnlineData>) {
        onlineData.forEach { data->
            cache.getOrPut(data.uuid){ getYaml(data.uuid) }.apply {
                (this.getMapList("online")).toMutableList().also {
                    debug("插入了在线数据: ${data.getDao()}")
                    it.add(BeanUtil.beanToMap(data.getDao()).apply {
                        this.remove("uuid")
                    })
                    set("online",it)
                }
            }
        }
    }

    override fun addData(uuid: UUID, onlineData: OnlineData) {
        addData(listOf(onlineData))
    }

    override fun setOtherData(uuid: UUID, key: String, any: Any) {
        getYaml(uuid).also {
            it.set(key,any)
            it.saveToFile(getFile(uuid))
        }
    }

    override fun initData(uuid: UUID) {
        setOtherData(uuid,"uuid",uuid)
    }

    override fun save(uuid: UUID) {
        cache.filterKeys { it == uuid }.forEach {
            it.value.saveToFile(getFile(uuid))
            debug("$uuid 玩家数据已保存至yaml")
        }
    }

    override fun save() {
        cache.forEach{
            it.value.saveToFile(getFile(it.key))
            debug("${it.key} 玩家数据已保存至yaml")
        }
        cache.clear()
    }
}