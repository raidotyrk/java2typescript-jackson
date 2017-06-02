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

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import java2typescript.jackson.module.grammar.FunctionType;
import java2typescript.jackson.module.grammar.base.AbstractType;

public class FunctionTypeWriter implements CustomAbstractTypeWriter {

	@Override
	public boolean accepts(AbstractType type, WriterPreferences preferences) {
		return type instanceof FunctionType;
	}

	@Override
	public void writeDef(AbstractType type, Writer writer, WriterPreferences preferences) throws IOException {
		write((FunctionType) type, writer, true, preferences);
	}

	public void write(
			FunctionType functionType,
			Writer writer,
			boolean lambdaSyntax,
			WriterPreferences preferences) throws IOException {
		writeParameters(writer, functionType.getParameters());
		writer.write(lambdaSyntax ? "=> " : ": ");
		functionType.getResultType().write(writer);
	}

	private void writeParameters(Writer writer, LinkedHashMap<String, AbstractType> parameters) throws IOException {
		writer.write("(");
		int i = 1;
		for (Entry<String, AbstractType> entry : parameters.entrySet()) {
			writer.write(entry.getKey());
			writer.write(": ");
			entry.getValue().write(writer);
			if (i < parameters.size()) {
				writer.write(", ");
			}
			i++;
		}
		writer.write(")");
	}

}
