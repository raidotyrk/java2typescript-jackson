package java2typescript.jackson.module;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
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
			TypeVariable<? extends Class<?>> typeParameter = clazz.getTypeParameters()[0];
			// Note, generic type name is based on generic type of the class (such as `List<E>`),
			// not based on generic type of the field (such as `List<T>`, where T could come from generic of the class)
			GenericType genericType = new GenericType(typeParameter);
			return new ArrayType(genericType);
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
		AbstractType namedType = module.getNamedType(name);
		if (namedType == null) {
			EnumType enumType = new EnumType(name);
			for (Object val : javaType.getRawClass().getEnumConstants()) {
				enumType.getValues().add(val.toString());
			}
			module.addNamedType(name, enumType, javaType.getRawClass());
			return enumType;
		} else {
			return (EnumType) namedType;
		}
	}

	private static JavaType getJavaTypeFromClass(Class<?> clazz) {
		TypeBindings bindings = createBindingsFromClass(clazz);
		return TypeFactory.defaultInstance().constructType(clazz, bindings);
	}

	public static TypeBindings createBindingsFromClass(Class<?> clazz) {
		JavaType[] javaTypeParameters = getGenericTypeParamsAsJavaType(clazz);
		return TypeBindings.create(clazz, javaTypeParameters);
	}

	private static JavaType[] getGenericTypeParamsAsJavaType(Class<?> clazz) {
		TypeFactory typeFactory = TypeFactory.defaultInstance();
		TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
		if (typeParameters == null || typeParameters.length == 0) {
			return null;
		}
		return Arrays.stream(typeParameters)
				.map(typeFactory::constructType)
				.toArray(JavaType[]::new);
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
