package io.github.anon10w1z.jsoner.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

/**
 * Wrapper class for tools
 */
@SuppressWarnings("all")
public class JsonerItemTool extends JsonerItem {
	private String toolMaterial;

	protected JsonerItemTool(Item item, boolean showRecipes) {
		super(item, 0, showRecipes);
		this.toolMaterial = ((ItemTool) item).getToolMaterialName();
	}
}
