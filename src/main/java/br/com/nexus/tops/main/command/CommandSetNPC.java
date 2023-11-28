package br.com.nexus.tops.main.command;

import br.com.nexus.tops.main.Main;
import br.com.nexus.tops.main.RankingType;
import br.com.nexus.tops.main.service.ServiceNPC;
import br.com.nexus.tops.main.util.LocationUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
public class CommandSetNPC implements CommandExecutor {

    private final Main main;
    private final ServiceNPC serviceNPC;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cApenas jogadores podem executar esse comando.");
            return true;
        }
        Player player = (Player) commandSender;
        if(!player.hasPermission("nexustop.admin.use")) {
            player.sendMessage("§cVocê não tem permissão para executar esse comando.");
            return true;
        }
        if (args.length < 1) {
            player.sendMessage("§eVocê deve selecionar pelo menos um top especifico.");
            modalidadeSend(player);
            return true;
        }
        if(args[0].equalsIgnoreCase("update")) {
            player.sendMessage("§6NPCs atualizados com sucesso!");
            serviceNPC.onReloadNPC();
            return true;
        }
        RankingType rankingType;
        try {
            rankingType = RankingType.valueOf(args[0]);
        } catch (Exception ignored) {
            player.sendMessage("§cVocê informou uma modalidade inválido.");
            modalidadeSend(player);
            return true;
        }
        if(args.length < 2) {
            player.sendMessage("§cPor favor informe a posição que você deseja, um valor entre 1 e 10.");
            return true;
        }
        if(args.length > 2) {
            player.sendMessage("§cVocê deve usar o comando da seguinte forma:§7 /settop <modalidade> <posiãço>");
            return true;
        }
        int position;
        try {
            position = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
            player.sendMessage("§cValor informado não é um número.");
            return true;
        }

        main.getConfig().set("NPCs."+rankingType.name()+"."+position, LocationUtil.serializeLoc(player.getLocation()));
        main.saveConfig();
        player.sendMessage("§6NPC setado com sucesso!");
        serviceNPC.onReloadNPC();
        return true;
    }

    private void modalidadeSend(Player player) {
        player.sendMessage("");
        StringBuilder modalidade = new StringBuilder();
        modalidade.append("§8 * ");
        for (RankingType type : RankingType.values()) {
            modalidade.append(type.name()).append(", ");
        }
        player.sendMessage(modalidade.substring(0, (modalidade.length()-2))+".");
    }

}
