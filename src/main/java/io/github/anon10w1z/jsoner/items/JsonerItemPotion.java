package io.github.anon10w1z.jsoner.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class JsonerItemPotion extends JsonerItem {
	private List effects;

	protected JsonerItemPotion(Item item, int metadata) {
		super(item, metadata);
		ItemPotion potion = (ItemPotion) item;
		List<PotionEffect> effects = potion.getEffects(metadata);
		if (effects == null)
			effects = new ArrayList<>();
		this.effects = effects.stream().map(JsonerPotionEffect::of).collect(Collectors.toList());
	}
}
