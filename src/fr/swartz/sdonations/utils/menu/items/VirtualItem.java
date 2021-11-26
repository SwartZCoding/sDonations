package fr.swartz.sdonations.utils.menu.items;

import fr.swartz.sdonations.utils.ItemBuilder;
import fr.swartz.sdonations.utils.menu.ClickAction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
public class VirtualItem {
	
  private final ItemStack item;
  private ClickAction action;
  
  public VirtualItem(ItemStack item) {
    this.item = item;
    action = null;
  }
  public VirtualItem(ItemBuilder item) {
      this.item = item.build();
      this.action = null;
  }

  public VirtualItem onItemClick(ClickAction action) {
    this.action = action;
    return this;
  }
}
