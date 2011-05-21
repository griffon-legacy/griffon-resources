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

url1 = builder.url('http://www.google.de')
url2 = builder.url(source: 'http://www.google.de')
url3 = builder.url(url: 'http://www.google.de')
url4 = builder.url(src: 'http://www.google.de')

uri1 = builder.uri('http://www.google.de')
uri2 = builder.uri(source: 'http://www.google.de')
uri3 = builder.uri(uri: 'http://www.google.de')
uri4 = builder.uri(src: 'http://www.google.de')

dimension1 = builder.dimension([100, 200])
dimension2 = builder.dimension('100, 200')
dimension3 = builder.dimension([width: 100, height: 200])
dimension4 = builder.dimension(width: 100, height: 200)

insets1 = builder.insets([10, 20, 30, 40])
insets2 = builder.insets('10, 20, 30, 40')
insets3 = builder.insets([t: 10, l: 20, b: 30, r: 40])
insets4 = builder.insets(t: 10, l: 20, b: 30, r: 40)
insets5 = builder.insets([top: 10, left: 20, bottom: 30, right: 40])
insets6 = builder.insets(top: 10, left: 20, bottom: 30, right: 40)

point1 = builder.point([10, 20])
point2 = builder.point('10, 20')
point3 = builder.point([x: 10, y: 20])
point4 = builder.point(x: 10, y: 20)

locale1 = builder.locale('de_DE_BW')
locale2 = builder.locale(locale: 'de_DE_BW')

rectangle1 = builder.rectangle([10, 20, 30, 40])
rectangle2 = builder.rectangle('10,20,30,40')
rectangle3 = builder.rectangle([x: 10, y: 20, w: 30, h: 40])
rectangle4 = builder.rectangle([x: 10, y: 20, width: 30, height: 40])
rectangle5 = builder.rectangle([point: new Point(10, 20), dimension: new Dimension(30, 40)])
rectangle6 = builder.rectangle([point: r.point(10, 20), dimension: r.dimension(30, 40)])

color1 = r.color('#4488bbff')
color2 = r.color('#4488bb')
color3 = r.color('#48b')
color4 = r.color('#48bf')
color5 = r.color('WHITE')
color6 = r.color('BLACK')
color7 = r.color(0x44, 0x88, 0xBB, 0xFF)
color8 = r.color(0x44, 0x88, 0xBB)
color9 = r.color([r:0x44, g:0x88, b:0xBB, a:0xFF])
color10 = r.color([red:0x44, green:0x88, blue:0xBB, alpha:0xFF])

font1 = r.font("'Times New Roman', 12, bold, italic, underline, strikethrough, [kerning: on, ligatures: on]")
font2 = r.font("Arial, 12")
font3 = r.font(['Times New Roman', 12, 'bold', 'italic', 'underline', 'strikethrough', [kerning: TextAttribute.KERNING_ON, ligatures: TextAttribute.LIGATURES_ON]])
font4 = r.font([FAMILY: 'Times New Roman', SIZE: 12, WEIGHT: TextAttribute.WEIGHT_BOLD, POSTURE: 'OBLIQUE'])

image1 = r.image(this.getClass().getResource('testImage.png').toExternalForm())
image2 = r.image(this.getClass().getResource('testImage.png'))
image3 = r.image(this.getClass().getResource('testImage.png').toURI())
image4 = r.image(this.getClass().getResource('testImage.png').newInputStream())
image5 = r.image(this.getClass().getResource('testImage.png').getBytes())
image6 = r.image(new File('test/unit/resourcemanager/resources/testImage.png'))
image7 = r.image(source: this.getClass().getResource('testImage.png'))
image8 = r.image(src: this.getClass().getResource('testImage.png').toExternalForm())

icon1 = r.icon(this.getClass().getResource('testImage.png').toExternalForm())
icon2 = r.icon(this.getClass().getResource('testImage.png'))
icon3 = r.icon(this.getClass().getResource('testImage.png').toURI())
icon4 = r.icon(this.getClass().getResource('testImage.png').newInputStream())
icon5 = r.icon(this.getClass().getResource('testImage.png').getBytes())
icon6 = r.icon(new File('test/unit/resourcemanager/resources/testImage.png'))
icon7 = r.icon(source: this.getClass().getResource('testImage.png'))
icon8 = r.icon(src: this.getClass().getResource('testImage.png').toExternalForm())

gradient1 = r.gradientPaint('10, 20, WHITE, 100, 200, #AA5500')
gradient2 = r.gradientPaint([10, 20, Color.WHITE, 100, 200, '#AA5500'])
gradient3 = r.gradientPaint([x1:10, y1:20, startColor:Color.WHITE, x2:100, y2:200, endColor:'#AA5500'])
gradient4 = r.gradientPaint([x1:10, y1:20, color1:Color.WHITE, x2:100, y2:200, color2:'#AA5500'])
gradient5 = r.gradientPaint([x1:10, y1:20, c1:Color.WHITE, x2:100, y2:200, c2:'#AA5500'])

linear1 = r.linearGradientPaint("10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
linear2 = r.linearGradientPaint("10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
linear3 = r.linearGradientPaint([10, 20, 100, 200, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
linear4 = r.linearGradientPaint([10, 20, 100, 200, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
linear5 = r.linearGradientPaint([x1:10, y1:20, x2:100, y2:200, colors: [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycleMethod: CycleMethod.REPEAT])
linear6 = r.linearGradientPaint([x1:10, y1:20, x2:100, y2:200, cols: [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycle: CycleMethod.REPEAT])

radial1 = r.radialGradientPaint("100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
radial2 = r.radialGradientPaint("100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
radial3 = r.radialGradientPaint([100, 200, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
radial4 = r.radialGradientPaint([100, 200, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
radial5 = r.radialGradientPaint([cx: 100, cy:200, r:50, cols:[0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycle:CycleMethod.REPEAT])
radial6 = r.radialGradientPaint("100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
radial7 = r.radialGradientPaint("100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
radial8 = r.radialGradientPaint([100, 200, 10, 20, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
radial9 = r.radialGradientPaint([100, 200, 10, 20, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
radial10 = r.radialGradientPaint([cx: 100, cy:200, fx:10, fy:20, radius:50, colors:[0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycleMethod:CycleMethod.REPEAT])

texture1 = r.texturePaint("${this.getClass().getResource('testImage.png').toExternalForm()}, 10, 20, 50, 100")
texture2 = r.texturePaint([this.getClass().getResource('testImage.png'), [10, 20, 50, 100]])
texture3 = r.texturePaint([this.getClass().getResource('testImage.png'), new Rectangle(10, 20, 50, 100)])
texture4 = r.texturePaint([image: this.getClass().getResource('testImage.png'), anchor: new Rectangle(10, 20, 50, 100)])
texture5 = r.texturePaint([source: this.getClass().getResource('testImage.png'), rectangle: new Rectangle(10, 20, 50, 100)])
texture6 = r.texturePaint([src: this.getClass().getResource('testImage.png'), rect: new Rectangle(10, 20, 50, 100)])

injections {
    property1 = {
        rm.blah
    }
    property2 = '2'
    property3.sub1 = 'Value3'
    url1 = 'http://www.google.de'
}