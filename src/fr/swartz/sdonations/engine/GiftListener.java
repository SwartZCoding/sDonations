package fr.swartz.sdonations.engine;

import fr.swartz.sdonations.SDonations;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class GiftListener implements Listener {

    private GiftManager giftManager = GiftManager.getInstance();
    private FileConfiguration config = SDonations.getInstance().getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if(player.hasPermission("dons.open")) {

            GiftUser giftUser = new GiftUser(player.getName());
            this.giftManager.createProfile(giftUser);
            GiftUser createdUser = this.giftManager.getProfile(player.getName());
            List<Gift> items = createdUser.getGifts();

            if(!items.isEmpty()) {
                config.getString("MESSAGES.INFORMATIONS_ABOUT_DONATIONS").replace("<size>", String.valueOf(items.size()));
            }
        }
    }
}
