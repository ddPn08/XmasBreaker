package run.dn5.XmasBreaker.item

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import run.dn5.XmasBreaker.Main
import java.text.SimpleDateFormat
import java.util.*

open class SemiAutoGun(
    material: Material,
    private val displayName: Component,
    private val id: String,
    private val all: Int,
    private val reloadTime: Long,
    var normalCustomModelData: Int? = null,
    var reloadCustomModelData: Int? = null
): ItemStack(material), Listener {

    init {
        val gunData = LocalizedGunData(this.id, this.all, this.all, null, false)

        val meta = this.itemMeta
        meta.displayName(this.displayName.append(Component.text(" ${ChatColor.GREEN}| ${ChatColor.BOLD}${all}/${all}")))
        meta.setLocalizedName(gunData.stringify())
        meta.setCustomModelData(this.normalCustomModelData)
        this.itemMeta = meta
    }

    fun reload(item: ItemStack, player: Player){
        val gunData = LocalizedGunData.parse(item) ?: return
        if(gunData.isReloading) return
        if(gunData.id != this.id) return

        val startTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSSS").format(Date())
        gunData.reloadStartTime = startTime
        gunData.isReloading = true

        val meta = item.itemMeta
        meta.setLocalizedName(gunData.stringify())
        meta.displayName(this.displayName.append(Component.text(" ${ChatColor.GREEN}| ${ChatColor.RED}reloading...")))
        meta.setCustomModelData(this.reloadCustomModelData)
        item.itemMeta = meta

        object: BukkitRunnable(){
            override fun run() {
                val items = player.inventory.contents ?: return
                items.forEach {
                    if(it == null) return@forEach
                    val newGunData = LocalizedGunData.parse(it) ?: return@forEach
                    if((newGunData.id != id) || (newGunData.reloadStartTime != startTime)) return@forEach
                    newGunData.remain = all
                    newGunData.isReloading = false

                    val newMeta = it.itemMeta
                    newMeta.displayName(displayName.append(Component.text(" ${ChatColor.GREEN}| ${ChatColor.BOLD}${all}/${all}")))
                    newMeta.setLocalizedName(newGunData.stringify())
                    newMeta.setCustomModelData(normalCustomModelData)
                    it.itemMeta = newMeta
                    return
                }
            }
        }.runTaskLater(Main.plugin, this.reloadTime * 20)
    }

    private fun checkItem(item: ItemStack): Boolean{
        val data = LocalizedGunData.parse(item) ?: return false
        if(data.id == this.id) return true
        return false
    }

    open fun shoot(item: ItemStack, player: Player): Boolean {
        val gunData = LocalizedGunData.parse(item) ?: return false
        if(gunData.isReloading) return false
        if(gunData.remain <= 0){
            this.reload(item, player)
            return false
        }
        gunData.remain --
        val meta = item.itemMeta
        meta.setLocalizedName(gunData.stringify())
        meta.displayName(this.displayName.append(Component.text(" ${ChatColor.GREEN}| ${ChatColor.BOLD}${gunData.remain}/${all}")))
        item.itemMeta = meta
        return true
    }
    open fun onRightClick(event: PlayerInteractEvent, data: LocalizedGunData) {}

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent){
        val item = event.item ?: return
        if(!this.checkItem(item)) return
        val gunData = LocalizedGunData.parse(item)!!
        if(event.action.isRightClick){
            this.shoot(item, event.player)
            this.onRightClick(event, gunData)
        }
    }
    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent){
        val item = event.itemDrop.itemStack
        if(!this.checkItem(item)) return

        val gunData = LocalizedGunData.parse(item) ?: return
        if(this.all == gunData.remain) return
        this.reload(item, event.player)
        event.isCancelled = true
    }
}