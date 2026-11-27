# Changelog

## 1.2.7
### Fixed
- Crash (ArrayIndexOutOfBoundsException in WorldGenVein) when an ore vein reached below Y 0 or above Y 255, e.g. when entering the Void Island dimension.

## 1.2.6
### Performance
- Titan ore veins in chunks that are still being generated are written directly into the chunk instead of through the world (no lighting or neighbour updates). Combined with 1.2.5 this roughly halves the cost of titan ore generation.

## 1.2.5
### Performance
- Titan ore veins read blocks from a cached chunk instead of the world for every block, and no longer load neighbouring chunks during generation. Generating new terrain is noticeably cheaper.

## 1.2.4
### Fixed
- Naturally spawned titans no longer carve a pit at world 0,0 (near spawn): the spawn-clearing ran on the titan's box before it was moved to its spawn point, so every worldgen titan dug out a shaft/room at the origin.

## 1.2.3
### Changed
- The original mod file is now downloaded automatically on first start (needs ArchaicLib 1.42.0+): sources and mirrors are listed in `config/archaiclib/originals/` and can be edited there. If the download fails, a banner on the main menu links to the manual download.

## 1.2.2
### Changed
- Performance: uses ArchaicLib 1.40.0 FastRandom instead of java.util.Random, plus the 1.40.0 cached block-state / allocation-free legacy world calls. Requires ArchaicLib 1.40.0+.

## 1.2.1
### Performance
- Less worldgen lag: titan ore-vein generation no longer recomputes per-block data inside its inner loop.

## 1.2.0
### Fixed
- Dying titans revived themselves: after 800 death ticks the max health shrinks with the titan and the max-health sync healed it to full every tick ("has been defeated" again and again; the Mutant Zombie Titan never died). Health now keeps its fraction and dead titans are never healed.
- Titan loot (item bombs) rendered 4-8x smaller than in 1.7.10.
- Server stalls of seconds when a titan teleported or flew into unexplored terrain: titan chunk loading no longer generates new chunks and only (un)forces the chunks that changed.
- Creeper Titan death blast wrecked up to 1536x1536x256 blocks in one tick (minutes-long freeze); now a budgeted shock wave.
- Less cascading worldgen lag from titan structures.
- Giant bones and other big entities: resting ones skip collision scans.

Mutant Titans. Requires ArchaicLib 1.22.0+. Optional: Mutant Creatures Legacy (modid `mutantbeasts`).

### Added
- Mutant Skeleton Titan falls apart on death into 18 giant bones (like the mutant): they fly out, crush what they hit and lie around for 30 minutes; right click a bone to take it as a Titanic Mutant Skeleton pelvis / rib / skull / limb / shoulder pad. Crafting as with the mutant's bones: rib cage, arms, then **Titanic Mutant Skeleton armour** (skull = helmet, chestplate, leggings, boots; between Withrenium and Titanus, night vision, strength + resistance, speed legs, jump boots without fall damage, full set: regeneration, fire resistance, absorption). One titan = one full set.
- Mutant Creeper Titan detonates on death in a 10000 x 10000 block crater down to bedrock (liquids included), carved as a shock wave by ArchaicLib's ChunkBlast without dropping TPS (10000x10000 in ~3 s on a test world).
- Every mutant titan has its own death and wake-up choreography (instead of just tipping over and standing up):
  - Zombie: sleeps face down; claws scrape the ground, plants its fists, heaves itself up, drags a knee under, stands and pounds its chest roaring. Dies staggering back with a howl, drops to its knees, keels over on its face and twitches.
  - Skeleton: sleeps as a scattered heap of bones that rattles, pulls its legs together, stacks its spine vertebra by vertebra, arms and skull snap back into place, cracks its neck and raises its crossbow. Dies with its ribs bursting open and skull thrown back before it falls apart into bones.
  - Creeper: sleeps curled up; vents flicker, sniffs around, rises front legs first, shakes itself off like a wet dog and hisses. Dies in convulsions, slumps onto its side twitching, then staggers up and bloats before the blast.
  - Ender Colossus: dies screaming with all four arms flung wide, sinks to its knees clutching its skull, topples on its back, fingers twitching.
