package resourcemanager.resources

import java.awt.Point
import java.awt.Dimension
import java.awt.font.TextAttribute
import java.awt.Color
import java.awt.MultipleGradientPaint.CycleMethod
import java.awt.Rectangle

test {
    key1 = 'resourcemanager/resources/ResourceManagerTests.groovy'
    key2 = 'resourcemanager/resources/ResourceManagerTests.groovy'
    key3 = 'resourcemanager/resources/ResourceManagerTests.groovy'
    key4 = 'resourcemanager/resources/ResourceManagerTests.groovy'
    key5 = 'resourcemanager/resources/ResourceManagerTests.groovy'
    key6 = 'resourcemanager/resources/ResourceManagerTests.groovy'
    key7 = 'resourcemanager/resources/ResourceManagerTests.groovy'
}

objects {
    list = [1, 2, 3]
    map = [key: 'value']
}
objects."boolean" = true
objects."int" = 123

test.fill1 = '[ #0, #1 ]'
test.fill2 = '[ #{a}, #b, \\#a ]'
test.fill3 = "[ #0 ]"

test.dynamic = {a, b -> return "$a $b"}

key = 'abc'

blah = 'Value1'

url1 = Url('http://www.google.de')
url2 = Url(source: 'http://www.google.de')
url3 = Url(url: 'http://www.google.de')
url4 = Url(src: 'http://www.google.de')

uri1 = Uri('http://www.google.de')
uri2 = Uri(source: 'http://www.google.de')
uri3 = Uri(uri: 'http://www.google.de')
uri4 = Uri(src: 'http://www.google.de')

dimension1 = Dimension([100, 200])
dimension2 = Dimension('100, 200')
dimension3 = Dimension([width: 100, height: 200])
dimension4 = Dimension(width: 100, height: 200)

insets1 = Insets([10, 20, 30, 40])
insets2 = Insets('10, 20, 30, 40')
insets3 = Insets([t: 10, l: 20, b: 30, r: 40])
insets4 = Insets(t: 10, l: 20, b: 30, r: 40)
insets5 = Insets([top: 10, left: 20, bottom: 30, right: 40])
insets6 = Insets(top: 10, left: 20, bottom: 30, right: 40)

point1 = Point([10, 20])
point2 = Point('10, 20')
point3 = Point([x: 10, y: 20])
point4 = Point(x: 10, y: 20)

locale1 = Locale('de_DE_BW')
locale2 = Locale(locale: 'de_DE_BW')

rectangle1 = Rectangle([10, 20, 30, 40])
rectangle2 = Rectangle('10,20,30,40')
rectangle3 = Rectangle([x: 10, y: 20, w: 30, h: 40])
rectangle4 = Rectangle([x: 10, y: 20, width: 30, height: 40])
rectangle5 = Rectangle([point: new Point(10, 20), dimension: new Dimension(30, 40)])
rectangle6 = Rectangle([point: Point(10, 20), dimension: Dimension(30, 40)])

color1 = Color('#4488bbff')
color2 = Color('#4488bb')
color3 = Color('#48b')
color4 = Color('#48bf')
color5 = Color('WHITE')
color6 = Color('BLACK')
color7 = Color(0x44, 0x88, 0xBB, 0xFF)
color8 = Color(0x44, 0x88, 0xBB)
color9 = Color([r:0x44, g:0x88, b:0xBB, a:0xFF])
color10 = Color([red:0x44, green:0x88, blue:0xBB, alpha:0xFF])

font1 = Font("'Times New Roman', 12, bold, italic, underline, strikethrough, [kerning: on, ligatures: on]")
font2 = Font("Arial, 12")
font3 = Font(['Times New Roman', 12, 'bold', 'italic', 'underline', 'strikethrough', [kerning: TextAttribute.KERNING_ON, ligatures: TextAttribute.LIGATURES_ON]])
font4 = Font([FAMILY: 'Times New Roman', SIZE: 12, WEIGHT: TextAttribute.WEIGHT_BOLD, POSTURE: 'OBLIQUE'])

