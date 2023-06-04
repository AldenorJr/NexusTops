package br.com.nexus.tops.main.tops.simpleclans;

import lombok.RequiredArgsConstructor;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.Bukkit;

import java.util.*;

@RequiredArgsConstructor
public class ServiceSimpleClans {

    private final SimpleClans simpleClans;

    public List<Clan> getHightKDR() {
        ClanManager clanManager = simpleClans.getSCPlugin().getClanManager();
        List<Clan> clans = clanManager.getClans();
        clans.sort((o1, o2) -> Float.compare(o2.getTotalKDR(), o1.getTotalKDR()));
        return clans;
    }

    public List<Clan> getHightDeath() {
        ClanManager clanManager = simpleClans.getSCPlugin().getClanManager();
        List<Clan> clans = clanManager.getClans();
        clans.sort((o1, o2) -> Float.compare(o2.getTotalDeaths(), o1.getTotalDeaths()));
        return clans;
    }

    public List<Clan> getHightKills() {
        ClanManager clanManager = simpleClans.getSCPlugin().getClanManager();
        List<Clan> clans = clanManager.getClans();
        clans.sort((o1, o2) -> Float.compare(getAllKills(o2), getAllKills(o1)));
        return clans;
    }

    public List<ClanPlayer> getHightMemberKills() {
        List<ClanPlayer> allClanPlayers = simpleClans.getSCPlugin().getClanManager().getAllClanPlayers();
        allClanPlayers.sort((o1, o2) -> Double.compare(getAllMemberKills(o2), getAllMemberKills(o1)));
        return allClanPlayers;
    }

    public List<ClanPlayer> getHightMemberKDR() {
        List<ClanPlayer> allClanPlayers = simpleClans.getSCPlugin().getClanManager().getAllClanPlayers();
        allClanPlayers.sort((o1, o2) -> Double.compare(o2.getKDR(), o1.getKDR()));
        return allClanPlayers;
    }

    public List<ClanPlayer> getHightMemberDeath() {
        List<ClanPlayer> allClanPlayers = simpleClans.getSCPlugin().getClanManager().getAllClanPlayers();
        allClanPlayers.sort((o1, o2) -> Double.compare(o2.getDeaths(), o1.getDeaths()));
        return allClanPlayers;
    }

    public int getAllKills(Clan clan) {
        return clan.getTotalNeutral()+clan.getTotalCivilian()+clan.getTotalRival();
    }

    public int getAllMemberKills(ClanPlayer clanPlayer) {
        return clanPlayer.getCivilianKills()+clanPlayer.getNeutralKills()+clanPlayer.getRivalKills();
    }



}
