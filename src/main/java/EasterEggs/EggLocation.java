package EasterEggs;

import org.bukkit.*;

public class EggLocation extends Location
{
    private final String command;
    
    public EggLocation(final World world, final int x, final int y, final int z, final String command) {
        super(world, x, y, z);
        this.command = command;
    }
    
    public String getCommand() {
        return this.command;
    }
}
