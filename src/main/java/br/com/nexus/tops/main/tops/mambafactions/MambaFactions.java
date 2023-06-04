package br.com.nexus.tops.main.tops.mambafactions;

import br.com.nexus.tops.main.Main;
import br.com.nexus.tops.main.RankingType;
import br.com.nexus.tops.main.service.ServiceNPC;
import br.com.nexus.tops.main.util.LocationUtil;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import lombok.RequiredArgsConstructor;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MambaFactions {

    private final Main main;
    private final MambaFactionsService mambaFactionsService;
    private final ServiceNPC serviceNPC;

    public MambaFactions(Main main, ServiceNPC serviceNPC) {
        this.main = main;
        this.mambaFactionsService = new MambaFactionsService();
        this.serviceNPC = serviceNPC;
    }

    public void onStart() {
        if (!hookFactions()) {
            Bukkit.getConsoleSender().sendMessage(main.prefix + "§cMambaFactions não foi encontrado.");
            return;
        }
        Bukkit.getConsoleSender().sendMessage(main.prefix + "§6MambaFactions foi encontrado.");
        onEnableNPCs(RankingType.MAMBA_FACTIONS_KDR);
        onEnableNPCs(RankingType.MAMBA_FACTIONS_DEATH);
        onEnableNPCs(RankingType.MAMBA_FACTIONS_KILL);
        onEnableNPCForPlayer(RankingType.MAMBA_FACTIONS_MEMBER_KDR);
        onEnableNPCForPlayer(RankingType.MAMBA_FACTIONS_MEMBER_DEATH);
        onEnableNPCForPlayer(RankingType.MAMBA_FACTIONS_MEMBER_KILL);
    }

    private void onEnableNPCForPlayer(RankingType type) {
        ConfigurationSection section = main.getConfig().getConfigurationSection("NPCs." + type.name());
        if (section == null || section.getKeys(false).isEmpty()) {
            return;
        }
        for(String key : main.getConfig().getConfigurationSection("NPCs."+ type.name()+"").getKeys(false)) {
            try {
                int position = Integer.parseInt(key);
                Location loc = LocationUtil.desarializeLoc(main.getConfig().getString("NPCs."+ type.name()+"."+key));
                boolean lookAtPlayer = main.getConfig().getBoolean("LookAtPlayer");

                String username = "";
                double value = 0;

                MPlayer mPlayer = null;

                switch (type) {
                    case MAMBA_FACTIONS_MEMBER_DEATH:
                        if(mambaFactionsService.getHightKDR().size() >= position) {
                            mPlayer = mambaFactionsService.getHightMemberDeath().get((position - 1));
                            value = mPlayer.getKdr();
                        }
                        break;
                    case MAMBA_FACTIONS_MEMBER_KDR:
                        if(mambaFactionsService.getHightKills().size() >= position) {
                            mPlayer = mambaFactionsService.getHightMemberKDR().get((position - 1));
                            value = mPlayer.getKills();
                        }
                        break;
                    case MAMBA_FACTIONS_MEMBER_KILL:
                        if(mambaFactionsService.getHightDeath().size() >= position) {
                            mPlayer = mambaFactionsService.getHightMemberKills().get((position - 1));
                            value = mPlayer.getDeaths();
                        }
                        break;
                }
                if(mPlayer != null) {
                    username = mPlayer.getName();
                }
                serviceNPC.registerNPC(loc, position, lookAtPlayer, username);
                serviceNPC.registerHologramPlayer(loc, position, username, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onEnableNPCs(RankingType type) {
        ConfigurationSection section = main.getConfig().getConfigurationSection("NPCs." + type.name());
        if (section == null || section.getKeys(false).isEmpty()) {
            return;
        }
        for(String key : main.getConfig().getConfigurationSection("NPCs."+ type.name()+"").getKeys(false)) {
            try {
                int position = Integer.parseInt(key);
                Location loc = LocationUtil.desarializeLoc(main.getConfig().getString("NPCs."+ type.name()+"."+key));
                boolean lookAtPlayer = main.getConfig().getBoolean("LookAtPlayer");

                String npcName = "";
                String nameGroup = "";
                String tagGroup = "";
                double value = 0;

                Faction faction = null;

                switch (type) {
                    case MAMBA_FACTIONS_KILL:
                        if(mambaFactionsService.getHightKDR().size() >= position) {
                            faction = mambaFactionsService.getHightKDR().get((position - 1));
                            value = faction.getKdr();
                        }
                        break;
                    case MAMBA_FACTIONS_DEATH:
                        if(mambaFactionsService.getHightKills().size() >= position) {
                            faction = mambaFactionsService.getHightKills().get((position - 1));
                            value = faction.getKills();
                        }
                        break;
                    case MAMBA_FACTIONS_KDR:
                        if(mambaFactionsService.getHightDeath().size() >= position) {
                            faction = mambaFactionsService.getHightDeath().get((position - 1));
                            value = faction.getDeaths();
                        }
                        break;
                }
                if(faction != null) {
                    npcName = faction.getLeader().getName();
                    nameGroup = faction.getName();
                    tagGroup = faction.getTag();
                }
                serviceNPC.registerNPC(loc, position, lookAtPlayer, npcName);
                serviceNPC.registerHologramGroup(loc, position, nameGroup, tagGroup, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hookFactions() {
        Plugin plug = main.getServer().getPluginManager().getPlugin("Factions");
        return plug != null;
    }

}
