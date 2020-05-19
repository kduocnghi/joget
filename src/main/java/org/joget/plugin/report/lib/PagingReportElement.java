/*    */ package org.joget.plugin.report.lib;
/*    */ 
/*    */ import org.joget.apps.app.service.AppPluginUtil;
/*    */ import org.joget.apps.app.service.AppUtil;
/*    */ import org.joget.commons.util.StringUtil;
/*    */ import org.joget.plugin.base.ExtDefaultPlugin;
/*    */ import org.joget.plugin.property.model.PropertyEditable;
/*    */ import org.joget.plugin.report.api.ReportElement;
/*    */ 
/*    */ public class PagingReportElement
/*    */   extends ExtDefaultPlugin
/*    */   implements PropertyEditable, ReportElement
/*    */ {
/*    */   public String getName() {
/* 15 */     return "PagingReportElement";
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
/* 40 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/PagingReportElement.json", null, true, "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getIcon() {
/* 45 */     return "<i><span>1/2</span></i>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String render() {
/* 50 */     String syntax = getPropertyString("syntax");
/* 51 */     if ("other".equalsIgnoreCase(syntax)) {
/* 52 */       syntax = getPropertyString("format");
/*    */     } else {
/* 54 */       syntax = AppPluginUtil.getMessage(syntax, getClassName(), "messages/ReportElements");
/*    */     } 
/*    */     
/* 57 */     String html = replaceVariable(syntax);
/*    */     
/* 59 */     String css = "";
/* 60 */     if (!getPropertyString("size").isEmpty()) {
/* 61 */       css = css + "font-size:" + getPropertyString("size") + ";";
/*    */     }
/* 63 */     if (!getPropertyString("align").isEmpty()) {
/* 64 */       css = css + "text-align:" + getPropertyString("align") + ";";
/*    */     }
/* 66 */     if (!getPropertyString("color").isEmpty()) {
/* 67 */       css = css + "color:" + getPropertyString("color") + ";";
/*    */     }
/*    */     
/* 70 */     return "<div style=\"" + css + "\">" + html + "</div>";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportReportContainer() {
/* 75 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String preview() {
/* 80 */     setProperty("elementPreview", "true");
/* 81 */     return render();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCSS() {
/* 86 */     return "";
/*    */   }
/*    */   
/*    */   protected String replaceVariable(String syntax) {
/* 90 */     if ("true".equals(getPropertyString("elementPreview"))) {
/* 91 */       syntax = syntax.replaceAll(StringUtil.escapeRegex("{CURRENT_PAGE}"), "<span class=\"currentpage\">1</span>");
/* 92 */       syntax = syntax.replaceAll(StringUtil.escapeRegex("{PAGE_NUMBER}"), "<span class=\"pagenumber\">1</span>");
/*    */     } else {
/* 94 */       syntax = syntax.replaceAll(StringUtil.escapeRegex("{CURRENT_PAGE}"), "<span class=\"currentpage\"></span>");
/* 95 */       syntax = syntax.replaceAll(StringUtil.escapeRegex("{PAGE_NUMBER}"), "<span class=\"pagenumber\"></span>");
/*    */     } 
/* 97 */     return syntax;
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\PagingReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */