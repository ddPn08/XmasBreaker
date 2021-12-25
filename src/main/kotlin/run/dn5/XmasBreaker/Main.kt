package run.dn5.XmasBreaker

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import run.dn5.XmasBreaker.command.XmasBreakerExecuter
import run.dn5.XmasBreaker.item.CakeLauncher

class Main: JavaPlugin() {

    companion object {
        lateinit var plugin: Main
    }

    override fun onEnable() {
        plugin = this
        this.initRecipe()

        this.server.getPluginCommand("xmasbreaker")?.setExecutor(XmasBreakerExecuter())

        this.server.pluginManager.registerEvents(CakeLauncher(), this)
    }

    private fun initRecipe(){
        val result = CakeLauncher()
        val key = NamespacedKey(this, "XmasBreaker")
        val recipe = ShapedRecipe(key, result)

        recipe.shape("di ", "icg", " gr")

        recipe.setIngredient('d', Material.DISPENSER)
        recipe.setIngredient('i', Material.IRON_BLOCK)
        recipe.setIngredient('c', Material.COOKIE)
        recipe.setIngredient('g', Material.GUNPOWDER)
        recipe.setIngredient('r', Material.REDSTONE)

        Bukkit.addRecipe(recipe)
    }
}