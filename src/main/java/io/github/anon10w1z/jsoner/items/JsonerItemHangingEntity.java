package io.github.anon10w1z.jsoner.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHangingEntity;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings("all")
public class JsonerItemHangingEntity extends JsonerItem {
	private String hangingEntity;

	protected JsonerItemHangingEntity(Item item, int metadata) {
		super(item, metadata, true);
		Class entityClass = ReflectionHelper.getPrivateValue(ItemHangingEntity.class, (ItemHangingEntity) item, "hangingEntityClass", "field_82811_a");
		this.hangingEntity = entityClass.getName().substring(entityClass.getName().lastIndexOf('.') + 1);
	}
}
