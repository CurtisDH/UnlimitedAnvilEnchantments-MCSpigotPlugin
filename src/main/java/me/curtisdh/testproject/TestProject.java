package me.curtisdh.testproject;

import org.bukkit.plugin.java.JavaPlugin;

public final class TestProject extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        GenericCommands gc = new GenericCommands();
        PrintWithClassName(this,"Starting...");
        //getServer().getPluginManager().registerEvents(new onBlockBreakEvent(),this); // old
        getServer() .getPluginManager().registerEvents(new onAnvilUseEvent(),this);
        this.getCommand("heal").setExecutor(gc); //TODO figure out permissions
        this.getCommand("food").setExecutor(gc);
        this.getCommand("top").setExecutor(gc);
        this.getCommand("SetXPLevel").setExecutor(gc);
        PrintWithClassName(this,"Initialised");
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
    public static void PrintWithClassName(Object ClassObject,String str)
    {
        String response = str;
        response = ClassObject.getClass().getName()+"::"+str;
        System.out.println(response);
    }
}
