# PreventBuild

Adds control over preventing the player from performing certain actions that are set in configs. Configs, in turn, have a very flexible structure that allows you to build complex logical conditions describing what actions the mod should prevent.

Original purpose of creating this mod was enhancing experience of hand-building map-arts without using any printers and easy-place mode. But now it's more than just preventing placing and breaking: you can prevent any right- or left-click actions, just make sure there is a suitable condition.

This mod is just cancelling the player's actions, so it's fully client-side.

## How to use the mod

It's almost mandatory to visit the [Wiki](https://github.com/Redheris/PreventBuild/wiki) to understand how to write a config, because there is no other way to write it except by hand.

There are a few files in config/preventbuild/:
- `conditions/` - contains config .cfg files
- `condition_configs.json` - defines which configurations are active by its individual name
- `ore_dictionaries.json` - defines custom ore dictionary lists for using in configs

(For developers) Also you can find out how to create and register [your own conditions](https://github.com/Redheris/PreventBuild/wiki/(For-deveolpers)-Condition-Registry) that you will be able to use in the same config files.

## Commands
- `/pb config list` - display a list of configs with their status
- `/pb config switch` <name> - enable/disable config by name
- `/pb config update` - reload all configs

## Contact

- GitHub: https://github.com/Redheris/PreventBuild/issues
- Discord: @redheris
