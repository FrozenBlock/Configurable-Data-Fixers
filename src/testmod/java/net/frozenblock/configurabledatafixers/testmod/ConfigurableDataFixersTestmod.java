package net.frozenblock.configurabledatafixers.testmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.frozenblock.lib.item.api.FrozenCreativeTabs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
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
		var testItem = Registry.register(BuiltInRegistries.ITEM, id("test_item2"), new Item(new FabricItemSettings()));
		FrozenCreativeTabs.add(testItem, CreativeModeTabs.TOOLS_AND_UTILITIES);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
