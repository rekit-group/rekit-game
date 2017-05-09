# Adding Levels

## Create a Level
If you want to create your own levels, follow the steps:
* Create a new text-file (the name has to start with *level* and has to end with *.dat*)
* Define the settings of the level. All possible settings can be found in the SettingKey-Enum. (See this [example](https://github.com/fuchss-dominik/rekit-game/blob/master/project/basic/src/main/resources/levels/level_1.dat) for reference)
* You can set a `#BOSS_SETTING::ATXXX->Boss` to create a Boss-Room at position `XXX` in your level (you can also specify a concrete Boss by its name)
* In a further step you can create `aliases` to create `structures` easily. You can use specific Items or Groups (e.g. Coin: `#ALIAS::1->Coin`)
* In a last step you can create `structures`. These structures represent pieces of the level that will be put together (either in order or randomly, depending on the shuffle setting).
* Each entry in the structure represents a block or an enemy. You can use an alias or name. Depending on the element you can also provide addition information to the element by adding `:INFO1:INFO2` to the name or alias. These information will be parsed by the create method of the GameElement.

## Load Levels
To load a level you have two options:
* You can add the level to a Mod (jar) and add it to the subfolder `levels` (or a subfolder of `levels`). If the `group`-Setting is not set, the level will be placed in the default group.
* You can add the level to the `levels`-Directory (Windows: `%APPDATA%/rekit`, Unix/Mac: `~/.config/rekit`). If you place the level into a subdirectory of the `levels`-Directory and won't set the `group`-Setting, the level will be placed into a group named like the subdirectory (this also applies if you place the level into a Mod).
