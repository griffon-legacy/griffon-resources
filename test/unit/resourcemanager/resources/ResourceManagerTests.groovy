package resourcemanager.resources

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
    list = [1,2,3]
    map = [key: 'value']
}
objects."boolean" = true
objects."int" = 123

test.fill1 = '[ #0, #1 ]'
test.fill2 = '[ #{a}, #b, \\#a ]'
test.fill3 = "[ #0 ]"

test.dynamic = {a,b -> return "$a $b"}

key = 'abc'

blah = 'Value1'

dimension1 = builder.dimension([100,200])
dimension2 = builder.dimension('100, 200')
dimension3 = builder.dimension([width:100, height: 200])
dimension4 = builder.dimension(width:100, height: 200)

injections {
    property1 = {
        rm.blah
    }
    property2 = '2'
    property3.sub1 = 'Value3'
    url1 = 'http://www.google.de'
}