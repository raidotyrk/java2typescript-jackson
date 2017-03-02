package java2typescript.jackson.module;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;
import org.junit.Assert;
import org.junit.Test;

public class ClassMemberUtilTest {
	@Test
	public void propertiesDeclaredInAnyAncestorClass() throws Exception {
		assertPropertyDeclared(Subclass.class, "publicPropertyInSubclass", Subclass.class);
		assertPropertyDeclared(Subclass.class, "inSubClassPublicField", Subclass.class);
		assertPropertyDeclared(Subclass.class, "inSubClassProtectedField", null);
		assertPropertyDeclared(Subclass.class, "inSubClassPackageProtectedField", null);

		assertPropertyDeclared(Subclass.class, "publicPropertyInSuperclass", Superclass.class);
		assertPropertyDeclared(Subclass.class, "inSuperclassPublicField", Superclass.class);
		assertPropertyDeclared(Subclass.class, "inSuperclassProtectedField", null);
		assertPropertyDeclared(Subclass.class, "inSuperclassPackageProtectedField", null);

		assertPropertyDeclared(Subclass.class, "publicPropertyInBaseclass", Baseclass.class);
		assertPropertyDeclared(Subclass.class, "inBaseclassPublicField", Baseclass.class);
		assertPropertyDeclared(Subclass.class, "inBaseclassProtectedField", null);
		assertPropertyDeclared(Subclass.class, "inBaseclassPackageProtectedField", null);
	}

	@Test
	public void overriddenProperties() throws Exception {
		assertPropertyDeclared(Subclass.class, "inBaseclassPublicFieldOverriddenInSubclass", Subclass.class);
		assertPropertyDeclared(Superclass.class, "inBaseclassPublicFieldOverriddenInSubclass", Superclass.class);
		assertPropertyDeclared(Baseclass.class, "inBaseclassPublicFieldOverriddenInSubclass", Baseclass.class);
	}

	@Test
	public void propertyNotFoundFromSubclass() throws Exception {
		assertPropertyDeclared(Baseclass.class, "publicPropertyInSubclass", null);
	}

	@Test
	public void settersWithoutGetters_shouldntCreateProperty() throws Exception {
		assertPropertyDeclared(Subclass.class, "writable", null);
	}

	@Test
	public void publicGettersWithoutSetters_shouldCreateProperty() throws Exception {
		assertPropertyDeclared(Subclass.class, "publicReadable", Baseclass.class);
		assertPropertyDeclared(Subclass.class, "protectedReadable", null);
		assertPropertyDeclared(Subclass.class, "packageProtectedReadable", null);
		assertPropertyDeclared(Subclass.class, "privateReadable", null);
	}

	@Test
	public void checkTypeScriptClassGeneratedFromSubclass() throws Exception {
		Module module = TestUtil.createTestModule(null, Subclass.class);

		ExpectedOutputChecker.writeAndCheckOutputFromFile(module, new ExternalModuleFormatWriter());
	}

	private void assertPropertyDeclared(
			Class<?> classToSearchPropertyFrom,
			String propertyOrFieldName,
			Class<?> expectedDeclaringClass) {
		Class<?> declaringClass = ClassMemberUtil.getDeclaringClass(classToSearchPropertyFrom, propertyOrFieldName);
		Assert.assertEquals(expectedDeclaringClass, declaringClass);
	}

	private class Baseclass {
		public String inBaseclassPublicFieldOverriddenInSubclass;
		public String inBaseclassPublicField;
		protected String inBaseclassProtectedField;
		String inBaseclassPackageProtectedField;
		private String inBaseclassPackagePrivateField;

		public String getPublicPropertyInBaseclass() {
			return null;
		}

		public void setPublicPropertyInBaseclass(String x) {
		}

		protected String getProtectedPropertyInBaseclass() {
			return null;
		}

		protected void setProtectedPropertyInBaseclass(String x) {
		}

		String getPackageProtectedPropertyInBaseclass() {
			return null;
		}

		void setPackageProtectedPropertyInBaseclass(String x) {
		}


		private String getPrivatePropertyInBaseclass() {
			return null;
		}

		private void setPrivatePropertyInBaseclass(String x) {
		}

		public void setWritable(String value) {
		}

		public String getPublicReadable() {
			return null;
		}

		protected String getProtectedReadable() {
			return null;
		}

		String getPackageProtectedReadable() {
			return null;
		}

		private String getPrivateReadable() {
			return null;
		}

	}

	private class Superclass extends Baseclass {
		public String inBaseclassPublicFieldOverriddenInSubclass;
		public String inSuperclassPublicField;
		protected String inSuperclassProtectedField;
		String inSuperclassPackageProtectedField;
		private String inSuperclassPackagePrivateField;

		public String getPublicPropertyInSuperclass() {
			return null;
		}

		public void setPublicPropertyInSuperclass(String x) {
		}
	}

	private class Subclass extends Superclass {
		public String inBaseclassPublicFieldOverriddenInSubclass;
		public String inSubClassPublicField;
		protected String inSubClassProtectedField;
		String inSubClassPackageProtectedField;
		private String inSubClassPackagePrivateField;

		public String getPublicPropertyInSubclass() {
			return null;
		}

		public void setPublicPropertyInSubclass(String x) {
		}
	}

}