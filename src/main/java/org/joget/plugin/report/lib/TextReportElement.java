/*    */ package org.joget.plugin.report.lib;
/*    */ 
/*    */ import org.joget.apps.app.service.AppPluginUtil;
/*    */ import org.joget.apps.app.service.AppUtil;
/*    */ import org.joget.commons.util.StringUtil;
/*    */ import org.joget.plugin.base.ExtDefaultPlugin;
/*    */ import org.joget.plugin.property.model.PropertyEditable;
/*    */ import org.joget.plugin.report.api.ReportElement;
/*    */ 
/*    */ public class TextReportElement
/*    */   extends ExtDefaultPlugin
/*    */   implements PropertyEditable, ReportElement
/*    */ {
/*    */   public String getName() {
/* 15 */     return "TextReportElement";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 20 */     return "1.0.0";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 25 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLabel() {
/* 30 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 35 */     return getClass().getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPropertyOptions() {
/* 40 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/TextReportElement.json", null, true, "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getIcon() {
/* 45 */     return "<i class=\"fas fa-font\"></i>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String render() {
/* 50 */     String html = StringUtil.escapeString(getPropertyString("text"), "nl2br", null);
/* 51 */     String css = "";
/* 52 */     if ("true".equalsIgnoreCase(getPropertyString("bold"))) {
/* 53 */       css = css + "font-weight:bold;";
/*    */     }
/* 55 */     if ("true".equalsIgnoreCase(getPropertyString("italic"))) {
/* 56 */       css = css + "font-style: italic;";
/*    */     }
/* 58 */     if (!getPropertyString("decoration").isEmpty()) {
/* 59 */       css = css + "text-decoration:" + getPropertyString("decoration").replaceAll(";", " ") + ";";
/*    */     }
/* 61 */     if (!getPropertyString("size").isEmpty()) {
/* 62 */       css = css + "font-size:" + getPropertyString("size") + ";";
/*    */     }
/* 64 */     if (!getPropertyString("align").isEmpty()) {
/* 65 */       css = css + "text-align:" + getPropertyString("align") + ";";
/*    */     }
/* 67 */     if (!getPropertyString("color").isEmpty()) {
/* 68 */       css = css + "color:" + getPropertyString("color") + ";";
/*    */     }
/* 70 */     if (!getPropertyString("height").isEmpty()) {
/* 71 */       css = css + "height:" + getPropertyString("height") + ";";
/*    */     }
/* 73 */     if (!getPropertyString("marginBottom").isEmpty()) {
/* 74 */       css = css + "margin-bottom:" + getPropertyString("marginBottom") + ";";
/*    */     }
/*    */     
/* 77 */     return "<div style=\"" + css + "\">" + html + "</div>";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportReportContainer() {
/* 82 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String preview() {
/* 87 */     return render();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCSS() {
/* 92 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\TextReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */