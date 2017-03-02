package java2typescript.jackson.module.grammar;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.TypeVariable;

import java2typescript.jackson.module.grammar.base.AbstractNamedType;
import java2typescript.jackson.module.grammar.base.AbstractPrimitiveType;
import java2typescript.jackson.module.grammar.base.AbstractType;

public class GenericType extends AbstractType {
	private String name;

	public GenericType(TypeVariable typeVariable) {
		this(typeVariable.getTypeName());
	}

	public GenericType(AbstractType tsType) {
		this(getName(tsType));
	}

	private GenericType(String name) {
		this.name = name;
	}

	private static String getName(AbstractType tsType) {
		if (tsType instanceof AbstractNamedType) {
			return ((AbstractNamedType) tsType).getName();
		} else if (tsType instanceof AbstractPrimitiveType) {
			return ((AbstractPrimitiveType) tsType).getToken();
		} else {
			throw new RuntimeException("Creating GenericType from " + tsType + " is not implemented!");
		}
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(name);
	}

}
