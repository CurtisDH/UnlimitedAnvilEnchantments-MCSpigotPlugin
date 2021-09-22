package me.curtisdh.testproject;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class onBlockBreakEvent implements Listener
{
//    @EventHandler
//    public void onPlayerBlockBreak(BlockBreakEvent event)
//    {
//        Block targetBlock = event.getBlock();
//        if(targetBlock.getType() == Material.GRASS_BLOCK )
//        {
//            TestProject.PrintWithClassName(this,"Block Broken");
//            event.setCancelled(true);
//            targetBlock.setType(Material.AIR);
//            ItemStack drop = new ItemStack(Material.GRASS_BLOCK,2);
//            targetBlock.getWorld().dropItemNaturally(targetBlock.getLocation(),drop);
//        }
//    }

}
