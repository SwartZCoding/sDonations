package fr.swartz.sdonations.engine.commands;

import fr.swartz.sdonations.engine.GiftManager;
import fr.swartz.sdonations.utils.menu.commands.Command;
import fr.swartz.sdonations.utils.menu.commands.CommandArgs;
import fr.swartz.sdonations.utils.menu.commands.ICommand;
import org.bukkit.entity.Player;

public class GiftCommand extends ICommand {

    private GiftManager giftManager = GiftManager.getInstance();

    @Override
    @Command(name = {"gift", "don", "gifts", "dons"}, permissionNode = "dons.open")
    public void onCommand(CommandArgs args) {

        Player player = args.getPlayer();
        this.giftManager.openGift(player);
    }
}
