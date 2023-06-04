package br.com.nexus.tops.main.tops.mambafactions;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MambaFactionsService {

    public List<Faction> getHightKDR() {
        Collection<Faction> topFactions = FactionColl.get().getTopFactions();
        List<Faction> factionList = new ArrayList<>(topFactions);
        factionList.sort(((o1, o2) -> Double.compare(o2.getKdr(), o1.getKdr())));
        return factionList;
    }

    public List<Faction> getHightKills() {
        Collection<Faction> topFactions = FactionColl.get().getTopFactions();
        List<Faction> factionList = new ArrayList<>(topFactions);
        factionList.sort(((o1, o2) -> Double.compare(o2.getKills(), o1.getKills())));
        return factionList;
    }

    public List<Faction> getHightDeath() {
        Collection<Faction> topFactions = FactionColl.get().getTopFactions();
        List<Faction> factionList = new ArrayList<>(topFactions);
        factionList.sort(((o1, o2) -> Double.compare(o2.getDeaths(), o1.getDeaths())));
        return factionList;
    }

    public List<MPlayer> getHightMemberKDR() {
        Collection<MPlayer> membersTops = MPlayerColl.get().getAll();
        List<MPlayer> memberList = new ArrayList<>(membersTops);
        memberList.sort(((o1, o2) -> Double.compare(o2.getKdr(), o1.getKdr())));
        return memberList;
    }

    public List<MPlayer> getHightMemberKills() {
        Collection<MPlayer> membersTops = MPlayerColl.get().getAll();
        List<MPlayer> memberList = new ArrayList<>(membersTops);
        memberList.sort(((o1, o2) -> Double.compare(o2.getKills(), o1.getKills())));
        return memberList;
    }

    public List<MPlayer> getHightMemberDeath() {
        Collection<MPlayer> membersTops = MPlayerColl.get().getAll();
        List<MPlayer> memberList = new ArrayList<>(membersTops);
        memberList.sort(((o1, o2) -> Double.compare(o2.getDeaths(), o1.getDeaths())));
        return memberList;
    }

}
