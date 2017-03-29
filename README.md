# R&#949;​KiT

[![Build Status](https://travis-ci.org/fuchss-dominik/rekit-game.svg?branch=master)](https://travis-ci.org/fuchss-dominik/rekit-game)
[![](https://jitpack.io/v/fuchss-dominik/rekit-game.svg)](https://jitpack.io/#fuchss-dominik/rekit-game)
[![GitHub issues](https://img.shields.io/github/issues/fuchss-dominik/rekit-game.svg?style=square)](https://github.com/fuchss-dominik/rekit-game/issues)
[![GitHub license](https://img.shields.io/badge/license-GPLv3-blue.svg?style=square)](https://github.com/fuchss-dominik/rekit-game/blob/master/LICENSE.md)

R&#949;​KiT is a platform jumper game in Java and [AWT](https://docs.oracle.com/javase/8/docs/api/java/awt/package-summary.html) and has a self-written game engine to understand and modify.

The project is aiming to educate about software design and was created by students of the [Karlsruher Institut of Technology](https://www.kit.edu/).

There are [auto-generated API-Docs](https://fuchss-dominik.github.io/rekit-game/) available.

## Mods / Addons

Addons are an easy way to add functionality without understanding the whole code.

Here is a list what can be added via addons:
- levels (arcade-like or randomly generated)
- enemies
- bosses
- items

### Adding Levels
#### Create a Level
If you want to create your own levels, follow the steps:
* Create a new text-file (the name has to start with *level*)
* Define the settings of the level. All possible settings can be found in the SettingKey-Enum. (The syntax should be easy to understand.) You could set the level's name by adding `#SETTING::name->My_Special_Name` (`_` will be replaced by space).
* You can set a `#BOSS_SETTING::ATXXX->Boss` to create a Boss-Room at position `XXX` in your level (you can also specify a concrete Boss by its name)
* In a further step you can create `aliases` to create `structures` easily. You can use specific Items or Groups (e.g. Coin: `#ALIAS::1->Coin`)
* In a last step you can create `structures`. A structure contains one or more logical row: The structure begins with `{` and ends with `}`. In a structure you can define several rows. They begin with `{` and end with `}`. A row contains the Elements of itself seperated by spaces (or tabs etc.). You can use an alias or name. You can also provide additional Info to the Element by adding `:INFO1:INFO2` to the name or alias. these info will be sent to the create method of the Element.

#### Load Levels
To load a level you have two options:
* You can add the level to a Mod (jar) and add it to the subfolder `levels`. If the `group`-Setting is not set, the level will be placed in the default group.
* You can add the level to the `levels`-Directory (Windows: `%APPDATA%/rekit`, Unix/Mac: `~/.config/rekit`). If you place the level into a subdirectory of the `levels`-Directory and won't set the `group`-Setting, the level will be placed into a group named like the subdirectory.


Check out this [example project on GitHub](https://github.com/fuchss-dominik/rekit-sample-mod) to see to create an addon. More detailed information will follow in future.

## Game Modes

Currently, the game features
- 11 blocks
- 12 arcade levels, infinite mode, boss rush mode
- 4 collectable items
- 6 enemies
- 2 bosses

### Infinite Mode
The infinite level is constructed of small components that [can be modified](https://github.com/fuchss-dominik/rekit-game/blob/master/project/logic/src/main/resources/levels/infinite.dat) The more varieties through structures and enemies there is, the more random this level becomes!

![Randomly generated levels and many different enemies and varieties](https://github.com/fuchss-dominik/rekit-game/blob/master/graphix/rekitScreenshotInfinite.png)

### Arcade Mode
In contrast to the random infinite mode, there is also a set of predesigned levels to master.
They will introduce one special block and feature at a time but always be challenging!

Levels can also be added via modding as shown in [the example](https://github.com/fuchss-dominik/rekit-sample-mod).

![New challenges special blocks](https://github.com/fuchss-dominik/rekit-game/blob/master/graphix/rekitScreenshotArcade.png)

### Boss Rush
The Boss Rush is a quick way to challenge the games bosses.

![Unique bosses](https://github.com/fuchss-dominik/rekit-game/blob/master/graphix/rekitScreenshotBossRush.png)
