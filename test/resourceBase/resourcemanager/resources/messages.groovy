test {
    key1 = 'resourcemanager/resources/messages.groovy'
    key2 = 'resourcemanager/resources/messages.groovy'
    key3 = 'resourcemanager/resources/messages.groovy'
    key4 = 'resourcemanager/resources/messages.groovy'
    key5 = 'resourcemanager/resources/messages.groovy'
    key6 = 'resourcemanager/resources/messages.groovy'
    key7 = 'resourcemanager/resources/messages.groovy'
}

objects {
    list = [1,2,3]
    map = [key: 'value']
}
objects."boolean" = true
objects."int" = 123

test.fill1 = '[ $_0, $_1 ]'
test.fill2 = '[ ${a}, ${b} ]'
test.fill3 = '[ $_0 ]'

key = 'abc'