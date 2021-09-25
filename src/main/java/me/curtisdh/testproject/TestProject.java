package me.curtisdh.testproject;

import org.bukkit.plugin.java.JavaPlugin;

public final class TestProject extends JavaPlugin
{
    public static TestProject Instance;
    private Integer requiredXp = 7;
    private Integer maxEnchantLevel = 10;
    private Integer _maxCost = 150;

    @Override
    public void onEnable()
    {
        PrintWithClassName(this, "Starting...");
        Instance = this;
        LoadConfig();
        SetupAnvilEvents();
        SetupCommands();
        PrintWithClassName(this, "Initialised");
    }
    private void SetupCommands()
    {
        PrintWithClassName(this,"Setting up commands...");
        getCommand("reloadConfig").setExecutor(new ReloadCommand());
        PrintWithClassName(this,"command:'reloadConfig' setup successfully");
    }
    public void LoadConfig()
    {
        PrintWithClassName(this, "Checking config");
        saveDefaultConfig();
        String requiredXpMultiplier = getConfig().getString("anvil.settings.requiredXpMultiplier");
        String maxEnchantmentLevel = getConfig().getString("anvil.settings.maxEnchantmentLevel");
        String maxCost = getConfig().getString("anvil.settings.maxCost");
        if (requiredXpMultiplier == null)
        {
            PrintWithClassName(this, "ERROR:requiredXpMultiplier is " + requiredXpMultiplier);
            getConfig().createSection("anvil.settings.requiredXpMultiplier");
            requiredXpMultiplier = requiredXp.toString();
            getConfig().set("anvil.settings.requiredXpMultiplier", requiredXp);
            PrintWithClassName(this, "Setting value automatically");
        }
        if (maxEnchantmentLevel == null)
        {
            PrintWithClassName(this, "ERROR:maxEnchantmentLevel is " + maxEnchantmentLevel);
            getConfig().createSection("anvil.settings.maxEnchantmentLevel");
            maxEnchantmentLevel = maxEnchantLevel.toString();
            getConfig().set("anvil.settings.maxEnchantmentLevel", maxEnchantLevel);
            PrintWithClassName(this, "Setting value automatically");
        }
        if (maxCost == null)
        {
            PrintWithClassName(this, "ERROR:maxCost is " + maxCost);
            getConfig().createSection("anvil.settings.maxCost");
            getConfig().set("anvil.settings.maxCost", _maxCost);
            maxCost = _maxCost.toString();
            PrintWithClassName(this, "Setting value automatically");
        }
        saveConfig();
        PrintWithClassName(this, "Loaded config from:" + getDataFolder().getAbsolutePath() + "/config.yml");
        PrintWithClassName(this, "requiredXpMultiplier:" + requiredXpMultiplier);
        PrintWithClassName(this, "maxEnchantmentLevel:" + maxEnchantmentLevel);
        PrintWithClassName(this, "maxCost:" + maxCost);
        onAnvilUseEvent.maxCost = Integer.parseInt(maxCost);
        onAnvilUseEvent.maxEnchantmentLevel = Integer.parseInt(maxEnchantmentLevel);
        onAnvilUseEvent.requiredXpMultiplier = Integer.parseInt(requiredXpMultiplier);
        PrintWithClassName(this, "Done");
    }

    private void SetupAnvilEvents()
    {
        PrintWithClassName(this, "Setting up Anvil Events");
        getServer().getPluginManager().registerEvents(new onAnvilUseEvent(), this);
        PrintWithClassName(this, "Done");
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }

    public static void PrintWithClassName(Object ClassObject, String str)
    {
        String response = ClassObject.getClass().getName() + "::" + str;
        System.out.println(response);
    }
}
