package io.github.anon10w1z.jsoner.blocks;

import com.google.common.collect.Lists;
import io.github.anon10w1z.jsoner.items.JsonerItem;
import io.github.anon10w1z.jsoner.recipes.JsonerShapedRecipe;
import io.github.anon10w1z.jsoner.recipes.JsonerShapelessRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
 * Wrapper class for blocks
 */
@SuppressWarnings("all")
public class JsonerBlock {
	private int id;
	private int metadata;
	private String unlocalizedName;
	private String localizedName;
	private String creativeTab;
	private boolean isFullBlock;
	private int lightOpacity;
	private int lightValue;
	private float hardness;
	private float blastResistance;
	private boolean isTileEntity;
	private JsonerMaterial material;
	private JsonerSoundType stepSound;
	private float slipperiness;
	private boolean hasGravity;
	private boolean hasItem;
	private int fuelValue;
	private int flammability;
	private boolean isBeaconBase;
	private float enchantingPower;
	private List<Object> recipes = Lists.newArrayList();

	private JsonerBlock(Block block, int metadata, boolean showRecipes) {
		this.id = Block.getIdFromBlock(block);
		this.metadata = metadata;
		ItemStack itemstack = new ItemStack(block, 1, metadata);
		this.unlocalizedName = itemstack.getItem() == null ? block.getUnlocalizedName().replaceFirst("tile.", "") : itemstack.getUnlocalizedName().replaceFirst("tile.", "");
		this.localizedName = itemstack.getItem() == null ? "N/A" : itemstack.getDisplayName();
		this.creativeTab = block.getCreativeTabToDisplayOn() == null ? "none" : block.getCreativeTabToDisplayOn().getTabLabel();
		this.isFullBlock = block.isFullBlock();
		this.lightOpacity = block.getLightOpacity();
		this.lightValue = block.getLightValue();
		this.hardness = block.getBlockHardness(null, null);
		this.blastResistance = block.getExplosionResistance(null);
		this.isTileEntity = block.hasTileEntity();
		this.material = new JsonerMaterial(block.getMaterial());
		this.stepSound = new JsonerSoundType(block.stepSound);
		this.slipperiness = block.slipperiness;
		this.hasGravity = block instanceof BlockFalling;
		this.hasItem = Item.getItemFromBlock(block) != null;
		this.fuelValue = TileEntityFurnace.getItemBurnTime(itemstack);
		this.flammability = block.getFlammability(null, null, null);
		this.isBeaconBase = block.isBeaconBase(null, null, null);
		this.enchantingPower = block.getEnchantPowerBonus(null, null);

		if (showRecipes)
			for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
				ItemStack recipeOutput = ((IRecipe) recipeObject).getRecipeOutput();
				if (recipeOutput != null && recipeOutput.getItem() == Item.getItemFromBlock(block) && recipeOutput.getMetadata() == metadata) {
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

	public static JsonerBlock of(Block block, int metadata, boolean showRecipes) {
		return new JsonerBlock(block, metadata, showRecipes);
	}
}
