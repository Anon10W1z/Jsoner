package io.github.anon10w1z.jsoner.main;

import com.cedarsoftware.util.io.JsonWriter;
import com.google.common.collect.Maps;
import io.github.anon10w1z.jsoner.blocks.JsonerBlock;
import io.github.anon10w1z.jsoner.blocks.JsonerMetadataBlock;
import io.github.anon10w1z.jsoner.items.JsonerItem;
import io.github.anon10w1z.jsoner.items.JsonerMetadataItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main mod file of Jsoner
 */
@Mod(modid = "jsoner", version = "2.1", name = "Jsoner", dependencies = "after:*", clientSideOnly = true)
@SuppressWarnings("unused")
public final class Jsoner {
	/**
	 * Jsoner's logger
	 */
	private static Logger logger;
	/**
	 * The folder Jsoner operates in
	 */
	private static File jsonerFolder;
	/**
	 * Arguments passed to JsonWriter when writing blocks/items
	 */
	private static Map<String, Object> writerArgs = new HashMap<>();

	/**
	 * Called to pre-initialize Jsoner
	 *
	 * @param event The FMLPreInitializationEvent
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog(); //initialize the logger
		logger.info("Setting mod metadata");
		ModMetadata modMetadata = event.getModMetadata();
		modMetadata.modId = "jsoner";
		modMetadata.version = "1.0";
		modMetadata.name = "Jsoner";
		modMetadata.authorList = Collections.singletonList("Anon10W1z");
		modMetadata.description = "Converts Minecraft's items and blocks into human-readable JSON format";
		//prepare the Jsoner folder
		jsonerFolder = new File(event.getModConfigurationDirectory().getParent() + "\\jsoner");
		if (!jsonerFolder.exists()) {
			logger.info("Creating Jsoner directory");
			jsonerFolder.mkdir();
		} else
			try {
				logger.info("Emptying Jsoner directory");
				FileUtils.cleanDirectory(jsonerFolder);
			} catch (Exception e) {
				logger.error("Emptying Jsoner directory failed with an exception?");
				e.printStackTrace();
			}
		logger.info("Finished pre-initialization");
	}

	/**
	 * Called to initialize Jsoner
	 *
	 * @param event The FMLInitializationEvent
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		//if json-io isn't on the classpath, download it
		try {
			Class.forName("com.cedarsoftware.util.io.JsonWriter");
		} catch (ClassNotFoundException e) {
			logger.warn("json-io was not found in your mods folder. Jsoner has been disabled.");
			logger.info("json-io will be automatically downloaded now.");
			try {
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpGet httpGet = new HttpGet("https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.cedarsoftware&a=json-io&v=LATEST");
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity entity = httpResponse.getEntity();
				FileOutputStream fileOutputStream = new FileOutputStream(new File(jsonerFolder.getParent() + "\\mods\\json-io.jar"));
				entity.writeTo(fileOutputStream);
				logger.info("json-io was successfully downloaded. Restart Minecraft to enable Jsoner.");
			} catch (Exception e1) {
				logger.error("Downloading json-io failed with an exception");
				e1.printStackTrace();
			}
			return;
		}
		//initialize the JsonWriter arguments
		writerArgs.put(JsonWriter.PRETTY_PRINT, true);
		writerArgs.put(JsonWriter.SHORT_META_KEYS, true);
		writerArgs.put(JsonWriter.TYPE, false);
		//finally, write blocks and items to JSON
		this.writeBlocks();
		this.writeItems();
		logger.info("Finished initialization");
	}

	/**
	 * Writes all blocks to JSON
	 */
	@SuppressWarnings("unchecked")
	private void writeBlocks() {
		logger.info("Writing blocks to JSON");
		try {
			File blocksFolder = new File(jsonerFolder.getPath() + "\\blocks");
			blocksFolder.mkdir();
			Map<String, Integer> blockNameToCount = Maps.newHashMap();
			for (Object blockObject : Block.blockRegistry)
				blockNameToCount.put(((Block) blockObject).getUnlocalizedName(), 1);
			for (Object blockObject : Block.blockRegistry) {
				Block block = (Block) blockObject;
				String unlocalizedName = block.getUnlocalizedName();
				if (unlocalizedName != null && !unlocalizedName.equals("") && !unlocalizedName.replaceFirst("tile.", "").equals("null")) {
					File blockFile = new File(blocksFolder.getPath() + "\\" + unlocalizedName.replaceFirst("tile.", "") + ".json");
					if (blockFile.exists()) {
						blockFile = new File(FilenameUtils.removeExtension(blockFile.getPath()) + "_" + blockNameToCount.get(block.getUnlocalizedName()) + ".json");
						blockNameToCount.put(block.getUnlocalizedName(), blockNameToCount.get(block.getUnlocalizedName()) + 1);
					}
					blockFile.createNewFile();
					OutputStream outputStream = new FileOutputStream(blockFile);
					JsonWriter jsonWriter = new JsonWriter(outputStream, writerArgs);
					List<IBlockState> stateList = block.getBlockState().getValidStates();
					int maxMetadata = 0;
					for (Object blockStateObject : stateList)
						maxMetadata = Math.max(maxMetadata, block.getMetaFromState((IBlockState) blockStateObject));
					if (maxMetadata == 0)
						jsonWriter.write(JsonerBlock.of(block, 0, true));
					else
						jsonWriter.write(JsonerMetadataBlock.of(block));
					jsonWriter.close();
				}
			}
		} catch (Exception e) {
			logger.error("Writing blocks to JSON failed with an exception");
			e.printStackTrace();
		}
	}

	/**
	 * Writes all items to JSON
	 */
	private void writeItems() {
		logger.info("Writing items to JSON");
		try {
			File itemsFolder = new File(jsonerFolder.getPath() + "\\items");
			itemsFolder.mkdir();
			Map<String, Integer> itemNameToCount = Maps.newHashMap();
			for (Object itemObject : Item.itemRegistry)
				itemNameToCount.put(((Item) itemObject).getUnlocalizedName(), 1);
			for (Object itemObject : Item.itemRegistry) {
				Item item = (Item) itemObject;
				if (item.getUnlocalizedName() != null && !item.getUnlocalizedName().equals("") && !(item instanceof ItemBlock)) {
					File itemFile = new File(itemsFolder.getPath() + "\\" + item.getUnlocalizedName().replaceFirst("item.", "") + ".json");
					if (itemFile.exists()) {
						itemFile = new File(FilenameUtils.removeExtension(itemFile.getPath()) + "_" + itemNameToCount.get(item.getUnlocalizedName()) + ".json");
						itemNameToCount.put(item.getUnlocalizedName(), itemNameToCount.get(item.getUnlocalizedName()) + 1);
					}
					itemFile.createNewFile();
					OutputStream outputStream = new FileOutputStream(itemFile);
					JsonWriter jsonWriter = new JsonWriter(outputStream, writerArgs);
					if (!item.getHasSubtypes())
						jsonWriter.write(JsonerItem.of(item, 0, true));
					else
						jsonWriter.write(JsonerMetadataItem.of(item));
					jsonWriter.close();
				}
			}
		} catch (Exception e) {
			logger.error("Writing items to JSON failed with an exception");
			e.printStackTrace();
		}
	}
}
