package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java2typescript.jackson.module.conf.typename.WithEnclosingClassTSTypeNamingStrategy;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;
import org.junit.Test;

public class TypeRenamingWithEnclosingClassTest {

	static class TestClass {
		public String fieldOfInnerClass;
		public java2typescript.jackson.module.TestClass other;
		public ClassToRename renamedWithAnnotation;
	}

	@JsonTypeName("ClassNameChangedWithAnnotation")
	static class ClassToRename {
		public String field;
	}

	@Test
	public void twoClassesWithSameName() throws IOException {
		// Arrange
		Configuration conf = new Configuration();
		conf.setNamingStrategy(new WithEnclosingClassTSTypeNamingStrategy());
		Module module = TestUtil.createTestModule(conf, TestClass.class);
		StringWriter out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}

}

class TestClass {
	public String fieldOfPackageProtectedClass;
}
