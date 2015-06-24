package io.github.anon10w1z.jsoner.blocks;

import net.minecraft.block.Block.SoundType;

/**
 * Wrapper class for block sound types
 */
@SuppressWarnings("all")
public class JsonerSoundType {
	private String name;
	private float volume;
	private float frequency;

	protected JsonerSoundType(SoundType soundType) {
		this.name = soundType.soundName;
		this.volume = soundType.volume;
		this.frequency = soundType.frequency;
	}
}
