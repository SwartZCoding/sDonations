package fr.swartz.sdonations.utils.menu.items.type;

import fr.swartz.sdonations.utils.ItemBuilder;
import fr.swartz.sdonations.utils.menu.ClickAction;
import fr.swartz.sdonations.utils.menu.MenuManager;
import fr.swartz.sdonations.utils.menu.items.VirtualItem;
import fr.swartz.sdonations.utils.menu.menus.VirtualGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BackItem extends VirtualItem {
	
	public BackItem() {
		super(new ItemBuilder(Material.ARROW).displayname("&c&lRetour").build());

		this.onItemClick(new ClickAction() {
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				VirtualGUI gui = (VirtualGUI) MenuManager.getInstance().getGuis().get(player.getUniqueId());
				if (gui != null) {
					gui.open(player);
				}
			}
		});
	}
}
