package fr.swartz.sdonations.engine.commands;

import fr.swartz.sdonations.SDonations;
import fr.swartz.sdonations.engine.GiftManager;
import fr.swartz.sdonations.utils.menu.commands.Command;
import fr.swartz.sdonations.utils.menu.commands.CommandArgs;
import fr.swartz.sdonations.utils.menu.commands.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GiftSendCommand extends ICommand {

    private GiftManager giftManager = GiftManager.getInstance();
    private FileConfiguration config = SDonations.getInstance().getConfig();

    @Override
    @Command(name = {"gift.send", "gifts.send", "don.send", "dons.send"})
    public void onCommand(CommandArgs args) {

        Player player = args.getPlayer();

        if(Bukkit.getPlayer(args.getArgs(0)) != null) {

            if(!Bukkit.getPlayer(args.getArgs(0)).hasPermission("dons.open")) {
                player.sendMessage(config.getString("MESSAGES.PLAYER_CANT_RECIVE_DONATIONS"));
                return;
            }

            this.giftManager.sendGift(player, args.getArgs(0));
        } else {
            player.sendMessage(config.getString("MESSAGES.PLAYER_DISCONNECTED"));
        }
    }
}