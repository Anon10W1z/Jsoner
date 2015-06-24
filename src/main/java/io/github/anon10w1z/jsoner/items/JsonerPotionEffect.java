package io.github.anon10w1z.jsoner.items;

import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class JsonerPotionEffect {
	private String effectName;
	private int duration;
	private int amplifier;
	private boolean isSplash;
	private List<JsonerItem> curativeItems;

	private JsonerPotionEffect(PotionEffect potionEffect) {
		this.effectName = StatCollector.translateToLocal(potionEffect.getEffectName());
		this.duration = potionEffect.getDuration();
		this.amplifier = potionEffect.getAmplifier();
		this.isSplash = potionEffect.toString().contains("Splash: true");
		this.curativeItems = potionEffect.getCurativeItems().stream().map(itemstack -> JsonerItem.of(itemstack.getItem(), itemstack.getMetadata())).collect(Collectors.toList());
	}

	public static JsonerPotionEffect of(PotionEffect potionEffect) {
		return new JsonerPotionEffect(potionEffect);
	}
}
