package me.curtisdh.testproject;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.w3c.dom.Attr;

public class GenericCommands implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("Only players can use that command");
            return true;
        }
        Player player = (Player) sender;
        switch (command.getName())
        {
            case "heal":
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                break;
            case "food":
                player.setFoodLevel(20); // Not sure how to grab max food level
                break;
            case "top":
                Block block = player.getWorld().getHighestBlockAt(player.getLocation());
                Location idealLocation = block.getLocation();
                idealLocation.add(0,1,0); // Otherwise we spawn inside the block.
                if (block != null)
                {
                    player.teleport(idealLocation);
                }
                break;
            case "SetXPLevel":
                //TODO allow for player specification

                if(args.length > 0)
                {
                    try
                    {
                        Integer test = Integer.parseInt(args[0]);
                        ((Player) sender).setLevel(test);
                        break;
                    }
                    catch (Exception e)
                    {
                        sender.sendMessage("Incorrect usage:'"+args[0]+"'");
                    }
                }
                sender.sendMessage("Error: usage is as follows: /<command> arg(numerical)");
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + command.getName());
        }
        if (command.getName().equalsIgnoreCase("heal"))
        {

        }

        return true;
    }
}
