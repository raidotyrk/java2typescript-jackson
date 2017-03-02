package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
		public long[] longPrimitiveArray;
		public Long[] longWrapperArray;
	}

	static class ClassWithListOfStrings {
		public List<String> stringList;
	}
	static class ClassWithMapOfBooleansByStrings {
		public Map<String, Boolean> booleansByStrings;
	}

	class GenericClass<T> {
		public T someField;
		public List<T> genericList;
	}

	public class StringClass extends GenericClass<String> {
	}

	public class AtomicIntegerClass extends GenericClass<AtomicInteger> {
	}

	public class BooleanClass extends GenericClass<Boolean> {
	}

	public class ClassWithNonPrimitiveGeneric extends ValueClass<BooleanClass> {
	}

	static class ClassWithGenericTypeParams<K, V> {
		public String stringField;
		public K genericFieldK;
		public V genericFieldV;
		public Map<String, Boolean> booleansByStrings;
	}

	static class ClassWithGenericFieldWhereClassHasGenericCollection {
		public ClassHasGenericCollection<String> genericClassOfStrings;
		public ClassHasGenericCollection<BooleanClass> genericClassOfNonPrimitiveGeneric;
	}

	class ClassHasGenericCollection<T> {
		public List<T> genericList;
	}

	@Test
	public void classWithCollections() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, ClassWithCollections.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void debug_classWithCollections_field_ListOfStrings() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, ClassWithListOfStrings.class);
		Writer out = new StringWriter();

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void debug_classWithCollections_field_MapOfBooleansByStrings() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, ClassWithMapOfBooleansByStrings.class);
		Writer out = new StringWriter();

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void classExtendsClassWithGenericTypeParams() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, StringClass.class, BooleanClass.class, AtomicIntegerClass.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void classExtendsClassWithNonPrimitiveGenericTypeParams() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, ClassWithNonPrimitiveGeneric.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void classWithGenericTypeParams() throws IOException {
		// Arrange
		Configuration conf = new Configuration();
		Module module = TestUtil.createTestModule(conf, ClassWithGenericTypeParams.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void classWithGenericFieldWhereClassHasGenericCollection() throws Exception {
		// Arrange
		Module module = TestUtil.createTestModule(null, ClassWithGenericFieldWhereClassHasGenericCollection.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	@Test
	public void debug() throws Exception {
		// Arrange
		Module module = TestUtil.createTestModule(null, Response.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	class Response {
		public ValueClass<String> stringValueClassField;
	}

	public static class ValueClass<T> {
		public T genericValue;
	}

}

