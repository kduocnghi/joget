/*    */ package org.joget.plugin.report.lib;
/*    */ 
/*    */ import org.joget.apps.app.service.AppPluginUtil;
/*    */ import org.joget.plugin.base.ExtDefaultPlugin;
/*    */ import org.joget.plugin.property.model.PropertyEditable;
/*    */ import org.joget.plugin.report.api.ReportElement;
/*    */ 
/*    */ public class PageBreakReportElement
/*    */   extends ExtDefaultPlugin
/*    */   implements PropertyEditable, ReportElement
/*    */ {
/*    */   public String getName() {
/* 13 */     return "PageBreakReportElement";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 18 */     return "1.0.0";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 23 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLabel() {
/* 28 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 33 */     return getClass().getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPropertyOptions() {
/* 38 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getIcon() {
/* 43 */     return "<i class=\"far fa-minus-square\"></i>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String render() {
/* 48 */     return "<div style=\"page-break-after: always;\"></div>";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportReportContainer() {
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String preview() {
/* 58 */     return "<hr style=\"border-top:2px dotted gray; margin:10px 0;\"/>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCSS() {
/* 63 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\PageBreakReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */