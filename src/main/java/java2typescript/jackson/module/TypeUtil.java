package java2typescript.jackson.module;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import java2typescript.jackson.module.conf.typename.TSTypeNamingStrategy;
import java2typescript.jackson.module.grammar.*;
import java2typescript.jackson.module.grammar.base.AbstractType;

public class TypeUtil {

	public static AbstractType getTypeScriptTypeFromJavaClass(
			Class<?> clazz,
			Module module,
			TSTypeNamingStrategy namingStrategy) {
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
			return parseTypeScriptEnumTypeOrGetFromCache(jacksonType, module, namingStrategy);
		} else if (clazz.isArray()) {
			Class<?> componentType = clazz.getComponentType();
			return new ArrayType(getTypeScriptTypeFromJavaClass(componentType, module, namingStrategy));
		}
		throw new UnsupportedOperationException();
	}

	public static EnumType parseTypeScriptEnumTypeOrGetFromCache(
			JavaType javaType,
			Module module,
			TSTypeNamingStrategy namingStrategy) {
		String name = namingStrategy.getName(javaType);
		AbstractType namedType = module.getNamedTypes().get(name);
		if (namedType == null) {
			EnumType enumType = new EnumType(name);
			for (Object val : javaType.getRawClass().getEnumConstants()) {
				enumType.getValues().add(val.toString());
			}
			module.getNamedTypes().put(name, enumType);
			return enumType;
		} else {
			return (EnumType) namedType;
		}
	}
}
