package net.frozenblock.configurabledatafixers.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.frozenblock.configurabledatafixers.config.DataFixerConfig;
import net.minecraft.nbt.CompoundTag;
import java.util.List;

public record NbtFixer(String key, List<NbtKeyFixer> fixers) {

	public static final Codec<NbtFixer> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.STRING.fieldOf("key").forGetter(NbtFixer::key),
					Codec.list(NbtKeyFixer.CODEC).fieldOf("fixers").forGetter(NbtFixer::fixers)
			).apply(instance, NbtFixer::new)
	);

	public static void fixCompound(CompoundTag tag) {
		var config = DataFixerConfig.get().config();
		var nbtFixers = config.nbtFixers.value();
		for (var nbtFixer : nbtFixers) {
			var key = nbtFixer.key();
			if (tag.contains(key)) {
				var tagToFix = tag.getCompound(key);
				var fixers = nbtFixer.fixers();
				DataFixerSharedConstants.log("Fixing NBT tag " + key + "...", DataFixerSharedConstants.UNSTABLE_LOGGING);
				for (var keyFixer : fixers) {
					if (tagToFix.contains(keyFixer.oldKey(), 10)) {
						var oldKey = keyFixer.oldKey();
						var newKey = keyFixer.newKey();
						var oldTag = tagToFix.getCompound(oldKey);

						DataFixerSharedConstants.log(
								"Fixing key " + oldKey + " to " + newKey +
										" in NBT tag " + key + "...",
								DataFixerSharedConstants.UNSTABLE_LOGGING
						);

						tagToFix.remove(oldKey);
						tagToFix.put(newKey, oldTag);
					}
				}
			}
		}
	}
}
