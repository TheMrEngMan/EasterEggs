package EasterEggs;

import org.bukkit.plugin.java.*;
import org.bukkit.command.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import PlaceholderAPIHook.*;
import java.util.*;
import org.bukkit.*;

public class Main extends JavaPlugin
{
    private static Main m;
    private String prefix;
    private String commandPermission;
    private String collectPermission;
    private String topCommandPermission;
    private String topChatFormat;
    private int topValue;
    private boolean finalCommandsEnabled;
    private List<String> finalCommands;
    private boolean messagesEnabled;
    private String claimedMessage;
    private String alreadyClaimedMessage;
    private String noCollectPermissionMessage;
    private List<String> enabledAnimations;
    
    public void onEnable() {
        (Main.m = this).loadSettings(true);
        this.getCommand("eastereggs").setExecutor((CommandExecutor)new Commands());
        Bukkit.getPluginManager().registerEvents((Listener)new EggHandler(), this);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ClipPlaceholder().load();
        }
    }
    
    public void onDisable() {
    }
    
    public void loadSettings(final boolean startup) {
        this.enabledAnimations = new ArrayList<String>();
        this.getConfig().options().copyDefaults(true);
        this.getConfig().addDefault("Prefix", "&7[&bEasterEggs&7]");
        this.getConfig().addDefault("CommandPermission", "eastereggs.admin");
        this.getConfig().addDefault("TopCommandPermission", "eastereggs.top");
        this.getConfig().addDefault("TopValue", 10);
        this.getConfig().addDefault("TopChatFormat", "%prefix% &f%place%&7. &c%player% &7Eggs Found: &f%eggs%");
        this.getConfig().addDefault("FinalCommandsEnabled", false);
        this.getConfig().addDefault("FinalCommands", Arrays.asList("say &e%player% &7found every egg!"));
        this.getConfig().addDefault("EggFoundAnimation.Firework", false);
        this.getConfig().addDefault("Messages.Enabled", false);
        this.getConfig().addDefault("Messages.Claimed", "%prefix% &aYou claimed this egg!");
        this.getConfig().addDefault("Messages.AlreadyClaimed", "%prefix% &cYou already claimed this egg!");
        this.getConfig().addDefault("Messages.NoCollectPermission", "%prefix% &cSorry, the egg hunt has not started yet!");
        if (startup) {
            this.saveConfig();
        }
        this.reloadConfig();
        if (this.getConfig().getBoolean("EggFoundAnimation.Firework")) {
            this.enabledAnimations.add("Firework");
        }
        this.messagesEnabled = this.getConfig().getBoolean("Messages.Enabled");
        this.claimedMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.Claimed"));
        this.alreadyClaimedMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.AlreadyClaimed"));
        this.noCollectPermissionMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.NoCollectPermission"));
        this.prefix = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Prefix"));
        this.topChatFormat = this.getConfig().getString("TopChatFormat");
        this.topValue = this.getConfig().getInt("TopValue");
        this.finalCommandsEnabled = this.getConfig().getBoolean("FinalCommandsEnabled");
        this.finalCommands = this.getConfig().getStringList("FinalCommands");
        this.commandPermission = this.getConfig().getString("CommandPermission");
        this.collectPermission = this.getConfig().getString("CollectPermission");
        this.topCommandPermission = this.getConfig().getString("TopCommandPermission");
        EggHandler.loadEggs();
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public boolean isMessageEnabled() {
        return this.messagesEnabled;
    }
    
    public String getClaimedMessage() {
        return this.claimedMessage.replace("%prefix%", this.getPrefix());
    }
    
    public String getAlreadyClaimedMessage() {
        return this.alreadyClaimedMessage.replace("%prefix%", this.getPrefix());
    }

    public String getNoCollectPermissionMessage() {
        return this.noCollectPermissionMessage.replace("%prefix%", this.getPrefix());
    }

    public String getCommandPermission() {
        return this.commandPermission;
    }

    public String getCollectPermission() {
        return this.collectPermission;
    }

    public String getTopCommandPermission() {
        return this.topCommandPermission;
    }
    
    public String getTopChatFormat() {
        return this.topChatFormat.replace("%prefix%", this.getPrefix());
    }
    
    public int getTopValue() {
        return this.topValue;
    }
    
    public boolean isFinalCommandsEnabled() {
        return this.finalCommandsEnabled;
    }
    
    public List<String> getFinalCommands() {
        return this.finalCommands;
    }
    
    public List<String> getEnabledAnimations() {
        return this.enabledAnimations;
    }
    
    public static Main getInstance() {
        return Main.m;
    }
}
