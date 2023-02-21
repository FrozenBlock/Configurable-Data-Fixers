package net.frozenblock.configurabledatafixers.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.frozenblock.configurabledatafixers.config.DataFixerConfig;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public record RegistryFixer(ResourceLocation registryKey, List<Fixer> fixers) {

	public static final Codec<RegistryFixer> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					ResourceLocation.CODEC.fieldOf("registry_key").forGetter(RegistryFixer::registryKey),
					Fixer.CODEC.listOf().fieldOf("fixers").forGetter(RegistryFixer::fixers)
			).apply(instance, RegistryFixer::new)
	);

	public static ResourceLocation getFixedValueInRegistry(Registry<?> registry, @Nullable ResourceLocation name) {
		if (name != null) {
			var instance = DataFixerConfig.get();
			var config = instance.config();
			var registryFixers = config.registryFixers;
			var fixers = registryFixers.value();
			for (RegistryFixer registryFixer : fixers) {
				if (registryFixer.registryKey().equals(registry.key().location())) {
					for (Fixer fixer : registryFixer.fixers()) {
						if (fixer.oldId().equals(name)) {
							DataFixerSharedConstants.log("Successfully changed old ID " + name + " to new ID " + fixer.newId(), DataFixerSharedConstants.UNSTABLE_LOGGING);
							return fixer.newId();
						}
					}
				}
			}
		}
		return null;
	}
}
