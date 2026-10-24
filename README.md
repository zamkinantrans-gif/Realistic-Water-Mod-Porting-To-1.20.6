# Realistic Water — 1.20.6 port (MODIFIED VERSION, NOT THE ORIGINAL)

**This is an unofficial, modified version of _Realistic Water_ by Skaffy. It is not the original mod.**

- Original author: **Skaffy**
- Original project: https://github.com/sskaffy/Realistic-Water
- Distributed under the same license as the original (see `LICENSE`, notice kept intact).
  Do not sell it or put it behind a paywall.

## What this is

A port of the *gameplay/server part* of Realistic Water 26.3-1.2.2 to **Minecraft 1.20.6 + Fabric**
(Java 21, Mojang mappings).

Included: Realistic Water / Lava / Slime fluids + buckets, and Realistic Sand, all registered with the
same ids as the original.

## What is NOT included (cannot be ported)

The original's "realistic" look is made by ~41,000 lines of **client** code: a custom **Vulkan** renderer,
a client-side fluid simulation, a shader-pack system and a GUI engine. Minecraft 1.20.6 has no Vulkan
backend, so none of that exists here. Differences from the original:

| Original (26.3) | This port (1.20.6) |
|---|---|
| Water drawn by Vulkan renderer, simulated on the client | Vanilla fluid renderer (tinted water/slime, vanilla lava) |
| Client simulation syncs blocks to the server via packets | No packets; fluids flow with vanilla server logic (no new infinite sources) |
| Sand has no collision, drawn by the renderer | Sand is a normal visible block with collision, 1–8 layers |
| `/water ...` commands, settings GUI, shader packs | Not available; get items from the Creative inventory |
| Piston mixins for sand | Not needed, removed |

## Build

1. Install JDK 21 and Gradle 8.x (once, to generate the wrapper).
2. In this folder: `gradle wrapper --gradle-version 8.8` then `./gradlew build`
   (Windows: `gradlew.bat build`).
3. The mod jar is in `build/libs/` (use the one without `-sources`). Put it in `mods/` together with Fabric API.

Check current versions for 1.20.6 at https://fabricmc.net/develop and edit `gradle.properties` if needed.

**Status: written against the 1.20.6 Mojang-mapped API but NOT compiled or tested** (no internet in the
environment it was written in). Expect to fix a few small signature mismatches on first build.
