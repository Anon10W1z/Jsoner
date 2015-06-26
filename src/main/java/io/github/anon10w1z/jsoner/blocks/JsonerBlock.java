package io.github.anon10w1z.jsoner.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

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
	}

	public static JsonerBlock of(Block block, int metadata, boolean showRecipes) {
		return new JsonerBlock(block, metadata, showRecipes);
	}
}
