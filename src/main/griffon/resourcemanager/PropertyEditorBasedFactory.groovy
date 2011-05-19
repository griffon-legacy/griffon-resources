package griffon.resourcemanager

import java.awt.Dimension
import java.awt.Insets
import java.awt.Point
import java.awt.Rectangle
import java.awt.Color
import org.springframework.beans.propertyeditors.LocaleEditor
import org.springframework.beans.propertyeditors.URIEditor
import org.springframework.beans.propertyeditors.URLEditor
import java.beans.PropertyEditor

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

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        def editor = this.editor.newInstance()
        try {
            editor.setValue(modifyValue ? modifyValue(value) : value)
        } catch (IllegalArgumentException e) {
            try {
                editor.setValue(modifyMap ? modifyMap(attr) : attr)
            } catch (IllegalArgumentException e1) {
                throw new IllegalArgumentException("${e.getMessage()} / ${e1.getMessage()}")
            }
        }
        return editor.getValue()
    }

    @Override
    boolean isLeaf() { true }
}

