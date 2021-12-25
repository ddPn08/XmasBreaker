package run.dn5.XmasBreaker.item

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import run.dn5.XmasBreaker.Constants
import run.dn5.XmasBreaker.Main

class CakeLauncher() : SemiAutoGun(
    Material.CARROT_ON_A_STICK,
    Component.text("CakeLauncher"),
    "CakeLauncher",
    1,
    5,
    "${Constants.CUSTOM_MODEL_DATA}01".toInt(),
    "${Constants.CUSTOM_MODEL_DATA}02".toInt()
), Listener {

    companion object {
        private const val CUSTOM_ENTITY_ID = "${Constants.CUSTOM_ENTITY_ID}:CakeLauncher"
    }

    override fun shoot(item: ItemStack, player: Player): Boolean {
        if(!super.shoot(item, player)) return false
        this.reload(item, player)

        val loc = player.location
        loc.y = loc.y + 1.5

        val block = player.world.spawnFallingBlock(loc, Material.CAKE.createBlockData())
        block.dropItem = false
        block.customName = CUSTOM_ENTITY_ID

        val vec = loc.direction
        vec.y = vec.y + 0.2
        block.velocity = vec

        player.playSound(Sound.sound(Key.key("entity.generic.explode"), Sound.Source.BLOCK, 1f, 1f))

        object : BukkitRunnable() {
            override fun run() {
                if (!block.isDead) return
                block.world.createExplosion(block.location, 50f, true, true, player)
                block.remove()
                this.cancel()
            }
        }.runTaskTimer(Main.plugin, 0, 1)

        return true
    }

    @EventHandler
    fun onBlockLanding(event: EntityChangeBlockEvent) {
        if (event.entity.customName == null) return
        if (event.entityType != EntityType.FALLING_BLOCK && event.entity.customName == CUSTOM_ENTITY_ID) return
        event.isCancelled = true
    }
}