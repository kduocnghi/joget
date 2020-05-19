{
    reloadElement : function(element){
        var thisObj = this;
        
        var elementObj = $(element)[0].dom;
        
        if (elementObj.elements === undefined) {
            elementObj.elements = [{className: "org.joget.plugin.report.lib.ColumnReportElement", properties: {}, elements:[]},{className: "org.joget.plugin.report.lib.ColumnReportElement", properties: {}, elements:[]}];
        }
        
        if ($(element).find("> .element-template .column").length === 0) {
            $(element).find("> .element-template").css("font-size", "0");
            
            for (var i in elementObj.elements) {
                thisObj.renderColumns(element, elementObj.elements[i]);
            }
            
            ReportBuilder.sortable($(element).find("> .element-template > .column > .container-elements"));
        }
        
        if (elementObj['properties']['type'] === "4") {
            thisObj.addColumn(element, elementObj, 2);
            thisObj.removeColumn(element, elementObj, 3);
            
            $(element).find("> .element-template > .column").css("width", "33.333333%");
        } else if (elementObj['properties']['type'] === "5") {
            thisObj.addColumn(element, elementObj, 2);
            thisObj.addColumn(element, elementObj, 3);
            
            $(element).find("> .element-template > .column").css("width", "25%");
        } else {
            thisObj.removeColumn(element, elementObj, 3);
            thisObj.removeColumn(element, elementObj, 2);
            
            if (elementObj['properties']['type'] === "2") {
                $(element).find("> .element-template > .column:eq(0)").css("width", "33.333333%");
                $(element).find("> .element-template > .column:eq(1)").css("width", "66.666667%");
            } else if (elementObj['properties']['type'] === "3") {
                $(element).find("> .element-template > .column:eq(0)").css("width", "66.666667%");
                $(element).find("> .element-template > .column:eq(1)").css("width", "33.333333%");
            } else {
                $(element).find("> .element-template > .column").css("width", "50%");
            }
        }
        
        ReportBuilder.equalHeight($(element).find("> .element-template > .column:eq(0)"));
    },
    renderColumns : function(element, columnObj) {
        var display = "inline-block";
        var html = '<div class="column report-container" style="font-size:initial; display:'+display+';"><div class="element-options"></div><div class="container-elements"></div></div>';
        var column = $(html); 
        $(column)[0].dom = columnObj;
        
        $(column).find('> .element-options').append('<button class="element-paste container-element" title="'+get_cbuilder_msg("cbuilder.paste")+'"><i class="fas fa-paste"></i><span>'+get_cbuilder_msg("cbuilder.paste")+'</span></button>');
        
        $(element).find("> .element-template").append(column);
        
        $(column).find('> .element-options .element-paste').on("click", function(){
            ReportBuilder.paste(column);
        });
        
        if (columnObj['elements'] !== undefined) {
            $.each(columnObj['elements'], function(i, el) {
                ReportBuilder.renderElement(column, el);
            });
        }
    },
    addColumn : function(element, elementObj, index) {
        if ($(element).find("> .element-template > .column:eq("+index+")").length === 0) {
            elementObj.elements.splice(index, 0, {className: "org.joget.plugin.report.lib.ColumnReportElement", properties: {}, elements:[]});
            this.renderColumns(element, elementObj.elements[index]);
            ReportBuilder.sortable($(element).find("> .element-template > .column:eq("+index+") > .container-elements"));
        }
    },
    removeColumn : function(element, elementObj, index) {
        if ($(element).find("> .element-template > .column:eq("+index+")").length > 0) {
            elementObj.elements.splice(index, 1);
            $(element).find("> .element-template > .column:eq("+index+")").remove();
        }
    }
}
