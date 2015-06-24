package io.github.anon10w1z.jsoner.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Wrapper class for items
 */
@SuppressWarnings("all")
public class JsonerItem {
	private int id;
	private int metadata;
	private String unlocalizedName;
	private String localizedName;
	private String creativeTab;
	private int maxStackSize;
	private int maxDamage;
	private boolean render3D;
	private boolean hasSubtypes;
	private String useAction;
	private boolean isPotionIngredient;
	private boolean isBeaconPayment;
	private boolean repairable;

	protected JsonerItem(Item item, int metadata) {
		this.id = Item.getIdFromItem(item);
		this.metadata = metadata;
		ItemStack itemstack = new ItemStack(item, 1, metadata);
		this.unlocalizedName = itemstack.getUnlocalizedName().replaceFirst("item.", "");
		String localizedName = itemstack.getDisplayName();
		this.localizedName = localizedName.equals(itemstack.getUnlocalizedName() + ".name") ? "N/A" : localizedName;
		try {
			this.creativeTab = ReflectionHelper.getPrivateValue(CreativeTabs.class, (CreativeTabs) ReflectionHelper.getPrivateValue(Item.class, item, "tabToDisplayOn", "field_77701_a"), "tabLabel", "field_78034_o");
		} catch (Exception e) {
			this.creativeTab = "none";
		}
		this.maxStackSize = itemstack.getMaxStackSize();
		this.maxDamage = itemstack.getMaxDamage();
		this.render3D = ReflectionHelper.getPrivateValue(Item.class, item, "bFull3D", "field_77789_bW");
		this.hasSubtypes = itemstack.getHasSubtypes();
		this.useAction = itemstack.getItemUseAction().name();
		this.isPotionIngredient = item.isPotionIngredient(itemstack);
		this.isBeaconPayment = item.isBeaconPayment(itemstack);
		this.repairable = item.isRepairable();
	}

	public static JsonerItem of(Item item, int metadata) {
		if (item instanceof ItemArmor)
			return new JsonerItemArmor(item);
		if (item instanceof ItemTool)
			return new JsonerItemTool(item);
		if (item instanceof ItemPotion)
			return new JsonerItemPotion(item, metadata);
		if (item instanceof ItemFood)
			return new JsonerItemFood(item, metadata);
		return new JsonerItem(item, metadata);
	}
}
