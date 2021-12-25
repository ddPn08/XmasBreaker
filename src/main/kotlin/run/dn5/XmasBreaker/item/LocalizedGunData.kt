package run.dn5.XmasBreaker.item

import org.bukkit.inventory.ItemStack
import java.util.*

class LocalizedGunData(
    val id: String,
    var remain: Int,
    val all: Int,
    var reloadStartTime: String?,
    var isReloading: Boolean
) {
    companion object {
        fun parse(item: ItemStack): LocalizedGunData?{
            val data = item.itemMeta.localizedName.split(":")
            if(data.size != 5) return null
            return LocalizedGunData(data[0], data[1].toInt(), data[2].toInt(), data[3], data[4].toBoolean())
        }
    }

    fun stringify(): String{
        return "${this.id}:${this.remain}:${this.all}:${this.reloadStartTime}:${this.isReloading}"
    }
}