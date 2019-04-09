# Crystals

## General overview

### Concept

Crystals is a roguelike with traditional JRPG elements.
This document describes the features of the endless game mode.

The game has three main states :

- The "new game" state, where the player chooses their starting hero and job. Choosing a job grants a crystal of the corresponding job.
- The "trip" state, where the player can check the inventory and the team status. The map is displayed and consists in a succession of vertical chunks containing 3 icons. The player moves from chunk to chunk by choosing an icon. Each icon corresponds to a category of events.
- The "event" state, triggered when the player chooses an event. These events can be regular battles, boss battles, visiting a mountain or a cave, entering in a house, in a shop, in a castle… Once the event is over, the "trip" state is restored and the player can choose the next event.

The goal is to survive as long as possible and to have fun creating a team with the job crystals feature.

### Job crystals

Heroes have different stats, but they start with the same abilities (attack, defense). In order to cast spells or use other abilities, up to three job crystals can be equipped. For instance, assigning a white mage crystal to a hero will make it possible for them to cast white magic spells.

A job crystal is given at the start of the run, and others can be found in shops or by defeating monsters.

Equipped crystals will resonate with each other, resulting in stats bonuses. 

### Events

All events should have many variants, including bad and good ones. They should also be affected by the current biome and by the progress of the run.

#### Battles

During battles, one or more enemies can appear.
Semi-bosses and bosses are indicated by a different icon on the map.

At the end of the battle, the party earns EXP, loots, and coins.
Heroes aren't healed after battles.

#### Inns

Inns make it possible to fully heal the party for free.
Occasionally a NPC will give or sell an object.

#### Houses

In houses, the party meets one or two characters. They give insights and sometimes objects.

#### Shops

Shops let the player buy 1 to 5 items. Shops can be themed (ie. armor shop) or contain random items.

#### Random events

Random events can be one of the following:

- A battle
- A treasure
- A trade
- A choice
- A misadventure
- A hero asking to join the team