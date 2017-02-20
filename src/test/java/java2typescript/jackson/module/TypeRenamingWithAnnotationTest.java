package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;
import org.junit.Test;

public class TypeRenamingWithAnnotationTest {

	@JsonTypeName("EnumNameChangedWithAnnotation")
	static enum EnumToRename {
		VAL1, VAL2, VAL3
	}

	@JsonTypeName("ClassNameChangedWithAnnotation")
	static class ClassToRename {
		public EnumToRename enumToRename;
	}

	@Test
	public void nameChangedWithAnnotation() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, EnumToRename.class, ClassToRename.class);
		StringWriter out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}
}
