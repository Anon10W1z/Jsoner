package io.github.anon10w1z.jsoner.blocks;

import com.google.common.collect.Lists;
import io.github.anon10w1z.jsoner.items.JsonerItem;
import io.github.anon10w1z.jsoner.items.JsonerRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
	private List<JsonerRecipe> recipes = Lists.newArrayList();

	private JsonerBlock(Block block, int metadata, boolean showRecipes) {
		this.id = Block.getIdFromBlock(block);
		this.metadata = metadata;
		ItemStack itemstack = new ItemStack(block, 1, metadata);
		this.unlocalizedName = itemstack.getItem() == null ? block.getUnlocalizedName().replaceFirst("tile.", "") : itemstack.getUnlocalizedName().replaceFirst("tile.", "");
		this.localizedName = itemstack.getItem() == null ? "N/A" : itemstack.getDisplayName();
		try {
			CreativeTabs creativeTab = ReflectionHelper.getPrivateValue(CreativeTabs.class, (CreativeTabs) ReflectionHelper.getPrivateValue(Block.class, block, "displayOnCreativeTab", "field_149772_a"), "tabLabel", "field_78034_o");
		} catch (Exception e) {
			this.creativeTab = "none";
		}
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
					Function<ItemStack, Object> stackToJsonerFunction = stack -> stack == null ? null : stack.getItem() instanceof ItemBlock ? JsonerBlock.ofNoRecipes(((ItemBlock) stack.getItem()).block, stack.getMetadata()) : JsonerItem.ofNoRecipes(stack.getItem(), stack.getMetadata());
					if (recipeObject instanceof ShapedRecipes)
						inputs.addAll(Arrays.stream(((ShapedRecipes) recipeObject).recipeItems).map(stackToJsonerFunction).collect(Collectors.toList()));
					if (recipeObject instanceof ShapelessRecipes)
						((ShapelessRecipes) recipeObject).recipeItems.stream().filter(itemstackObject -> itemstackObject != null).forEach(itemstackObject -> inputs.add(stackToJsonerFunction.apply((ItemStack) itemstackObject)));

					this.recipes.add(JsonerRecipe.of(inputs, recipeOutput.stackSize));
				}
			}
	}

	public static JsonerBlock of(Block block, int metadata) {
		return new JsonerBlock(block, metadata, true);
	}

	public static JsonerBlock ofNoRecipes(Block block, int metadata) {
		JsonerBlock jsonerBlock = of(block, metadata);
		jsonerBlock.recipes.clear();
		return jsonerBlock;
	}
}
