package griffon.resourcemanager

import org.springframework.beans.propertyeditors.URLEditor
import org.springframework.beans.propertyeditors.URIEditor
import org.springframework.beans.propertyeditors.LocaleEditor

/**
 * Created by IntelliJ IDEA.
 * User: sascha
 * Date: 19.05.11
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
public class ResourceBuilder extends FactoryBuilderSupport {
		public ResourceBuilder(boolean init = true) {
			super(init)
		}

		def registerFactories() {
            registerFactory("Url", new PropertyEditorBasedFactory(URLEditor, modifyMap: {attr ->
                attr.source ?: attr.url ?: attr.src
            }))
            registerFactory("Uri", new PropertyEditorBasedFactory(URIEditor, modifyMap: {attr ->
                attr.source ?: attr.uri ?: attr.src
            }))
            registerFactory("Dimension", new PropertyEditorBasedFactory(DimensionEditor))
            registerFactory("Insets", new PropertyEditorBasedFactory(InsetsEditor))
            registerFactory("Point", new PropertyEditorBasedFactory(PointEditor))
            registerFactory("Locale", new PropertyEditorBasedFactory(LocaleEditor, modifyMap: {attr ->
                attr.locale
            }))
            registerFactory("Rectangle", new PropertyEditorBasedFactory(RectangleEditor))
            registerFactory("Color", new PropertyEditorBasedFactory(ColorEditor))
            registerFactory("Font", new PropertyEditorBasedFactory(FontEditor))
            registerFactory("Image", new PropertyEditorBasedFactory(ImageEditor))
            registerFactory("Icon", new PropertyEditorBasedFactory(IconEditor))
            registerFactory("GradientPaint", new PropertyEditorBasedFactory(GradientPaintEditor))
            registerFactory("LinearGradientPaint", new PropertyEditorBasedFactory(LinearGradientPaintEditor))
            registerFactory("RadialGradientPaint", new PropertyEditorBasedFactory(RadialGradientPaintEditor))
            registerFactory("TexturePaint", new PropertyEditorBasedFactory(TexturePaintEditor))
		}
	}

