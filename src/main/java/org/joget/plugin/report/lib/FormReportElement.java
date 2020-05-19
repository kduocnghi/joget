/*    */ package org.joget.plugin.report.lib;
/*    */ 
/*    */ import org.joget.apps.app.model.AppDefinition;
/*    */ import org.joget.apps.app.service.AppPluginUtil;
/*    */ import org.joget.apps.app.service.AppUtil;
/*    */ import org.joget.apps.form.service.FormPdfUtil;
/*    */ import org.joget.plugin.base.ExtDefaultPlugin;
/*    */ import org.joget.plugin.property.model.PropertyEditable;
/*    */ import org.joget.plugin.report.api.ReportElement;
/*    */ 
/*    */ public class FormReportElement
/*    */   extends ExtDefaultPlugin
/*    */   implements PropertyEditable, ReportElement
/*    */ {
/*    */   public String getName() {
/* 16 */     return "FormReportElement";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 21 */     return "1.0.0";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 26 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLabel() {
/* 31 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 36 */     return getClass().getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPropertyOptions() {
/* 41 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/FormReportElement.json", null, true, "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getIcon() {
/* 46 */     return "<i class=\"fas fa-file-alt\"></i>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String render() {
/* 51 */     AppDefinition appDef = AppUtil.getCurrentAppDefinition();
/* 52 */     String formDefId = getPropertyString("formDefId");
/*    */     
/* 54 */     if (formDefId.isEmpty()) {
/* 55 */       return "";
/*    */     }
/*    */     
/* 58 */     Boolean hideEmptyValueField = Boolean.valueOf("true".equalsIgnoreCase(getPropertyString("hideEmptyValueField")));
/* 59 */     Boolean showNotSelectedOptions = Boolean.valueOf("true".equalsIgnoreCase(getPropertyString("showNotSelectedOptions")));
/*    */     
/* 61 */     String html = FormPdfUtil.getSelectedFormHtml(formDefId, getPropertyString("recordId"), appDef, null, hideEmptyValueField);
/* 62 */     html = FormPdfUtil.cleanFormHtml(html, showNotSelectedOptions);
/*    */     
/* 64 */     return html;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportReportContainer() {
/* 69 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String preview() {
/* 74 */     return "<style>" + FormPdfUtil.formPdfCss() + "</style>" + render();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCSS() {
/* 79 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\FormReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */