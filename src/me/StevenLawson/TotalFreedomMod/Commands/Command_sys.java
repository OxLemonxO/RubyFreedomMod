package me.StevenLawson.TotalFreedomMod.Commands;

import static me.StevenLawson.TotalFreedomMod.Commands.Command_smite.smite;
import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_Ban;
import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.OP, source = SourceType.BOTH)
@CommandParameters(
        description = "A command for system admins only",
        usage = "/<command> [add <player> | del <player> | suspend <player> | teston | testoff]")
public class Command_sys extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole){
    	
          
        if (!TFM_Util.SYS.contains(sender.getName()) && !TFM_Util.DEVELOPERS.contains(sender.getName()) && !TFM_Util.COOWNER.contains(sender.getName()) && !TFM_Util.EX.contains(sender.getName()) && !TFM_Util.LEADDEV.contains(sender.getName()) && !sender.getName().equals("falceso"))
        {
            playerMsg(TFM_Command.MSG_NO_PERMS);
            Bukkit.broadcastMessage(ChatColor.RED + "WARNING: " + sender.getName() + " has attempted to use a system admin only command. System administration team has been alerted.");
            smite(sender_p);
            //lol smites them if they cant do /sys i'm really evil :)
            return true;
        } 
        if (args.length == 0)
        {
            return false;
        }

        String mode = args[0].toLowerCase();

        if (mode.equals("add"))
        {
        	Player player = getPlayer(args[1]);
        	if (player == null){
        		sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
        	}
        	TFM_Util.adminAction(sender.getName(), "Adding " + args[1] + " to the superadmin list", true);
        	TFM_AdminList.addSuperadmin(player);
        }
        
        if (mode.equals("del"))
        {
        	Player player = getPlayer(args[1]);
        	if (player == null){
        		sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
        	}
        	TFM_Util.adminAction(sender.getName(), "Removing " + args[1] + " from the superadmin list", true);
        	TFM_AdminList.removeSuperadmin(player);
        }
        
        if (mode.equals("suspend")) {
            if (!TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName())) {
                sender.sendMessage(TFM_Command.MSG_NO_PERMS);
                return true;
            }
            Player player = getPlayer(args[1]);
        	if (player == null){
        		sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
        	}
            TFM_Util.adminAction(sender.getName(), "Suspending " + args[1], true);
            TFM_AdminList.removeSuperadmin(player);
            for (String playerIp : TFM_PlayerList.getEntry(player).getIps()) {
                TFM_BanManager.addIpBan(new TFM_Ban(playerIp, player.getName()));
            }
            TFM_BanManager.addUuidBan(player);
            player.closeInventory();
            player.getInventory().clear();
            player.kickPlayer("You have been suspended. Check the forums for more information.");
        }
        
        if (mode.equals("teston"))
        {
        	TFM_Util.bcastMsg(ChatColor.RED + "WARNING: " + sender.getName() + " has started testing on this server.");
        }
        
        if (mode.equals("testoff"))
        {
        	TFM_Util.bcastMsg(ChatColor.RED + sender.getName() + " has successfully tested on this server.");
        }
        
        return true;
    }  
}
