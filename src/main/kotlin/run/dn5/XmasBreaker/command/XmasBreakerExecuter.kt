package run.dn5.XmasBreaker.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import run.dn5.XmasBreaker.Log
import run.dn5.XmasBreaker.item.CakeLauncher

class XmasBreakerExecuter: TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return when(args.size){
            1 -> {
                mutableListOf("give")
            }
            else -> {
                mutableListOf()
            }
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.isEmpty()){
            sender.sendMessage(Log.info("因数が足りません。"))
            return false
        }
        val player = sender as Player
        when (args[0]){
            "give" -> {
                player.world.dropItem(player.location, CakeLauncher())
            }
        }
        return false
    }

}