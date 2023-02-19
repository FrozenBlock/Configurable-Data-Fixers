package net.frozenblock.configurabledatafixers;

import net.fabricmc.api.ModInitializer;
import net.frozenblock.configurabledatafixers.config.DataFixerConfig;
import net.frozenblock.configurabledatafixers.util.DataFixerSharedConstants;

public class ConfigurableDataFixers implements ModInitializer {

	@Override
	public void onInitialize() {
		DataFixerSharedConstants.startMeasuring(this);

		DataFixerConfig.get().load();

		for (var registryFixer : DataFixerConfig.get().config().registryFixers.value()) {
			for (var fixer : registryFixer.fixers()) {
				DataFixerSharedConstants.log(
						"Fixing ID " + fixer.oldId() + " to " + fixer.newId() +
						" in registry " + registryFixer.registryKey() + "...",
						DataFixerSharedConstants.UNSTABLE_LOGGING
				);
			}
		}

		DataFixerSharedConstants.stopMeasuring(this);
	}
}
