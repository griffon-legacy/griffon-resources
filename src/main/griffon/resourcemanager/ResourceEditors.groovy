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

/**
 * @author Alexander Klein
 */
class ToStringEditor extends PropertyEditorSupport {
    @Override
    void setValue(Object o) throws IllegalArgumentException {
        super.setValue(o == null ? null : o.toString())
    }

    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }
}

/**
 * @author Alexander Klein
 */
class ColorEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            if (o instanceof String || o instanceof GString) {
                if (o.startsWith('#')) {
                    switch (o.size()) {
                        case 4:
                            o = "#${o[1]}${o[1]}${o[2]}${o[2]}${o[3]}${o[3]}FF"
                            break
                        case 5:
                            o = "#${o[1]}${o[1]}${o[2]}${o[2]}${o[3]}${o[3]}${o[4]}${o[4]}"
                            break
                        case 7:
                            o = "#${o[1..6]}FF"
                            break
                        case 9:
                            break
                        default:
                            throw new IllegalArgumentException("Incorrect Color format: $o")
                    }
                    super.setValue(new Color(Integer.parseInt(o[1..2], 16), Integer.parseInt(o[3..4], 16), Integer.parseInt(o[5..6], 16), Integer.parseInt(o[7..8], 16)))
                } else {
                    def color = Color.@"$o"
                    if (color) super.setValue(color)
                }
            } else if (o instanceof Collection) {
                switch (o.size()) {
                    case 3:
                        o += 255
                    case 4:
                        super.setValue(new Color(o[0] as int, o[1] as int, o[2] as int, o[3] as int))
                }
            } else if (o instanceof Map) {
                def r = o['red'] ?: o['r'] ?: 0
                def g = o['green'] ?: o['g'] ?: 0
                def b = o['blue'] ?: o['b'] ?: 0
                def a = o['alpha'] ?: o['a'] ?: 255
                super.setValue(new Color(r as int, g as int, b as int, a as int))
            } else
                throw new IllegalArgumentException("Incorrect Color format: $o")
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect Color format: $o", e)}
    }
}

/**
 * @author Alexander Klein
 */
class DimensionEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            int w, h
            if (o instanceof String || o instanceof GString) {
                (w, h) = o.split(/\s*,\s*/).collect {it as int}
            } else if ((o instanceof Collection && o.size() > 1) || (o instanceof Object[] && o.length > 1)) {
                (w, h) = o
            } else if (o instanceof Map) {
                w = (o['width'] ?: o['w'] ?: 0) as int
                h = (o['height'] ?: o['h'] ?: 0) as int
            } else
                throw new IllegalArgumentException("Incorrect Dimension format: $o")
            super.setValue(new Dimension(w, h))
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect Dimension format: $o", e)}
    }
}

/**
 * @author Alexander Klein
 */
class InsetsEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            int t = 0, l = 0, b = 0, r = 0
            if (o instanceof String || o instanceof GString) {
                (t, l, b, r) = o.split(/\s*,\s*/).collect {it as int}
            } else if ((o instanceof Collection && o.size() > 3) || (o instanceof Object[] && o.length > 3)) {
                (t, l, b, r) = o
            } else if (o instanceof Map) {
                t = (o['top'] ?: o['t'] ?: 0) as int
                l = (o['left'] ?: o['l'] ?: 0) as int
                r = (o['right'] ?: o['r'] ?: 0) as int
                b = (o['bottom'] ?: o['b'] ?: 0) as int
            } else
                throw new IllegalArgumentException("Incorrect Insets format: $o")
            super.setValue(new Insets(t, l, b, r))
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect Insets format: $o", e)}
    }
}

/**
 * @author Alexander Klein
 */
class PointEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            int x, y
            if (o instanceof String || o instanceof GString) {
                (x, y) = o.split(/\s*,\s*/).collect {it as int}
            } else if ((o instanceof Collection && o.size() > 1) || (o instanceof Object[] && o.length > 1)) {
                (x, y) = o
            } else if (o instanceof Map) {
                x = (o['x'] ?: 0) as int
                y = (o['y'] ?: 0) as int
            } else
                throw new IllegalArgumentException("Incorrect Point format: $o")
            super.setValue(new Point(x, y))
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect Point format: $o", e)}
    }
}

/**
 * @author Alexander Klein
 */
class RectangleEditor extends PropertyEditorSupport {
    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            int x, y, w, h
            if (o instanceof String || o instanceof GString) {
                o = o.split(/\s*,\s*/)
            }
            if ((o instanceof Collection && o.size() > 3) || (o instanceof Object[] && o.length > 3)) {
                (x, y, w, h) = o.collect {it as int}
            } else if (o instanceof Map) {
                x = (o['x'] ?: 0) as int
                y = (o['y'] ?: 0) as int
                w = (o['width'] ?: o['w'] ?: 0) as int
                h = (o['height'] ?: o['h'] ?: 0) as int
                if (o.dimension instanceof Dimension) {
                    w = o.dimension.@width
                    h = o.dimension.@height
                }
                if (o.point instanceof Point) {
                    x = o.point.@x
                    y = o.point.@y
                }
            } else
                throw new IllegalArgumentException("Incorrect Rectangle format: $o")
            super.setValue(new Rectangle(x, y, w, h))
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect Rectangle format: $o", e)}
    }
}

/**
 * @author Alexander Klein
 */
class FontEditor extends PropertyEditorSupport {
    private GroovyShell shell = new GroovyShell(new TolerantBinding())

    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            Map<TextAttribute, ?> attributes = [:]
            //'Times New Roman', 12, bold, italic, underline, strikethrough, [kerning: on]
            if (o instanceof String || o instanceof GString)
                o = shell.evaluate("[$o]")
            if (o instanceof Collection) {
                o.eachWithIndex { part, idx ->
                    if (idx == 0)
                        attributes[TextAttribute.FAMILY] = ensureFontIsInstalled(part)
                    else if (idx == 1)
                        attributes[TextAttribute.SIZE] = part as BigDecimal
                    else {
                        switch (part) {
                            case ~/(?i)bold/:
                                attributes[TextAttribute.WEIGHT] = TextAttribute.WEIGHT_BOLD
                                break
                            case ~/(?i)italic/:
                                attributes[TextAttribute.POSTURE] = TextAttribute.POSTURE_OBLIQUE
                                break
                            case ~/(?i)underline/:
                                attributes[TextAttribute.UNDERLINE] = TextAttribute.UNDERLINE_ON
                                break
                            case ~/(?i)strikethrough/:
                                attributes[TextAttribute.STRIKETHROUGH] = TextAttribute.STRIKETHROUGH_ON
                                break
                            case {it instanceof Map}:
                                addMapToAttributes(part, attributes)
                                break
                        }
                    }
                }
            } else if (o instanceof Map) {
                addMapToAttributes(o, attributes)
            }
            if (attributes) {
                super.setValue(new Font(attributes))
                return
            }
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect Font format: $o", e)}
    }

    private def addMapToAttributes(Map map, Map<TextAttribute, ?> attributes) {
        map.each { k, v ->
            if (k instanceof String) {
                k = k.toUpperCase()
                def attr = TextAttribute.@"$k"
                if (attr == TextAttribute.FAMILY)
                    attributes[attr] = ensureFontIsInstalled(v)
                else if (v instanceof String) {
                    attributes[attr] = TextAttribute.@"${k}_${v.toUpperCase()}"
                } else {
                    attributes[attr] = v
                }
            } else if (k instanceof TextAttribute) {
                attributes[k] = v
            }
        }
    }

    private String ensureFontIsInstalled(def name) {
        URL source
        try {
            if (name instanceof URL)
                source = name
            else if (name instanceof URI)
                source = name.toURL()
            else if (name instanceof String)
                source = this.getClass().classLoader.getResource(name) ?: name.toURL()
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

/**
 * @author Alexander Klein
 */
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
            if (o instanceof String || o instanceof GString) {
                def url = this.getClass().classLoader.getResource(o) ?: o.toURL()
                img = ImageIO.read(url)
            } else if (o instanceof File)
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
                img = new ImageIcon(o).image
            else
                throw new IllegalArgumentException("Incorrect Image format: $o")
            super.setValue(img)
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect Image format: $o", e)}

    }
}

