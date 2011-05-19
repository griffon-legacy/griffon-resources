package griffon.resourcemanager

import java.awt.MultipleGradientPaint.CycleMethod
import java.awt.font.TextAttribute
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.beans.PropertyEditorSupport
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream
import javax.swing.ImageIcon
import java.awt.*

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

class ToStringEditor extends PropertyEditorSupport {
    @Override
    void setValue(Object value) throws IllegalArgumentException {
        super.setValue(value == null ? null : value.toString())
    }

    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }
}

class ColorEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object value) throws IllegalArgumentException {
        try {
            if (value instanceof String) {
                if (value.startsWith('#')) {
                    switch (value.size()) {
                        case 4:
                            value = "#${value[1]}${value[1]}${value[2]}${value[2]}${value[3]}${value[3]}FF"
                        case 7:
                            value = "#${value[1..6]}FF"
                        case 9:
                            super.setValue(new Color(Integer.parseInt(value[1..2], 16), Integer.parseInt(value[3..4], 16), Integer.parseInt(value[5..6], 16), Integer.parseInt(value[7..8], 16)))
                    }
                } else {
                    def color = Color.getColor(value)
                    if (color) super.setValue(color)
                }
            } else if (value instanceof Collection) {
                switch (value.size()) {
                    case 3:
                        value << 255
                    case 4:
                        super.setValue(new Color(value[0] as int, value[1] as int, value[2] as int, value[3] as int))
                }
            } else if (value instanceof Map) {
                def r = value['red'] ?: value['r'] ?: 0
                def g = value['green'] ?: value['g'] ?: 0
                def b = value['blue'] ?: value['b'] ?: 0
                def a = value['alpha'] ?: value['a'] ?: 255
                super.setValue(new Color(r as int, g as int, b as int, a as int))
            } else
                throw new IllegalArgumentException("Incorrect Color format: $value")
        } catch (e) {throw new IllegalArgumentException("Incorrect Color format: $value", e)}
    }
}

class DimensionEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object value) throws IllegalArgumentException {
        try {
            int w, h
            if (value instanceof String) {
                (w, h) = value.split(/\s*,\s*/).collect {it as int}
            } else if ((value instanceof Collection && value.size() > 1) || (value instanceof Object[] && value.length > 1)) {
                (w, h) = value
            } else if (value instanceof Map) {
                w = (value['width'] ?: value['w'] ?: 0) as int
                h = (value['height'] ?: value['h'] ?: 0) as int
            } else
                throw new IllegalArgumentException("Incorrect Dimension format: $value")
            super.setValue(new Dimension(w, h))
        } catch (e) {throw new IllegalArgumentException("Incorrect Dimension format: $value", e)}
    }
}

class InsetsEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            int t = 0, l = 0, b = 0, r = 0
            if (o instanceof String) {
                (t, l, b, r) = o.split(/\s*,\s*/).collect {it as int}
            } else if ((o instanceof Collection && o.size() > 3) || (o instanceof Object[] && o.length > 3)) {
                (t, l, b, r) = o
            } else if (value instanceof Map) {
                t = (value['top'] ?: value['t'] ?: 0) as int
                l = (value['left'] ?: value['l'] ?: 0) as int
                r = (value['right'] ?: value['r'] ?: 0) as int
                b = (value['bottom'] ?: value['b'] ?: 0) as int
            } else
                throw new IllegalArgumentException("Incorrect Insets format: $o")
            super.setValue(new Insets(t, l, b, r))
        } catch (e) {throw new IllegalArgumentException("Incorrect Insets format: $o", e)}
    }
}

class PointEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            int x, y
            if (o instanceof String) {
                (x, y) = o.split(/\s*,\s*/).collect {it as int}
            } else if ((o instanceof Collection && o.size() > 1) || (o instanceof Object[] && o.length > 1)) {
                (x, y) = o
            } else if (value instanceof Map) {
                x = (value['x'] ?: 0) as int
                y = (value['y'] ?: 0) as int
            } else
                throw new IllegalArgumentException("Incorrect Point format: $o")
            super.setValue(new Point(x, y))
        } catch (e) {throw new IllegalArgumentException("Incorrect Point format: $o", e)}
        throw new IllegalArgumentException("Incorrect Point format: $o")
    }
}

class RectangleEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            int x, y, w, h
            if (o instanceof String) {
                (x, y, w, h) = o.split(/\s*,\s*/).collect {it as int}
            } else if ((o instanceof Collection && o.size() > 3) || (o instanceof Object[] && o.length > 3)) {
                (x, y, w, h) = o
            } else if (o instanceof Map) {
                x = (o['x'] ?: 0) as int
                y = (o['y'] ?: 0) as int
                w = (o['width'] ?: o['w'] ?: 0) as int
                h = (o['height'] ?: o['h'] ?: 0) as int
                if(o.dimension instanceof Dimension) {
                    w = o.dimension.@width
                    h = o.dimension.@height
                }
                if(o.point instanceof Point) {
                    x = o.dimension.@x
                    y = o.dimension.@y
                }
            } else
                throw new IllegalArgumentException("Incorrect Rectangle format: $o")
            super.setValue(new Rectangle(x, y, w, h))
        } catch (e) {throw new IllegalArgumentException("Incorrect Rectangle format: $o", e)}
    }
}

class FontEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            Map<TextAttribute, ?> attributes = [:]
            if (o instanceof String) {
                def matcher = o =~ /^\s*((\w*\s*)*)((\[.*\])\s*)?$/
                def parts = matcher[0][1].split()
                def map = matcher[0][3] ? new GroovyShell().evaluate(matcher[0][3]) : [:]
                if (!map instanceof Map)
                    throw new RuntimeException("Attributes in incorrect format")
                if (parts.size() < 2)
                    throw new RuntimeException("Please specify at least name and size")
                attributes[TextAttribute.FAMILY] = ensureFontIsInstalled(parts[0])
                attributes[TextAttribute.SIZE] = parts[1] as BigDecimal
                for (int i = 2; i < parts.size(); i++) {
                    def part = parts[i].toUpperCase()
                    switch (part) {
                        case 'BOLD':
                            attributes[TextAttribute.WEIGHT] = TextAttribute.WEIGHT_BOLD
                            break
                        case 'ITALIC':
                            attributes[TextAttribute.POSTURE] = TextAttribute.POSTURE_OBLIQUE
                            break
                        case 'UNDERLINE':
                            attributes[TextAttribute.UNDERLINE] = TextAttribute.UNDERLINE_ON
                            break
                        case 'STRIKETHROUGH':
                            attributes[TextAttribute.STRIKETHROUGH] = TextAttribute.STRIKETHROUGH_ON
                            break
                    }
                }
                map.each {k, v ->
                    k = k.toUpperCase()
                    def attrib = TextAttribute.@"$k"
                    if (attrib == TextAttribute.FAMILY)
                        attributes[attrib] = ensureFontIsInstalled(v)
                    else if (v instanceof String) {
                        attributes[attrib] = TextAttribute.@"${k}_${v.toUpperCase()}"
                    } else {
                        attributes[attrib] = v
                    }
                }
            } else if (o instanceof Map) {
                o.each { k, v ->
                    def attrib
                    if (k instanceof String) {
                        k = k.toUpperCase()
                        attrib = TextAttribute.@"$k"
                        if (attrib == TextAttribute.FAMILY)
                            attributes[attrib] = ensureFontIsInstalled(v)
                        else if (v instanceof String) {
                            attributes[attrib] = TextAttribute.@"${k}_${v.toUpperCase()}"
                        } else {
                            attributes[attrib] = v
                        }
                    } else if (k instanceof TextAttribute) {
                        attributes[k] = v
                    }
                }
            }
            if (attributes) {
                super.setValue(new Font(attributes))
                return
            }
        } catch (e) {throw new IllegalArgumentException("Incorrect Font format: $o", e)}
        throw new IllegalArgumentException("Incorrect Rectangle format: $o")
    }

    private String ensureFontIsInstalled(def name) {
        try {
            URL source
            if (name instanceof URL)
                source = name
            else if (name instanceof URI)
                source = name.toURL()
            else if (name instanceof String)
                source = name.toURL()
        } catch (MalformedURLException e) {}
        if (source) {
            InputStream stream = source.newInputStream()
            Font font = Font.createFont(Font.TRUETYPE_FONT, stream)
            stream.close()
            GraphicsEnvironment ge = GraphicsEnvironment.localGraphicsEnvironment
            ge.registerFont(font)
            name = font.family
        }
        return name
    }
}

class ImageEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            if (o instanceof Map)
                o = o.source ?: o.src
            def img
            if (o instanceof String)
                img = ImageIO.read(o.toURL())
            else if (o instanceof File)
                img = ImageIO.read(o)
            else if (o instanceof URL)
                img = ImageIO.read(o)
            else if (o instanceof URI)
                img = ImageIO.read(o.toURL())
            else if (o instanceof ImageInputStream)
                img = ImageIO.read(o)
            else if (o instanceof InputStream)
                img = ImageIO.read(o)
            else if (o instanceof byte[])
                img = new BufferedImage(new ImageIcon(o))
            else
                throw new IllegalArgumentException("Incorrect Image format: $o")
            super.setValue(img)
        } catch (e) {throw new IllegalArgumentException("Incorrect Image format: $o", e)}

    }
}

class IconEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            def source
            def description
            if (o instanceof Map) {
                o = o.source ?: o.src
                description = o.description
            }
            if (o instanceof String) {
                def parts = source.split(/\s*,\s*/)
                source = parts[0].toURL()
                description = parts.size() > 1 ? parts[1] : null
            } else if (o instanceof File)
                source = o
            else if (o instanceof URL)
                source = o
            else if (o instanceof URI)
                source = o.toURL()
            else if (o instanceof ImageInputStream)
                source = o
            else if (o instanceof InputStream)
                source = o
            else if (o instanceof Image)
                source = o
            else if (o instanceof byte[])
                source = o
            if (source) {
                def icon
                if (source instanceof Image)
                    icon = new ImageIcon(source)
                else if (source instanceof byte[])
                    icon = new ImageIcon(source)
                else
                    icon = new ImageIcon(ImageIO.read(source))
                if (description)
                    icon.description = description
                super.setValue(icon)
                return
            }
        } catch (e) {throw new IllegalArgumentException("Incorrect Icon format: $o", e)}
        throw new IllegalArgumentException("Incorrect Icon format: $o")
    }
}

class GradientPaintEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        ColorEditor colorEditor = new ColorEditor()
        try {
            float x1, y1, x2, y2
            Color c1, c2
            if (o instanceof String) {
                def i = 1
                (x1, y1, c1, x2, y2, c2) = o.split(/\s*,\s*/).collect {
                    def val
                    if ((i % 3))
                        val = it
                    else {
                        colorEditor.setValue(it)
                        val = colorEditor.getValue()
                    }
                    i++
                    return val
                }
            } else if ((o instanceof Collection && o.size() > 5) || (o instanceof Object[] && o.length > 5)) {
                (x1, y1, c1, x2, y2, c2) = o
            } else if (o instanceof Map) {
                x1 = (o['x1'] ?: 0)
                y1 = (o['y1'] ?: 0)
                x2 = (o['x2'] ?: 0)
                y2 = (o['y2'] ?: 0)
                def col1 = o['startColor'] ?: o['color1'] ?: o['c1'] ?: Color.WHITE
                if (col1 instanceof Color)
                    c1 = col1
                else {
                    colorEditor.setValue(col1)
                    c1 = colorEditor.getValue()
                }
                def col2 = o['endColor'] ?: o['color2'] ?: o['c2'] ?: Color.BLACK
                if (col1 instanceof Color)
                    c2 = col2
                else {
                    colorEditor.setValue(col2)
                    c2 = colorEditor.getValue()
                }
            } else
                throw new IllegalArgumentException("Incorrect GradientPaint format: $o")
            super.setValue(new GradientPaint(x1 as float, y1 as float, c1, x2 as float, y2 as float, c2))
        } catch (e) {
            throw new IllegalArgumentException("Incorrect GradientPaint format: $o", e)
        }
    }
}

