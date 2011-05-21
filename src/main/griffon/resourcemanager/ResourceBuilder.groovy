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
            registerFactory("url", new PropertyEditorBasedFactory(URLEditor, modifyMap: {attr ->
                attr.source ?: attr.url ?: attr.src
            }))
            registerFactory("uri", new PropertyEditorBasedFactory(URIEditor, modifyMap: {attr ->
                attr.source ?: attr.uri ?: attr.src
            }))
            registerFactory("dimension", new PropertyEditorBasedFactory(DimensionEditor))
            registerFactory("insets", new PropertyEditorBasedFactory(InsetsEditor))
            registerFactory("point", new PropertyEditorBasedFactory(PointEditor))
            registerFactory("locale", new PropertyEditorBasedFactory(LocaleEditor, modifyMap: {attr ->
                attr.locale
            }))
            registerFactory("rectangle", new PropertyEditorBasedFactory(RectangleEditor))
            registerFactory("color", new PropertyEditorBasedFactory(ColorEditor))
            registerFactory("font", new PropertyEditorBasedFactory(FontEditor))
            registerFactory("image", new PropertyEditorBasedFactory(ImageEditor))
            registerFactory("icon", new PropertyEditorBasedFactory(IconEditor))
            registerFactory("gradientPaint", new PropertyEditorBasedFactory(GradientPaintEditor))
            registerFactory("linearGradientPaint", new PropertyEditorBasedFactory(LinearGradientPaintEditor))
            registerFactory("radialGradientPaint", new PropertyEditorBasedFactory(RadialGradientPaintEditor))
            registerFactory("texturePaint", new PropertyEditorBasedFactory(TexturePaintEditor))
		}
	}

