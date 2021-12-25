package run.dn5.XmasBreaker

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor

class Log {
    companion object {
        fun info(value: String): Component{
            return Component.text(ChatColor.translateAlternateColorCodes('&', "&b[XmasBreaker] $value"))
        }
        fun warn(value: String): Component{
            return Component.text(ChatColor.translateAlternateColorCodes('&', "&c[XmasBreaker] $value"))
        }
    }
}