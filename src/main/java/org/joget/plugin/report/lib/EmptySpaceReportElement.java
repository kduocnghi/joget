/*    */ package org.joget.plugin.report.lib;
/*    */ 
/*    */ import org.joget.apps.app.service.AppPluginUtil;
/*    */ import org.joget.apps.app.service.AppUtil;
/*    */ import org.joget.plugin.base.ExtDefaultPlugin;
/*    */ import org.joget.plugin.property.model.PropertyEditable;
/*    */ import org.joget.plugin.report.api.ReportElement;
/*    */ 
/*    */ public class EmptySpaceReportElement
/*    */   extends ExtDefaultPlugin
/*    */   implements PropertyEditable, ReportElement
/*    */ {
/*    */   public String getName() {
/* 14 */     return "EmptySpaceReportElement";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 19 */     return "1.0.0";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 24 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLabel() {
/* 29 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 34 */     return getClass().getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPropertyOptions() {
/* 39 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/EmptySpaceReportElement.json", null, true, "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getIcon() {
/* 44 */     return "<i class=\"fas fa-arrows-alt-v\"></i>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String render() {
/* 49 */     return "<div style=\"height:" + getPropertyString("height") + ";\"></div>";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportReportContainer() {
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String preview() {
/* 59 */     return render();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCSS() {
/* 64 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\EmptySpaceReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */