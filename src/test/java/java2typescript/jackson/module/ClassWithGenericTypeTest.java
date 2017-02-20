package java2typescript.jackson.module;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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
	}

	@Test
	public void classWithCollections() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, ClassWithCollections.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void classExtendsClassWithGenericTypeParams() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, StringClass.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void classWithGenericTypeParams() throws IOException {
		// Arrange
		Configuration conf = new Configuration();
		Module module = TestUtil.createTestModule(conf, ClassWithGenericTypeParams.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

}