class LinearGradientPaintEditor extends PropertyEditorSupport {
    @Override

    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        ColorEditor colorEditor = new ColorEditor()
        try {
            def x1, y1, x2, y2
            Map<Float, Color> colors = []
            CycleMethod cycleMethod = CycleMethod.REPEAT
            if (o instanceof String) {
                o = o.trim()
                // "10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT"
                def b1 = o.indexOf('[')
                def b2 = src.indexOf(']')
                (x1, y1, x2, y2) = src.substring(0, b1).split(/\s*,\s*/)
                colors = src.substring(b1 + 1, b2).split(/\s*,\s*/).collect {
                    def p = it.split(/\s*:\s*/)
                    colorEditor.setValue(p[1])
                    [p[0] as float, colorEditor.getValue()]
                }
                if (src.length() > b2 + 1) {
                    def sub = src.substring(b2 + 1).tr(',', ' ').trim().toUpperCase()
                    switch (sub) {
                        case 'REPEAT':
                            cycleMethod = CycleMethod.REPEAT
                        case 'REFLECT':
                            cycleMethod = CycleMethod.REFLECT
                        case 'NO_CYCLE':
                            cycleMethod = CycleMethod.NO_CYCLE
                    }
                }
            } else if ((o instanceof Collection && o.size() > 5) || (o instanceof Object[] && o.length > 5)) {
                (x1, y1, x2, y2, colors, cycleMethod) = o
            }
            else if (o instanceof Map) {
                x1 = (o['x1'] ?: 0)
                y1 = (o['y1'] ?: 0)
                x2 = (o['x2'] ?: 0)
                y2 = (o['y2'] ?: 0)
                colors = o['colors'] ?: o['c']
                cycleMethod = o['cycleMethod'] ?: o['cycle'] ?: CycleMethod.REPEAT
            } else
                throw new IllegalArgumentException("Incorrect LinearGradientPaint format:  $o")
            super.setValue(new LinearGradientPaint(x1 as float, y1 as float, x2 as float, y2 as float, colors.keySet() as float[], colors.values() as Color[], cycleMethod))
        } catch (e) {throw new IllegalArgumentException("Incorrect LinearGradientPaint format:  $o", e)}
    }
}

