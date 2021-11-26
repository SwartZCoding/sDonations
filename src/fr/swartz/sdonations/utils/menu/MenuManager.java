package fr.swartz.sdonations.utils.menu;

import com.google.common.collect.Maps;
import fr.swartz.sdonations.utils.menu.menus.GUI;
import fr.swartz.sdonations.utils.menu.menus.VirtualGUI;
import fr.swartz.sdonations.utils.menu.menus.VirtualHolder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

@Getter
public class MenuManager implements Listener {
	
	private static @Getter MenuManager instance = new MenuManager();
	private Plugin plugin = null;
	private Map<UUID, GUI> guis = Maps.newHashMap();

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if ((event.getWhoClicked() instanceof Player)) {
			if ((event.getInventory().getHolder() instanceof VirtualHolder)) {
				((VirtualHolder) event.getInventory().getHolder()).getGui().onInventoryClick(event);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getPlayer() instanceof Player && event.getInventory().getHolder() instanceof VirtualHolder) {
			GUI gui = ((VirtualHolder) event.getInventory().getHolder()).getGui();
			gui.onInventoryClose(event);
			if ((gui instanceof VirtualGUI)) {
				VirtualGUI virtualGUI = (VirtualGUI) gui;
				this.guis.put(event.getPlayer().getUniqueId(), virtualGUI);
			}
		}
	}
	
	public void register(JavaPlugin plugin) {
		if (!isRegistered(plugin)) {
			plugin.getServer().getPluginManager().registerEvents(this.instance, plugin);
			this.plugin = plugin;
		}
	}
	public boolean isRegistered(JavaPlugin plugin) {
		if (plugin.equals(this.plugin)) {
			for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
				if (listener.getListener().equals(this.instance)) {
					return true;
				}
			}
		}
		return false;
	}
	public static void closeOpenMenus() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getOpenInventory() != null) {
				Inventory inventory = player.getOpenInventory().getTopInventory();
				if ((inventory.getHolder() instanceof VirtualHolder)) {
					player.closeInventory();
				}
			}
		}
	}
	
	 public static void closeOpenMenus(String name) {
		   
	        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
	            Inventory inventory = player.getOpenInventory().getTopInventory();
	            if (player.getOpenInventory() != null 
	            		&& inventory.getHolder() instanceof VirtualHolder 
	            		&& inventory.getName() != null 
	            		&& inventory.getName().equalsIgnoreCase(name)) {
	            	
	                player.closeInventory();
	            }
	        }
	    }
}
