package fr.swartz.sdonations.utils.menu.items.type;

import fr.swartz.sdonations.utils.ItemBuilder;
import fr.swartz.sdonations.utils.menu.ClickAction;
import fr.swartz.sdonations.utils.menu.items.VirtualItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CloseItem extends VirtualItem {
	public CloseItem() {
		super(new ItemBuilder(Material.TRAP_DOOR).displayname("&6&lFermer le menu").build());

		this.onItemClick(new ClickAction() {
			public void onClick(InventoryClickEvent event) {
				event.getWhoClicked().closeInventory();
			}
		});
	}
}
