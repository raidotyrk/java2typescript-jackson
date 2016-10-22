package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;
import org.junit.Test;

public class ClassWithGenericTypeTest {

	class GenericClass<T> {
		public T someField;
		public List<T> genericList;
	}

	public class StringClass extends GenericClass<String> {
	}

	@Test
	public void classExtendsClassWithGenericTypeParams() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, StringClass.class);
		Writer out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}

}

