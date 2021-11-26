package fr.swartz.sdonations.engine;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import fr.swartz.sdonations.SDonations;
import fr.swartz.sdonations.engine.commands.GiftCommand;
import fr.swartz.sdonations.engine.commands.GiftSendCommand;
import fr.swartz.sdonations.utils.InventoryUtils;
import fr.swartz.sdonations.utils.ItemBuilder;
import fr.swartz.sdonations.utils.Utils;
import fr.swartz.sdonations.utils.jsons.DiscUtil;
import fr.swartz.sdonations.utils.jsons.Saveable;
import fr.swartz.sdonations.utils.menu.ClickAction;
import fr.swartz.sdonations.utils.menu.CloseAction;
import fr.swartz.sdonations.utils.menu.items.VirtualItem;
import fr.swartz.sdonations.utils.menu.menus.Size;
import fr.swartz.sdonations.utils.menu.menus.VirtualGUI;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Getter
public class GiftManager extends Saveable {

    private static @Getter GiftManager instance;
    private List<GiftUser> users;
    private FileConfiguration config = SDonations.getInstance().getConfig();

    public GiftManager(SDonations plugin) {
        super(plugin, "Gift");
        this.instance = this;
        this.users = Lists.newArrayList();

        this.registerCommand(new GiftCommand());
        this.registerCommand(new GiftSendCommand());

        this.registerListener(new GiftListener());
    }

    public void openGift(Player player) {
        GiftUser user = this.getProfile(player.getName());
        List<Gift> items = user.getGifts();

        if(items.isEmpty()) {
            player.sendMessage(config.getString("MESSAGES.EMPTY_DONATIONS_BOX"));
            return;
        }

        if(user == null) {
            player.sendMessage(config.getString("MESSAGES.NULL_USER"));
            return;
        }

        VirtualGUI gui = new VirtualGUI(config.getString("GUI_OPTIONS_OPEN.NAME"), Size.SIX_LIGNE);

        user.getGifts().forEach(gift -> {

            gui.addItem(gift.toVirtualItem(new ClickAction() {

                @Override
                public void onClick(InventoryClickEvent paramInventoryClickEvent) {

                    GiftManager.this.openGiftAction(player, user, gift);
                }
            }));
        });

        gui.open(player);
    }

    public void sendGift(Player player, String receiver) {

        VirtualGUI gui = new VirtualGUI(config.getString("GUI_OPTIONS_SEND.NAME"), Size.TROIS_LIGNE);
        gui.setAllowClick(true);
        gui.open(player);

        gui.onCloseAction(new CloseAction() {
            @Override
            public void onClose(InventoryCloseEvent event) {

                ItemStack[] content = event.getInventory().getContents();

                Gift gift = new Gift(player.getName(), receiver, Arrays.asList(content));
                GiftUser user = GiftManager.this.getProfile(receiver);

                int number = 0;

                if(user != null) {
                    for (ItemStack itemStack : content) {
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            number++;
                        }
                    }
                    if (number > 0) {
                        user.addGift(gift);
                        player.sendMessage(config.getString("MESSAGES.DONATION_SEND_SUCCESSFULY").replace("<player>", user.getOwnerName()));
                    } else {
                        player.sendMessage(config.getString("MESSAGES.EMPTY_DONATION"));
                    }
                }
            }
        });
    }

    public GiftUser getProfile(String value) {

        return this.users.stream().filter(user -> user.getOwnerName().equals(value)).findFirst().orElse(null);
    }

    public void createProfile(GiftUser profile) {
        if(this.getProfile(profile.getOwnerName()) == null) {
            this.users.add(profile);
        }
    }

    public void openGiftAction(Player player, GiftUser storingProfile, Gift gift) {

        VirtualGUI gui = new VirtualGUI(config.getString("GUI_OPTIONS_CONFIRM.NAME"), Size.TROIS_LIGNE);
        gui.setItem(11, new VirtualItem(new ItemBuilder(Material.STAINED_CLAY).dye(DyeColor.GREEN).displayname(config.getString("GUI_OPTIONS_CONFIRM.COLLECT")).lore("", "&eVous permet de récupérer le don", "&edans votre inventaire").build()).onItemClick(new ClickAction() {
            @Override
            public void onClick(InventoryClickEvent event) {

                gift.getItems().forEach(gift -> {

                    if(gift != null)
                    InventoryUtils.addItem(player, gift, gift.getAmount());
                });

                storingProfile.removeGift(gift);
                player.closeInventory();
                player.sendMessage(config.getString("MESSAGES.DONATION_COLLECT_SUCCESSFULY"));
            }
        }));
        gui.setItem(13, new VirtualItem(new ItemBuilder(Material.ENDER_PORTAL_FRAME).displayname(config.getString("GUI_OPTIONS_CONFIRM.INFOS")).build()));
        gui.setItem(15, new VirtualItem(new ItemBuilder(Material.STAINED_CLAY).dye(DyeColor.RED).displayname(config.getString("GUI_OPTIONS_CONFIRM.CANCEL")).lore("", "&eLe don va rester dans l'inventaire").build()).onItemClick(new ClickAction() {
            @Override
            public void onClick(InventoryClickEvent event) {
                player.closeInventory();
            }
        }));
        gui.open(player);
    }

    @Override
    public File getFile() { return Utils.getFormatedFile("/gifts/"); }

    @Override
    public void loadData() {
        File[] files = getFile().listFiles();
        for (int count = 0; count < files.length; count++) {
            File file = files[count];
            String content = DiscUtil.readCatch(file);
            if (content != null) {
                try {
                    GiftUser user = SDonations.getInstance().getGson().fromJson(content, new TypeToken<GiftUser>() {}.getType());
                    this.createProfile(user);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @Override
    public void saveData(boolean sync) {
        for (GiftUser user : this.users) {
            DiscUtil.writeCatch(SDonations.getInstance(), user.getProfileFile(), SDonations.getInstance().getGson().toJson(user), sync);
        }
    }
}
