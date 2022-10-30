# Speedrun

A Spigot plugin for multiplayer speedruns

Players are either

- spectators, spectator mode
- speedrunners, adventure mode till game starts, then survival

The timer runs only when at least one speedrunner is online.

### Commands

- `/reset` the world and the speedrunner list
- `/add` new speedrunners
- `/list` all speedrunners
- `/start` the timer
- `/score` see past scores

### Config

`till_dragon: boolean`
Whether the timer stops when the dragon is killed or when the first player uses the end portal.