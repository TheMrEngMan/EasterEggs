package EasterEggs;

import org.bukkit.entity.*;
import io.netty.util.internal.*;
import org.bukkit.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import org.bukkit.inventory.meta.*;

public class Animation
{
    public static void play(final Location location) {
        if (Main.getInstance().getEnabledAnimations().contains("Firework")) {
            final Firework firework = (Firework)location.getWorld().spawn(location.clone().add(0.5, 0.0, 0.5), (Class)Firework.class);
            final FireworkMeta fireworkMeta = firework.getFireworkMeta();
            final FireworkEffect.Type fireworkType = FireworkEffect.Type.values()[ThreadLocalRandom.current().nextInt(FireworkEffect.Type.values().length)];
            final Color fireworkColor = Color.fromRGB(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
            final Color fireworkFadeColor = Color.fromRGB(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
            fireworkMeta.addEffect(FireworkEffect.builder().flicker(ThreadLocalRandom.current().nextBoolean()).trail(ThreadLocalRandom.current().nextBoolean()).with(fireworkType).withFade(fireworkFadeColor).withColor(fireworkColor).build());
            fireworkMeta.setPower(1);
            firework.setFireworkMeta(fireworkMeta);
            new BukkitRunnable() {
                public void run() {
                    firework.detonate();
                }
            }.runTaskLater((Plugin)Main.getInstance(), 5L);
        }
    }
}
