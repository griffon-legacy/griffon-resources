package griffon.resourcemanager

import java.awt.Dimension
import java.awt.Insets
import java.awt.Point
import java.awt.Rectangle
import java.awt.Color

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

class URLFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (!(value instanceof String))
            throw new RuntimeException("The URL-spec must be of type String");
        if (attributes.context instanceof URL)
            return new URL(attributes.context, value)
        else if (attributes.context instanceof URI)
            return new URL(attributes.context.toURL(), value)
        else
            return new URL(value)
    }

    @Override
    boolean isLeaf() { true }
}

class URIFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (value instanceof String)
            return new URI(value)
        else if (value instanceof URL)
            return value.toURI()
        else
            throw new RuntimeException("The URI-spec must be of type String or java.net.URL")
    }

    @Override
    boolean isLeaf() { true }
}

class DimensionFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        int w, h
        try {
            if (value instanceof String) {
                (w, h) = value.split(/\s*,\s*/).collect {it as int}
            } else if ((value instanceof Collection && value.size()) > 1 || (value instanceof Object[] && value.length > 1)) {
                (w, h) = value
            } else if ((attr.width || attr.w) && (attr.height || attr.h)) {
                w = attr.width ?: attr.w
                h = attr.height ?: attr.h
            } else
                throw new RuntimeException("Invalid Dimension format")
        } catch (e) {
            throw new RuntimeException("Invalid Dimension format")
        }
        return new Dimension(w, h)
    }

    @Override
    boolean isLeaf() { true }
}

class InsetsFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        int t = 0, l = 0, b = 0, r = 0
        try {
            if (value instanceof String) {
                (t, l, b, r) = value.split(/\s*,\s*/).collect {it as int}
            } else if ((value instanceof Collection && value.size()) > 3 || (value instanceof Object[] && value.length > 3)) {
                (t, l, b, r) = value
            }
            if (attr.top || attr.t) {
                t = attr.top ?: attr.t
            }
            if (attr.left || attr.l) {
                t = attr.left ?: attr.l
            }
            if (attr.right || attr.r) {
                t = attr.right ?: attr.r
            }
            if (attr.bottom || attr.b) {
                t = attr.bottom ?: attr.b
            }
        } catch (e) {
            throw new RuntimeException("Invalid Insets format")
        }
        return new Insets(t, l, b, r)
    }

    @Override
    boolean isLeaf() { true }
}

class PointFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        int x, y
        try {
            if (value instanceof String) {
                (x, y) = value.split(/\s*,\s*/).collect {it as int}
            } else if ((value instanceof Collection && value.size()) > 1 || (value instanceof Object[] && value.length > 1)) {
                (x, y) = value
            } else if (attr.x && attr.y) {
                x = attr.y
                y = attr.y
            } else
                throw new RuntimeException("Invalid Point format")
        } catch (e) {
            throw new RuntimeException("Invalid Point format")
        }
        return new Point(x, y)
    }

    @Override
    boolean isLeaf() { true }
}

class RectangleFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        int x, y, w, h
        try {
            if (value instanceof String) {
                (x, y, w, h) = value.split(/\s*,\s*/).collect {it as int}
            } else if ((value instanceof Collection && value.size()) > 3 || (value instanceof Object[] && value.length > 3)) {
                (x, y, w, h) = value
            }
            if (attr.x)
                x = attr.x
            if (attr.y)
                y = attr.y
            if (attr.width || attr.w)
                w = attr.width ?: attr.w
            if (attr.height || attr.h)
                h = attr.height ?: attr.h
            if (attr.point instanceof Point) {
                x = attr.point.x
                y = attr.point.y
            }
            if (attr.dimension instanceof Dimension) {
                h = attr.dimension.height
                w = attr.dimension.width
            }
        } catch (e) {
        }
        if (!x || !y || !w || !h)
            throw new RuntimeException("Invalid Rectangle format")
        return new Rectangle(x, y, w, h)
    }

    @Override
    boolean isLeaf() { true }
}

class LocaleFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        try {
            if (value instanceof String) {
                if (!value)
                    return Locale.default
                return new Locale(* value.trim().split('_'))
            } else if (attr.language) {
                return new Locale(attr.language, attr.country ?: "", attr.variant ?: "")
            }
        } catch (e) {
            throw new RuntimeException("Invalid Locale format")
        }
        throw new RuntimeException("Invalid Locale format")
    }

    @Override
    boolean isLeaf() { true }
}

class ColorFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        int r, g, b, a = 255
        try {
            if (value instanceof String) {
                if (!value)
                    return Locale.default
                return new Locale(* value.trim().split('_'))
            } else if (attr.language) {
                return new Color(attr.language, attr.country ?: "", attr.variant ?: "")
            }
        } catch (e) {
            throw new RuntimeException("Invalid Color format")
        }
        throw new RuntimeException("Invalid Color format")
        return new Color(r, g, b, a)
    }

    @Override
    boolean isLeaf() { true }
}
// TODO:
// Gfx: color, font, image, rgba
