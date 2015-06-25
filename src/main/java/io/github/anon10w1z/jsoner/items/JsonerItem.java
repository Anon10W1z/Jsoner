package io.github.anon10w1z.jsoner.items;

import com.google.common.collect.Lists;
import io.github.anon10w1z.jsoner.blocks.JsonerBlock;
import io.github.anon10w1z.jsoner.recipes.JsonerShapedRecipe;
import io.github.anon10w1z.jsoner.recipes.JsonerShapelessRecipe;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
	private List<Object> recipes = Lists.newArrayList();

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
		if (showRecipes)
			for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
				ItemStack recipeOutput = ((IRecipe) recipeObject).getRecipeOutput();
				if (recipeOutput != null && recipeOutput.getItem() == item && recipeOutput.getMetadata() == metadata) {
					List<Object> inputs = Lists.newArrayList();
					for (int i = 0; i < 9; ++i)
						inputs.add(null);
					Function<ItemStack, Object> stackToJsonerFunction = stack -> stack == null ? "empty" : (stack.getItem() instanceof ItemBlock ? JsonerBlock.of(((ItemBlock) stack.getItem()).block, stack.getMetadata(), false) : JsonerItem.of(stack.getItem(), stack.getMetadata(), false));
					if (recipeObject instanceof ShapedRecipes) {
						ShapedRecipes shapedRecipe = (ShapedRecipes) recipeObject;
						Object[] recipeItems = Arrays.stream(((ShapedRecipes) recipeObject).recipeItems).map(stackToJsonerFunction).toArray();
						if (recipeItems.length < 9) {
							Object[] newRecipeItems = new Object[9];
							for (int i = 0; i < recipeItems.length; ++i)
								newRecipeItems[i] = recipeItems[i];
							recipeItems = newRecipeItems;
						}
						this.recipes.add(JsonerShapedRecipe.of(recipeItems, recipeOutput.stackSize));
					}
					if (recipeObject instanceof ShapelessRecipes) {
						ShapelessRecipes shapelessRecipe = (ShapelessRecipes) recipeObject;
						this.recipes.add(JsonerShapelessRecipe.of((List<Object>) shapelessRecipe.recipeItems.stream().map(stackToJsonerFunction).collect(Collectors.toList()), recipeOutput.stackSize));
					}
					if (recipeObject instanceof ShapedOreRecipe) {
						Object[] input = ((ShapedOreRecipe) recipeObject).getInput();
						for (int i = 0; i < input.length; ++i)
							if (input[i] instanceof ItemStack)
								inputs.set(i, stackToJsonerFunction.apply((ItemStack) input[i]));
							else if (input[i] instanceof List) {
								List<Object> inputList = Lists.newArrayList();
								for (ItemStack inputStack : (List<ItemStack>) input[i])
									inputList.add(stackToJsonerFunction.apply(inputStack));
								inputList = inputList.stream().filter(input1 -> input1 != null).collect(Collectors.toList());
								inputs.set(i, inputList.size() > 0 ? inputList : inputList.get(0));
							}
						this.recipes.add(JsonerShapedRecipe.of(inputs.toArray(), recipeOutput.stackSize));
					}
					if (recipeObject instanceof ShapelessOreRecipe) {
						for (Object input : ((ShapelessOreRecipe) recipeObject).getInput())
							if (input instanceof ItemStack)
								inputs.add(stackToJsonerFunction.apply((ItemStack) input));
							else if (input instanceof List) {
								List<Object> inputList = Lists.newArrayList();
								for (ItemStack inputStack : (List<ItemStack>) input)
									inputList.add(stackToJsonerFunction.apply((ItemStack) inputStack));
							}
						this.recipes.add(JsonerShapelessRecipe.of(inputs, recipeOutput.stackSize));
					}
				}
			}
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
