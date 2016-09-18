package java2typescript.jackson.module.grammar;

import java.io.IOException;
import java.io.Writer;

import java2typescript.jackson.module.grammar.base.AbstractType;

public class GenericType extends AbstractType {
	private String name;

	public GenericType(String name) {
		this.name = name;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(name);
	}

}
