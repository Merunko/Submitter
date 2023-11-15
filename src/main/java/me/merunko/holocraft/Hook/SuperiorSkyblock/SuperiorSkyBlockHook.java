package me.merunko.holocraft.Hook.SuperiorSkyblock;

import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;

import java.util.List;

public class SuperiorSkyBlockHook {

    public String getIslandName(SuperiorPlayer player) {
        Island island = player.getIsland();
        return island.getName();
    }

    public SuperiorPlayer getSSBPlayer(String playerName) {
        return SuperiorSkyblockAPI.getPlayer(playerName);
    }

    public List<SuperiorPlayer> getMembersList(String playerName) {
        SuperiorPlayer player = getSSBPlayer(playerName);
        Island island = player.getIsland();
        return island.getIslandMembers(false);
    }

    public String getIslandOwnerName(String playerName) {
        SuperiorPlayer player = getSSBPlayer(playerName);
        Island island = player.getIsland();
        return island.getOwner().getName();
    }

}
