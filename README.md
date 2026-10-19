# PreventBuild

Adds control over preventing the player from performing certain actions
that are set in configs.
Configs, in turn, have a very flexible structure that allows you to build complex
logical conditions describing what actions the mod should prevent.

With PreventBuild, you can prevent:
- placing/attacking a block;
- attacking an entity;
- interacting with a block/entity
- using an item;

Depending on:
- block/item/entity;
- block/player's coordinates;
- a specific block's state;
- whether the held item has a specific custom name or enchantment;
- dimension;
- etc.

This mod simply cancels the player's actions, so it's entirely client-side.

## Currently under major rewriting

It's been a while since the last updates. And there are were few issues I couldn't fix with the original
implementation approaches. I still don't have a lot of time, but here I will be working on the completely
new version of PreventBuild.

Essential points I'd like to achieve with 2.0:
- Weird custom .cfg configs format + unstable custom parser → YAML + parser dependency;
- Translation keys → Identifiers;
- Custom ore dictionaries → custom and built-in tags;
- More stable and straightforward preventions;
- Adequate examples of complex conditions.

You can still find the old version's sources in another [branch](https://github.com/Redheris/PreventBuild/tree/1.x)

## Official links to download the mod
- [Modrinth](https://modrinth.com/project/VYOvHxMm)
- This GitHub repository (only source code)

## Contacts
- GitHub: https://github.com/Redheris/PreventBuild/issues
- Discord: [Server](https://discord.gg/QTykTF8D5p) or @redheris
