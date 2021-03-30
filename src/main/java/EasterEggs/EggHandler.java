package EasterEggs;

import org.bukkit.entity.*;
import java.io.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.command.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import java.util.*;
import com.mojang.authlib.*;
import com.mojang.authlib.properties.*;
import java.lang.reflect.*;

public class EggHandler implements Listener
{
    private static Map<Player, List<EggLocation>> unclaimedEggs;
    private static List<EggLocation> availableEggs;
    private static Class<?> tileEntityClass;
    private static Class<?> blockPositionClass;
    private static int mcVersion;
    private static int versionId;
    private final Map<String, Long> playerDelayTime;
    
    public EggHandler() {
        this.playerDelayTime = new HashMap<String, Long>();
    }
    
    public static void loadEggs() {
        EggHandler.availableEggs = new ArrayList<EggLocation>();
        try {
            final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            EggHandler.mcVersion = Integer.parseInt(version.replaceAll("[^0-9]", ""));
            try {
                EggHandler.tileEntityClass = Class.forName("net.minecraft.server." + version + ".TileEntitySkull");
                if (EggHandler.mcVersion > 174) {
                    EggHandler.blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
                }
                else {
                    EggHandler.blockPositionClass = null;
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        final File playerFolders = new File(Main.getInstance().getDataFolder(), "playerEggs");
        if (!playerFolders.exists()) {
            playerFolders.mkdirs();
        }
        for (final String egg : Main.getInstance().getConfig().getStringList("EggLocations")) {
            final String world = egg.split(":")[0];
            final String x = egg.split(":")[1];
            final String y = egg.split(":")[2];
            final String z = egg.split(":")[3];
            final String command = egg.replaceFirst(world + ":" + x + ":" + y + ":" + z + ":", "");
            final World bukkitWorld = Bukkit.getWorld(world);
            if (bukkitWorld != null) {
                final EggLocation location = new EggLocation(bukkitWorld, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z), command);
                EggHandler.availableEggs.add(location);
            }
        }
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            loadPlayerEggs(onlinePlayer);
        }
    }
    
    private static void loadPlayerEggs(final Player player) {
        EggHandler.unclaimedEggs.remove(player);
        final File playerFolder = new File(Main.getInstance().getDataFolder(), "playerEggs/" + player.getUniqueId().toString());
        if (!playerFolder.exists()) {
            final List<EggLocation> eggLocations = new ArrayList<EggLocation>();
            eggLocations.addAll(EggHandler.availableEggs);
            EggHandler.unclaimedEggs.put(player, eggLocations);
            try {
                playerFolder.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            final List<EggLocation> eggLocations = new ArrayList<EggLocation>();
            eggLocations.addAll(EggHandler.availableEggs);
            EggHandler.unclaimedEggs.put(player, eggLocations);
            final FileConfiguration playerConfiguration = YamlConfiguration.loadConfiguration(playerFolder);
            for (final String egg : playerConfiguration.getStringList("EggLocations")) {
                final String world = egg.split(":")[0];
                final String x = egg.split(":")[1];
                final String y = egg.split(":")[2];
                final String z = egg.split(":")[3];
                final String command = egg.replaceFirst(world + ":" + x + ":" + y + ":" + z + ":", "");
                final World bukkitWorld = Bukkit.getWorld(world);
                if (bukkitWorld != null) {
                    final EggLocation location = new EggLocation(bukkitWorld, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z), command);
                    EggHandler.unclaimedEggs.get(player).remove(location);
                }
            }
        }
    }
    
    public void unloadPlayerEgg(final EggLocation eggLocation) {
        for (final File file : new File(Main.getInstance().getDataFolder(), "/playerEggs").listFiles()) {
            final FileConfiguration playerConfiguration = YamlConfiguration.loadConfiguration(file);
            final List<String> playerEggs = playerConfiguration.getStringList("EggLocations");
            final String path = eggLocation.getWorld().getName() + ":" + eggLocation.getBlockX() + ":" + eggLocation.getBlockY() + ":" + eggLocation.getBlockZ() + ":" + eggLocation.getCommand();
            playerEggs.remove(path);
            playerConfiguration.set("EggLocations", playerEggs);
            try {
                playerConfiguration.save(file);
                playerConfiguration.load(file);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @EventHandler
    public void loadEggs(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        loadPlayerEggs(player);
    }
    
    @EventHandler
    public void unloadEggs(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        EggHandler.unclaimedEggs.remove(player);
    }
    
    @EventHandler
    public void unloadEggs(final PlayerKickEvent e) {
        final Player player = e.getPlayer();
        EggHandler.unclaimedEggs.remove(player);
    }
    
    @EventHandler
    public void interact(final PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            boolean cancelled = false;
            if (EggHandler.versionId >= 9) {
                if (e.getHand() != null) {
                    if (!e.getHand().name().equals("HAND")) {
                        cancelled = true;
                    }
                }
                else {
                    cancelled = true;
                }
            }
            if (cancelled) {
                return;
            }
            final Location location = e.getClickedBlock().getLocation();
            EggLocation eggLocation = null;
            for (final EggLocation egg : EggHandler.availableEggs) {
                if (egg.getBlock().getLocation().equals((Object)location)) {
                    eggLocation = egg;
                }
            }
            if (eggLocation == null) {
                return;
            }


            if (!player.hasPermission(Main.getInstance().getCollectPermission())) {
                player.sendMessage(Main.getInstance().getNoCollectPermissionMessage());
                return;
            }

            final File playerFolder = new File(Main.getInstance().getDataFolder(), "playerEggs/" + player.getUniqueId().toString());
            final FileConfiguration playerConfiguration = YamlConfiguration.loadConfiguration(playerFolder);
            if (EggHandler.unclaimedEggs.get(player).contains(eggLocation)) {
                final String commands = eggLocation.getCommand().replace("%player%", player.getName()).replace("%totaleggs%", String.valueOf(EggHandler.availableEggs.size())).replace("%eggsfound%", String.valueOf(playerConfiguration.getStringList("EggLocations").size() + 1));
                for (String command : commands.replace("|", "%#%").split("%#%")) {
                    if (command.startsWith(" ")) {
                        command = command.replaceFirst(" ", "");
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
                if (EggHandler.unclaimedEggs.get(player).size() == 1 && Main.getInstance().isFinalCommandsEnabled()) {
                    for (final String command2 : Main.getInstance().getFinalCommands()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', command2.replace("%player%", player.getName())));
                    }
                }
                final List<EggLocation> eggLocationsList = EggHandler.unclaimedEggs.get(player);
                eggLocationsList.remove(eggLocation);
                EggHandler.unclaimedEggs.put(player, eggLocationsList);
                this.playerDelayTime.put(player.getUniqueId().toString(), System.currentTimeMillis());
                Animation.play(eggLocation);
                if (Main.getInstance().isMessageEnabled()) {
                    player.sendMessage(Main.getInstance().getClaimedMessage().replace("%totaleggs%", String.valueOf(EggHandler.availableEggs.size())).replace("%eggsfound%", String.valueOf(playerConfiguration.getStringList("EggLocations").size() + 1)));
                }
                final List<String> eggLocations = playerConfiguration.getStringList("EggLocations");
                final String world = eggLocation.getWorld().getName();
                final int x = eggLocation.getBlock().getX();
                final int y = eggLocation.getBlock().getY();
                final int z = eggLocation.getBlock().getZ();
                eggLocations.add(world + ":" + x + ":" + y + ":" + z + ":" + eggLocation.getCommand());
                playerConfiguration.set("EggLocations", eggLocations);
                try {
                    playerConfiguration.save(playerFolder);
                    playerConfiguration.load(playerFolder);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (!EggHandler.unclaimedEggs.get(player).contains(eggLocation) && EggHandler.availableEggs.contains(eggLocation) && Main.getInstance().isMessageEnabled()) {
                if (this.playerDelayTime.containsKey(player.getUniqueId().toString())) {
                    final long time = this.playerDelayTime.get(player.getUniqueId().toString());
                    if (System.currentTimeMillis() > time + 500L) {
                        player.sendMessage(Main.getInstance().getAlreadyClaimedMessage().replace("%totaleggs%", String.valueOf(EggHandler.availableEggs.size())).replace("%eggsfound%", String.valueOf(playerConfiguration.getStringList("EggLocations").size())));
                        this.playerDelayTime.put(player.getUniqueId().toString(), System.currentTimeMillis());
                    }
                }
                else {
                    this.playerDelayTime.put(player.getUniqueId().toString(), System.currentTimeMillis());
                    player.sendMessage(Main.getInstance().getAlreadyClaimedMessage().replace("%totaleggs%", String.valueOf(EggHandler.availableEggs.size())).replace("%eggsfound%", String.valueOf(playerConfiguration.getStringList("EggLocations").size())));
                }
            }
        }
    }
    
    @EventHandler
    public void placeEggs(final BlockPlaceEvent e) {
        final Player player = e.getPlayer();
        if (player.hasPermission(Main.getInstance().getCommandPermission()) && player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta() instanceof SkullMeta && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().getDisplayName().equals(Main.getInstance().getPrefix() + " Place this skull to create eggs")) {
            String command = player.getItemInHand().getItemMeta().getLore().get(0).replaceFirst("§7Commands§8: ", "");
            command = ChatColor.stripColor(command);
            final List<String> eggLocations = (List<String>)Main.getInstance().getConfig().getStringList("EggLocations");
            final String world = e.getBlock().getWorld().getName();
            final int x = e.getBlock().getX();
            final int y = e.getBlock().getY();
            final int z = e.getBlock().getZ();
            eggLocations.add(world + ":" + x + ":" + y + ":" + z + ":" + command);
            Main.getInstance().getConfig().set("EggLocations", (Object)eggLocations);
            Main.getInstance().saveConfig();
            Main.getInstance().reloadConfig();
            EggHandler.availableEggs.add(new EggLocation(e.getBlock().getWorld(), x, y, z, command));
            for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                loadPlayerEggs(onlinePlayer);
            }
            this.applySkullTexture(RandomSkull.getTexture(), e.getBlock());
            player.sendMessage(Main.getInstance().getPrefix() + " §aEgg has been added!");
        }
    }
    
    @EventHandler
    public void breakEggs(final BlockBreakEvent e) {
        Material blockMaterial = e.getBlock().getType();
        if (blockMaterial != Material.PLAYER_HEAD && blockMaterial != Material.PLAYER_WALL_HEAD && blockMaterial != Material.LEGACY_SKULL) {
            return;
        }
        final Location location = e.getBlock().getLocation();
        EggLocation eggLocation = null;
        for (final EggLocation egg : EggHandler.availableEggs) {
            if (egg.getBlock().getLocation().equals((Object) location)) {
                eggLocation = egg;
            }
        }
        if (eggLocation == null) {
            return;
        }
        if (EggHandler.availableEggs.contains(eggLocation)) {
            final Player player = e.getPlayer();
            if (player.hasPermission(Main.getInstance().getCommandPermission())) {
                EggHandler.availableEggs.remove(eggLocation);
                final List<String> eggLocations = (List<String>)Main.getInstance().getConfig().getStringList("EggLocations");
                eggLocations.remove(eggLocation.getWorld().getName() + ":" + eggLocation.getBlockX() + ":" + eggLocation.getBlockY() + ":" + eggLocation.getBlockZ() + ":" + eggLocation.getCommand());
                Main.getInstance().getConfig().set("EggLocations", (Object)eggLocations);
                Main.getInstance().saveConfig();
                Main.getInstance().reloadConfig();
                this.unloadPlayerEgg(eggLocation);
                for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    loadPlayerEggs(onlinePlayer);
                }
                player.sendMessage(Main.getInstance().getPrefix() + " §aEgg has been removed!");
            }
            else {
                player.sendMessage(Main.getInstance().getPrefix() + " §cSorry, you can't break that!");
                e.setCancelled(true);
            }
        }
    }
    
    public void applySkullTexture(final String texture, final Block block) {
        final Location location = block.getLocation();
        EggLocation eggLocation = null;
        for (final EggLocation egg : EggHandler.availableEggs) {
            if (egg.getBlock().getLocation().equals((Object)location)) {
                eggLocation = egg;
            }
        }
        if (eggLocation == null) {
            throw new IllegalArgumentException("Block must be a skull.");
        }
        try {
            final GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
            profile.getProperties().put("textures", new Property("textures", texture));
            final Object nmsWorld = block.getWorld().getClass().getMethod("getHandle", new Class[0]).invoke(block.getWorld());
            Object tileEntity;
            if (EggHandler.mcVersion <= 174) {
                final Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                tileEntity = EggHandler.tileEntityClass.cast(getTileEntity.invoke(nmsWorld, block.getX(), block.getY(), block.getZ()));
            }
            else {
                final Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", EggHandler.blockPositionClass);
                tileEntity = EggHandler.tileEntityClass.cast(getTileEntity.invoke(nmsWorld, getBlockPositionFor(block.getX(), block.getY(), block.getZ())));
            }
            EggHandler.tileEntityClass.getMethod("setGameProfile", GameProfile.class).invoke(tileEntity, profile);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static Object getBlockPositionFor(final int x, final int y, final int z) {
        Object blockPosition = null;
        try {
            final Constructor<?> cons = EggHandler.blockPositionClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            blockPosition = cons.newInstance(x, y, z);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return blockPosition;
    }
    
    public static int getEggsFound(final Player player) {
        int eggs = -1;
        final File playerFolder = new File(Main.getInstance().getDataFolder(), "playerEggs/" + player.getUniqueId().toString());
        if (playerFolder.exists()) {
            final FileConfiguration playerConfiguration = YamlConfiguration.loadConfiguration(playerFolder);
            eggs = playerConfiguration.getStringList("EggLocations").size();
        }
        return eggs;
    }
    
    public static int getTotalEggs() {
        return EggHandler.availableEggs.size();
    }
    
    static {
        EggHandler.unclaimedEggs = new HashMap<Player, List<EggLocation>>();
        EggHandler.availableEggs = new ArrayList<EggLocation>();
        EggHandler.versionId = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].replace(".", "#").split("#")[1]);
    }
}
