package io.github.anon10w1z.jsoner.items;

import com.google.common.collect.Lists;
import io.github.anon10w1z.jsoner.blocks.JsonerBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
	private List<JsonerRecipe> recipes = Lists.newArrayList();

	protected JsonerItem(Item item, int metadata, boolean showRecipes) {
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
		this.fuelValue = TileEntityFurnace.getItemBurnTime(itemstack);
		if (showRecipes)
			for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
				ItemStack recipeOutput = ((IRecipe) recipeObject).getRecipeOutput();
				if (recipeOutput != null && recipeOutput.getItem() == item && recipeOutput.getMetadata() == metadata) {
					List<Object> inputs = Lists.newArrayList();
					Function<ItemStack, Object> stackToJsonerFunction = stack -> stack == null ? null : stack.getItem() instanceof ItemBlock ? JsonerBlock.ofNoRecipes(((ItemBlock) stack.getItem()).block, stack.getMetadata()) : JsonerItem.ofNoRecipes(stack.getItem(), stack.getMetadata());
					if (recipeObject instanceof ShapedRecipes)
						inputs.addAll(Arrays.stream(((ShapedRecipes) recipeObject).recipeItems).map(stackToJsonerFunction).collect(Collectors.toList()));
					if (recipeObject instanceof ShapelessRecipes)
						((ShapelessRecipes) recipeObject).recipeItems.forEach(itemstackObject -> inputs.add(stackToJsonerFunction.apply((ItemStack) itemstackObject)));

					this.recipes.add(JsonerRecipe.of(inputs, recipeOutput.stackSize));
				}
			}
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
		if (item instanceof ItemHangingEntity)
			return new JsonerItemHangingEntity(item, metadata);
		return new JsonerItem(item, metadata, true);
	}

	public static JsonerItem ofNoRecipes(Item item, int metadata) {
		JsonerItem jsonerItem = of(item, metadata);
		jsonerItem.recipes.clear();
		return jsonerItem;
	}
}
