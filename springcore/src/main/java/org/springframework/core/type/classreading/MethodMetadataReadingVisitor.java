/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.type.classreading;

import java.util.List;
import java.util.Map;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.SpringAsmInfo;
import org.springframework.asm.Type;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * ASM method visitor which looks for the annotations defined on the method,
 * exposing them through the {@link org.springframework.core.type.MethodMetadata}
 * interface.
 *
 * @author Juergen Hoeller
 * @author Mark Pollack
 * @author Costin Leau
 * @author Chris Beams
 * @author Phillip Webb
 * @since 3.0
 */
final class MethodMetadataReadingVisitor extends MethodVisitor implements MethodMetadata {

	private final String name;

	private final int access;

	private String declaringClassName;

	private final ClassLoader classLoader;

	private final MultiValueMap<String, MethodMetadata> methodMetadataMap;

	private final MultiValueMap<String, AnnotationAttributes> attributeMap = new LinkedMultiValueMap<String, AnnotationAttributes>(2);

	private final MetadataReaderLog logger;

	public MethodMetadataReadingVisitor(String name, int access, String declaringClassName, ClassLoader classLoader,
			MultiValueMap<String, MethodMetadata> methodMetadataMap, MetadataReaderLog logger) {
		super(SpringAsmInfo.ASM_VERSION);
		this.name = name;
		this.access = access;
		this.declaringClassName = declaringClassName;
		this.classLoader = classLoader;
		this.methodMetadataMap = methodMetadataMap;
		this.logger = logger;
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
		String className = Type.getType(desc).getClassName();
		methodMetadataMap.add(className, this);
		return new AnnotationAttributesReadingVisitor(className, this.attributeMap, null,
				this.classLoader, this.logger);
	}

	public String getMethodName() {
		return this.name;
	}

	public boolean isStatic() {
		return ((this.access & Opcodes.ACC_STATIC) != 0);
	}

	public boolean isFinal() {
		return ((this.access & Opcodes.ACC_FINAL) != 0);
	}

	public boolean isOverridable() {
		return (!isStatic() && !isFinal() && ((this.access & Opcodes.ACC_PRIVATE) == 0));
	}

	public boolean isAnnotated(String annotationType) {
		return this.attributeMap.containsKey(annotationType);
	}

	public Map<String, Object> getAnnotationAttributes(String annotationType) {
		return getAnnotationAttributes(annotationType, false);
	}

	public Map<String, Object> getAnnotationAttributes(String annotationType,
			boolean classValuesAsString) {
		List<AnnotationAttributes> attributes = this.attributeMap.get(annotationType);
		return (attributes == null ? null : AnnotationReadingVisitorUtils.convertClassValues(
				this.classLoader, attributes.get(0), classValuesAsString, false));
	}

	public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationType) {
		return getAllAnnotationAttributes(annotationType, false);
	}

	public MultiValueMap<String, Object> getAllAnnotationAttributes(
			String annotationType, boolean classValuesAsString) {
		if(!this.attributeMap.containsKey(annotationType)) {
			return null;
		}
		MultiValueMap<String, Object> allAttributes = new LinkedMultiValueMap<String, Object>();
		for (AnnotationAttributes annotationAttributes : this.attributeMap.get(annotationType)) {
			for (Map.Entry<String, Object> entry : AnnotationReadingVisitorUtils.convertClassValues(
					this.classLoader, annotationAttributes, classValuesAsString, false).entrySet()) {
				allAttributes.add(entry.getKey(), entry.getValue());
			}
		}
		return allAttributes;
	}

	public String getDeclaringClassName() {
		return this.declaringClassName;
	}
}
