package java2typescript.jackson.module;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class ClassMemberUtil {

	public static Class<?> getDeclaringClass(Class clazz, String propertyOrFields) {
		Map<String, Class<?>> declaringClassByPropertyName = ClassMemberUtil.getPropertyAndFieldNamesByDeclaringClass(clazz);
		return declaringClassByPropertyName.get(propertyOrFields);
	}

	private static Map<String, Class<?>> getPropertyAndFieldNamesByDeclaringClass(Class clazz) {
		Map<String, Class<?>> declaringClassByPropertyName = new HashMap<>();
		declaringClassByPropertyName.putAll(getDeclaringClassByFields(clazz));
		declaringClassByPropertyName.putAll(getDeclaringClassByBeanPropertyName(clazz));
		return declaringClassByPropertyName;
	}

	private static Map<String, Class<?>> getDeclaringClassByFields(Class clazz) {
		Map<String, Class<?>> declaringClassByFieldName = new HashMap<>();
		Class<?> aClass = clazz;
		do {
			Class<?> declaringClass = aClass;
			Field[] declaredFields = declaringClass.getDeclaredFields();
			Stream.of(declaredFields)
					.filter(f -> Modifier.isPublic(f.getModifiers()))
					.map(Field::getName)
					.forEach(fieldName -> put(declaringClassByFieldName, fieldName, declaringClass));
			aClass = declaringClass.getSuperclass();
		} while (aClass != null);
		return declaringClassByFieldName;
	}

	private static Map<String, Class<?>> getDeclaringClassByBeanPropertyName(Class clazz) {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
		Map<String, Class<?>> declaringClassByPropertyName = new HashMap<>();
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : propertyDescriptors) {
			String propName = pd.getName();
			Method declaringMethod = pd.getReadMethod();
			if(declaringMethod == null) {
				continue;
			}
			Class<?> declaringClass = declaringMethod.getDeclaringClass();
			put(declaringClassByPropertyName, propName, declaringClass);
		}
		return declaringClassByPropertyName;
	}

	private static void put(
			Map<String, Class<?>> declaringClassByPropertyName,
			String propName,
			Class<?> declaringClass) {
		if (!declaringClassByPropertyName.containsKey(propName)) {
			declaringClassByPropertyName.put(propName, declaringClass);
		}
	}

}
