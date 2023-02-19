package net.frozenblock.configurabledatafixers.config;

import com.mojang.serialization.Codec;
import java.util.List;
import net.frozenblock.configurabledatafixers.util.DataFixerSharedConstants;
import net.frozenblock.configurabledatafixers.util.Fixer;
import net.frozenblock.configurabledatafixers.util.RegistryFixer;
import net.frozenblock.lib.config.api.entry.Exclude;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.gson.GsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class DataFixerConfig {

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

	public TypedEntry<List<RegistryFixer>> registryFixers = new TypedEntry<>(
			REGISTRY_FIXER_LIST,
			List.of(
					new RegistryFixer(
							Registry.BLOCK_REGISTRY.location(),
							List.of(
									new Fixer(
											new ResourceLocation("examplemod:example_block"),
											new ResourceLocation("stone")
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
					),
					new RegistryFixer(
							Registry.BIOME_REGISTRY.location(),
							List.of(
									new Fixer(
											new ResourceLocation("examplemod:example_biome"),
											new ResourceLocation("minecraft:plains")
									)
							)
					)
			)
	);

	public static Config<DataFixerConfig> get() {
		return INSTANCE;
	}
}
