package fr.swartz.sdonations.utils.menu.items.type;

import fr.swartz.sdonations.utils.ItemBuilder;
import fr.swartz.sdonations.utils.menu.items.VirtualItem;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public class PageItem extends VirtualItem {
  private int page;
  
  public PageItem(int page) {
    super(new ItemBuilder(Material.PAPER).displayname("&6Page suivante: &f" + page).build());
    this.page = page;
  }
}
