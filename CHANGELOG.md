# Changelog

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
