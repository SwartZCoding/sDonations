package fr.swartz.sdonations.engine;

import com.google.common.collect.Lists;
import fr.swartz.sdonations.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter @Setter
public class GiftUser {

    private String ownerName;
    private List<Gift> gifts;

    public GiftUser(String ownerName)
    {
        this.ownerName = ownerName;
        this.gifts = Lists.newArrayList();
    }

    public void addGift(Gift gift) {
        this.gifts.add(gift);
    }

    public void removeGift(Gift gift) {
        this.gifts.remove(gift);
    }

    public boolean hasGiftFrom(String sender)
    {
        return this.gifts.stream().anyMatch(gift -> gift.getSender().equalsIgnoreCase(sender));
    }

    public Gift getGiftSize(int index)
    {
        if (index >= this.gifts.size()) {
            return null;
        }
        return this.gifts.get(index);
    }

    public File getProfileFile() {
        return Utils.getFormatedFile("/gifts/" + this.ownerName + ".json");
    }

}
