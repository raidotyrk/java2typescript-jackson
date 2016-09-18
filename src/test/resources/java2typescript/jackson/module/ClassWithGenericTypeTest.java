package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;
import org.junit.Test;

public class ClassWithGenericTypeTest {

	static class ClassWithCollections {
		public List<String> stringList;
		public Collection<Boolean> booleanCollection;
		public Boolean[] booleanWrapperArray;
		public boolean[] booleanPrimitiveArray;
	}

	class GenericClass<T> {
		public T someField;
		public List<T> genericList;
	}

	public class StringClass extends GenericClass<String> {
	}

	static class ClassWithGenericTypeParams<K, V> {
		public String stringField;
		public K genericFieldK;
		public V genericFieldV;
		public Map<String, Boolean> booleansByStrings;
	}

	@Test
	public void classWithCollections() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, ClassWithCollections.class);
		Writer out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
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

	@Test
	public void classWithGenericTypeParams() throws IOException {
		// Arrange
		Configuration conf = new Configuration();
		Module module = TestUtil.createTestModule(conf, ClassWithGenericTypeParams.class);
		Writer out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}

}

