package fr.swartz.sdonations.engine;

import fr.swartz.sdonations.utils.ItemBuilder;
import fr.swartz.sdonations.utils.menu.ClickAction;
import fr.swartz.sdonations.utils.menu.items.VirtualItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter @Setter
public class Gift {

    private String sender;
    private String reciever;
    private List<ItemStack> items;

    public Gift(String sender, String reciever, List<ItemStack> items)
    {
        this.sender = sender;
        this.reciever = reciever;
        this.items = items;
    }

    public VirtualItem toVirtualItem(ClickAction action) {

        return new VirtualItem(new ItemBuilder(Material.BOOK_AND_QUILL).displayname("§eDon de " + sender)).onItemClick(action);
    }
}
