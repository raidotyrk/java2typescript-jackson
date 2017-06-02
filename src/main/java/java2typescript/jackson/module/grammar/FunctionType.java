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
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import java2typescript.jackson.module.grammar.base.AbstractType;
import java2typescript.jackson.module.writer.FunctionTypeWriter;

public class FunctionType extends AbstractType {
	private static final FunctionTypeWriter DEFAULT_WRITER = new FunctionTypeWriter();

	private LinkedHashMap<String, AbstractType> parameters = new LinkedHashMap<String, AbstractType>();

	private AbstractType resultType;
	private Method originalJavaClassMethod;

	/** By default, printed as lambda function type (with =>) */
	@Override
	public void write(Writer writer) throws IOException {
		write(writer, true);
	}

	/** Write as non lambda : func(a:string) : string */
	public void writeNonLambda(Writer writer) throws IOException {
		write(writer, false);
	}

	private void write(Writer writer, boolean lambdaSyntax) throws IOException {
		DEFAULT_WRITER.write(this, writer, lambdaSyntax, null);
	}

	public LinkedHashMap<String, AbstractType> getParameters() {
		return parameters;
	}

	public AbstractType getResultType() {
		return resultType;
	}

	public void setResultType(AbstractType resultType) {
		this.resultType = resultType;
	}

	public void setOriginalJavaClassMethod(Method originalJavaClassMethod) {
		this.originalJavaClassMethod = originalJavaClassMethod;
	}

	public Method getOriginalJavaClassMethod() {
		return originalJavaClassMethod;
	}
}
