package PlaceholderAPIHook;

import me.clip.placeholderapi.expansion.*;
import org.bukkit.entity.*;
import EasterEggs.*;

public class ClipPlaceholder extends PlaceholderExpansion
{
    public void load() {
        this.register();
    }
    
    public boolean persist() {
        return true;
    }
    
    public boolean canRegister() {
        return true;
    }
    
    public String getIdentifier() {
        return "eastereggs";
    }
    
    public String getAuthor() {
        return Main.getInstance().getDescription().getAuthors().toString();
    }
    
    public String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }
    
    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (player == null) {
            return "";
        }
        if (identifier.equalsIgnoreCase("eggsfound")) {
            return String.valueOf(EggHandler.getEggsFound(player));
        }
        if (identifier.equalsIgnoreCase("totaleggs")) {
            return String.valueOf(EggHandler.getTotalEggs());
        }
        return null;
    }
}
