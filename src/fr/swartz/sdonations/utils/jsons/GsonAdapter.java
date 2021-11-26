package fr.swartz.sdonations.utils.jsons;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import fr.swartz.sdonations.SDonations;

public abstract class GsonAdapter<T> extends TypeAdapter<T> {
	
  private SDonations plugin;
  
  public GsonAdapter(SDonations plugin) {
    this.plugin = plugin;
  }
  
  public Gson getGson() {
    return this.plugin.getGson();
  }
}
