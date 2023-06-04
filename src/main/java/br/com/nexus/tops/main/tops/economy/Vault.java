package br.com.nexus.tops.main.tops.economy;

import br.com.nexus.tops.main.Main;
import br.com.nexus.tops.main.RankingType;
import br.com.nexus.tops.main.service.ServiceNPC;
import br.com.nexus.tops.main.util.LocationUtil;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.List;

public class Vault {

    private final Main main;
    private final ServiceNPC serviceNPC;
    private final ServiceVault serviceVault;
    public Economy economy = null;

    public Vault(Main main, ServiceNPC serviceNPC) {
        this.main = main;
        this.serviceNPC = serviceNPC;
        this.serviceVault = new ServiceVault(this);
    }

    public void onStart() {
        if(!hookVault()) {
            Bukkit.getConsoleSender().sendMessage(main.prefix+"§cVault não foi encontrado.");
            return;
        }
        if(!setupEconomy()) return;
        Bukkit.getConsoleSender().sendMessage(main.prefix+"§6Vault foi encontrado.");
        onEnableNPC();
    }

    private void onEnableNPC() {
        ConfigurationSection section = main.getConfig().getConfigurationSection("NPCs." + RankingType.MONEY.name());
        if (section == null || section.getKeys(false).isEmpty()) {
            return;
        }
        for(String key : main.getConfig().getConfigurationSection("NPCs."+ RankingType.MONEY.name()+"").getKeys(false)) {
            try {
                int position = Integer.parseInt(key);
                Location loc = LocationUtil.desarializeLoc(main.getConfig().getString("NPCs."+ RankingType.MONEY.name()+"."+key));
                boolean lookAtPlayer = main.getConfig().getBoolean("LookAtPlayer");

                String npcName = "";
                String username = "";
                double value = 0;

                OfflinePlayer offlinePlayer = null;
                List<OfflinePlayer> topMoney = serviceVault.getTopMoney();
                if (topMoney.size() >= position) {
                    offlinePlayer = topMoney.get((position - 1));
                }
                if(offlinePlayer != null) {
                    npcName = offlinePlayer.getName();
                    username = offlinePlayer.getName();
                    value = economy.getBalance(offlinePlayer);
                }
                serviceNPC.registerNPC(loc, position, lookAtPlayer, npcName);
                serviceNPC.registerHologramPlayer(loc, position, username, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = main.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(main.prefix+"Não foi encontrado nem um plugin de economia.");
        return false;
    }

    public boolean hookVault() {
        Plugin plug = main.getServer().getPluginManager().getPlugin("Vault");
        return plug != null;
    }

}
