package me.curtisdh.testproject;

import org.bukkit.plugin.java.JavaPlugin;

public final class TestProject extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        PrintWithClassName(this,"Starting...");
        getServer() .getPluginManager().registerEvents(new onAnvilUseEvent(),this);
        PrintWithClassName(this,"Initialised");
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
    public static void PrintWithClassName(Object ClassObject,String str)
    {
        String response = ClassObject.getClass().getName()+"::"+str;
        System.out.println(response);
    }
}
