package cn.ctcraft.ctonlinereward.common.database

import cn.ctcraft.ctonlinereward.CtOnlineReward
import cn.ctcraft.ctonlinereward.common.document.Document
import cn.ctcraft.ctonlinereward.common.document.DocumentFile
import cn.ctcraft.ctonlinereward.common.document.buildMarkdown
import cn.ctcraft.ctonlinereward.common.logger.Logging
import net.steppschuh.markdowngenerator.text.emphasis.BoldText
import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Awake
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.ConfigSection
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.withComment
import java.util.concurrent.ConcurrentHashMap

object DatabaseHandler:Document{

    @Config(value = "datasource.yml")
    lateinit var dataSourceSetting:Configuration

    private var type = getDatabaseType()

    /**
     * databaseSourceMap
     * datasourceName and databaseObj
     */
    private val databaseSources = ConcurrentHashMap<String,AbstractDatabase>()

    private lateinit var database:AbstractDatabase

    val setting = type.getSetting().let {
        val section = CtOnlineReward.config.get("database") as ConfigSection
        section.getEnum("type",DatabaseType::class.java)
    }

    val tableMode = getTableModeForConfig()

    val saveTime = getSaveTimeConfig()

    private fun getSaveTimeConfig():Int{
        return CtOnlineReward.config.getInt("database.save_time",60).also {
            Logging.info("&6数据保存间隔: &a$it &6s&f.")
        }
    }


    private fun getDatabaseType(): DatabaseType {
       val type = CtOnlineReward.config.getString("database.type") ?: "yaml"
       return DatabaseType.valueOf(type)
    }

    /**
     * 注册内置数据库
     */
    @Awake(LifeCycle.ENABLE)
    fun registerInnerDatabase(){
        Logging.info("当前数据存储模式为: &6${type.name}")
        val config = Configuration.empty().apply {
            val section = CtOnlineReward.config.get("database") as ConfigSection
            setting!!.getSetting().forEach {
                this.set(it.key,section.get(it.key))
            }
        }
        type.database.getConstructor(ConfigSection::class.java).newInstance(config).also {
            database = it
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun onDisable(){
        database.onDisable()
        databaseSources.values.forEach {
            it.onDisable()
        }
    }

    /**
     * 获取内置数据库
     *
     */
    fun getInnerDatabase(): AbstractDatabase {
        if (!::database.isInitialized){
            error("数据库未初始化,请检查数据库配置是否正常")
        }
        return database
    }

    fun registerDatabase(databaseSource:String,database: AbstractDatabase){
        databaseSources[databaseSource] = database
    }

    fun getExtendsDatabase(databaseSource: String): AbstractDatabase? {
        return databaseSources[databaseSource]
    }

    fun getDatabase(databaseSource: String? = null): AbstractDatabase? {
        return if (databaseSource == null){
            databaseSources[type.name]
        }else{
            databaseSources[databaseSource]
        }
    }

    fun disableOriginDatabase(){
        type = DatabaseType.disable
    }

    fun getTableModeForConfig(): TableMode {
        val mode = CtOnlineReward.config.getEnum("database.table_mode", TableMode::class.java)
        return mode ?: run {
            Logging.info("&f分表模式识别失败,自动选择 &6按月分表 &f.")
            return@run TableMode.month
        }
    }

    override fun generateDoc(): DocumentFile {

        buildMarkdown {
            title("数据库配置")
            br()
            text("""
                配置文件: config.yml
            """.trimIndent())
            title("存储类型",4)
            DatabaseType.entries.forEach{type->
                title("数据库类型: ${type.name}",5)
                codeBlock(type.getSetting().let {
                    (Configuration.fromMap(mapOf("database" to it,"database.type" to type.name.withComment("数据类型"))) as ConfigFile).saveToString() },"yaml")
            }
            title("分表类型",4)
            TableMode.entries.forEach {
                title(it.description,5)

                codeBlock(Configuration.empty().apply {
                    set("database", mapOf(
                        "table_mode" to it.name.withComment(it.description)
                    ))
                }.saveToString(),"yaml")
            }

        }.also {
            return DocumentFile("config",it)
        }

    }


}