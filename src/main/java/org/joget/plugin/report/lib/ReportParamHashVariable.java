/*    */ package org.joget.plugin.report.lib;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import org.joget.apps.app.model.DefaultHashVariablePlugin;
/*    */ import org.joget.apps.app.service.AppPluginUtil;
/*    */ import org.joget.plugin.report.ReportBuilder;
/*    */ 
/*    */ public class ReportParamHashVariable
/*    */   extends DefaultHashVariablePlugin
/*    */ {
/*    */   public String getName() {
/* 13 */     return "ReportParamHashVariable";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPrefix() {
/* 18 */     return "reportParam";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 23 */     return "1.0.0";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 28 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLabel() {
/* 33 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 38 */     return getClass().getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPropertyOptions() {
/* 43 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String processHashVariable(String variableKey) {
/* 48 */     if (ReportBuilder.getReportParameters().containsKey(variableKey)) {
/* 49 */       return (String)ReportBuilder.getReportParameters().get(variableKey);
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> availableSyntax() {
/* 56 */     Collection<String> syntax = new ArrayList<>();
/* 57 */     syntax.add("reportParam.name");
/* 58 */     return syntax;
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\ReportParamHashVariable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */