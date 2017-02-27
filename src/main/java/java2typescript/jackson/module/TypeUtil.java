package java2typescript.jackson.module;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java2typescript.jackson.module.conf.typename.TSTypeNamingStrategy;
import java2typescript.jackson.module.grammar.*;
import java2typescript.jackson.module.grammar.base.AbstractType;

public class TypeUtil {

	public static AbstractType getTypeScriptTypeFromJavaClass(
			Class<?> clazz,
			Module module,
			TSTypeNamingStrategy namingStrategy) {
		if (clazz == boolean.class || clazz == Boolean.class) {
			return BooleanType.getInstance();
		} else if (isTypeScriptTypeNumber(clazz)) {
			return NumberType.getInstance();
		} else if (clazz == String.class) {
			return StringType.getInstance();
		} else if (clazz.isEnum()) {
			SimpleType jacksonType = SimpleType.construct(clazz);
			return parseTypeScriptEnumTypeOrGetFromCache(jacksonType, module, namingStrategy);
		} else if (clazz.isArray()) {
			Class<?> componentType = clazz.getComponentType();
			return new ArrayType(getTypeScriptTypeFromJavaClass(componentType, module, namingStrategy));
		} else if (Map.class.isAssignableFrom(clazz)) {
			throw new RuntimeException("TODO implementation for Map " + clazz);
		} else if (Collection.class.isAssignableFrom(clazz)) {
			throw new RuntimeException("TODO implementation for Collection " + clazz);
		}
		return new ClassType(namingStrategy.getName(getJavaTypeFromClass(clazz)));
	}

	private static boolean isTypeScriptTypeNumber(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz)
				|| clazz == double.class
				|| clazz == float.class
				|| clazz == int.class
				|| clazz == long.class;
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

	private static JavaType getJavaTypeFromClass(Class<?> clazz) {
		TypeBindings bindings = new TypeBindings(TypeFactory.defaultInstance(), clazz);
		return bindings.resolveType(clazz);
	}

	public static Class<?> getClass(Type type) {
		Class<?> typeClass;
		String typeName = type.getTypeName();
		try {
			typeClass = Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return typeClass;
	}

}
