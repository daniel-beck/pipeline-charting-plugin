namespace(lib.FormTagLib).with {
    entry(title: _("Chart Name"), field: "chartName") {
        textbox()
    }
    entry(title: _("Series Name"), field: "series") {
        textbox()
    }
    entry(title: _("X Axis Value"), field: "x") {
        number()
    }
    entry(title: _("Y Axis Value"), field: "y") {
        number()
    }
}