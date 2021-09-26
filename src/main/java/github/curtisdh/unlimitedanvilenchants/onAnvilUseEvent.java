package github.curtisdh.unlimitedanvilenchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class onAnvilUseEvent implements Listener
{
    public int requiredXpMultiplier = 7;
    public int maxEnchantmentLevel = 10;
    public int maxCost = 150;

    @EventHandler
    void AnvilEvent(PrepareAnvilEvent event)
    {

        AnvilInventory anvilInventory = event.getInventory();
        Boolean itemIsBook = false;
        //ItemMeta bookMetaData = null;
        Map<Enchantment, Integer> item1Enchants = new HashMap<>();
        Map<Enchantment, Integer> item2Enchants = new HashMap<>();

        if (anvilInventory.getItem(0) != null && anvilInventory.getItem(1) != null)
        {
            Material item1Mat = anvilInventory.getItem(0).getType();
            Material item2Mat = anvilInventory.getItem(1).getType();
            //TestProject.PrintWithClassName(this, item1Mat.toString());
            if (item1Mat != item2Mat && !item2Mat.toString().equalsIgnoreCase("ENCHANTED_BOOK")) //TODO rework this to allow books to enchant
            {
                UnlimitedAnvilEnchants.PrintWithClassName(this, "HELLO");
                return;
            }

            //getEnchantments doesnt work on enchanted books for whatever reason
            if (IsItemEnchantedBook(item1Mat))
            {
                EnchantmentStorageMeta itemMeta1 = (EnchantmentStorageMeta) anvilInventory.getItem(0).getItemMeta();
                item1Enchants = itemMeta1.getStoredEnchants();
                //Cant take books out of the anvil. I think it has something to do with the meta data but this doesnt work.
//                for(Map.Entry<Enchantment,Integer> enchants: item1Enchants.entrySet())
//                {
//                   itemMeta1.removeStoredEnchant(enchants.getKey());
//                }
                //bookMetaData = itemMeta1;
                if(item1Enchants.isEmpty())
                {
                    item1Enchants = anvilInventory.getItem(0).getEnchantments();
                }
                System.out.println(item1Enchants);
            } else
            {
                item1Enchants = anvilInventory.getItem(0).getEnchantments();
            }
            if (IsItemEnchantedBook(item2Mat))
            {
                EnchantmentStorageMeta itemMeta2 = (EnchantmentStorageMeta) anvilInventory.getItem(1).getItemMeta();
                item2Enchants = itemMeta2.getStoredEnchants();
                if(item2Enchants.isEmpty())
                {
                    item2Enchants = anvilInventory.getItem(1).getEnchantments();
                }
                System.out.println(item2Enchants);

            } else
            {
                item2Enchants = anvilInventory.getItem(1).getEnchantments();
            }
            Map<Enchantment, Integer> newEnchantments = new HashMap<>();
            //Copy all the enchantments to a new map
            for (Map.Entry<Enchantment, Integer> all : item1Enchants.entrySet())
            {
                System.out.println("Item1Enchants");
                newEnchantments.put(all.getKey(), all.getValue());
            }
            //check if they have the same enchantment, if so add one to the enchantment
            for (Map.Entry<Enchantment, Integer> all : item2Enchants.entrySet())
            {
                System.out.println("Item2Enchants");

                if (newEnchantments.containsKey(all.getKey()))
                {
                    System.out.println("matching");
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
                            newEnchantments.put(all.getKey(), all.getValue() + 1);
                            anvilInventory.setMaximumRepairCost(((all.getValue() + 1) * requiredXpMultiplier) * 2);
                            anvilInventory.setRepairCost((all.getValue() + 1) * requiredXpMultiplier);
                        }
                    }
                } else // doesnt contain the current enchantment so lets add it
                {
                    newEnchantments.put(all.getKey(), all.getValue());
                }
            }
            //create a new item based on the previously acquired type
            ItemStack resultingItem = new ItemStack(item1Mat);

            //Testing data to see if we can use the book if we maintain the NBT
            //It does work but only for the original enchantment..
//            if(bookMetaData != null)
//            {
//                resultingItem.setItemMeta(bookMetaData);
//            }

            //Rename the item to whatever the user inputs.
            String renameText = event.getInventory().getRenameText();
            if (!(renameText.isEmpty()))
            {
                ItemMeta m = resultingItem.getItemMeta();
                m.setDisplayName(renameText);
                resultingItem.setItemMeta(m);
            }
            //System.out.println(resultingItem.getItemMeta());

            //enchant the newly created item
            resultingItem.addUnsafeEnchantments(newEnchantments);
            //add the new item to the anvil result
            event.setResult(resultingItem);
        }
    }

    private boolean IsItemEnchantedBook(Material itemMaterial)
    {
        if (itemMaterial.toString().equalsIgnoreCase("ENCHANTED_BOOK"))
        {
            return true;
        }
        return false;
    }
}
