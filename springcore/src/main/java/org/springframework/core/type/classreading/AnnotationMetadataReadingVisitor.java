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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * ASM class visitor which looks for the class name and implemented types as
 * well as for the annotations defined on the class, exposing them through
 * the {@link org.springframework.core.type.AnnotationMetadata} interface.
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Costin Leau
 * @author Phillip Webb
 * @since 2.5
 */
final class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata {

	private final ClassLoader classLoader;

	private final Set<String> annotationSet = new LinkedHashSet<String>();

	private final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<String, Set<String>>(4);

	private final MultiValueMap<String, AnnotationAttributes> attributeMap = new LinkedMultiValueMap<String, AnnotationAttributes>(4);

	private final MultiValueMap<String, MethodMetadata> methodMetadataMap = new LinkedMultiValueMap<String, MethodMetadata>();

	private final MetadataReaderLog logger;


	public AnnotationMetadataReadingVisitor(ClassLoader classLoader, MetadataReaderLog logger) {
		this.classLoader = classLoader;
		this.logger = logger;
	}


	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new MethodMetadataReadingVisitor(name, access, this.getClassName(),
				this.classLoader, this.methodMetadataMap, this.logger);
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
		String className = Type.getType(desc).getClassName();
		this.annotationSet.add(className);
		return new AnnotationAttributesReadingVisitor(className, this.attributeMap,
				this.metaAnnotationMap, this.classLoader, this.logger);
	}


	public Set<String> getAnnotationTypes() {
		return this.annotationSet;
	}

	public Set<String> getMetaAnnotationTypes(String annotationType) {
		return this.metaAnnotationMap.get(annotationType);
	}

	public boolean hasAnnotation(String annotationType) {
		return this.annotationSet.contains(annotationType);
	}

	public boolean hasMetaAnnotation(String metaAnnotationType) {
		Collection<Set<String>> allMetaTypes = this.metaAnnotationMap.values();
		for (Set<String> metaTypes : allMetaTypes) {
			if (metaTypes.contains(metaAnnotationType)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAnnotated(String annotationType) {
		return this.attributeMap.containsKey(annotationType);
	}

	public AnnotationAttributes getAnnotationAttributes(String annotationType) {
		return getAnnotationAttributes(annotationType, false);
	}

	public AnnotationAttributes getAnnotationAttributes(String annotationType, boolean classValuesAsString) {
		return getAnnotationAttributes(annotationType, classValuesAsString, false);
	}

	public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationType) {
		return getAllAnnotationAttributes(annotationType, false);
	}

	public MultiValueMap<String, Object> getAllAnnotationAttributes(
			String annotationType, boolean classValuesAsString) {
		MultiValueMap<String, Object> allAttributes = new LinkedMultiValueMap<String, Object>();
		List<AnnotationAttributes> attributes = this.attributeMap.get(annotationType);
		if (attributes == null) {
			return null;
		}
		for (AnnotationAttributes raw : attributes) {
			for (Map.Entry<String, Object> entry : AnnotationReadingVisitorUtils.convertClassValues(
					this.classLoader, raw, classValuesAsString, false).entrySet()) {
				allAttributes.add(entry.getKey(), entry.getValue());
			}
		}
		return allAttributes;
	}

	public AnnotationAttributes getAnnotationAttributes(
			String annotationType, boolean classValuesAsString, boolean nestedAttributesAsMap) {
		List<AnnotationAttributes> attributes = this.attributeMap.get(annotationType);
		AnnotationAttributes raw = attributes == null ? null : attributes.get(0);
		return AnnotationReadingVisitorUtils.convertClassValues(this.classLoader, raw,
				classValuesAsString, nestedAttributesAsMap);
	}

	public boolean hasAnnotatedMethods(String annotationType) {
		return this.methodMetadataMap.containsKey(annotationType);
	}

	public Set<MethodMetadata> getAnnotatedMethods(String annotationType) {
		List<MethodMetadata> list = this.methodMetadataMap.get(annotationType);
		if (CollectionUtils.isEmpty(list)) {
			return new LinkedHashSet<MethodMetadata>(0);
		}
		Set<MethodMetadata> annotatedMethods = new LinkedHashSet<MethodMetadata>(list.size());
		annotatedMethods.addAll(list);
		return annotatedMethods;
	}
}