/**
 * @author Alexander Klein
 */
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
                description = o.description
                o = o.source ?: o.src
            } else if (o instanceof Collection) {
                o = o[0]
                description = o[1]
            }
            if (o instanceof String || o instanceof GString) {
                def parts = o.split(/\s*,\s*/)
                source = this.getClass().classLoader.getResource(parts[0]) ?: parts[0].toURL()
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
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect Icon format: $o", e)}
        throw new IllegalArgumentException("Incorrect Icon format: $o")
    }
}

/**
 * @author Alexander Klein
 */
class GradientPaintEditor extends PropertyEditorSupport {
    private GroovyShell shell = new GroovyShell(new TolerantBinding())

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
            if (o instanceof String || o instanceof GString) {
                // 10, 20, WHITE, 100, 200, #AA5500
                o = shell.evaluate(TolerantBinding.escapeColorLiterals("[$o]"))
                if (o.size() < 6)
                    throw new IllegalArgumentException("Incorrect GradientPaint format: $o")
            }
            if ((o instanceof Collection && o.size() > 5) || (o instanceof Object[] && o.length > 5)) {
                if (!(o[2] instanceof Color)) {
                    colorEditor.setValue(o[2])
                    o[2] = colorEditor.getValue()
                }
                if (!(o[5] instanceof Color)) {
                    colorEditor.setValue(o[5])
                    o[5] = colorEditor.getValue()
                }
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
                if (col2 instanceof Color)
                    c2 = col2
                else {
                    colorEditor.setValue(col2)
                    c2 = colorEditor.getValue()
                }
            } else
                throw new IllegalArgumentException("Incorrect GradientPaint format: $o")
            super.setValue(new GradientPaint(x1 as float, y1 as float, c1, x2 as float, y2 as float, c2))
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect GradientPaint format: $o", e)
        }
    }
}

/**
 * @author Alexander Klein
 */
class LinearGradientPaintEditor extends PropertyEditorSupport {
    private GroovyShell shell = new GroovyShell(new TolerantBinding())

    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        ColorEditor colorEditor = new ColorEditor()
        try {
            def x1, y1, x2, y2
            Map<Float, Color> colors = [:]
            CycleMethod cycleMethod = CycleMethod.REPEAT
            if (o instanceof String || o instanceof GString) {
                // "10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]"
                // "10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT"
                o = shell.evaluate(TolerantBinding.escapeColorLiterals("[$o]"))
            }
            if ((o instanceof Collection && o.size() > 4) || (o instanceof Object[] && o.length > 4)) {
                (x1, y1, x2, y2) = o
                o[4].each {k, v ->
                    if (!(v instanceof Color)) {
                        colorEditor.setValue(v)
                        v = colorEditor.getValue()
                    }
                    colors[k as float] = v
                }
                if (o[5] instanceof CycleMethod)
                    cycleMethod = o[5]
                else if (o[5] instanceof String)
                    cycleMethod = CycleMethod.@"${o[5]}"
            }
            else if (o instanceof Map) {
                x1 = (o['x1'] ?: 0)
                y1 = (o['y1'] ?: 0)
                x2 = (o['x2'] ?: 0)
                y2 = (o['y2'] ?: 0)
                colors = o['colors'] ?: o['cols']
                colors.each{k,v ->
                    if (!(v instanceof Color)) {
                        colorEditor.setValue(v)
                        colors[k] = colorEditor.getValue()
                    }
                }
                cycleMethod = o['cycleMethod'] ?: o['cycle'] ?: CycleMethod.REPEAT
            } else
                throw new IllegalArgumentException("Incorrect LinearGradientPaint format:  $o")
            super.setValue(new LinearGradientPaint(x1 as float, y1 as float, x2 as float, y2 as float, colors.keySet() as float[], colors.values() as Color[], cycleMethod))
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect LinearGradientPaint format:  $o", e)}
    }
}

