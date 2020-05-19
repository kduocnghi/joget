/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.apps.datalist.model.DataListBinder;
/*     */ import org.joget.apps.datalist.model.DataListCollection;
/*     */ import org.joget.apps.datalist.service.DataListService;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.base.PluginManager;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ 
/*     */ public class LabelValueTableReportElement
/*     */   extends ExtDefaultPlugin
/*     */   implements PropertyEditable, ReportElement {
/*  18 */   private DataListCollection cachedData = null;
/*     */ 
/*     */   
/*     */   public String getName() {
/*  22 */     return "LabelValueTableReportElement";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  27 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  32 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  37 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  42 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  47 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/LabelValueTableReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  52 */     return "<i class=\"fas fa-table\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  57 */     String html = "";
/*     */     
/*  59 */     String listStyle = "width:100%;border-collapse: collapse;border-spacing: 0;";
/*  60 */     if (!getPropertyString("marginBottom").isEmpty()) {
/*  61 */       listStyle = listStyle + "margin-bottom:" + getPropertyString("marginBottom") + ";";
/*     */     }
/*  63 */     String borderStyle = "";
/*  64 */     if (!getPropertyString("borderSize").isEmpty()) {
/*  65 */       borderStyle = borderStyle + "border:" + getPropertyString("borderSize") + " " + getPropertyString("borderStyle") + " " + getPropertyString("borderColor") + ";";
/*     */     }
/*  67 */     String labelStyle = "";
/*  68 */     if (!getPropertyString("label_width").isEmpty()) {
/*  69 */       labelStyle = labelStyle + "width:" + getPropertyString("label_width") + ";";
/*     */     }
/*  71 */     if ("true".equalsIgnoreCase(getPropertyString("label_bold"))) {
/*  72 */       labelStyle = labelStyle + "font-weight:bold;";
/*     */     }
/*  74 */     if ("true".equalsIgnoreCase(getPropertyString("label_italic"))) {
/*  75 */       labelStyle = labelStyle + "font-style: italic;";
/*     */     }
/*  77 */     if (!getPropertyString("label_decoration").isEmpty()) {
/*  78 */       labelStyle = labelStyle + "text-decoration:" + getPropertyString("label_decoration").replaceAll(";", " ") + ";";
/*     */     }
/*  80 */     if (!getPropertyString("label_size").isEmpty()) {
/*  81 */       labelStyle = labelStyle + "font-size:" + getPropertyString("label_size") + ";";
/*     */     }
/*  83 */     if (!getPropertyString("label_align").isEmpty()) {
/*  84 */       labelStyle = labelStyle + "text-align:" + getPropertyString("label_align") + ";";
/*     */     }
/*  86 */     if (!getPropertyString("label_color").isEmpty()) {
/*  87 */       labelStyle = labelStyle + "color:" + getPropertyString("label_color") + ";";
/*     */     }
/*  89 */     if (!getPropertyString("label_background").isEmpty()) {
/*  90 */       labelStyle = labelStyle + "background:" + getPropertyString("label_background") + ";";
/*     */     }
/*  92 */     String valueStyle = "";
/*  93 */     if (!getPropertyString("value_width").isEmpty()) {
/*  94 */       valueStyle = valueStyle + "width:" + getPropertyString("value_width") + ";";
/*     */     }
/*  96 */     if ("true".equalsIgnoreCase(getPropertyString("value_bold"))) {
/*  97 */       valueStyle = valueStyle + "font-weight:bold;";
/*     */     }
/*  99 */     if ("true".equalsIgnoreCase(getPropertyString("value_italic"))) {
/* 100 */       valueStyle = valueStyle + "font-style: italic;";
/*     */     }
/* 102 */     if (!getPropertyString("value_decoration").isEmpty()) {
/* 103 */       valueStyle = valueStyle + "text-decoration:" + getPropertyString("value_decoration").replaceAll(";", " ") + ";";
/*     */     }
/* 105 */     if (!getPropertyString("value_size").isEmpty()) {
/* 106 */       valueStyle = valueStyle + "font-size:" + getPropertyString("value_size") + ";";
/*     */     }
/* 108 */     if (!getPropertyString("value_align").isEmpty()) {
/* 109 */       valueStyle = valueStyle + "text-align:" + getPropertyString("value_align") + ";";
/*     */     }
/* 111 */     if (!getPropertyString("value_color").isEmpty()) {
/* 112 */       valueStyle = valueStyle + "color:" + getPropertyString("value_color") + ";";
/*     */     }
/* 114 */     if (!getPropertyString("value_background").isEmpty()) {
/* 115 */       valueStyle = valueStyle + "background:" + getPropertyString("value_background") + ";";
/*     */     }
/* 117 */     if (!getPropertyString("padding").isEmpty()) {
/* 118 */       labelStyle = labelStyle + "padding:" + getPropertyString("padding") + ";";
/* 119 */       valueStyle = valueStyle + "padding:" + getPropertyString("padding") + ";";
/*     */     } 
/*     */     
/* 122 */     if ("binder".equals(getPropertyString("type"))) {
/* 123 */       DataListCollection rows = getData();
/* 124 */       if (rows != null && !rows.isEmpty()) {
/* 125 */         String label = "";
/* 126 */         String value = "";
/* 127 */         html = html + "<table cellspacing=\"0\" style=\"" + listStyle + borderStyle + "\"><tbody>";
/* 128 */         for (Object o : rows) {
/* 129 */           label = "";
/* 130 */           value = "";
/* 131 */           if (!getPropertyString("mapping_label").isEmpty()) {
/* 132 */             label = DataListService.evaluateColumnValueFromRow(o, getPropertyString("mapping_label")).toString();
/*     */           }
/* 134 */           if (!getPropertyString("mapping_value").isEmpty()) {
/* 135 */             value = DataListService.evaluateColumnValueFromRow(o, getPropertyString("mapping_value")).toString();
/*     */           }
/*     */           
/* 138 */           html = html + "<tr style=\"" + borderStyle + "\">";
/* 139 */           html = html + "<td class=\"list_label\" style=\"" + labelStyle + borderStyle + "\">" + label + "</td>";
/* 140 */           html = html + "<td class=\"list_value\" style=\"" + valueStyle + borderStyle + "\">" + value + "</td>";
/* 141 */           html = html + "</tr>";
/*     */         } 
/* 143 */         html = html + "</tbody></table>";
/*     */       } 
/*     */     } else {
/* 146 */       Object[] rows = (Object[])getProperty("label_value");
/* 147 */       if (rows != null && rows.length > 0) {
/* 148 */         html = html + "<table cellspacing=\"0\" class=\"label_value_list\" style=\"" + listStyle + borderStyle + "\"><tbody>";
/* 149 */         Map row = null;
/* 150 */         for (Object o : rows) {
/* 151 */           row = (HashMap)o;
/* 152 */           html = html + "<tr style=\"" + borderStyle + "\">";
/* 153 */           html = html + "<td class=\"list_label\" style=\"" + labelStyle + borderStyle + "\">" + row.get("label").toString() + "</td>";
/* 154 */           html = html + "<td class=\"list_value\" style=\"" + valueStyle + borderStyle + "\">" + row.get("value").toString() + "</td>";
/* 155 */           html = html + "</tr>";
/*     */         } 
/* 157 */         html = html + "</tbody></table>";
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     return html;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/* 166 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/* 171 */     return render();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCSS() {
/* 176 */     return "";
/*     */   }
/*     */   
/*     */   protected DataListCollection getData() {
/* 180 */     if (this.cachedData == null) {
/*     */       
/* 182 */       Object binderData = getProperty("binder");
/* 183 */       if (binderData != null && binderData instanceof Map) {
/* 184 */         Map bdMap = (Map)binderData;
/* 185 */         if (bdMap != null && bdMap.containsKey("className") && !bdMap.get("className").toString().isEmpty()) {
/* 186 */           PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
/* 187 */           DataListBinder binder = (DataListBinder)pluginManager.getPlugin(bdMap.get("className").toString());
/*     */           
/* 189 */           if (binder != null) {
/* 190 */             Map bdProps = (Map)bdMap.get("properties");
/* 191 */             binder.setProperties(bdProps);
/*     */             
/* 193 */             this.cachedData = binder.getData(null, bdProps, new org.joget.apps.datalist.model.DataListFilterQueryObject[0], null, null, null, null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 198 */     return this.cachedData;
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\LabelValueTableReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */