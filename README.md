# PreventBuild

Adds client-side control over preventing the player from performing certain actions
that are set in configs.
Configs, in turn, have a very flexible YAML structure that allows you to build complex
logical conditions describing what actions the mod should prevent.

With PreventBuild, you can prevent:
- placing/attacking a block;
- attacking an entity;
- interacting with a block/entity
- using an item;

...depending:
- block/item/entity;
- block/player's coordinates;
- a specific block's state;
- whether the held item has a specific custom name or enchantment;
- and much more

This mod simply cancels the player's actions on client-side, so it's entirely client-side.

## 2.0 update
There were a few issues I couldn't fix with the original implementation approaches.
And the best decision was to recreate the mod from a scratch.

Here are essential points that differ 2.x from 1.x:
- More stable and direct preventions;
- Weird custom .cfg configs format + unstable custom parser → YAML + parser dependency;
- Translation keys → Identifiers;
- Custom ore dictionaries → custom and built-in tags;
- Simple GUI instead of commands-only;
- Cleaner code;
- Adequate examples of complex conditions on wiki.

## Official links to download the mod
- [Modrinth](https://modrinth.com/project/VYOvHxMm)
- [Curseforge] (https://www.curseforge.com/minecraft/mc-mods/preventbuild)
- [GitHub](https://github.com/Redheris/PreventBuild)

## Contacts
- GitHub: https://github.com/Redheris/PreventBuild/issues
- Discord: [Server](https://discord.gg/QTykTF8D5p)
