package io.github.anon10w1z.jsoner.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings("all")
public class JsonerItemFood extends JsonerItem {
	private int healAmount;
	private float saturationModifier;
	private boolean isWolfFood;
	private JsonerPotionEffect potionEffect;
	private float potionEffectProbability;

	protected JsonerItemFood(Item item, int metadata, boolean showRecipes) {
		super(item, metadata, showRecipes);
		ItemFood food = (ItemFood) item;
		ItemStack foodStack = new ItemStack(food, 1, metadata);
		this.healAmount = food.getHealAmount(foodStack);
		this.saturationModifier = food.getSaturationModifier(foodStack);
		this.isWolfFood = food.isWolfsFavoriteMeat();
		Integer potionId = ReflectionHelper.getPrivateValue(ItemFood.class, food, "potionId", "field_77851_ca");
		Integer potionDuration = ReflectionHelper.getPrivateValue(ItemFood.class, food, "potionDuration", "field_77850_cb");
		Integer potionAmplifier = ReflectionHelper.getPrivateValue(ItemFood.class, food, "potionAmplifier", "field_77857_cc");
		if (Potion.potionTypes[potionId] != null)
			this.potionEffect = JsonerPotionEffect.of(new PotionEffect(potionId, potionDuration * 20, potionAmplifier));
		this.potionEffectProbability = ReflectionHelper.getPrivateValue(ItemFood.class, food, "potionEffectProbability", "field_77858_cd");
	}
}
