package java2typescript.jackson.module;

import java.util.Arrays;
import java.util.List;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;
import org.junit.Test;

public class ProvidedClassesTest {

	static class ClassWithFieldsOfProvidedTypes {
		public Provided1 fieldProvided1;
		public NotProvided1 fieldNotProvided1;
		public NotProvided1ReferencesProvided2 notProvided1ReferencesProvided2;
	}

	static class Provided1 {
	}

	static class NotProvided1 {
	}

	static class NotProvided1ReferencesProvided2 {
		public Provided2 fieldProvided2;
	}

	static class Provided2 {
	}

	@Test
	public void moduleWriterIgnoresProvidedTypes() throws Exception {
		// Arrange
		List<Class<?>> providedClasses = Arrays.asList(Provided1.class, Provided2.class);
		Configuration conf = new Configuration();
		Module module = TestUtil.createTestModule(conf, ClassWithFieldsOfProvidedTypes.class);
		ExternalModuleFormatWriter moduleWriter = new ExternalModuleFormatWriter();
		moduleWriter.preferences.setProvidedTypes(providedClasses);
		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, moduleWriter);
	}

}

