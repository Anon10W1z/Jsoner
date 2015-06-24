package io.github.anon10w1z.jsoner.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Wrapper class for armor
 */
@SuppressWarnings("all")
public class JsonerItemArmor extends JsonerItem {
	private int armorType;
	private int damageReduceAmount;
	private String armorMaterial;

	protected JsonerItemArmor(Item item) {
		super(item, 0);
		ItemArmor armor = (ItemArmor) item;
		this.armorType = armor.armorType;
		this.damageReduceAmount = armor.damageReduceAmount;
		this.armorMaterial = ReflectionHelper.getPrivateValue(ArmorMaterial.class, armor.getArmorMaterial(), "name", "field_179243_f");
	}
}
