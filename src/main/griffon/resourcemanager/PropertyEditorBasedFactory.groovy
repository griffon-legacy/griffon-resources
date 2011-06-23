/*
 * Copyright 2010 the original author or authors.
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

package griffon.resourcemanager

import java.beans.PropertyEditor

/**
 * @author Alexander Klein
 */
class PropertyEditorBasedFactory extends AbstractFactory {
    Class editor
    Closure modifyValue
    Closure modifyMap

    PropertyEditorBasedFactory(Map params = [:], Class editor) {
        this.editor = editor
        this.modifyValue = params.modifyValue
        this.modifyMap = params.modifyMap
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        def editor = this.editor.newInstance()
        if (value) {
            try {
                translate(editor, (this.modifyValue ? this.modifyValue(value) : value))
            } catch (Exception e) {
                throw new IllegalArgumentException(e)
            }
        } else {
            try {
                translate(editor, (this.modifyMap ? this.modifyMap(attr) : attr))
                attr.clear()
            } catch (Exception e) {
                throw new IllegalArgumentException(e)
            }
        }
        return editor.getValue()
    }

    private translate(PropertyEditor editor, def value) {
        (value instanceof String) ? editor.setAsText(value) : editor.setValue(value)
    }

    @Override
    boolean isLeaf() { true }
}

