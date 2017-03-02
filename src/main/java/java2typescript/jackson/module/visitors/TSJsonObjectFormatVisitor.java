/*******************************************************************************
 * Copyright 2013 Raphael Jolivet
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package java2typescript.jackson.module.visitors;

import static com.fasterxml.jackson.databind.PropertyName.NO_NAME;
import static java.lang.reflect.Modifier.isPublic;
import static java2typescript.jackson.module.visitors.TSJsonFormatVisitorWrapper.getTSTypeForHandler;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;
import java2typescript.jackson.module.ClassMemberUtil;
import java2typescript.jackson.module.Configuration;
import java2typescript.jackson.module.TypeUtil;
import java2typescript.jackson.module.grammar.*;
import java2typescript.jackson.module.grammar.base.AbstractPrimitiveType;
import java2typescript.jackson.module.grammar.base.AbstractType;

public class TSJsonObjectFormatVisitor extends ABaseTSJsonFormatVisitor<ClassType> implements JsonObjectFormatVisitor {

	private Class clazz;

	public TSJsonObjectFormatVisitor(ABaseTSJsonFormatVisitor<?> parentHolder, String className, Class clazz, Configuration conf) {
		super(parentHolder, conf);
		type = new ClassType(className);
		type.setGenericTypes(getGenericTypes(clazz.getTypeParameters()));
		this.clazz = clazz;
	}

	private GenericTypes getGenericTypes(Type[] genericTypes) {
		GenericTypes generics = new GenericTypes();
		for (Type type : genericTypes) {
			generics.addGenericType(new GenericType((TypeVariable) type));
		}
		return generics;
	}

	private GenericTypes getGenericTypesWithTSNames(Type[] genericTypes) {
		GenericTypes generics = new GenericTypes();
		for (Type type : genericTypes) {
			GenericType tsGeneric = getTypeScriptGenericTypeFromJavaType(type);
			generics.addGenericType(tsGeneric);
		}
		return generics;
	}

	private GenericType getTypeScriptGenericTypeFromJavaType(Type type) {
		AbstractType tsType;
		if(type instanceof TypeVariable) {
			TypeVariable typeVariable = (TypeVariable) type;
			GenericType tsGeneric = new GenericType(typeVariable);
			return tsGeneric;
		}
		Class<?> typeClass = TypeUtil.getClass(type);
		tsType = getTypeScriptTypeFromJavaClass(typeClass);
		if(tsType instanceof AbstractPrimitiveType) {
			// no need to add primitive TypeScript types to module
		} else if(tsType instanceof ClassType) {
			addGenericTypeToModule(typeClass);
		} else {
			throw new RuntimeException("TODO: should " + typeClass + " be added to generated output?");
		}
		return new GenericType(tsType);
	}

	private void addField(String name, AbstractType fieldType) {
		type.getFields().put(name, fieldType);
	}

	private boolean isAccessorMethod(Method method, BeanInfo beanInfo) {
		for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
			if (method.equals(property.getReadMethod())) {
				return true;
			}
			if (method.equals(property.getWriteMethod())) {
				return true;
			}
		}
		return false;
	}

	void addPublicMethods() {

		for (Method method : this.clazz.getDeclaredMethods()) {

			// Only public
			if (!isPublic(method.getModifiers())) {
				continue;
			}

			// Exclude accessors
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
				if (isAccessorMethod(method, beanInfo)) {
					continue;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			if(conf.isIgnoredMethod(method)) {
				continue;
			}

			addMethod(method);
		}
	}

	private AbstractType getTSTypeForClass(AnnotatedMember member) {

		TypeBindings bindings = new TypeBindings(TypeFactory.defaultInstance(), member.getDeclaringClass());
		BeanProperty prop = new BeanProperty.Std(member.getName(), member.getType(bindings), NO_NAME,
				new AnnotationMap(), member, false);

		try {
			return getTSTypeForProperty(prop);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		}
	}

	private void addMethod(Method method) {
		FunctionType function = new FunctionType();

		AnnotatedMethod annotMethod = new AnnotatedMethod(null, method, new AnnotationMap(), null);

		function.setResultType(getTSTypeForClass(annotMethod));
		for (int i = 0; i < annotMethod.getParameterCount(); i++) {
			AnnotatedParameter param = annotMethod.getParameter(i);
			String name = "param" + i;
			function.getParameters().put(name, getTSTypeForClass(param));
		}
		this.type.getMethods().put(method.getName(), function);
	}

	@Override
	public void property(BeanProperty writer) throws JsonMappingException {
		addField(writer.getName(), getTSTypeForProperty(writer));
	}

	@Override
	public void property(String name, JsonFormatVisitable handler, JavaType propertyTypeHint)
			throws JsonMappingException {
		addField(name, getTSTypeForHandler(this, handler, propertyTypeHint, conf));
	}


	@Override
	public void optionalProperty(BeanProperty writer) throws JsonMappingException {
		addField(writer.getName(), getTSTypeForProperty(writer));
	}

	@Override
	public void optionalProperty(String name, JsonFormatVisitable handler, JavaType propertyTypeHint)
			throws JsonMappingException {
		addField(name, getTSTypeForHandler(this, handler, propertyTypeHint, conf));
	}

	protected AbstractType getTSTypeForProperty(BeanProperty writer) throws JsonMappingException {
		if (writer == null) {
			throw new IllegalArgumentException("Null writer");
		}
		JavaType type = writer.getType();
		return getTSTypeForProperty(writer, type);
	}

	private AbstractType getTSTypeForProperty(BeanProperty writer, JavaType type) {
		if (type.getRawClass().equals(Void.TYPE)) {
			return VoidType.getInstance();
		}

		AbstractType customType = conf.getCustomTypes().get(type.getRawClass().getName());
		if(customType != null) {
			if(customType instanceof TypeDeclarationType) {
				TypeDeclarationType tdt = (TypeDeclarationType) customType;
				getModule().getNamedTypes().put(tdt.getName(), tdt);
			}
			return customType;
		}

		try {
			JsonSerializer<Object> ser = getSer(writer);

			if (ser != null) {
				if (type == null) {
					throw new IllegalStateException("Missing type for property '" + writer.getName() + "'");
				}
				Type genericType = getGenericType(writer);
				if (genericType != null) {
					AbstractType tsTypeWithResolvedGenerics = resolveTSTypeIfNeeded(writer, type, genericType);
					if(tsTypeWithResolvedGenerics != null) {
						return tsTypeWithResolvedGenerics;
					}
				}
				return getTSTypeForHandler(this, ser, type, conf);
			} else {
				return AnyType.getInstance();
			}

		} catch (Exception e) {
			throw new RuntimeException(String.format(//
					"Error when serializing %s, you should add a custom mapping for it", type.getRawClass()), e);
		}

	}

	private AbstractType resolveTSTypeIfNeeded(BeanProperty writer, JavaType type, Type genericType) {
		if (!isSupportedWithoutGenerics(type)) {
			boolean resolveGenericType = !(genericType instanceof TypeVariable) || isDeclaredInSameClass(writer);
			if (resolveGenericType) {
				return getTsTypeForGenericType(type, genericType);
			}
		}
		if (genericType instanceof ParameterizedType) {
			if (type instanceof CollectionType) {
				ArrayType tsType = (ArrayType) getTypeScriptTypeFromJavaClass(type.getRawClass());
				ParameterizedType parameterizedType = (ParameterizedType) genericType;
				Type actualTypeArgumentFromProperty = parameterizedType.getActualTypeArguments()[0];
				if (actualTypeArgumentFromProperty instanceof TypeVariable && isDeclaredInSameClass(writer)) {
					// swap generic item type List<E> (E from class) to List<T> (T from field)
					tsType.setItemType(new GenericType((TypeVariable) actualTypeArgumentFromProperty));
					return tsType;
				}
			}
		}
		return null;
	}

	private boolean isDeclaredInSameClass(BeanProperty writer) {
		Class<?> declaringClassOfProperty = ClassMemberUtil.getDeclaringClass(clazz, writer.getName());
		return clazz.equals(declaringClassOfProperty);
	}

	private boolean isSupportedWithoutGenerics(JavaType jacksonType) {
		if (jacksonType instanceof CollectionType || jacksonType instanceof MapType) {
			return true;
		}
		AbstractType tsType = getTypeScriptTypeFromJavaClass(jacksonType.getRawClass());
		return tsType instanceof AbstractPrimitiveType;
	}

	private AbstractType getTsTypeForGenericType(JavaType type, Type genericType) {
		if (genericType instanceof TypeVariable) {
			return new GenericType((TypeVariable) genericType);
		}
		if (genericType instanceof ParameterizedType) {
			AbstractType tsType = getTypeScriptTypeFromJavaClass(type.getRawClass());
			if (tsType instanceof ClassType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericType;
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				ClassType classType = (ClassType) tsType;
				classType.setGenericTypes(getGenericTypesWithTSNames(actualTypeArguments));
				addGenericTypeToModule(type.getRawClass());
			} else {
				throw new RuntimeException("TODO return TypeScript type for " + tsType);
			}
			return tsType;

		} else {
			throw new RuntimeException("Unhandled generic type: " + genericType);
		}
	}

	private AbstractType getTypeScriptTypeFromJavaClass(Class<?> clazz) {
		return TypeUtil.getTypeScriptTypeFromJavaClass(clazz, getModule(), conf.getNamingStrategy());
	}

	private void addGenericTypeToModule(Class<?> rawClass) {
		// can't use JsonFormatVisitable.acceptJsonFormatVisitor() to add type with GENERICS to module,
		// as otherwise generic type parameters would be replaced with types declared by the first class that uses the typeWithGenerics
		// which would usually be incorrect for other classes
		getModule().addClassesToParse(Lists.newArrayList(rawClass));
	}

	private Type getGenericType(BeanProperty writer) {
		AnnotatedMember member = writer.getMember();
		Type genericType = member.getGenericType();
		if(genericType instanceof ParameterizedType || genericType instanceof TypeVariable) {
			return genericType;
		}
		return null;
	}

	protected JsonSerializer<java.lang.Object> getSer(BeanProperty writer) throws JsonMappingException {
		JsonSerializer<Object> ser = null;
		if (writer instanceof BeanPropertyWriter) {
			ser = ((BeanPropertyWriter) writer).getSerializer();
		}
		if (ser == null) {
			ser = getProvider().findValueSerializer(writer.getType(), writer);
		}
		return ser;
	}

}
