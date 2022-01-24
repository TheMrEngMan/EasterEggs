## EasterEggs
Minecraft Spigot plugin for easter egg hunt, decompiled from https://www.spigotmc.org/resources/eastereggs-1-7-1-16.39523/ using Luyten.

This version:
* Fixes Easter eggs not loading when Multiverse is used
* Fixes textures not being applied to the Easter egg player heads in Minecraft 1.17+ (note that as a compromise, all player heads placed down [always face the same direction](https://github.com/deanveloper/SkullCreator/issues/18); since all the textures are symmetrical, this is not a huge problem)
* Makes it so that players without `eastereggs.admin` permission cannot break Easter eggs that have been placed (by players with that permission).<br>

(MVdWPlaceholderAPI support is not in this version since it would not compile)  