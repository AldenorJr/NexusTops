package br.com.nexus.tops.main.model;

import br.com.nexus.tops.main.Main;
import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.profile.Profile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

import java.util.Random;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class NPCModel {

    private String playerTopName;
    private boolean isPlayer;
    private int position;
    private Location location;
    private Boolean lookAtPlayer;
    private final NPCPool npcPool;
    private final Main main;

    public void spawnNPC() {
        if (playerTopName.isEmpty()) playerTopName = "banana";
        if(location == null || location.getWorld() == null) return;
        if(!location.getChunk().isLoaded()) location.getChunk().load(true);
        Profile profile = new Profile(playerTopName);
        profile.complete();
        profile.setName("");
        profile.setUniqueId(new UUID(new Random().nextLong(), 0));

        NPC.builder()
                .profile(profile)
                .location(location.clone())
                .imitatePlayer(false)
                .lookAtPlayer(lookAtPlayer)
                .spawnCustomizer((spawnedNpc, player) -> spawnedNpc.rotation().queueRotate(location.getYaw(), location.getPitch()).send(player))
                .build(npcPool);
    }

}