- Mutant Skeleton Titan: new **giga volley** - braces, levels the crossbow and fires 7 fans of 3 giant arrows in a row (every 8 ticks), every shot kicks it back. The leaping multi-shot now fires 3 bolts at once like the mutant's crossbow.
- Mutant Ender Colossus: the block barrage throws **giant spinning blocks** of terrain (size scales with the colossus, the 4th one double size) on a ballistic arc that explode and send out a shock wave on impact.
- Mutant Zombie Titan: the roar now **calls up a titan horde** - 2 Mutant Zombie Titans and 3 Zombie Titans claw their way out of the ground around it and join the fight. Called-up titans drop no loot or xp, spawn no minions and can't call a horde themselves; a new horde comes only once the last one is dead (2 min cooldown). Zombie titans no longer hit mutant zombie titans.
- With Mutant Creatures Legacy installed, four Mutant Titans: **Mutant Zombie Titan, Mutant Skeleton Titan, Mutant Creeper Titan, Mutant Ender Colossus** (spawn eggs in the Spawn Eggs tab, `/summon thetitans:MutantZombieTitan` etc.).
- Health, strength and body size follow the real mutant-to-vanilla ratios read from the installed mod's config (by default: x7.5 health for zombie/skeleton/creeper, x5 for the enderman; Mutant Zombie Titan 72x157 blocks vs 24x96).
- Each one keeps the full move set of its titan and acts it out on the mutant body, plus the mutant's own moves:
  - Zombie: seismic hammer-fists (a wave of ground ruptures), grab &amp; hurl, summoning roar (hunger, slowness, zombie horde), three lives (falls and gets back up with 1/3.75 health unless burning).
  - Skeleton: rib-cage constriction (opens its ribs, swallows and crushes the victim), leaping multi-shot, charged piercing bolt from the rib crossbow.
  - Creeper: pounce that detonates on landing, lightning charge-up (becomes charged below half health), creeper-minion brood.
  - Ender Colossus: four-armed block barrage, tele-smash (lifts the victim into the sky and slams it down), scream (nausea, blindness, weakness), clone swarm + blink.
- Models are the Mutant Creatures rigs with extra titan bones (jaw, claws, spikes, horns, tusks, tail, a fourth rib segment...). Textures for the new bones are painted at runtime from the installed mod's textures; titan variants (jungle/blood/radioactive/void, wither skeleton, hell/ghost/ender creeper, holy/icy colossus) are recoloured. Sounds come from the installed mod. Nothing from Mutant Creatures is copied into this jar.
- `/titanbench mutant`: server test that spawns the mutant titans with a target and forces every mutant move.
- Boss bars of titan subclasses fall back to their parent titan's bar.

## 1.1.0
Performance update. Requires ArchaicLib 1.5.1+.

### Fixed
- Server freeze: an explosion next to a titan could stall the server for up to a minute (watchdog crash). Explosion exposure for huge bodies is now sampled on a small grid.
- Executor Dragon / Witherzilla no longer load or generate chunks up to 100 blocks away while picking lightning spots.

### Performance
- Dormant titans standing on the ground run their full tick only once per 10 ticks (config: "Dormant Titans Fast Tick", on by default). Sleeping titans are now 4-30x cheaper.
- Titan body parts share one entity search per tick instead of 2-3 per part.
- Ghast minions and Ghast guards no longer scan a 200-block cube every tick looking for their titan; their path check is much cheaper.
- Blocks smashed by titans drop their loot as merged stacks: same items, far fewer item entities on the ground.
- Blaze Titan particles are no longer processed on the server.

### Added
- `/titanbench <dormant|awake|none|profile> [ticks] [copies]` (op): built-in performance test that spawns every titan type and reports server tick time per entity type.
