package io.github.anon10w1z.jsoner.items;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Wrapper class for metadata items
 */
@SuppressWarnings("all")
public class JsonerMetadataItem {
	private List<JsonerItem> subItems = Lists.newArrayList();

	private JsonerMetadataItem(Item item) {
		List<ItemStack> itemSubItems = Lists.newArrayList();
		item.getSubItems(item, null, itemSubItems);
		Set<Integer> validMetadata = Sets.newHashSet();
		for (ItemStack itemstack : itemSubItems)
			validMetadata.add(itemstack.getMetadata());
		for (int metadata : validMetadata)
			subItems.add(JsonerItem.of(item, metadata));
	}

	public static JsonerMetadataItem of(Item item) {
		return new JsonerMetadataItem(item);
	}
}
