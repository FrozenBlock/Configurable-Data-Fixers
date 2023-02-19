package net.frozenblock.configurabledatafixers.mixin;

import com.mojang.serialization.Lifecycle;
import net.frozenblock.configurabledatafixers.config.DataFixerConfig;
import net.frozenblock.configurabledatafixers.util.DataFixerSharedConstants;
import net.frozenblock.configurabledatafixers.util.Fixer;
import net.frozenblock.configurabledatafixers.util.RegistryFixer;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> extends WritableRegistry<T> {

	@Shadow
	@Final
	private Map<ResourceLocation, Holder.Reference<T>> byLocation;

	@Shadow
	@Nullable
	private static <T> T getValueFromNullable(Holder.@Nullable Reference<T> reference) {
		return null;
	}

	public MappedRegistryMixin(ResourceKey<? extends Registry<T>> resourceKey, Lifecycle lifecycle) {
		super(resourceKey, lifecycle);
	}

	@ModifyVariable(
			method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/core/MappedRegistry;getValueFromNullable(Lnet/minecraft/core/Holder$Reference;)Ljava/lang/Object;"
			)
	)
	private Holder.Reference<T> fixedValue(Holder.Reference<T> original, ResourceLocation key) {
		if (original == null) {
			var fixed = fixValues(key);
			return this.byLocation.get(fixed);
		}
		return original;
	}

	@Inject(
			method = "get(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;",
			at = @At("RETURN"),
			cancellable = true
	)
	private void fixedValue(@Nullable ResourceKey<T> key, CallbackInfoReturnable<@Nullable T> cir) {
		if (key != null && cir.getReturnValue() == null) {
			var fixed = fixValues(key.location());
			cir.setReturnValue(getValueFromNullable(this.byLocation.get(fixed)));
		}
	}

	@Unique
	private ResourceLocation fixValues(@Nullable ResourceLocation name) {
		var instance = DataFixerConfig.get();
		var config = instance.config();
		var registryFixers = config.registryFixers;
		DataFixerSharedConstants.log("Running fixer for ID " + name, DataFixerSharedConstants.UNSTABLE_LOGGING);
		var fixers = registryFixers.value();
		for (RegistryFixer registryFixer : fixers) {
			if (registryFixer.registryKey() == this.key().location()) {
				DataFixerSharedConstants.log("Value fixer for registry " + this.key().location() + " ran", DataFixerSharedConstants.UNSTABLE_LOGGING);
				for (Fixer fixer : registryFixer.fixers()) {
					DataFixerSharedConstants.log("Fixer for old_id: " + fixer.oldId() + " new_id: " + fixer.newId() + " ran whilst fixing " + name, DataFixerSharedConstants.UNSTABLE_LOGGING);
					if (fixer.oldId() == name) {
						DataFixerSharedConstants.log("Changed old ID " + name + " to new ID " + fixer.newId(), DataFixerSharedConstants.UNSTABLE_LOGGING);
						return fixer.newId();
					}
				}
			}
		}
		return null;
	}
}
