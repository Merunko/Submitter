# Changelogs

## 1.1 Release
- Fixed items are not returned to player when submitter inventory is closed or cancelled.

## 1.2 Release
- Added MMOItems support.
- Added command "/submitter mmoinspect" to check MMOItems ID and Type on main hand.
- Added command "/submitter inspect" to check the item value on main hand.

## 1.3 Release
- Submitter inventory title is now changeable through config.yml.
- Added CMILib color format support for title.
- The point name is now changeable through config.yml.
- Added default message when player submitted items. The default message sent to player is changeable through config.yml. CMILib color format is supported.
- Added commands section in config.yml. A list of commands will be run when player submitted items.

## 1.4 Release
- Added leaderboard system.
- Added leaderboard reset time in config.yml.

## 1.5 Release
- Added rewards system.
- Rewards can be configured through rewards.yml and command "/submitter set reward <position>".

## 1.6 Release
- Added command "/submitter calculate topitem" to check top submitted items.
- Added command "/submitter updatelboard" to update the leaderboard placements.
- Added command "/submitter debug get reward <position>" to test if rewards is working.

## 1.7 Release
- Added item serialization in rewards.yml with Base64 encoding to reduce size.
- Added unclaimed.yml to handle rewards distribution to offline player.
- Improvements on backup processes.

## 1.7.1 Release
- Fixed a bug where all player in the leaderboard is not getting the rewards due to indexing.
- Fixed a bug where data for offline player is not stored properly when rewards distributions are running.
- Fixed a bug where commands based rewards is not executed due to asynchronous task.
- Fixed a bug where player data is not removed properly when they already received the rewards due to data removal while on asynchronous task.

## 1.7.2 Release
- Added TabCompleter for commands. Player can now see the command's arguments.
- Fixed a bug where leaderboard is not updated for the last time before giving out rewards and leaderboard reset.
- Improvements on rewards distribution to offline player.
- Added support for 1.19 - 1.20
