package fr.swartz.sdonations.utils.jsons.adapters;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import fr.swartz.sdonations.SDonations;
import fr.swartz.sdonations.utils.jsons.GsonAdapter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ItemStackAdapter extends GsonAdapter<ItemStack> {

	private static Type seriType = new TypeToken<Map<String,Object>>() {}.getType();

	public ItemStackAdapter(SDonations plugin) {
		super(plugin);
	}
	
	public void write(JsonWriter jsonWriter, org.bukkit.inventory.ItemStack itemStack) throws IOException {
		if (itemStack == null) {
			jsonWriter.nullValue();
			return;
		}
		jsonWriter.value(getRaw(removeSlotNBT(itemStack)));
	}

	public org.bukkit.inventory.ItemStack read(JsonReader jsonReader) throws IOException {
		if (jsonReader.peek() == JsonToken.NULL) {
			jsonReader.nextNull();
			return null;
		}
		return fromRaw(jsonReader.nextString());
	}

	@SuppressWarnings("unchecked")
	private String getRaw(org.bukkit.inventory.ItemStack item) {
		Map<String, Object> serial = item.serialize();

		if (serial.get("meta") != null) {
			ItemMeta itemMeta = item.getItemMeta();

			Map<String, Object> originalMeta = itemMeta.serialize();
			Map<String, Object> meta = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry : originalMeta.entrySet()) {
				meta.put((String) entry.getKey(), entry.getValue());
			}
			for (Object entry : meta.entrySet()) {
				Object o = ((Map.Entry<String, Object>) entry).getValue();
				if ((o instanceof ConfigurationSerializable)) {
					ConfigurationSerializable serializable = (ConfigurationSerializable) o;
					Map<String, Object> serialized = recursiveSerialization(serializable);
					meta.put((String) ((Map.Entry<String, Object>) entry).getKey(), serialized);
				}
			}
			serial.put("meta", meta);
		}

		return this.getGson().toJson(serial);
	}

	private org.bukkit.inventory.ItemStack fromRaw(String raw) {
		Map<String, Object> keys = this.getGson().fromJson(raw, seriType);

		if (keys.get("amount") != null) {
			Double d = (Double) keys.get("amount");
			Integer i = Integer.valueOf(d.intValue());
			keys.put("amount", i);
		}
		org.bukkit.inventory.ItemStack item = null;
		try {
			item = org.bukkit.inventory.ItemStack.deserialize(keys);
		} catch (Exception e) {
			return null;
		}
		if (item == null) {
			return null;
		}
		if (keys.containsKey("meta")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> itemmeta = (Map<String, Object>) keys.get("meta");
			itemmeta = recursiveDoubleToInteger(itemmeta);
			ItemMeta meta = (ItemMeta) ConfigurationSerialization.deserializeObject(itemmeta,
					ConfigurationSerialization.getClassByAlias("ItemMeta"));
			item.setItemMeta(meta);
		}
		return item;
	}

	private static org.bukkit.inventory.ItemStack removeSlotNBT(org.bukkit.inventory.ItemStack item) {
		if (item == null)
			return null;
		net.minecraft.server.v1_8_R3.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		if (nmsi == null)
			return null;
		NBTTagCompound nbtt = nmsi.getTag();
		if (nbtt != null) {
			nbtt.remove("Slot");
			nmsi.setTag(nbtt);
		}
		return CraftItemStack.asBukkitCopy(nmsi);
	}

	private static Map<String, Object> recursiveDoubleToInteger(Map<String, Object> originalMap) {
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
			Object o = entry.getValue();
			if (o instanceof Double) {
				Double d = (Double) o;
				Integer i = Integer.valueOf(d.intValue());
				map.put((String) entry.getKey(), i);
			} else if (o instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> subMap = (Map<String, Object>) o;
				map.put((String) entry.getKey(), recursiveDoubleToInteger(subMap));
			} else {
				map.put((String) entry.getKey(), o);
			}
		}
		return map;
	}

	private static Map<String, Object> recursiveSerialization(ConfigurationSerializable o) {
		Map<String, Object> originalMap = o.serialize();
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
			Object o2 = entry.getValue();
			if ((o2 instanceof ConfigurationSerializable)) {
				ConfigurationSerializable serializable = (ConfigurationSerializable) o2;
				Map<String, Object> newMap = recursiveSerialization(serializable);
				newMap.put("SERIAL-ADAPTER-CLASS-KEY", ConfigurationSerialization.getAlias(serializable.getClass()));
				map.put((String) entry.getKey(), newMap);
			}
		}
		map.put("SERIAL-ADAPTER-CLASS-KEY", ConfigurationSerialization.getAlias(o.getClass()));
		return map;
	}
}
