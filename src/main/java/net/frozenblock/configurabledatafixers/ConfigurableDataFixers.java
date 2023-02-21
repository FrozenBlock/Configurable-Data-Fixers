package net.frozenblock.configurabledatafixers;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.configurabledatafixers.config.DataFixerConfig;
import net.frozenblock.configurabledatafixers.util.DataFixerSharedConstants;
import net.frozenblock.configurabledatafixers.util.DataFixEntry;
import net.frozenblock.configurabledatafixers.util.Fixer;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.SimpleFixes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigurableDataFixers implements ModInitializer {

	@Override
	public void onInitialize() {
		DataFixerSharedConstants.startMeasuring(this);

		applyDataFixes(DataFixerSharedConstants.MOD_CONTAINER);

		DataFixerSharedConstants.stopMeasuring(this);
	}

	private static void applyDataFixes(final @NotNull ModContainer mod) {
		DataFixerSharedConstants.log("Applying configurable data fixes", DataFixerSharedConstants.UNSTABLE_LOGGING);
		var config = DataFixerConfig.get().config();
		if (config.schemas == null) {
			throw new IllegalStateException("Please update your config file!");
		}
		var schemas = config.schemas.value();
		var dataVersion = config.dataVersion;

		var builder = new QuiltDataFixerBuilder(dataVersion);

		var maxSchema = 0;
		List<Schema> addedSchemas = new ArrayList<>();
		if (schemas.size() > 0) {
			var base = builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA);
			addedSchemas.add(base);
		}

		for (var fix : schemas) {
			var version = fix.version();
			if (version > dataVersion) {
				DataFixerSharedConstants.error("Data fix version " + version + " is higher than the current data version " + dataVersion, true);
				continue;
			}

			if (version > maxSchema) {
				var schema = builder.addSchema(version, NamespacedSchema::new);
				addedSchemas.add(schema);
				maxSchema = version;
			}

			try {
				var schema = addedSchemas.get(version);

				for (var entry : fix.entries()) {
					for (var fixer : entry.fixers()) {
						handleFixer(builder, schema, entry, fixer);
					}
				}
			} catch (IndexOutOfBoundsException e) {
				DataFixerSharedConstants.error("Invalid data fix version: " + version, true);
			}
		}

		QuiltDataFixes.buildAndRegisterFixer(mod, builder);
		DataFixerSharedConstants.log(
				"Finished applying configurable data fixes"
				+ "\nData Version: " + dataVersion
				+ "\nMax schema: " + maxSchema,
				DataFixerSharedConstants.UNSTABLE_LOGGING
		);
	}

	private static void handleFixer(DataFixerBuilder builder, Schema schema, DataFixEntry entry, Fixer fixer) {
		var oldId = fixer.oldId();
		var newId = fixer.newId();

		var fixName = "fix_" + oldId + "_to_" + newId;

		switch (entry.type()) {
			case "biome" -> SimpleFixes.addBiomeRenameFix(builder, fixName, Map.of(oldId, newId), schema);
			case "block" -> SimpleFixes.addBlockRenameFix(builder, fixName, oldId, newId, schema);
			case "entity" -> SimpleFixes.addEntityRenameFix(builder, fixName, oldId, newId, schema);
			case "item" -> SimpleFixes.addItemRenameFix(builder, fixName, oldId, newId, schema);
			default -> DataFixerSharedConstants.error("Invalid data fix type: " + entry.type(), true);
		}
	}
}
