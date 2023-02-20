package net.frozenblock.configurabledatafixers.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record NbtKeyFixer(String oldKey, String newKey) {

	public static final Codec<NbtKeyFixer> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.STRING.fieldOf("old_id").forGetter(NbtKeyFixer::oldKey),
					Codec.STRING.fieldOf("new_id").forGetter(NbtKeyFixer::newKey)
			).apply(instance, NbtKeyFixer::new)
	);
}
