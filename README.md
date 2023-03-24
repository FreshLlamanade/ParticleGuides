# ParticleGuides

ParticleGuides is a Minecraft Spigot plugin that introduces two new commands, /guide and /breadcrumbs, to create trails
of colorful particles to guide players to specific targets and back to where they came.

## Commands

- `/guide` - creates a trail of particles in front of the player to guide them to a specified target. The target can be
a set of coordinates, the player's last death location, or another player on the server. 
- `/breadcrumbs` - leaves a trail of particles behind the player as they walk, guiding them back to their starting point
or out of a deep cave. The number of particles dropped can be limited to conserve resources.

Both commands accept an optional color argument to set the color of the particles, otherwise the color will be randomly 
chosen from colors specified in the config.yml file.

## Permissions

The following permissions are available to control access to the plugin:

- `particleguides.guide.coords` - Allows access to `/guide coords` to specify coordinates as a target.
- `particleguides.guide.death` - Allows access to `/guide death` to specify the player's last death location as a target.
- `particleguides.guide.here` - Allows access to `/guide here` to specify the player's current location as a target.
- `particleguides.guide.home` - Allows access to `/guide home` to specify one of the player's Essentials homes as a target.
- `particleguides.guide.player` - Allows access to `/guide player` to specify another player as a target.
- `particleguides.guide` - Allows full access to the `/guide` command and all subcommands.
- `particleguides.guide.<limit>` - Allows the creation of `<limit>` guides at a time.
- `particleguides.guide.*` - Allows the creation of an unlimited number of guides at a time.
- `particleguides.breadcrumbs.<limit>` - Allows access to /breadcrumbs with a limit of `<limit>` dropped particles.
- `particleguides.breadcrumbs.*` - Allows unlimited use of /breadcrumbs.

## Configuration

Particle size, speed, density, and color options are all configurable via the config.yml file and in-game through the 
`/particleguides config` command.

## Quick Installation Guide

1. Download the latest release from the [releases page](https://github.com/FreshLlamanade/ParticleGuides/releases).
2. Place the ParticleGuides.jar file in your server's plugins folder.
3. Restart your server.
ParticleGuides is now installed and ready to use!

## Support

If you encounter any issues with ParticleGuides or have any feature requests, please submit an issue on the 
[GitHub repository](https://github.com/FreshLlamanade/ParticleGuides).
