/*******************************************************************************
 * Copyright 2013 Raphael Jolivet
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package java2typescript.jackson.module.grammar;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import com.google.common.base.Preconditions;
import java2typescript.jackson.module.grammar.base.AbstractNamedType;
import java2typescript.jackson.module.grammar.base.AbstractType;
import java2typescript.jackson.module.writer.InternalModuleFormatWriter;
import lombok.Getter;
import lombok.Setter;

public class Module {

	@Getter
	@Setter
	private String name;

	@Getter
	private Map<String, AbstractNamedType> namedTypes = new HashMap<>();

	@Getter
	private Map<String, AbstractType> vars = new HashMap<>();

	private Deque<Class<?>> classesToParse = new LinkedList<>();

	public Module() {
	}

	public Module(String name) {
		this.name = name;
	}

	public AbstractNamedType getNamedType(String typeName) {
		return namedTypes.get(typeName);
	}

	public void addNamedType(String typeName, AbstractNamedType namedType, Class<?> originalClass) {
		Preconditions
				.checkNotNull(originalClass, "Please provide original java class that was used to create the named type");
		namedType.setOriginalClass(originalClass);
		namedTypes.put(typeName, namedType);
	}

	public void write(Writer writer) throws IOException {
		new InternalModuleFormatWriter().write(this, writer);
	}

	public void addClassesToParse(Collection<? extends Class<?>> classes) {
		this.classesToParse.addAll(classes);
	}

	public Class<?> pollNextClassToParse() {
		return this.classesToParse.poll();
	}

}