/**
 * @author Alexander Klein
 */
class RadialGradientPaintEditor extends PropertyEditorSupport {
    private GroovyShell shell = new GroovyShell(new TolerantBinding())
    private ColorEditor colorEditor = new ColorEditor()

    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            def cx, cy, fx = null, fy = null, r
            Map<Float, Color> colors = [:]
            CycleMethod cycleMethod = CycleMethod.REPEAT
            if (o instanceof String || o instanceof GString) {
                // "100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]"
                // "100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT"
                // "100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]"
                // "100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT"
                o = shell.evaluate(TolerantBinding.escapeColorLiterals("[$o]"))
            }
            if ((o instanceof Collection && o.size() > 3) || (o instanceof Object[] && o.length > 3)) {
                if (o.size() > 5) {
                    (cx, cy, fx, fy, r) = o
                    colors = convertColors(o[5])
                    cycleMethod = convertCycleMethod(o[6])
                } else {
                    (cx, cy, r) = o
                    colors = convertColors(o[3])
                    cycleMethod = convertCycleMethod(o[4])
                }
            } else if (o instanceof Map) {
                cx = (o['cx'] ?: 0)
                cy = (o['cy'] ?: 0)
                fx = (o['fx'] ?: null)
                fy = (o['fy'] ?: null)
                r = o['radius'] ?: o['r'] ?: 0
                colors = o['colors'] ?: o['cols']
                colors.each{k,v ->
                    if (!(v instanceof Color)) {
                        colorEditor.setValue(v)
                        colors[k] = colorEditor.getValue()
                    }
                }
                cycleMethod = o['cycleMethod'] ?: o['cycle'] ?: CycleMethod.REPEAT
            } else
                throw new IllegalArgumentException("Incorrect RadialGradientPaint format:  $o")
            if (fx == null || fy == null)
                super.setValue(new RadialGradientPaint(cx as float, cy as float, r as float, colors.keySet() as float[], colors.values() as Color[], cycleMethod))
            else
                super.setValue(new RadialGradientPaint(cx as float, cy as float, r as float, fx as float, fy as float, colors.keySet() as float[], colors.values() as Color[], cycleMethod))
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect RadialGradientPaint format:  $o", e)}
    }

    private Map convertColors(Map map) {
        Map<Float, Color> colors = [:]
        map.each {k, v ->
            if (!(v instanceof Color)) {
                colorEditor.setValue(v)
                v = colorEditor.getValue()
            }
            colors[k as float] = v
        }
        colors
    }

    private CycleMethod convertCycleMethod(def value) {
        if (value instanceof CycleMethod)
            value
        else if (value instanceof String)
            CycleMethod.@"${value}"
        CycleMethod.REPEAT
    }
}

/**
 * @author Alexander Klein
 */
class TexturePaintEditor extends PropertyEditorSupport {
    private GroovyShell shell = new GroovyShell(new TolerantBinding())

    @Override
    void setAsText(String text) throws IllegalArgumentException {
        setValue(text)
    }

    @Override
    void setValue(Object o) throws IllegalArgumentException {
        try {
            def img
            def anchor
            if (o instanceof String || o instanceof GString) {
                def parts = o.split(/\s*,\s*/)
                if (parts.length == 5) {
                    img = parts[0]
                    anchor = parts[1..4]
                } else
                    throw new IllegalArgumentException("Incorrect TexturePaint format: $o")
            } else if ((o instanceof Collection && o.size() > 1) || (o instanceof Object[] && o.length > 1)) {
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
                editor.setValue(img)
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
        } catch (Exception e) {throw new IllegalArgumentException("Incorrect TexturePaint format: $o", e)}
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

/**
 * @author Alexander Klein
 */
class TolerantBinding extends Binding {
    public Object getVariable(String name) {
        try {
            return super.getVariable(name)
        } catch (Exception e) {
            return name
        }
    }

    public static String escapeColorLiterals(String src) {
        src.replaceAll(/(#[0123456789abcdefABCDEF]*)/, /'$1'/)
    }
}