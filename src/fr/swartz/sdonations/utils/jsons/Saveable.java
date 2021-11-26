package fr.swartz.sdonations.utils.jsons;

import fr.swartz.sdonations.SDonations;
import fr.swartz.sdonations.utils.menu.commands.CommandFramework;
import fr.swartz.sdonations.utils.menu.commands.ICommand;
import org.bukkit.event.Listener;

import java.io.File;

public abstract class Saveable implements JsonPersist {

    public boolean needDir, needFirstSave;

    public Saveable(SDonations plugin, String name) {
        this(plugin, name, false, false);
    }

    public Saveable(SDonations plugin, String name, boolean needDir, boolean needFirstSave) {
        this.needDir = needDir;
        this.needFirstSave = needFirstSave;
        if (this.needDir) {
            if (this.needFirstSave) {
                saveData(false);
            } else {
                File directory = getFile();
                if (!directory.exists()) {
                    try {
                        directory.mkdir();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }

    public void registerCommand(ICommand command) {
        SDonations plugin = SDonations.getInstance();
        CommandFramework framework = plugin.getFramework();
        framework.registerCommands(command);
    }

    public void registerListener(Listener listener) {
        SDonations plugin = SDonations.getInstance();
        plugin.registerListener(listener);
    }
}
