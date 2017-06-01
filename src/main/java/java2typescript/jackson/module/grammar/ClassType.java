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
package java2typescript.jackson.module.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import java2typescript.jackson.module.grammar.base.AbstractNamedType;
import java2typescript.jackson.module.grammar.base.AbstractType;
import java2typescript.jackson.module.writer.FunctionTypeWriter;
import java2typescript.jackson.module.writer.SortUtil;
import java2typescript.jackson.module.writer.WriterPreferences;

public class ClassType extends AbstractNamedType {

	private GenericTypes genericTypes = new GenericTypes();
	private Map<String, AbstractType> fields = new LinkedHashMap<String, AbstractType>();

	private Map<String, FunctionType> methods = new LinkedHashMap<String, FunctionType>();

	static private ClassType objectType = new ClassType("Object");

	/** Root Object class */
	static public ClassType getObjectClass() {
		return objectType;
	}

	public ClassType(String className) {
		super(className);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(name);
		StringWriter genericsSW = new StringWriter();
		genericTypes.write(genericsSW);
		writer.write(genericsSW.toString());
	}

	@Override
	public void writeDefInternal(Writer writer, WriterPreferences preferences) throws IOException {
		StringWriter genericsSW = new StringWriter();
		genericTypes.write(genericsSW);
		writer.write("interface ");
		write(writer);
		writer.write(" {\n");
		preferences.increaseIndentation();
		Collection<Entry<String, AbstractType>> fieldsEntrySet = fields.entrySet();
		Collection<String> methodsKeySet = methods.keySet();
		if(preferences.isSort()) {
			fieldsEntrySet = SortUtil.sortEntriesByKey(fieldsEntrySet);
			methodsKeySet = SortUtil.sort(methodsKeySet);
		}
		for (Entry<String, AbstractType> entry : fieldsEntrySet) {
			writer.write(format("%s%s: ", preferences.getIndentation(), entry.getKey()));
			entry.getValue().write(writer);
			writer.write(";\n");
		}
		for (String methodName : methodsKeySet) {
			writer.write(preferences.getIndentation() + methodName);
			FunctionType functionType = this.methods.get(methodName);
			if (!preferences.hasCustomWriter(functionType)) {
				functionType.writeNonLambda(writer);
			} else {
				FunctionTypeWriter functionTypeWriter = (FunctionTypeWriter) preferences.getCustomWriter(functionType);
				functionTypeWriter.write(functionType, writer, false, preferences);
			}
			writer.write(";\n");
		}
		preferences.decreaseIndention();
		writer.write(preferences.getIndentation() + "}");
	}

	public Map<String, AbstractType> getFields() {
		return fields;
	}

	public void setFields(Map<String, AbstractType> fields) {
		this.fields = fields;
	}

	public Map<String, FunctionType> getMethods() {
		return methods;
	}

	public void setGenericTypes(GenericTypes genericTypes) {
		this.genericTypes = genericTypes;
	}
}
