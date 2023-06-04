package br.com.nexus.tops.main.service;

import br.com.nexus.tops.main.Main;
import br.com.nexus.tops.main.model.NPCModel;
import br.com.nexus.tops.main.tops.economy.ServiceVault;
import br.com.nexus.tops.main.tops.economy.Vault;
import br.com.nexus.tops.main.tops.mambafactions.MambaFactions;
import br.com.nexus.tops.main.tops.simpleclans.SimpleClans;
import com.github.juliarn.npc.NPCPool;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ServiceNPC {

    public ServiceNPC(Main main) {
        this.main = main;
        npcPool = NPCPool.builder(main).spawnDistance(60).actionDistance(30).tabListRemoveTicks(20).build();
        this.simpleClans = new SimpleClans(main, this);
        this.vault = new Vault(main, this);
        this.mambaFactions = new MambaFactions(main, this);
    }

    public static ArrayList<Hologram> hologramsList = new ArrayList<>();
    public DecimalFormat df = new DecimalFormat("#,###,###,##0.00");;
    private NPCPool npcPool;
    private SimpleClans simpleClans;
    private MambaFactions mambaFactions;
    private Vault vault;
    private final Main main;

    public NPCPool getNPCPool() {
        return npcPool;
    }

    public void registerNPC(Location location, int position, boolean lookAtPlayer, String username) {
        NPCModel npcModel = new NPCModel(getNPCPool(), main);
        npcModel.setLocation(location);
        npcModel.setPosition(position);
        npcModel.setLookAtPlayer(lookAtPlayer);
        npcModel.setPlayerTopName(username);
        npcModel.spawnNPC();
    }

    public void registerHologramPlayer(Location location, int position, String username, double value) {
        try {
            Hologram hologram;
            if (username.isEmpty()) {
                hologram = HologramsAPI.createHologram(main, location.add(0, 2.6, 0));
                hologram.insertTextLine(0, "§cIndefinido");
            } else {
                hologram = HologramsAPI.createHologram(main, location.add(0, main.getConfig().getDouble("Altura-holograma"), 0));
                main.getConfig().getStringList("Holograme-player").forEach(text -> hologram.insertTextLine(0,
                        text.replaceAll("&", "§")
                                .replaceAll("%posicao%", Integer.toString(position))
                                .replaceAll("%nickname%", username)
                                .replaceAll("%value%", df.format(value))));
            }
            hologramsList.add(hologram);
        } catch (Exception ignored) {
        }
    }

    public void registerHologramGroup(Location location, int position, String nameGroup, String tagGroup, double value) {
        try {
            Hologram hologram;
            if (nameGroup.isEmpty()) {
                hologram = HologramsAPI.createHologram(main, location.add(0, 2.6, 0));
                hologram.insertTextLine(0, "§cIndefinido");
            } else {
                hologram = HologramsAPI.createHologram(main, location.add(0, main.getConfig().getDouble("Altura-holograma"), 0));
                main.getConfig().getStringList("Holograme").forEach(text -> hologram.insertTextLine(0,
                        text.replaceAll("&", "§")
                                .replaceAll("%posicao%", Integer.toString(position))
                                .replaceAll("%tagGroup%", tagGroup)
                                .replaceAll("%nameGroup%", nameGroup)
                                .replaceAll("%value%", df.format(value))));
            }
            hologramsList.add(hologram);
        } catch (Exception ignored) {
        }
    }

    public void onDisableNPC() {
        npcPool.getNPCs().forEach(npc -> npcPool.removeNPC(npc.getEntityId()));
        if (!hologramsList.isEmpty()) hologramsList.forEach(Hologram::delete);
    }

    public void onEnableNPC() {
        this.simpleClans.onStart();
        this.vault.onStart();
        this.mambaFactions.onStart();
    }

    public void onReloadNPC() {
        onDisableNPC();
        onEnableNPC();
    }

}
