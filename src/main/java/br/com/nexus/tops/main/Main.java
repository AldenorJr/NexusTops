package br.com.nexus.tops.main;

import br.com.nexus.tops.main.command.CommandSetNPC;
import br.com.nexus.tops.main.service.ServiceNPC;
import br.com.nexus.tops.main.tops.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public String prefix = "§2[NexusTops] ";
    public ServiceNPC serviceNPC;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(prefix + "§6Plugin iniciado com sucesso.");
        registerClass();
        saveDefaultConfig();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            long start = System.currentTimeMillis();
            serviceNPC.onReloadNPC();
            long finish = System.currentTimeMillis();
            Bukkit.getConsoleSender().sendMessage(prefix + "§6levou " + (finish - start) + "ms para atualizar os NPCs.");
        }, 20 * 3, 20 * 60 * 15);
        registerCommand();
    }

    private void registerCommand() {
        serviceNPC.onDisableNPC();
        getCommand("settop").setExecutor(new CommandSetNPC(this, serviceNPC));
    }

    public void registerClass() {
        serviceNPC = new ServiceNPC(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(prefix + "§cPlugin desligado com sucesso.");
    }


}
