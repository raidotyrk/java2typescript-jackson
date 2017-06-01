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
package java2typescript.jackson.module.writer;

import static java.lang.String.format;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import java2typescript.jackson.module.grammar.ClassType;
import java2typescript.jackson.module.grammar.FunctionType;
import java2typescript.jackson.module.grammar.base.AbstractType;

/**
 * Writes java class as TypeScript interface
 */
public class ClassToInterfaceWriter implements CustomAbstractTypeWriter {

	@Override
	public boolean accepts(AbstractType type, WriterPreferences preferences) {
		return type instanceof ClassType;
	}

	@Override
	public void writeDef(AbstractType type, Writer writer, WriterPreferences preferences) throws IOException {
		ClassType classType = (ClassType) type;
		writer.write("interface ");
		writeTypeNameWithGenerics(classType, writer);
		writeBody(classType, writer, preferences);
	}

	public void writeTypeNameWithGenerics(ClassType classType, Writer writer) throws IOException {
		writer.write(classType.getName());
		StringWriter genericsSW = new StringWriter();
		classType.getGenericTypes().write(genericsSW);
		writer.write(genericsSW.toString());
	}

	protected void writeBody(ClassType classType, Writer writer, WriterPreferences preferences) throws IOException {
		writer.write(" {\n");
		preferences.increaseIndentation();
		Collection<Entry<String, AbstractType>> fieldsEntrySet = classType.getFields().entrySet();
		Map<String, FunctionType> methods = classType.getMethods();
		Collection<String> methodsKeySet = methods.keySet();
		if (preferences.isSort()) {
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
			FunctionType functionType = methods.get(methodName);
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

}
