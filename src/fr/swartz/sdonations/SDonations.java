package fr.swartz.sdonations;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.swartz.sdonations.engine.GiftManager;
import fr.swartz.sdonations.utils.jsons.JsonPersist;
import fr.swartz.sdonations.utils.jsons.adapters.ItemStackAdapter;
import fr.swartz.sdonations.utils.jsons.adapters.LocationAdapter;
import fr.swartz.sdonations.utils.jsons.adapters.PotionEffectAdapter;
import fr.swartz.sdonations.utils.menu.MenuManager;
import fr.swartz.sdonations.utils.menu.commands.CommandFramework;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Modifier;
import java.util.List;

public class SDonations extends JavaPlugin {

    private @Getter static SDonations instance;
    private @Getter CommandFramework framework;
    private @Getter Gson gson;
    private List<JsonPersist> persists = Lists.newArrayList();

    @Override
    public void onEnable() {

        saveDefaultConfig();
        instance = this;
        this.framework = new CommandFramework(this);
        this.getDataFolder().mkdir();
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter(this)).registerTypeAdapter(PotionEffect.class, new PotionEffectAdapter(this)).registerTypeAdapter(Location.class, new LocationAdapter(this)).create();
        MenuManager.getInstance().register(this);

        this.registerPersist(new GiftManager(this));

        for (JsonPersist persist : this.persists) {
            persist.loadData();
        }

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§csDonations §7- [§cON§7]");
        Bukkit.getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§bNomeliaCore §7- [§cOFF§7]");
        Bukkit.getConsoleSender().sendMessage("");

        this.persists.forEach(p -> {
            try {
                p.saveData(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void registerListener(Listener listener) {

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(listener, this);
    }

    public void registerPersist(JsonPersist persist)
    {
        this.persists.add(persist);
    }
}

