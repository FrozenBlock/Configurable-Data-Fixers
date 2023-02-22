package net.frozenblock.configurabledatafixers.testmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.frozenblock.configurabledatafixers.util.DataFixerSharedConstants;
import net.frozenblock.lib.worldgen.biome.api.FrozenOverworldBiomes;
import net.frozenblock.lib.worldgen.biome.api.parameters.Continentalness;
import net.frozenblock.lib.worldgen.biome.api.parameters.Depth;
import net.frozenblock.lib.worldgen.biome.api.parameters.Erosion;
import net.frozenblock.lib.worldgen.biome.api.parameters.Humidity;
import net.frozenblock.lib.worldgen.biome.api.parameters.Temperature;
import net.frozenblock.lib.worldgen.biome.api.parameters.Weirdness;
import net.frozenblock.lib.item.api.FrozenCreativeTabs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents;
import java.util.Set;

public class ConfigurableDataFixersTestmod implements ModInitializer {

	public static final String MOD_ID = "configurabledatafixers_testmod";

	public static final ResourceKey<Biome> TEST_BIOME = ResourceKey.create(Registries.BIOME, id("test_biome2"));

	@Override
	public void onInitialize() {
		/*
		 * To test the data fixer, first create a world and give yourself the test item.
		 * Then, quit the world and change the id of the item here to something else.
		 * In the configuration, add a data fixer with the old id and the new id.
		 */
		var testItem = Registry.register(BuiltInRegistries.ITEM, id("test_item"), new Item(new FabricItemSettings()));
		FrozenCreativeTabs.add(testItem, CreativeModeTabs.TOOLS_AND_UTILITIES);

		RegistryEvents.DYNAMIC_REGISTRY_SETUP.register(context -> {
			context.withRegistries(registries -> {
				DataFixerSharedConstants.log("Adding test biome", DataFixerSharedConstants.UNSTABLE_LOGGING);
				var placedFeatures = context.registryManager().lookupOrThrow(Registries.PLACED_FEATURE);
				var worldCarvers = context.registryManager().lookupOrThrow(Registries.CONFIGURED_CARVER);

				context.register(Registries.BIOME, TEST_BIOME.location(), () -> new Biome.BiomeBuilder()
						.temperature(0.5F)
						.precipitation(Biome.Precipitation.RAIN)
						.downfall(0.5F)
						.specialEffects(
								new BiomeSpecialEffects.Builder()
										.waterColor(0x3f76e4)
										.waterFogColor(0x050533)
										.fogColor(0xc0d8ff)
										.skyColor(0x77a9f8)
										.build()
						)
						.mobSpawnSettings(new MobSpawnSettings.Builder().build())
						.generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).build())
						.build()
				);
			}, Set.of(Registries.BIOME, Registries.PLACED_FEATURE, Registries.CONFIGURED_CARVER));
		});

		FrozenOverworldBiomes.addOverworldBiome(
				TEST_BIOME,
				Climate.parameters(
						Climate.Parameter.span(Temperature.NEUTRAL, Temperature.WARM),
						Humidity.NEUTRAL,
						Climate.Parameter.span(Continentalness.NEAR_INLAND, Continentalness.MID_INLAND),
						Erosion.FULL_RANGE,
						Depth.SURFACE,
						Weirdness.FULL_RANGE,
						0.0F
				)
		);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
