package io.github.anon10w1z.jsoner.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * Wrapper class for metadata blocks
 */
@SuppressWarnings("all")
public class JsonerMetadataBlock {
	private List<JsonerBlock> subBlocks = Lists.newArrayList();

	private JsonerMetadataBlock(Block block) {
		List<ItemStack> blockSubBlocks = Lists.newArrayList();
		block.getSubBlocks(Item.getItemFromBlock(block), null, blockSubBlocks);
		Set<Integer> validMetadata = Sets.newHashSet();
		for (ItemStack itemstack : blockSubBlocks)
			validMetadata.add(itemstack.getItem() == null ? 0 : itemstack.getMetadata());
		for (int metadata : validMetadata)
			subBlocks.add(JsonerBlock.of(block, metadata, true));
	}

	public static JsonerMetadataBlock of(Block block) {
		return new JsonerMetadataBlock(block);
	}
}
