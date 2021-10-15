/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.beans.factory.parsing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;

/**
 * {@link ReaderEventListener}的简单实现，提供空白操作
 * 可参考网络一篇实现：
 * https://blog.csdn.net/RobertoHuang/article/details/78881118
 *
 * Empty implementation of the {@link ReaderEventListener} interface,
 * providing no-op implementations of all callback methods.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
@Slf4j
public class EmptyReaderEventListener implements ReaderEventListener {

	public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
		if (defaultsDefinition instanceof DocumentDefaultsDefinition) {
			DocumentDefaultsDefinition documentDefaultsDefinition = (DocumentDefaultsDefinition) defaultsDefinition;
//			documentDefaultsDefinition.setLazyInit("true");
		}
		log.warn("I am in the defaultsRegistered");
		// no-op
	}

	public void componentRegistered(ComponentDefinition componentDefinition) {
		// no-op
	}

	public void aliasRegistered(AliasDefinition aliasDefinition) {
		// no-op
	}

	public void importProcessed(ImportDefinition importDefinition) {
		// no-op
	}

}