class RadialGradientPaintEditor extends PropertyEditorSupport {
    @Override

    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        ColorEditor colorEditor = new ColorEditor()
        try {
            def cx, cy, fx = null, fy = null, r
            Map<Float, Color> colors = []
            CycleMethod cycleMethod = CycleMethod.REPEAT
            if (o instanceof String) {
                o = o.trim()
                // "100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT"
                // "100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT"
                def b1 = o.indexOf('[')
                def b2 = src.indexOf(']')
                def parts = src.substring(0, b1).split(/\s*,\s*/)
                if (parts.length == 3)
                    (cx, cy, r) = parts
                else
                    (cx, cy, fx, fy, r) = parts
                colors = src.substring(b1 + 1, b2).split(/\s*,\s*/).collect {
                    def p = it.split(/\s*:\s*/)
                    colorEditor.setValue(p[1])
                    [p[0] as float, colorEditor.getValue()]
                }
                if (src.length() > b2 + 1) {
                    def sub = src.substring(b2 + 1).tr(',', ' ').trim().toUpperCase()
                    switch (sub) {
                        case 'REPEAT':
                            cycleMethod = CycleMethod.REPEAT
                        case 'REFLECT':
                            cycleMethod = CycleMethod.REFLECT
                        case 'NO_CYCLE':
                            cycleMethod = CycleMethod.NO_CYCLE
                    }
                }
            } else if ((o instanceof Collection && o.size() > 6) || (o instanceof Object[] && o.length > 6)) {
                if (o.length == 4 | o.length == 5)
                    (cx, cy, r, colors, cycleMethod) = o
                else if (o.length > 5)
                    (cx, cy, fx, fy, r, colors, cycleMethod) = o
                else
                    throw new IllegalArgumentException("Incorrect RadialGradientPaint format:  $o")
            }
            else if (o instanceof Map) {
                cx = (o['cx'] ?: 0)
                cy = (o['cy'] ?: 0)
                fx = (o['fx'] ?: null)
                fy = (o['fy'] ?: null)
                r = o['radius'] ?: o['r'] ?: 0
                colors = o['colors'] ?: o['c']
                cycleMethod = o['cycleMethod'] ?: o['cycle'] ?: CycleMethod.REPEAT
            } else
                throw new IllegalArgumentException("Incorrect RadialGradientPaint format:  $o")
            if (fx == null || fy == null)
                super.setValue(new RadialGradientPaint(cx as float, cy as float, r as float, colors.keySet() as float[], colors.values() as Color[], cycleMethod))
            else
                super.setValue(new RadialGradientPaint(cx as float, cy as float, fx as float, fy as float, r as float, colors.keySet() as float[], colors.values() as Color[], cycleMethod))
        } catch (e) {throw new IllegalArgumentException("Incorrect RadialGradientPaint format:  $o", e)}
    }
}

class TexturePaintEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            def img
            def anchor
            if (o instanceof String) {
                def parts = o.split(/\s*,\s*/)
                if (parts.length == 5) {
                    img = parts[0]
                    anchor = parts[1..4]
                } else
                    throw new IllegalArgumentException("Incorrect TexturePaint format: $o")
            } else if ((o instanceof Collection && o.size() > 2) || (o instanceof Object[] && o.length > 2)) {
                img = o[0]
                anchor = o[1]
            } else if (o instanceof Map) {
                img = o['source'] ?: o['image'] ?: o['src']
                anchor = o['anchor'] ?: o['rectangle'] ?: o['rect']
            } else
                throw new IllegalArgumentException("Incorrect TexturePaint format: $o")
            if (img instanceof BufferedImage) {
            } else if (img instanceof Image)
                img = bufferImage(img)
            else {
                def editor = new ImageEditor()
                editor.setValue(o[0])
                img = editor.getValue()
                if (!img instanceof BufferedImage)
                    img = bufferImage(img)
            }
            if (anchor instanceof Rectangle2D) {
            } else {
                def editor = new RectangleEditor()
                editor.setValue(anchor)
                anchor = editor.getValue()
            }
            super.setValue(new TexturePaint(img, anchor))
        } catch (e) {throw new IllegalArgumentException("Incorrect TexturePaint format: $o", e)}
    }

    static BufferedImage bufferImage(Image image, int type = BufferedImage.TYPE_INT_ARGB) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type)
        Graphics2D g = bufferedImage.createGraphics()
        g.drawImage(image, null, null)
        waitForImage(bufferedImage)
        return bufferedImage
    }

    private static waitForImage(BufferedImage bufferedImage) {
        boolean widthDone = false
        boolean heightDone = false
        bufferedImage.getHeight(new ImageObserver() {
            boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                if (infoflags == ImageObserver.ALLBITS) {
                    heightDone = true
                    return true
                }
                return false
            }
        });
        bufferedImage.getWidth(new ImageObserver() {
            boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                if (infoflags == ImageObserver.ALLBITS) {
                    widthDone = true
                    return true
                }
                return false
            }
        });
        while (!widthDone && !heightDone) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {}
        }
    }
}