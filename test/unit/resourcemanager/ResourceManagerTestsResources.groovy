package resourcemanager

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

url1 = url('http://www.google.de')
url2 = url(source: 'http://www.google.de')
url3 = url(url: 'http://www.google.de')
url4 = url(src: 'http://www.google.de')

uri1 = uri('http://www.google.de')
uri2 = uri(source: 'http://www.google.de')
uri3 = uri(uri: 'http://www.google.de')
uri4 = uri(src: 'http://www.google.de')

dimension1 = dimension([100, 200])
dimension2 = dimension('100, 200')
dimension3 = dimension([width: 100, height: 200])
dimension4 = dimension(width: 100, height: 200)

insets1 = insets([10, 20, 30, 40])
insets2 = insets('10, 20, 30, 40')
insets3 = insets([t: 10, l: 20, b: 30, r: 40])
insets4 = insets(t: 10, l: 20, b: 30, r: 40)
insets5 = insets([top: 10, left: 20, bottom: 30, right: 40])
insets6 = insets(top: 10, left: 20, bottom: 30, right: 40)

point1 = point([10, 20])
point2 = point('10, 20')
point3 = point([x: 10, y: 20])
point4 = point(x: 10, y: 20)

locale1 = locale('de_DE_BW')
locale2 = locale(locale: 'de_DE_BW')

rectangle1 = rectangle([10, 20, 30, 40])
rectangle2 = rectangle('10,20,30,40')
rectangle3 = rectangle([x: 10, y: 20, w: 30, h: 40])
rectangle4 = rectangle([x: 10, y: 20, width: 30, height: 40])
rectangle5 = rectangle([point: new Point(10, 20), dimension: new Dimension(30, 40)])
rectangle6 = rectangle([point: point(10, 20), dimension: dimension(30, 40)])

color1 = color('#4488bbff')
color2 = color('#4488bb')
color3 = color('#48b')
color4 = color('#48bf')
color5 = color('WHITE')
color6 = color('BLACK')
color7 = color(0x44, 0x88, 0xBB, 0xFF)
color8 = color(0x44, 0x88, 0xBB)
color9 = color([r:0x44, g:0x88, b:0xBB, a:0xFF])
color10 = color([red:0x44, green:0x88, blue:0xBB, alpha:0xFF])

font1 = font("'Times New Roman', 12, bold, italic, underline, strikethrough, [kerning: on, ligatures: on]")
font2 = font("Arial, 12")
font3 = font(['Times New Roman', 12, 'bold', 'italic', 'underline', 'strikethrough', [kerning: TextAttribute.KERNING_ON, ligatures: TextAttribute.LIGATURES_ON]])
font4 = font([FAMILY: 'Times New Roman', SIZE: 12, WEIGHT: TextAttribute.WEIGHT_BOLD, POSTURE: 'OBLIQUE'])

image1 = image(this.getClass().getResource('testImage.png').toExternalForm())
image2 = image(this.getClass().getResource('testImage.png'))
image3 = image(this.getClass().getResource('testImage.png').toURI())
image4 = image(this.getClass().getResource('testImage.png').newInputStream())
image5 = image(this.getClass().getResource('testImage.png').getBytes())
image6 = image(new File('test/unit/resourcemanager/testImage.png'))
image7 = image(source: this.getClass().getResource('testImage.png'))
image8 = image(src: this.getClass().getResource('testImage.png').toExternalForm())

icon1 = icon(this.getClass().getResource('testImage.png').toExternalForm())
icon2 = icon(this.getClass().getResource('testImage.png'))
icon3 = icon(this.getClass().getResource('testImage.png').toURI())
icon4 = icon(this.getClass().getResource('testImage.png').newInputStream())
icon5 = icon(this.getClass().getResource('testImage.png').getBytes())
icon6 = icon(new File('test/unit/resourcemanager/testImage.png'))
icon7 = icon(source: this.getClass().getResource('testImage.png'))
icon8 = icon(src: this.getClass().getResource('testImage.png').toExternalForm())

gradient1 = gradientPaint('10, 20, WHITE, 100, 200, #AA5500')
gradient2 = gradientPaint([10, 20, Color.WHITE, 100, 200, '#AA5500'])
gradient3 = gradientPaint([x1:10, y1:20, startColor:Color.WHITE, x2:100, y2:200, endColor:'#AA5500'])
gradient4 = gradientPaint([x1:10, y1:20, color1:Color.WHITE, x2:100, y2:200, color2:'#AA5500'])
gradient5 = gradientPaint([x1:10, y1:20, c1:Color.WHITE, x2:100, y2:200, c2:'#AA5500'])

linear1 = linearGradientPaint("10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
linear2 = linearGradientPaint("10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
linear3 = linearGradientPaint([10, 20, 100, 200, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
linear4 = linearGradientPaint([10, 20, 100, 200, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
linear5 = linearGradientPaint([x1:10, y1:20, x2:100, y2:200, colors: [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycleMethod: CycleMethod.REPEAT])
linear6 = linearGradientPaint([x1:10, y1:20, x2:100, y2:200, cols: [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycle: CycleMethod.REPEAT])

radial1 = radialGradientPaint("100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
radial2 = radialGradientPaint("100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
radial3 = radialGradientPaint([100, 200, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
radial4 = radialGradientPaint([100, 200, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
radial5 = radialGradientPaint([cx: 100, cy:200, r:50, cols:[0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycle:CycleMethod.REPEAT])
radial6 = radialGradientPaint("100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")
radial7 = radialGradientPaint("100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")
radial8 = radialGradientPaint([100, 200, 10, 20, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])
radial9 = radialGradientPaint([100, 200, 10, 20, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])
radial10 = radialGradientPaint([cx: 100, cy:200, fx:10, fy:20, radius:50, colors:[0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycleMethod:CycleMethod.REPEAT])

texture1 = texturePaint("${this.getClass().getResource('testImage.png').toExternalForm()}, 10, 20, 50, 100")
texture2 = texturePaint([this.getClass().getResource('testImage.png'), [10, 20, 50, 100]])
texture3 = texturePaint([this.getClass().getResource('testImage.png'), new Rectangle(10, 20, 50, 100)])
texture4 = texturePaint([image: this.getClass().getResource('testImage.png'), anchor: new Rectangle(10, 20, 50, 100)])
texture5 = texturePaint([source: this.getClass().getResource('testImage.png'), rectangle: new Rectangle(10, 20, 50, 100)])
texture6 = texturePaint([src: this.getClass().getResource('testImage.png'), rect: new Rectangle(10, 20, 50, 100)])

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