image1 = Image(this.getClass().getResource('testImage.png').toExternalForm())
image2 = Image(this.getClass().getResource('testImage.png'))
image3 = Image(this.getClass().getResource('testImage.png').toURI())
image4 = Image(this.getClass().getResource('testImage.png').newInputStream())
image5 = Image(this.getClass().getResource('testImage.png').getBytes())
image6 = Image(new File('test/unit/resourcemanager/resources/testImage.png'))
image7 = Image(source: this.getClass().getResource('testImage.png'))
image8 = Image(src: this.getClass().getResource('testImage.png').toExternalForm())

icon1 = Icon(this.getClass().getResource('testImage.png').toExternalForm())
icon2 = Icon(this.getClass().getResource('testImage.png'))
icon3 = Icon(this.getClass().getResource('testImage.png').toURI())
icon4 = Icon(this.getClass().getResource('testImage.png').newInputStream())
icon5 = Icon(this.getClass().getResource('testImage.png').getBytes())
icon6 = Icon(new File('test/unit/resourcemanager/resources/testImage.png'))
icon7 = Icon(source: this.getClass().getResource('testImage.png'))
icon8 = Icon(src: this.getClass().getResource('testImage.png').toExternalForm())

gradient1 = GradientPaint('10, 20, WHITE, 100, 200, #AA5500')
gradient2 = GradientPaint([10, 20, Color.WHITE, 100, 200, '#AA5500'])
gradient3 = GradientPaint([x1:10, y1:20, startColor:Color.WHITE, x2:100, y2:200, endColor:'#AA5500'])
gradient4 = GradientPaint([x1:10, y1:20, color1:Color.WHITE, x2:100, y2:200, color2:'#AA5500'])
gradient5 = GradientPaint([x1:10, y1:20, c1:Color.WHITE, x2:100, y2:200, c2:'#AA5500'])

linear1 = LinearGradientPaint("10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
linear2 = LinearGradientPaint("10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
linear3 = LinearGradientPaint([10, 20, 100, 200, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
linear4 = LinearGradientPaint([10, 20, 100, 200, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
linear5 = LinearGradientPaint([x1:10, y1:20, x2:100, y2:200, colors: [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycleMethod: CycleMethod.REPEAT])
linear6 = LinearGradientPaint([x1:10, y1:20, x2:100, y2:200, cols: [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycle: CycleMethod.REPEAT])

radial1 = RadialGradientPaint("100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
radial2 = RadialGradientPaint("100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
radial3 = RadialGradientPaint([100, 200, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
radial4 = RadialGradientPaint([100, 200, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
radial5 = RadialGradientPaint([cx: 100, cy:200, r:50, cols:[0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycle:CycleMethod.REPEAT])
radial6 = RadialGradientPaint("100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
radial7 = RadialGradientPaint("100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
radial8 = RadialGradientPaint([100, 200, 10, 20, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
radial9 = RadialGradientPaint([100, 200, 10, 20, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
radial10 = RadialGradientPaint([cx: 100, cy:200, fx:10, fy:20, radius:50, colors:[0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycleMethod:CycleMethod.REPEAT])

texture1 = TexturePaint("${this.getClass().getResource('testImage.png').toExternalForm()}, 10, 20, 50, 100")
texture2 = TexturePaint([this.getClass().getResource('testImage.png'), [10, 20, 50, 100]])
texture3 = TexturePaint([this.getClass().getResource('testImage.png'), new Rectangle(10, 20, 50, 100)])
texture4 = TexturePaint([image: this.getClass().getResource('testImage.png'), anchor: new Rectangle(10, 20, 50, 100)])
texture5 = TexturePaint([source: this.getClass().getResource('testImage.png'), rectangle: new Rectangle(10, 20, 50, 100)])
texture6 = TexturePaint([src: this.getClass().getResource('testImage.png'), rect: new Rectangle(10, 20, 50, 100)])

def method() { "Test" }
callTest =  method()

injections {
    property1 = {
        rm.blah
    }
    property2 = '2'
    property3.sub1 = 'Value3'
    url1 = 'http://www.google.de'
}