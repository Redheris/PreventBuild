# PreventBuild

Adds control over preventing the player from perfoming certain actions that are set in configs. Configs, in turn, have a very flexible structure that allows you to build complex logical conditions describing what actions the mod should prevent.

A full list of existing conditions with descriptions and (for developers) how to add you own are presented on [Wiki]().

This mod is just cancelling the player's actions, so it's fully client-side.

## Basics of using the mod

There are a few files in config/preventbuild/:
- conditions/ - contains config .cfg files ([how to write a cofig]())
- condition_configs.json - defines which configurations are active by its individual name
- ore_dictionaries.json - defines custom ore dictionary lists for using in configs

Commands:
- /pb config list - display a list of configs with their status
- /pb config switch <name> - enable/disable config by name
- /pb config update - reload all configs

## Contact

- GitHub: https://github.com/Redheris/PreventBuild/issues
- Discord: @redheris
- Telegram: @redheris0
