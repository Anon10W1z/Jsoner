package io.github.anon10w1z.jsoner.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Wrapper class for metadata blocks
 */
@SuppressWarnings("all")
public class JsonerMetadataBlock {
	private List<JsonerBlock> subBlocks = Lists.newArrayList();

	private JsonerMetadataBlock(Block block) {
		List<ItemStack> blockSubBlocks = Lists.newArrayList();
		block.getSubBlocks(Item.getItemFromBlock(block), null, blockSubBlocks);
		List<Integer> validMetadata = Lists.newArrayList();
		for (ItemStack itemstack : blockSubBlocks)
			if (!validMetadata.contains(itemstack.getMetadata()))
				validMetadata.add(itemstack.getMetadata());
		for (int metadata : validMetadata)
			subBlocks.add(JsonerBlock.of(block, metadata));
	}

	public static JsonerMetadataBlock of(Block block) {
		return new JsonerMetadataBlock(block);
	}
}
