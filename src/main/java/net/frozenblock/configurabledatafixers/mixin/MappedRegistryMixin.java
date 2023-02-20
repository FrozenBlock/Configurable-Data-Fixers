package net.frozenblock.configurabledatafixers.mixin;

import com.mojang.serialization.Lifecycle;
import net.frozenblock.configurabledatafixers.util.RegistryFixer;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;

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

	@Inject(
			method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;",
			at = @At("RETURN"),
			cancellable = true
	)
	private void fixedValue(@Nullable ResourceLocation name, CallbackInfoReturnable<@Nullable T> cir) {
		if (name != null && cir.getReturnValue() == null) {
			var fixed = RegistryFixer.getFixedValueInRegistry(this, name);
			cir.setReturnValue(getValueFromNullable(this.byLocation.get(fixed)));
		}
	}

	@Inject(
			method = "get(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;",
			at = @At("RETURN"),
			cancellable = true
	)
	private void fixedValue(@Nullable ResourceKey<T> key, CallbackInfoReturnable<@Nullable T> cir) {
		if (key != null && cir.getReturnValue() == null) {
			var fixed = RegistryFixer.getFixedValueInRegistry(this, key.location());
			cir.setReturnValue(getValueFromNullable(this.byLocation.get(fixed)));
		}
	}
}
