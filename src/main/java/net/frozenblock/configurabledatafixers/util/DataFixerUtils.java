package net.frozenblock.configurabledatafixers.util;

import net.frozenblock.configurabledatafixers.config.DataFixerConfig;
import java.util.ArrayList;
import java.util.List;

public class DataFixerUtils {

	private static final List<SchemaEntry> SCHEMAS = new ArrayList<>();

	private static final List<RegistryFixer> REGISTRY_FIXERS = new ArrayList<>();

	public static void addSchema(SchemaEntry schema) {
		SCHEMAS.add(schema);
	}

	public static void addRegistryFixer(RegistryFixer fixer) {
		REGISTRY_FIXERS.add(fixer);
	}

	public static List<SchemaEntry> getSchemas() {
		var list = new ArrayList<>(SCHEMAS);
		list.addAll(DataFixerConfig.get().config().schemas.value());
		return list;
	}

	public static List<RegistryFixer> getRegistryFixers() {
		var list = new ArrayList<>(REGISTRY_FIXERS);
		list.addAll(DataFixerConfig.get().config().registryFixers.value());
		return list;
	}
}
