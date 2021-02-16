package EasterEggs;

import io.netty.util.internal.*;
import java.util.*;

public class RandomSkull
{
    private static List<String> textures;
    
    public static String getTexture() {
        return RandomSkull.textures.get(ThreadLocalRandom.current().nextInt(RandomSkull.textures.size()));
    }
    
    static {
        (RandomSkull.textures = new ArrayList<String>()).add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWViMzM1MTgyZGI1ZjNiZTgwZmNjZjZlYWJlNTk5ZjQxMDdkNGZmMGU5ZjQ0ZjM0MTc0Y2VmYTZlMmI1NzY4In19fQ==");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJjZDVkZjlkN2YxZmE4MzQxZmNjZTJmM2MxMThlMmY1MTdlNGQyZDk5ZGYyYzUxZDYxZDkzZWQ3ZjgzZTEzIn19fQ==");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc2NTk1ZWZmY2M1NjI3ZTg1YjE0YzljODgyNDY3MWI1ZWMyOTY1NjU5YzhjNDE3ODQ5YTY2Nzg3OGZhNDkwIn19fQ==");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGViNWFiYjEyMzUyN2E3YTFmNWM5NTg5NzE1OTY0YjU5Zjc2ODI0OTI2ZDNiOTgyZmE4NDExZDQ2MDZjNzkifX19");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjNkNjliMjNhZTU5MmM2NDdlYjhkY2ViOWRhYWNlNDQxMzlmNzQ4ZTczNGRjODQ5NjI2MTNjMzY2YTA4YiJ9fX0=");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY0NDMwZTQ5M2ZlYjVlYWExNDU1ODJlNTRlNzYxYTg2MDNmYjE2Y2MwZmYxMjY4YTVkMWU4NjRlNmY0NzlmNiJ9fX0=");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThiOWUyOWFiMWE3OTVlMmI4ODdmYWYxYjFhMzEwMjVlN2NjMzA3MzMzMGFmZWMzNzUzOTNiNDVmYTMzNWQxIn19fQ==");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTViOGRjYmVhMjdmNDJmNWFlOTEwNDQ1ZTA1ZGFjODlkMzEwYWFmMjM2YTZjMjEyM2I4NTI4MTIwIn19fQ==");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmI4YjJiZGZjNWQxM2I5ZjQ1OTkwZWUyZWFhODJlNDRhZmIyYTY5YjU2YWM5Mjc2YTEzMjkyYjI0YzNlMWRlIn19fQ==");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg4OWYxMWM4ODM4YzA5ZTFlY2YyZjgzNDM5ZWJjYjlmMzI0ZTU2N2IwZTlkYzRiN2MyNWQ5M2U1MGZmMmIifX19");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM2ZmU4M2M0MmM2Y2U3Mzg2Y2YyMzMzYTFjNTk1ZDBiNDMzZGE3YmM1NTkyYjg2ODY2ODU1MWQ2OWI5YjAifX19");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY0MmFmYTM5Njg1M2I4MWIxN2JlZjVjOGQ3YTQ0YzEyZGU2ODlhNTZhZjQ3NDg0NjY3OTgzOTlkYTNjZmVhZSJ9fX0=");
        RandomSkull.textures.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWYzMjlmMWI0NDhlYWFiMmY5ZjA0NDZmYzBiMjMxZWI3NzUxMzczYWZlNDc0MjZlNDk1MGY3ZDA4NjIwZjA4ZCJ9fX0=");
    }
}
