package net.frozenblock.configurabledatafixers.config;

import com.mojang.serialization.Codec;
import java.util.List;
import net.frozenblock.configurabledatafixers.util.DataFixerSharedConstants;
import net.frozenblock.configurabledatafixers.util.DataFixEntry;
import net.frozenblock.configurabledatafixers.util.Fixer;
import net.frozenblock.configurabledatafixers.util.RegistryFixer;
import net.frozenblock.configurabledatafixers.util.SchemaEntry;
import net.frozenblock.lib.config.api.entry.Exclude;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.gson.GsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class DataFixerConfig {

	private static final TypedEntryType<List<SchemaEntry>> SCHEMA_ENTRY_LIST = ConfigRegistry.register(
			new TypedEntryType<>(
					DataFixerSharedConstants.MOD_ID,
					Codec.list(SchemaEntry.CODEC)
			)
	);

	private static final TypedEntryType<List<RegistryFixer>> REGISTRY_FIXER_LIST = ConfigRegistry.register(
			new TypedEntryType<>(
					DataFixerSharedConstants.MOD_ID,
					Codec.list(RegistryFixer.CODEC)
			)
	);

	@Exclude
	private static final Config<DataFixerConfig> INSTANCE = ConfigRegistry.register(
			new GsonConfig<>(DataFixerSharedConstants.MOD_ID, DataFixerConfig.class)
	);

	public int dataVersion = 0;

	public TypedEntry<List<SchemaEntry>> dataFixes = new TypedEntry<>(
			SCHEMA_ENTRY_LIST,
			List.of(
					new SchemaEntry(
							1,
							List.of(
									new DataFixEntry(
											"biome",
											List.of(
													new Fixer(
															new ResourceLocation("examplemod:example_biome"),
															new ResourceLocation("minecraft:forest")
													),
													new Fixer(
															new ResourceLocation("examplemod:swampy_biome"),
															new ResourceLocation("minecraft:swamp")
													)
											)
									),
									new DataFixEntry(
											"block",
											List.of(
													new Fixer(
															new ResourceLocation("examplemod:dark_stone"),
															new ResourceLocation("minecraft:deepslate")
													)
											)
									)
							)
					),
					new SchemaEntry(
							2,
							List.of(
									new DataFixEntry(
											"block",
											List.of(
													new Fixer(
															new ResourceLocation("examplemod:old_block"),
															new ResourceLocation("minecraft:grass_block")
													)
											)
									)
							)
					)
			)
	);

	public TypedEntry<List<RegistryFixer>> registryFixers = new TypedEntry<>(
			REGISTRY_FIXER_LIST,
			List.of(
					new RegistryFixer(
							Registry.BLOCK_REGISTRY.location(),
							List.of(
									new Fixer(
											new ResourceLocation("examplemod:example_block"),
											new ResourceLocation("minecraft:stone")
									)
							)
					),
					new RegistryFixer(
							Registry.ENTITY_TYPE_REGISTRY.location(),
							List.of(
									new Fixer(
											new ResourceLocation("examplemod:example_entity"),
											new ResourceLocation("minecraft:cow")
									)
							)
					),
					new RegistryFixer(
							Registry.ITEM_REGISTRY.location(),
							List.of(
									new Fixer(
											new ResourceLocation("examplemod:example_item"),
											new ResourceLocation("minecraft:stone")
									)
							)
					)
			)
	);

	public static Config<DataFixerConfig> get() {
		return INSTANCE;
	}
}
