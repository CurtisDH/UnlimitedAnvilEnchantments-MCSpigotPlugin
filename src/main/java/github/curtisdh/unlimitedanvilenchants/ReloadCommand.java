package github.curtisdh.unlimitedanvilenchants;

import github.curtisdh.unlimitedanvilenchants.UnlimitedAnvilEnchants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        UnlimitedAnvilEnchants.PrintWithClassName(this, "Reloading config");
        UnlimitedAnvilEnchants.Instance.reloadConfig();
        UnlimitedAnvilEnchants.Instance.LoadConfig();
        sender.sendMessage(ChatColor.GREEN+"Successfully reloaded config");
        return true;
    }
}
