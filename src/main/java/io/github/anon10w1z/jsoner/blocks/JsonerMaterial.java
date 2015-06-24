package io.github.anon10w1z.jsoner.blocks;

import net.minecraft.block.material.Material;

/**
 * Wrapper class for block materials
 */
@SuppressWarnings("all")
public class JsonerMaterial {
	private boolean canBurn;
	private boolean replaceable;
	private boolean opaque;
	private boolean toolRequired;
	private boolean isLiquid;

	protected JsonerMaterial(Material material) {
		this.canBurn = material.getCanBurn();
		this.replaceable = material.isReplaceable();
		this.opaque = material.isOpaque();
		this.toolRequired = !material.isToolNotRequired();
		this.isLiquid = material.isLiquid();
	}
}
