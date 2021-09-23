package me.curtisdh.testproject;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class onAnvilUseEvent implements Listener
{
    int requiredXpMultiplier = 7;
    int maxEnchantmentLevel = 10;
    int maxCost = 150;
    @EventHandler
    void AnvilEvent(PrepareAnvilEvent event)
    {

        AnvilInventory anvilInventory = event.getInventory();
        String renameText = anvilInventory.getRenameText();
        TestProject.PrintWithClassName(this, "RenameText:" + renameText);
        if (anvilInventory.getItem(0) != null && anvilInventory.getItem(1) != null)
        {
            Material item1Mat = anvilInventory.getItem(0).getType();
            Material item2Mat = anvilInventory.getItem(1).getType();
            if (item1Mat != item2Mat)
            {
                return;
            }

            Map<Enchantment, Integer> item1Enchants = anvilInventory.getItem(0).getEnchantments();
            TestProject.PrintWithClassName(this,item1Enchants.toString());
            Map<Enchantment, Integer> item2Enchants = anvilInventory.getItem(1).getEnchantments();
            Map<Enchantment,Integer> newEnchantments = new HashMap<>();
            //Copy all the enchantments to a new map
            for(Map.Entry<Enchantment, Integer> all: item1Enchants.entrySet())
            {
                newEnchantments.put(all.getKey(), all.getValue());
            }
            //check if they have the same enchantment, if so add one to the enchantment
            for(Map.Entry<Enchantment, Integer> all: item2Enchants.entrySet())
            {
                if(newEnchantments.containsKey(all.getKey()))
                {
                    //only adds if matching currently, this seems to be okay for balancing aswell
                    if(newEnchantments.get(all.getKey()) == all.getValue() )
                    {
                        if(all.getValue() + 1 > maxEnchantmentLevel)
                        {
                            newEnchantments.put(all.getKey(),all.getValue());
                            anvilInventory.setMaximumRepairCost(maxCost);
                            anvilInventory.setRepairCost(maxCost);
                        }
                        else
                        {
                            newEnchantments.put(all.getKey(),all.getValue() + 1);
                            anvilInventory.setMaximumRepairCost(((all.getValue()+1)*requiredXpMultiplier)*2);
                            anvilInventory.setRepairCost((all.getValue()+1)*requiredXpMultiplier);
                        }
                    }
                }
                else // doesnt contain the current enchantment so lets add it
                {
                    newEnchantments.put(all.getKey(),all.getValue());
                }
            }
            //create a new item based on the previously acquired type
            ItemStack resultingItem = new ItemStack(item1Mat);
            //enchant the newly created item
            resultingItem.addUnsafeEnchantments(newEnchantments);
            //add the new item to the anvil result

            event.setResult(resultingItem);


        }
    }
}
