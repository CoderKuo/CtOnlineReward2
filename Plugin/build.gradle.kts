import io.izzel.taboolib.gradle.*

plugins {
    id("io.izzel.taboolib")
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
    id("com.github.johnrengelman.shadow")
}

group = "cn.ctcraft.ctonlinereward"
version = "2.0.0"
setProperty("archivesBaseName","CtOnlineReward-Plugin")

taboolib {
    env {
        install(UI)
        install(CHAT, CONFIGURATION, LANG, KETHER)
        install(BUKKIT_ALL)
        install(EXPANSION_PLAYER_FAKE_OP)
        install("platform-bukkit-impl")
        install(EXPANSION_COMMAND_HELPER)
    }

    classifier = null
    version {
        taboolib = "6.1.0"
    }

    relocate("com.zaxxer.hikari.", "com.zaxxer.hikari_4_0_3.")
    relocate("cn.hutool.", "cn.hutool_5_8_24.")
    relocate("net.steppschuh.markdowngenerator.","net.steppschuh.markdowngenerator1_3_2.")
    relocate("com.github.alanger.commonjs.", "cn.ctcraft.ctonlinereward.common.script.commonjs")

    description {
        name = "CtOnlineReward"
        contributors {
            name("大阔").description("CtOnlineReward")
        }

        dependencies {
            name("PlaceholderAPI").optional(true)
        }
    }
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly("net.milkbowl.vault:Vault:1")


    compileOnly("com.h2database:h2:2.2.224")
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    compileOnly("org.postgresql:postgresql:42.7.1")

    compileOnly("cn.hutool:hutool-db:5.8.24")
    testCompileOnly("cn.hutool:hutool-db:5.8.24")
    testImplementation(kotlin("test"))

    taboo("com.github.a-langer:jsr223-commonjs-modules:1.0.1")
    taboo("com.github.Steppschuh:Java-Markdown-Generator:1.3.2")

    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}


tasks.test {
    useJUnitPlatform()
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_9
    targetCompatibility = JavaVersion.VERSION_1_9
}



tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


