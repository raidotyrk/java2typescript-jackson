package java2typescript.jackson.module.grammar;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import java2typescript.jackson.module.grammar.base.AbstractType;

public class GenericTypes extends AbstractType {
	private List<GenericType> genericTypes = new ArrayList<GenericType>();

	public GenericTypes() {
	}

	public void addGenericType(GenericType generic) {
		genericTypes.add(generic);
	}

	@Override
	public void write(Writer writer) throws IOException {
		if (!genericTypes.isEmpty()) {
			boolean firstWritten = false;
			writer.write("<");
			for (GenericType genericType : genericTypes) {
				if (firstWritten) {
					writer.write(", ");
				}
				genericType.write(writer);
				firstWritten = true;
			}
			writer.write(">");
		}
	}

}
