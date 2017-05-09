# R&#949;​KiT

[![Build Status](https://travis-ci.org/fuchss-dominik/rekit-game.svg?branch=master)](https://travis-ci.org/fuchss-dominik/rekit-game)
[![](https://jitpack.io/v/fuchss-dominik/rekit-game.svg)](https://jitpack.io/#fuchss-dominik/rekit-game)
[![GitHub issues](https://img.shields.io/github/issues/fuchss-dominik/rekit-game.svg?style=square)](https://github.com/fuchss-dominik/rekit-game/issues)
[![GitHub license](https://img.shields.io/badge/license-GPLv3-blue.svg?style=square)](https://github.com/fuchss-dominik/rekit-game/blob/master/LICENSE.md)

R&#949;​KiT is a platform jumper game in Java and [AWT](https://docs.oracle.com/javase/8/docs/api/java/awt/package-summary.html) and has a self-written game engine to understand and modify.

The project is aiming to educate about software design and was created by students of the [Karlsruher Institut für Technologie](https://www.kit.edu/).

There are [auto-generated API-Docs](https://fuchss-dominik.github.io/rekit-game/) available.

## Getting Started
To simply play the game download a version of the [releases](https://github.com/fuchss-dominik/rekit-game/releases) and start it by either double clicking the jar-file or by using the command `java -jar <JAR-FILE>`.

## GameModes

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

## Features
Currently, the game features
- 7 special blocks
- 12 arcade levels, infinite mode, boss rush mode
- 5 collectable items
- 8 enemies
- 2 bosses

## Development
To setup your workspace for view or altering the code follow these steps:
- Have [eclipse](https://www.eclipse.org/downloads/?) with the [Maven Eclipse plugin](http://www.eclipse.org/m2e/) installed
- Clone the repository
- Switch workspace to the main directory (File -> Switch Workspace)
- Import every maven module in this directory (File -> Import -> Maven -> Existing Maven Projects)
- To compile the project click the run button after opening Main.java in the game module

## Mods / Addons
Addons are an easy way to add functionality without understanding the whole code.

Check out this [example project on GitHub](https://github.com/fuchss-dominik/rekit-sample-mod) on how to create an addon.

Also, see this detailed instruction on [adding levels](https://github.com/fuchss-dominik/rekit-game/blob/master/documentation/LEVEL.md).

Here is a list what can be added via addons:
- levels (see link above)
- enemies (extending the `Enemy` class)
- special blocks (extending the `DynamicInanimate` class)
- bosses (extending the `Boss` class)
- items (extending the `PickUp` class)
