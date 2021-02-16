package EasterEggs;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import java.io.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.configuration.file.*;

public class Commands implements CommandExecutor
{
    private static int versionId;
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            if (cmd.getName().equalsIgnoreCase("eastereggs") && sender.hasPermission(Main.getInstance().getCommandPermission())) {
                if (args.length == 0) {
                    sender.sendMessage(Main.getInstance().getPrefix() + " §7/eastereggs reload");
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(Main.getInstance().getPrefix() + " §aReloading..");
                    final long startReloadDelay = System.currentTimeMillis();
                    Main.getInstance().loadSettings(false);
                    final long reloadDelay = System.currentTimeMillis() - startReloadDelay;
                    sender.sendMessage(Main.getInstance().getPrefix() + " §aReload complete. §7( Took: " + reloadDelay + " ms )");
                }
            }
            return false;
        }
        final Player player = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("eastereggs")) {
            if (player.hasPermission(Main.getInstance().getTopCommandPermission()) && !player.hasPermission(Main.getInstance().getCommandPermission()) && args.length == 0) {
                player.sendMessage(Main.getInstance().getPrefix() + " §7/eastereggs top");
            }
            if (player.hasPermission(Main.getInstance().getTopCommandPermission()) && args.length == 1 && args[0].equalsIgnoreCase("top")) {
                final Map<File, Integer> eggValues = new HashMap<File, Integer>();
                final Map<Integer, File> places = new HashMap<Integer, File>();
                int placeId = 1;
                final File[] fileList = new File(Main.getInstance().getDataFolder(), "/playerEggs").listFiles();
                Arrays.sort(fileList, new Comparator<File>() {
                    public int compare(final File f1, final File f2) {
                        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                    }
                });
                final List<File> files = Arrays.asList(fileList);
                Collections.reverse(files);
                for (final File file : files) {
                    final FileConfiguration playerConfiguration = YamlConfiguration.loadConfiguration(file);
                    final int size = playerConfiguration.getStringList("EggLocations").size();
                    eggValues.put(file, size);
                }
                for (int id = 1; id <= Main.getInstance().getTopValue(); ++id) {
                    File file = null;
                    for (final Map.Entry<File, Integer> entry : eggValues.entrySet()) {
                        if (file == null) {
                            final int maxValue = Collections.max(eggValues.values());
                            if (entry.getValue() != maxValue) {
                                continue;
                            }
                            file = entry.getKey();
                            places.put(placeId, entry.getKey());
                            ++placeId;
                        }
                    }
                    eggValues.remove(file);
                }
                for (int id = 1; id <= Main.getInstance().getTopValue(); ++id) {
                    if (places.containsKey(id)) {
                        final String uuid = places.get(id).getName();
                        final Player onlinePlayer = Bukkit.getPlayer(UUID.fromString(uuid));
                        String playerName;
                        if (onlinePlayer != null) {
                            playerName = onlinePlayer.getName();
                        }
                        else {
                            playerName = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getTopChatFormat().replace("%place%", String.valueOf(id)).replace("%player%", playerName).replace("%eggs%", String.valueOf(YamlConfiguration.loadConfiguration(places.get(id)).getStringList("EggLocations").size()))));
                    }
                }
            }
            if (player.hasPermission(Main.getInstance().getCommandPermission())) {
                if (args.length == 0) {
                    player.sendMessage(Main.getInstance().getPrefix() + " §7/eastereggs add <Command>");
                    player.sendMessage(Main.getInstance().getPrefix() + " §7/eastereggs top");
                    player.sendMessage(Main.getInstance().getPrefix() + " §7/eastereggs reload");
                    player.sendMessage("");
                    player.sendMessage(Main.getInstance().getPrefix() + " §cBreaking eggs will remove them.");
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    player.sendMessage(Main.getInstance().getPrefix() + " §aReloading..");
                    final long startReloadDelay2 = System.currentTimeMillis();
                    Main.getInstance().loadSettings(false);
                    final long reloadDelay2 = System.currentTimeMillis() - startReloadDelay2;
                    player.sendMessage(Main.getInstance().getPrefix() + " §aReload complete. §7( Took: " + reloadDelay2 + " ms )");
                }
                if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
                    final StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; ++i) {
                        builder.append(args[i] + " ");
                    }
                    final ItemStack itemStack = new ItemStack(Material.valueOf((Commands.versionId >= 13) ? "LEGACY_SKULL_ITEM" : "SKULL_ITEM"), 1, (short)3);
                    final SkullMeta meta = (SkullMeta)itemStack.getItemMeta();
                    meta.setDisplayName(Main.getInstance().getPrefix() + " Place this skull to create eggs");
                    meta.setLore(Arrays.asList("§7Commands§8: " + builder.toString()));
                    itemStack.setItemMeta(meta);
                    final HashMap<Integer, ItemStack> droppedItems = player.getInventory().addItem(new ItemStack[] { itemStack });
                    if (droppedItems.isEmpty()) {
                        player.sendMessage(Main.getInstance().getPrefix() + " §6Item to place eggs has been added to your inventory!");
                    }
                    else {
                        player.sendMessage(Main.getInstance().getPrefix() + " §cYour inventory is too full, make some space for the 'Egg Placer'!");
                    }
                }
            }
        }
        return false;
    }
    
    static {
        Commands.versionId = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].replace(".", "#").split("#")[1]);
    }
}
