<div class="list_reportelement_${dataList.id!}">
    <table class="border-table" cellspacing="0" style="width:100%;border-collapse: collapse; -fs-table-paginate: paginate; border-spacing: 0;">
        <thead style="${element.headerStyle!}">
            <tr>
            <#list dataList.columns as column>
                <#if element.isVisible(column) >
                    <#assign style = "">
                    <#if column.properties.headerAlignment! == "dataListAlignRigth">
                        <#assign style = style + "text-align:right;">
                    <#elseif column.properties.headerAlignment! == "dataListAlignCenter">
                        <#assign style = style + "text-align:center;">
                    <#else>
                        <#assign style = style + "text-align:left;">
                    </#if>    
                    <th class="column_${column.name!}" style="${style!}${column.style!}">${column.label!}</th>
                </#if>
            </#list>
            </tr>
        </thead>
        <tbody>
            <#list rows as row>
                <tr>
                    <#list dataList.columns as column>
                        <#assign style = "">
                        <#if column.properties.alignment! == "dataListAlignRigth">
                            <#assign style = style + "text-align:right;">
                        <#elseif column.properties.alignment! == "dataListAlignCenter">
                            <#assign style = style + "text-align:center;">
                        <#else>
                            <#assign style = style + "text-align:left;">
                        </#if>  
                        <td class="column_${column.name!}" style="${style!}${column.style!}">${row[column.name]!}</td>
                    </#list>
                </tr>
                ${element.getRowExtra(row)}
            </#list>
        </tbody>
        <#if element.hasFooter() >
            <tfoot style="font-weight:bold;">
                <tr>
                    <#assign colspan = 0>
                    <#list dataList.columns as column>
                        <#if colspan != -1 && column.properties.footer! == "">
                            <#assign colspan = colspan + 1>
                        <#else>
                            <#if colspan gt 0>
                                <td class="footerRowlabel" style="text-align:right;" colspan="${colspan}">${element.properties.footerRowlabel!}</td>
                                <#assign colspan = -1>
                            </#if>
                            <#assign style = "">
                            <#if column.properties.alignment! == "dataListAlignRigth">
                                <#assign style = style + "text-align:right;">
                            <#elseif column.properties.alignment! == "dataListAlignCenter">
                                <#assign style = style + "text-align:center;">
                            <#else>
                                <#assign style = style + "text-align:left;">
                            </#if>
                            <td class="column_${column.name!}" style="${style!}${column.style!}">${element.getFooter(column)}</td>
                        </#if>
                    </#list>
                </tr>
            </tfoot>    
        </#if>  
    </table>
</div>