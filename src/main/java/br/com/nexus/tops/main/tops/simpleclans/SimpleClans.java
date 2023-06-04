package br.com.nexus.tops.main.tops.simpleclans;

import br.com.nexus.tops.main.Main;
import br.com.nexus.tops.main.RankingType;
import br.com.nexus.tops.main.service.ServiceNPC;
import br.com.nexus.tops.main.util.LocationUtil;
import com.massivecraft.factions.entity.MPlayer;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class SimpleClans {

    private final Main main;
    private final ServiceNPC serviceNPC;
    private ServiceSimpleClans serviceSimpleClans;
    private boolean simpleClans;
    private net.sacredlabyrinth.phaed.simpleclans.SimpleClans sc;

    public SimpleClans(Main main, ServiceNPC serviceNPC) {
        this.main = main;
        this.serviceNPC = serviceNPC;
        this.serviceSimpleClans = new ServiceSimpleClans(this);
    }

    public void onStart() {
        simpleClans = hookSimpleClans();
        if(!simpleClans) {
            Bukkit.getConsoleSender().sendMessage(main.prefix+"§cSimpleClans não foi encontrado.");
            return;
        }
        Bukkit.getConsoleSender().sendMessage(main.prefix+"§6SimpleClans foi encontrado.");
        onEnableNPCs(RankingType.SIMPLE_CLANS_DEATH);
        onEnableNPCs(RankingType.SIMPLE_CLANS_KILL);
        onEnableNPCs(RankingType.SIMPLE_CLANS_KDR);
        onEnableNPCForPlayer(RankingType.SIMPLE_CLANS_MEMBER_DEATH);
        onEnableNPCForPlayer(RankingType.SIMPLE_CLANS_MEMBER_KILL);
        onEnableNPCForPlayer(RankingType.SIMPLE_CLANS_MEMBER_KDR);
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

                ClanPlayer clanPlayer = null;

                switch (type) {
                    case SIMPLE_CLANS_MEMBER_DEATH:
                        List<ClanPlayer> hightMemberDeath = serviceSimpleClans.getHightMemberDeath();
                        if(hightMemberDeath.size() >= position) {
                            clanPlayer = hightMemberDeath.get((position - 1));
                            value = clanPlayer.getDeaths();
                        }
                        break;
                    case SIMPLE_CLANS_MEMBER_KILL:
                        List<ClanPlayer> hightMemberKills = serviceSimpleClans.getHightMemberKills();
                        if(hightMemberKills.size() >= position) {
                            clanPlayer = hightMemberKills.get((position - 1));
                            value = serviceSimpleClans.getAllMemberKills(clanPlayer);
                        }
                        break;
                    case SIMPLE_CLANS_MEMBER_KDR:
                        List<ClanPlayer> hightMemberKDR = serviceSimpleClans.getHightMemberKDR();
                        if(hightMemberKDR.size() >= position) {
                            clanPlayer = hightMemberKDR.get((position - 1));
                            value = clanPlayer.getKDR();
                        }
                        break;
                }
                if(clanPlayer != null) {
                    username = clanPlayer.getName();
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

                Clan clan = null;

                switch (type) {
                    case SIMPLE_CLANS_KDR:
                        List<Clan> hightKDR = serviceSimpleClans.getHightKDR();
                        if(hightKDR.size() >= position) {
                            clan = hightKDR.get((position - 1));
                            value = clan.getTotalKDR();
                        }
                        break;
                    case SIMPLE_CLANS_KILL:
                        List<Clan> hightKills = serviceSimpleClans.getHightKills();
                        if(hightKills.size() >= position) {
                            clan = hightKills.get((position - 1));
                            value = serviceSimpleClans.getAllKills(clan);
                        }
                        break;
                    case SIMPLE_CLANS_DEATH:
                        List<Clan> hightDeath = serviceSimpleClans.getHightDeath();
                        if(hightDeath.size() >= position) {
                            clan = hightDeath.get((position - 1));
                            value = clan.getTotalDeaths();
                        }
                        break;
                }
                if(clan != null) {
                    npcName = clan.getLeaders().get(0).getName();
                    nameGroup = clan.getName();
                    tagGroup = clan.getTag();
                }
                serviceNPC.registerNPC(loc, position, lookAtPlayer, npcName);
                serviceNPC.registerHologramGroup(loc, position, nameGroup, tagGroup, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hookSimpleClans() {
        Plugin plug = main.getServer().getPluginManager().getPlugin("SimpleClans");
        if (plug != null) {
            sc = (net.sacredlabyrinth.phaed.simpleclans.SimpleClans) plug;
            return true;
        }
        return false;
    }

    public net.sacredlabyrinth.phaed.simpleclans.SimpleClans getSCPlugin() {
        return sc;
    }

}
