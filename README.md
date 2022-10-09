# Deep Mob Learning: Backported <img src="https://raw.githubusercontent.com/9p4/DML-Backported/1.18.2-backport/mod_cover.png" align="right" width="160"/>

[![Fabric API](https://images2.imgbox.com/8e/38/bfInI5qv_o.png)](https://www.curseforge.com/minecraft/mc-mods/fabric-api) [![Fabric Language Kotlin](https://images2.imgbox.com/e6/72/9pHQB5ZC_o.png)](https://www.curseforge.com/minecraft/mc-mods/fabric-language-kotlin)

Deep Mob Learning: Backported is a 1.18.2-specific parallel version of the Deep Mob Learning: Refabricated Minecraft mod which is in turn a parallel version of the Deep Mob Learning Minecraft Mod (originally authored by [xt9](https://github.com/xt9/DeepMobLearning)) made to Fabric Mod Loader (yes, it's confusing).

The main theme of this mod (by "this" I mean the version you can find in this repository) is providing cool ways to acquire mob loot. I also have a lot of other plans to implement here and give the mod other purposes too, but since the current stage of the mod is pre-pre-pre-pre alpha thats all I have to offer for now :/

## Difference Between This Mod and Deep Mob Learning: Refabricated

This mod is a 1.18.2 backport of the bugfixes in Deep Mob Learning: Refabricated. All that has been changed is some class names, mappings, and a new brand to comply with the license.

Other than that, it's more or less a copy-paste of the other mod.

- [Original Deep Mob Learning: Refabricated on Curseforge](https://www.curseforge.com/minecraft/mc-mods/deep-mob-learning-refabricated)
- [Original Deep Mob Learning: Refabricated on Github](https://github.com/CafeteriaGuild/DeepMobLearning-Refabricated)
- [CafeteriaGuild Discord Guild](https://discord.com/invite/G4PjhEf)

**DO NOT GO TO DML: REFABRICATED FOR SUPPORT FOR THIS MOD**

## Xt9's Version

This project is a rework of the original Deep Mob Learning mod, authored by [xt9](https://github.com/xt9/DeepMobLearning).

His version of the mod is focused in providing a better way to acquire mob loot in servers, because creating big mobtraps and things like that doesn't always works well or can have huge performance impacts. If you don't know his version, you should definitively check it out first, he is a pretty cool guy and does a lot of nice things in the Minecraft community.

- [Original Deep Mob Learning on Curseforge](https://www.curseforge.com/minecraft/mc-mods/deep-mob-learning)
- [Original Deep Mob Learning on GitHub](https://github.com/xt9/DeepMobLearning)
- [Xt9's Discord Guild](https://discord.com/invite/gj9kVup)

## Can I add it to a modpack?

Absolutely! You can download the mod on [Curseforge](https://www.curseforge.com/minecraft/mc-mods/deep-mob-learning-backported), just pick the latest release for your game version, make sure you installed the dependencies too.

## Will there ever be a Forge version?

**Well yes but actually no.** If you want a Forge version of this mod you should check out the [xt9's version](#xt9s-version)

## What about Fabric 1.19+?

Check out the [Deep Mob Learning: Refabricated](https://www.curseforge.com/minecraft/mc-mods/deep-mob-learning-refabricated) project (which the current project you are looking at right now is forked from).

## Setting up dev env

The only special thing that differs from a gradle project is that you will need to have this section in your `~/.gradle/gradle.properties` file:

```
mcdUsername=foo
mcdPassword=bar
curseforgeToken=foobar
```

The values can be dummy, fill with the right ones if you somehow need to do the publications.

## Contact

If you want to **report bugs**, give ideas, **feedbacks** or discuss the mod at all, feel free to open an issue or hop on to our [discussions](https://github.com/9p4/DML-Backported/discussions) page.

## License

Most of the textures and models are All Rights Reserved, owned by [IterationFunk](https://github.com/xt9/).

All the files that contains the LGPL-v3 header are licensed over LGPL-v3, this includes all the source code.

```
Copyright (C) 2020 Nathan P. Bombana, IterationFunk

This file is part of Deep Mob Learning: Backported.

Deep Mob Learning: Backported is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

Deep Mob Learning: Backported is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Deep Mob Learning: Backported.  If not, see <https://www.gnu.org/licenses/>.
```
