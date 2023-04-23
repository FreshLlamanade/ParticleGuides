# ParticleGuides

ParticleGuides is a Minecraft Spigot plugin that introduces two new commands, /guide and /breadcrumbs, to create trails
of colorful particles to guide players to specific targets and back to where they came.

ParticleGuides optionally integrates with Essentials homes - if Essentials is running on your server, the `/guide home` 
command will be available for use!

## Commands

- `/guide` - creates a trail of particles in front of the player to guide them to a specified target. The target can be 
a set of coordinates, the player's current location, the player's last death location, one of the player's Essentials 
homes, or even another player on the server.
- `/breadcrumbs` - leaves a trail of particles behind the player as they walk, guiding them back to their starting point
or out of a deep cave. The number of particles dropped can be limited to conserve resources.

Both commands accept an optional color argument to set the color of the particles, otherwise the color will be randomly 
chosen from colors specified in the config.yml file.

## Permissions

The following permissions are available to control access to the plugin:

- `particleguides.guide` - Allows full access to the `/guide` command and all subcommands (default true).
- `particleguides.guide.coords` - Allows access to `/guide coords` to specify coordinates as a target.
- `particleguides.guide.death` - Allows access to `/guide death` to specify the player's last death location as a target.
- `particleguides.guide.here` - Allows access to `/guide here` to specify the player's current location as a target.
- `particleguides.guide.home` - Allows access to `/guide home` to specify one of the player's Essentials homes as a target.
- `particleguides.guide.player` - Allows access to `/guide player` to specify another player as a target.
- `particleguides.guide.player.no-ask` - Allows using `/guide player` without asking the target player for permission (default OP)
- `particleguides.guide.limit.*` - Adjust the maximum number of guides a player can create at once.

- `particleguides.breadcrumbs` - Allows access to `/breadcrumbs` to create a breadcrumb trail behind the player (default true).
- `particleguides.breadcrumbs.limit.*` - Adjust the maximum length of a player's breadcrumb trail.

- `particleguides.admin` - Allows access to `/particleguides` to manage the plugin's configuration (default OP).

## Configuration

The plugin is configurable directly via the `config.yml` file and in-game through the `/particleguides config` command.
The available configuration values are as follows:


- `particle-density`: The density, or opacity, of each particle "dot". The higher the number, the more particles will 
be shown per dot, and the longer the dots will be visible. The default value is 5.

- `highlight-density`: The density, or opacity, of a target being highlighted with particles. The higher the number, the 
more particles will be shown around the target, and the longer the highlighting will be visible. The default value is 20.

- `always-highlight-target`: If true, a puff of particles will be spawned around the target of a guide every time the
guide is shown, regardless of distance away. If false, the puff will only be shown when the player is close enough 
for the guide to reach the target.

- `repeat-delay-millis`: The delay, in milliseconds, between repetitions of the particle effect. The higher the number, the 
less frequently the particles will be shown. The default value is 1500 milliseconds.

- `particle-delay-millis`: The delay, in milliseconds, between each particle being shown. The higher the number, the slower 
the particles will appear to move. The default value is 50 milliseconds, or a speed of 20 blocks per second.

- `guide-length`: The distance, in blocks, that a particle guide will move forward in front of the player. 
The default value is 10 blocks.

- `blocks-per-breadcrumb`: The number of blocks the player must move before another breadcrumb is dropped on their position.
The breadcrumbs will occasionally be dropped closer to one another than this value, if the player moves diagonally.
This is because this value is used to subdivide the world into cubic chunks of this size, and a breadcrumb is
dropped when a player moves from one such chunk to another. The default value is 3 blocks.

- `colors`: The available colors for the particle guides, in the format `name:hex`, where `name` is the name of the
color and `hex` is that RGB color in [hexadecimal format](https://www.rgbtohex.net/).
For example, adding the entry `red: 'FF0000'` will allow the color `red` to be used in the `/guide` and `/breadcrumbs` 
commands, and will appear in-game as a bright red color (RGB 255, 0, 0). A large list of default colors is provided in
the `config.yml` file.

## Quick Installation Guide

1. Download the latest release from the [releases page](https://github.com/FreshLlamanade/ParticleGuides/releases).
2. Place the ParticleGuides.jar file in your server's plugins folder.
3. Restart your server.
ParticleGuides is now installed and ready to use!

## Support

If you encounter any issues with ParticleGuides or have any feature requests, please submit an issue on the 
[GitHub repository](https://github.com/FreshLlamanade/ParticleGuides).
