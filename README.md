# PreventBuild

Adds control over preventing the player from performing certain actions that are set in configs. Configs, in turn, have a very flexible structure that allows you to build complex logical conditions describing what actions the mod should prevent.

Original purpose of creating this mod was enhancing experience of hand-building map-arts without using any printers and easy-place mode. But now it's more than just preventing placing and breaking: you can prevent any right- or left-click actions, just make sure there is a suitable condition.

This mod simply cancels the player's actions, so it's entirely client-side.

## How to use the mod

It's almost mandatory to visit the [Wiki](https://github.com/Redheris/PreventBuild/wiki) to understand how to write a config, because there is no other way to write it except by hand.

There are a few files in config/preventbuild/:
- `conditions/` - contains config .cfg files
- `condition_configs.json` - defines which configurations are active by its individual name
- `ore_dictionaries.json` - defines custom ore dictionary lists for using in configs

Once you have created/updated your configs, you should update configs in the game using commands.

*(For developers) Also you can find out how to create and register [your own conditions](https://github.com/Redheris/PreventBuild/wiki/(For-deveolpers)-Condition-Registry) that you will be able to use in the same config files.*

## Commands
- `/pb config list` - display a list of configs with their status
- `/pb config switch` <name> - enable/disable config by name
- `/pb config update` - reload all configs

## Server restrictions of the conditions
Since I'm trying to keep gameplay fair and rules-friendly, I decided to give server owners the ability to restrict
some conditions that they can consider as "cheating-like" actions.

For now, there's only a few of such restrictions. To use them, you need to add the following strings to the server
message sent to players on joining the game:

`§p§b§i§t§e§m§d§a§m§a§g§e` - Disables the "Item Damage" condition

`§p§b§s§t§a§t§e` - Disables the "Block State" condition

`§p§b§a§g§e§s§t§a§t§e` - Disables only the use of the "age" state in the "Block State" condition

`§p§b§r§e§s§e§t`- Resets all restrictions

## Official links to download the mod
- [Modrinth](https://modrinth.com/project/VYOvHxMm)
- This GitHub repository (only source code)

## Contacts
If you have some issues with the mod, any questions or just want to leave feedback, you are always welcome:
- GitHub: https://github.com/Redheris/PreventBuild/issues
- Discord: @redheris
