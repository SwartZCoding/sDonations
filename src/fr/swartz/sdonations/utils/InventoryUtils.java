package fr.swartz.sdonations.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryUtils {

    public static boolean isNullItem(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            return false;
        }
        return true;
    }

	public static void addItem(Player player, ItemStack item) {
		addItem(player, item, 1);
	}
	
	public static void addItem(Player player, ItemStack item, int quantity) {
		Inventory inventory = player.getInventory();
		ItemStack current = new ItemStack(item);
		int max = current.getMaxStackSize();
		
		if (quantity > max) {
			int leftOver = quantity;
			while (leftOver > 0) {
				int add = 0;
				if (leftOver <= max) {
					add += leftOver;
				} else {
					add += max;
				}
				current = current.clone();
				current.setAmount(add);
				inventory.addItem(current);
				
				leftOver -= add;
			}
		} else {
			current = current.clone();
			current.setAmount(quantity);	
			inventory.addItem(current);
		}		
	}
	
	public static boolean haveRequiredItem(Player player, ItemStack item) {
		return haveRequiredItem(player, item, 1);
	}

	public static boolean haveRequiredItem(Player player, ItemStack item, int quantity) {
		int quantityInInventory = getItemCount(player, item);
		if(quantityInInventory >= quantity) {
			return true;
		}
		return false;
	}
	
	public static boolean isFullInventory(Player player) {
		Inventory inventory = player.getInventory();
		for (ItemStack current : inventory.getContents()) {
			if (isNullItem(current)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasSpaceInventory(Player player, ItemStack item, int count) {
		int left = count;
		ItemStack[] items = player.getInventory().getContents();
		
		for (int i = 0; i < items.length; i++) {
			ItemStack stack = items[i];
			if (stack == null || stack.getType() == Material.AIR) {
				left -= item.getMaxStackSize();
			} else {
				if (stack.getType() == item.getType() && stack.getData().getData() == item.getData().getData()) {
					if (item.getMaxStackSize() > 1 && stack.getAmount() < item.getMaxStackSize()) {
						left -= (stack.getMaxStackSize() - stack.getAmount());
					} 
				}
			}
			if (left <= 0) break;
		}
		
		return left <= 0;
	}
	
	public static int getItemCount(Player player, ItemStack item) {
		int quantityInInventory = 0;
		Inventory inventory = player.getInventory();
		for(ItemStack current : inventory.getContents()) {
			if(!isNullItem(current)) {
				if(current.getType() == item.getType() && current.getData().getData() == item.getData().getData()) {
					quantityInInventory += current.getAmount();
				}
			}
		}
		return quantityInInventory;
	}
	
	public static void decrementCurrentItem(Player player, ItemStack item, int quantity) {
		int currentAmount = item.getAmount();
		if (currentAmount <= 1) {
			player.setItemInHand(null);
		} else {
			int amount = currentAmount - quantity;
			item.setAmount(amount);
		}
	}

	public static void decrementItem(Player player, ItemStack item, int quantity) {
		int toRemove = quantity;
		Inventory inv = player.getInventory();
		for (ItemStack is : inv.getContents()) {
			if (toRemove <= 0) break;
			
			if (is != null && is.getType() == item.getType() && is.getData().getData() == item.getData().getData()) {
				int amount = is.getAmount() - toRemove;
				toRemove -= is.getAmount();
				
				if (amount <= 0) {
					player.getInventory().removeItem(is);
				} else {
					is.setAmount(amount);
				}
			}
		}
	}
	
	public static void addItems(Player player, List<ItemStack> items) {
		for(ItemStack item : items) {
			InventoryUtils.addItem(player, item, item.getAmount());
		}
	}
	
	public static boolean haveSpaceInInventory(Player player, List<ItemStack> items) {
		int itemsCount = 0;
		for(ItemStack item : items) {
			if(InventoryUtils.hasSpaceInventory(player, item, item.getAmount())) {
				itemsCount++;
			}
		}
		return itemsCount >= items.size();
	}
}
