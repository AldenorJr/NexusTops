package br.com.nexus.tops.main.tops.economy;

import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class ServiceVault {

    private final Vault vault;

    public List<OfflinePlayer> getTopMoney() {
        Economy economy = vault.economy;
        List<OfflinePlayer> accounts = Arrays.asList(Bukkit.getOfflinePlayers());
        accounts.sort(((o1, o2) -> Double.compare(economy.getBalance(o2), economy.getBalance(o1))));
        return accounts;
    }

}
