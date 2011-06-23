import javax.swing.Icon
import griffon.resourcemanager.*
import java.awt.*

onLoadAddonEnd = { name, addon, app ->
    if(name != 'ResourcesGriffonAddon')
        return

    // TODO: Remove
    println "--------------------ResourcesGriffonAddon---------------------"
/*
DefaultEditors:

all primitives
byte[]
char[]
java.nio.Charset
java.lang.Class
java.lang.Class[]
java.util.Currency
java.io.File
java.io.InputStream
org.xml.sax.InputSource
java.util.Locale
java.util.regexp.Pattern
java.util.Properties
java.lang.String[]
java.util.TimeZone
java.net.URI
java.net.URL
java.util.UUID
java.lang.Boolean
java.lang.Byte
java.lang.Character
java.lang.Short
java.lang.Integer
java.lang.Long
java.math.BigInteger
java.lang.Float
java.lang.Double
java.math.BigDecimal
java.util.Collection
java.util.Set
java.util.SortedSet
java.util.List
java.util.SortedMap
org.codehaus.groovy.runtime.GStringImpl
java.awt.Color
java.awt.Dimension
java.awt.Insets
java.awt.Point
java.awt.Rectangle
java.awt.Font
java.awt.Image
java.awt.Icon
java.awt.GradientPaint
java.awt.LinearGradientPaint
java.awt.RadialGradientPaint
java.awt.TexturePaint
*/
    ResourceManager.with{
        registerEditor org.codehaus.groovy.runtime.GStringImpl, new ToStringEditor()
        registerEditor Color, new ColorEditor()
        registerEditor Dimension, new DimensionEditor()
        registerEditor Insets, new InsetsEditor()
        registerEditor Point, new PointEditor()
        registerEditor Rectangle, new RectangleEditor()
        registerEditor Font, new FontEditor()
        registerEditor Image, new ImageEditor()
        registerEditor Icon, new IconEditor()
        registerEditor GradientPaint, new GradientPaintEditor()
        registerEditor LinearGradientPaint, new LinearGradientPaintEditor()
        registerEditor RadialGradientPaint, new RadialGradientPaintEditor()
        registerEditor TexturePaint, new TexturePaintEditor()
    }
}