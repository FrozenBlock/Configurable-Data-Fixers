package net.frozenblock.configurabledatafixers.testmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ConfigurableDataFixersTestmod implements ModInitializer {

	public static final String MOD_ID = "configurabledatafixers_testmod";

	@Override
	public void onInitialize() {
		/*
		 * To test the data fixer, first create a world and give yourself the test item.
		 * Then, quit the world and change the id of the item here to something else.
		 * In the configuration, add a data fixer with the old id and the new id.
		 */
		Registry.register(Registry.ITEM, id("test_item2"), new Item(new FabricItemSettings().group(CreativeModeTab.TAB_MISC)));
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
