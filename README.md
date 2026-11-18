# The Titans Port (1.12.2)

19 gigantic Titan bosses, their minions, giant weapons, titan armor, new ores and dimensions — The Titans brought to **Minecraft 1.12.2 on Cleanroom Loader**.
Requires [ArchaicLib](https://github.com/ElectrifiedDed/ArchaicLib) 1.5.1+.

> **This is not a re-upload.** The mod was created by **Enderman_of_D00M** for Minecraft 1.7.10. This repository contains only the port's own code. It ships **none of the original textures, sounds or texts** — they are read at runtime from your own copy of the original mod.

## Original mod (required for textures and sounds)

**Download: [The Titans 1.7.10, version 0.5 beta](https://www.curseforge.com/minecraft/mc-mods/the-titans-mod/download/3828537)** — file `The Titans MC 1.7.10 version-0.5 beta v0712.jar`.

1. Download the original 1.7.10 jar from the link above.
2. Put it in `<minecraft folder>/archaic_originals/` (create the folder). **Not** in `mods/` — a 1.7.10 jar cannot be loaded as a mod in 1.12.2.
3. Start the game. ArchaicLib serves textures, sounds and translations straight from that jar; nothing is extracted or copied.

If the jar is missing, a window on the main menu shows the link and the exact folder.

## Installation

1. Minecraft 1.12.2 + [Cleanroom Loader](https://github.com/CleanroomMC/Cleanroom) (Java 21+).
2. Put `archaiclib-1.5.1.jar` and `thetitans-1.0.0.jar` into `mods/`.
3. Put the original jar into `archaic_originals/` (see above). A dedicated server does not need it.

## Features

- 19 Titans (giant versions of the vanilla mobs and bosses, up to Witherzilla) with multi-part hitboxes, attack animations and custom boss bars.
- Minions with ranks, titan spawn eggs, Titan Spirits and corpses.
- Giant weapons (Ultima Blade, Optima Axe, …), titan armor, tools and armor for many new metals and gems.
- New ores and blocks, the Void and the Nowhere dimensions.

## Building

Needs a sibling folder `../ArchaicLib` (clone of [ArchaicLib](https://github.com/ElectrifiedDed/ArchaicLib)) built beforehand.

```
cd ../ArchaicLib && ./gradlew build
cd ../TheTitans && ./gradlew build
```

## License

HardEvolution Restricted Source-Available License (HE-RSAL) 1.1 — see [LICENSE.en.md](LICENSE.en.md) and [LICENSE.ru.md](LICENSE.ru.md) (the Russian version prevails).
The original The Titans and all its assets belong to their author, Enderman_of_D00M; this project neither contains nor modifies them.

Author: **electrifiedded** / [HardEvolution](https://github.com/HardEvolution-team) · [Discord](https://discord.gg/92JTPmfGSx)
