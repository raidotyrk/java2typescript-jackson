package java2typescript.jackson.module.writer;

import java2typescript.jackson.module.grammar.EnumType;
import java2typescript.jackson.module.grammar.base.AbstractType;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class EnumTypeToStringValuedEnumWriter implements CustomAbstractTypeWriter {

	@Override
	public boolean accepts(AbstractType type, WriterPreferences preferences) {
		return type instanceof EnumType;
	}

	@Override
	public void writeDef(AbstractType type, Writer writer, WriterPreferences preferences) throws IOException {
		EnumType enumType = (EnumType) type;
		String enumTypeName = enumType.getName();
		writer.write(String.format("enum %s {\n", enumTypeName));
		preferences.increaseIndentation();
		List<String> enumConstants = enumType.getValues();
		if (preferences.isSort()) {
			enumConstants = SortUtil.sort(enumConstants);
		}
		for (String value : enumConstants) {
			writer.write(String.format(preferences.getIndentation() + "%s = '%s',\n", getConstantName(value), value));
		}
		preferences.decreaseIndention();
		writer.write(preferences.getIndentation() + "}");
	}

	String getConstantName(String value) {
		// lowercase "name" is special, can cause issues with JavaScript at runtime
		return "name".equals(value) ? "name_" : value;
	}
}
