f = namespace(lib.FormTagLib)
l = namespace(lib.LayoutTagLib)

l.layout {
    l."main-panel" {
        my.chartNames.each { name ->
            img(src: name + "/png")
        }
    }
}