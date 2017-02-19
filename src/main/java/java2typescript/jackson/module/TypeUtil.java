package java2typescript.jackson.module;

import com.fasterxml.jackson.databind.type.SimpleType;
import java2typescript.jackson.module.grammar.*;
import java2typescript.jackson.module.grammar.base.AbstractType;
import java2typescript.jackson.module.visitors.TSJsonFormatVisitorWrapper;

public class TypeUtil {
	public static AbstractType getTypeScriptTypeFromJavaClass(
			Class<?> clazz,
			Module module,
			TSJsonFormatVisitorWrapper tsJsonFormatVisitorWrapper) {
		if (clazz == boolean.class) {
			return BooleanType.getInstance();
		} else if (clazz == int.class) {
			return NumberType.getInstance();
		} else if (clazz == double.class) {
			return NumberType.getInstance();
		} else if (clazz == String.class) {
			return StringType.getInstance();
		} else if (clazz.isEnum()) {
			SimpleType jacksonType = SimpleType.construct(clazz);
			return tsJsonFormatVisitorWrapper.parseEnumOrGetFromCache(module, jacksonType);
		} else if (clazz.isArray()) {
			Class<?> componentType = clazz.getComponentType();
			return new ArrayType(getTypeScriptTypeFromJavaClass(componentType, module, tsJsonFormatVisitorWrapper));
		}
		throw new UnsupportedOperationException();
	}
}
