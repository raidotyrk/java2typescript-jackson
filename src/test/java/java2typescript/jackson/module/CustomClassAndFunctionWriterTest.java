/*******************************************************************************
 * Copyright 2013 Ats Uiboupin
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
package java2typescript.jackson.module;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import java2typescript.jackson.module.grammar.ClassType;
import java2typescript.jackson.module.grammar.FunctionType;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.grammar.base.AbstractType;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ClassToInterfaceWriter;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;
import java2typescript.jackson.module.writer.FunctionTypeWriter;
import java2typescript.jackson.module.writer.WriterPreferences;
import org.junit.Test;

public class CustomClassAndFunctionWriterTest {

	class Page<T> {
		public List<T> genericList;
	}

	class RequestClass<T> {
		public List<T> genericList;
	}

	class ResponseDto {
		public String stringField;
	}

	static class MyController {
		public Page<ResponseDto> find(RequestClass<String> req) {
			return null;
		}
	}

	static class RegularClass {
		public Page<ResponseDto> find(RequestClass<String> req) {
			return null;
		}
	}

	static class ControllerInvokerClassTypeWriter extends ClassToInterfaceWriter {

		@Override
		public void writeDef(AbstractType type, Writer writer, WriterPreferences preferences) throws IOException {
			ClassType classType = (ClassType) type;
			if (!classType.getOriginalClass().getName().endsWith("Controller")) {
				super.writeDef(classType, writer, preferences);
			} else {
				writer.write("class ");
				writeTypeNameWithGenerics(classType, writer);
				writeBody(classType, writer, preferences);
			}

		}
	}

	static class FunctionWithMethodBodyWriter extends FunctionTypeWriter {

		@Override
		public void write(
				FunctionType functionType,
				Writer writer,
				boolean lambdaSyntax,
				WriterPreferences preferences) throws IOException {
			super.write(functionType, writer, lambdaSyntax, preferences);
			if (lambdaSyntax) {
				return;
			}
			if (functionType.getOriginalJavaClassMethod().getDeclaringClass().getName().endsWith("Controller")) {
				writer.write(" {\n");
				preferences.increaseIndentation();
				writer.write(preferences.getIndentation() + "// could invoke api endpoint\n");
				writer.write(preferences.getIndentation() + "return null;\n");
				preferences.decreaseIndention();
				writer.write(preferences.getIndentation() + "}");
			}
		}
	}

	@Test
	public void canReplaceClassAndFunctionWriter() throws IOException {
		// Arrange
		Configuration conf = new Configuration();
		Module module = TestUtil.createTestModule(conf, MyController.class, RegularClass.class);

		ExternalModuleFormatWriter moduleWriter = new ExternalModuleFormatWriter();
		moduleWriter.preferences.addWriter(new FunctionWithMethodBodyWriter());
		moduleWriter.preferences.addWriter(new ControllerInvokerClassTypeWriter());
		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, moduleWriter);
	}
}
