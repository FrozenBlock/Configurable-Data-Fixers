package net.frozenblock.configurabledatafixers.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public record RegistryFixer(ResourceLocation registryKey, List<Fixer> fixers) {

	public static final Codec<RegistryFixer> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					ResourceLocation.CODEC.fieldOf("registry_key").forGetter(RegistryFixer::registryKey),
					Fixer.CODEC.listOf().fieldOf("fixers").forGetter(RegistryFixer::fixers)
			).apply(instance, RegistryFixer::new)
	);
}
