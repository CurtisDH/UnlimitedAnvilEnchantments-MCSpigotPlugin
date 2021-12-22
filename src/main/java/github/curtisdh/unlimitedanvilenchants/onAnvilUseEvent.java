package github.curtisdh.unlimitedanvilenchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class onAnvilUseEvent implements Listener
{
    public int requiredXpMultiplier = 7;
    public int maxEnchantmentLevel = 10;
    public int maxCost = 150;
    private Integer setCost = 0;

    //Used to prevent spamming messages to the player
    long timer = System.currentTimeMillis();
    Integer timerDelay = 10;


    @EventHandler
    void AnvilEvent(PrepareAnvilEvent event)
    {
        AnvilInventory anvilInventory = event.getInventory();
//        String renameText = anvilInventory.getRenameText();
//        TestProject.PrintWithClassName(this, "RenameText:" + renameText);
        if (anvilInventory.getItem(0) != null && anvilInventory.getItem(1) != null)
        {
            Material item1Mat = anvilInventory.getItem(0).getType();
            Material item2Mat = anvilInventory.getItem(1).getType();
            //TestProject.PrintWithClassName(this, item1Mat.toString());
            if (item1Mat != item2Mat)
            {
                return;
            }
            if (item1Mat == item2Mat)
            {
                if (item1Mat.toString().equalsIgnoreCase("ENCHANTED_BOOK"))
                {
                    UnlimitedAnvilEnchants.PrintWithClassName(this, anvilInventory.getItem(0).getItemMeta().toString());
                    return;
                }
            }

            Map<Enchantment, Integer> item1Enchants = anvilInventory.getItem(0).getEnchantments();
            //TestProject.PrintWithClassName(this, item1Enchants.toString());
            Map<Enchantment, Integer> item2Enchants = anvilInventory.getItem(1).getEnchantments();
            Map<Enchantment, Integer> newEnchantments = new HashMap<>();
            //Copy all the enchantments to a new map
            for (Map.Entry<Enchantment, Integer> all : item1Enchants.entrySet())
            {
                newEnchantments.put(all.getKey(), all.getValue());
            }
            //check if they have the same enchantment, if so add one to the enchantment
            for (Map.Entry<Enchantment, Integer> all : item2Enchants.entrySet())
            {
                if (newEnchantments.containsKey(all.getKey()))
                {
                    //only adds if matching currently, this seems to be okay for balancing aswell
                    if (newEnchantments.get(all.getKey()) == all.getValue())
                    {
                        if (all.getValue() + 1 > maxEnchantmentLevel)
                        {
                            newEnchantments.put(all.getKey(), all.getValue());
                            anvilInventory.setMaximumRepairCost(maxCost);
                            anvilInventory.setRepairCost(maxCost);
                        } else
                        {
                            setCost = (all.getValue() + 1) * requiredXpMultiplier;
                            newEnchantments.put(all.getKey(), all.getValue() + 1);
                            anvilInventory.setMaximumRepairCost(((all.getValue() + 1) * requiredXpMultiplier) * 2);
                            anvilInventory.setRepairCost(setCost);
                        }
                    }
                } else // doesnt contain the current enchantment so lets add it
                {
                    newEnchantments.put(all.getKey(), all.getValue());
                }
            }
            //create a new item based on the previously acquired type
            ItemStack resultingItem = new ItemStack(item1Mat);
            //enchant the newly created item
            resultingItem.addUnsafeEnchantments(newEnchantments);
            //add the new item to the anvil result
            //UnlimitedAnvilEnchants.PrintWithClassName(this, "repair cost:" + anvilInventory.getRepairCost());

            if (anvilInventory.getRepairCost() <= 1)
            {
                anvilInventory.setRepairCost(50);
                event.getInventory().setRepairCost(50);

                //Repair cost has to be set on the next tick. The follow accomplishes this.
                Bukkit.getScheduler().runTask(UnlimitedAnvilEnchants.Instance, () ->
                {
                    event.getInventory().setRepairCost(50);
                    event.setResult(resultingItem);
                    for (HumanEntity humanEntity : event.getViewers())
                    {
                        if (humanEntity instanceof Player)
                        {
                            Player player = (((Player) humanEntity).getPlayer());
                            player.updateInventory();
                            //UnlimitedAnvilEnchants.PrintWithClassName(this, "player:" + player);
                        }
                    }
                });
            }

            // quick fix -- Anything above 39 results in "too expensive" when in survival mode
            if(anvilInventory.getRepairCost() >= 40)
            {
                Bukkit.getScheduler().runTask(UnlimitedAnvilEnchants.Instance, () ->
                {
                    event.getInventory().setMaximumRepairCost(Integer.MAX_VALUE);
                    for (HumanEntity humanEntity : event.getViewers())
                    {
                        if (humanEntity instanceof Player)
                        {
                            Player player = (((Player) humanEntity).getPlayer());
                            ChatColor color = ChatColor.GREEN;
                            String MessageToPlayer = "Enchantment Cost:"+setCost;
                            float xpLevel = player.getLevel();
                            if(xpLevel < setCost)
                            {
                                MessageToPlayer = "Insufficient XP | Cost:"+setCost+" You require "+
                                        (setCost-xpLevel) +" more levels";
                                color = ChatColor.RED;
                            }
                            if(System.currentTimeMillis() - timer > timerDelay)
                            {
                                //Reset timer
                                timer = System.currentTimeMillis();

                                player.sendMessage(color + MessageToPlayer);
                                player.updateInventory();
                            }
                        }
                    }
                });
            }

            event.setResult(resultingItem);
        }
    }
}
