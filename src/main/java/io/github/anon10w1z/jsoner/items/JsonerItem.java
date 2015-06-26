package io.github.anon10w1z.jsoner.items;

import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityFurnace;

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
	private int fuelValue;

	protected JsonerItem(Item item, int metadata, boolean showRecipes) {
		this.id = Item.getIdFromItem(item);
		this.metadata = metadata;
		ItemStack itemstack = new ItemStack(item, 1, metadata);
		this.unlocalizedName = itemstack.getUnlocalizedName().replaceFirst("item.", "");
		String localizedName = itemstack.getDisplayName();
		this.localizedName = localizedName.equals(itemstack.getUnlocalizedName() + ".name") ? "N/A" : localizedName;
		this.creativeTab = item.getCreativeTab() == null ? "none" : item.getCreativeTab().getTabLabel();
		this.maxStackSize = itemstack.getMaxStackSize();
		this.maxDamage = itemstack.getMaxDamage();
		this.render3D = item.isFull3D();
		this.hasSubtypes = itemstack.getHasSubtypes();
		this.useAction = itemstack.getItemUseAction().name();
		this.isPotionIngredient = item.isPotionIngredient(itemstack);
		this.isBeaconPayment = item.isBeaconPayment(itemstack);
		this.repairable = item.isRepairable();
		this.fuelValue = TileEntityFurnace.getItemBurnTime(itemstack);
	}

	public static JsonerItem of(Item item, int metadata, boolean showRecipes) {
		if (item instanceof ItemArmor)
			return new JsonerItemArmor(item, showRecipes);
		if (item instanceof ItemTool)
			return new JsonerItemTool(item, showRecipes);
		if (item instanceof ItemPotion)
			return new JsonerItemPotion(item, metadata, showRecipes);
		if (item instanceof ItemFood)
			return new JsonerItemFood(item, metadata, showRecipes);
		if (item instanceof ItemHangingEntity)
			return new JsonerItemHangingEntity(item, metadata, showRecipes);
		return new JsonerItem(item, metadata, showRecipes);
	}
}
