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

import org.springframework.beans.propertyeditors.LocaleEditor
import org.springframework.beans.propertyeditors.URIEditor
import org.springframework.beans.propertyeditors.URLEditor

/**
 * @author Alexander Klein
 */
public class ResourceBuilder extends FactoryBuilderSupport {
    public ResourceBuilder() {
        super(true)
    }

    static factoryMap = [
            url: new PropertyEditorBasedFactory(URLEditor, modifyMap: {attr ->
                attr.source ?: attr.url ?: attr.src
            }),
            uri: new PropertyEditorBasedFactory(URIEditor, modifyMap: {attr ->
                attr.source ?: attr.uri ?: attr.src
            }),
            dimension: new PropertyEditorBasedFactory(DimensionEditor),
            insets: new PropertyEditorBasedFactory(InsetsEditor),
            point: new PropertyEditorBasedFactory(PointEditor),
            locale: new PropertyEditorBasedFactory(LocaleEditor, modifyMap: {attr ->
                attr.locale
            }),
            rectangle: new PropertyEditorBasedFactory(RectangleEditor),
            color: new PropertyEditorBasedFactory(ColorEditor),
            font: new PropertyEditorBasedFactory(FontEditor),
            image: new PropertyEditorBasedFactory(ImageEditor),
            icon: new PropertyEditorBasedFactory(IconEditor),
            gradientPaint: new PropertyEditorBasedFactory(GradientPaintEditor),
            linearGradientPaint: new PropertyEditorBasedFactory(LinearGradientPaintEditor),
            radialGradientPaint: new PropertyEditorBasedFactory(RadialGradientPaintEditor),
            texturePaint: new PropertyEditorBasedFactory(TexturePaintEditor),
    ]

    def registerFactories() {
        factoryMap.each {k,v ->
            registerFactory k, v
        }
    }
}

