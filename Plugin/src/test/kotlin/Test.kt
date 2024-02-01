import cn.ctcraft.ctonlinereward.bukkit.menu.SlotMapping
import org.junit.jupiter.api.Test


fun main() {

    val layout = listOf(
        "XXXXXXXXX".toList(),
        "abc BBCXX".toList(),
        "***&&&^^^".toList()
    )

    val craftLayout = listOf(
        "aaa".toList(),
        "cba".toList(),
        "**^".toList(),
        "*".toList(),
        "*".toList(),
    )
    runCatching {
        SlotMapping.CraftMapping.get(craftLayout).also {
            println(it)
        }
    }.onFailure {
        println(it.message)
    }



}

class Test {

    @Test
    fun readConfig(){

    }

}