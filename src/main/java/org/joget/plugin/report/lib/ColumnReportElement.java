/*    */ package org.joget.plugin.report.lib;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.joget.apps.app.service.AppPluginUtil;
/*    */ import org.joget.plugin.base.ExtDefaultPlugin;
/*    */ import org.joget.plugin.base.HiddenPlugin;
/*    */ import org.joget.plugin.property.model.PropertyEditable;
/*    */ import org.joget.plugin.report.api.ReportContainer;
/*    */ import org.joget.plugin.report.api.ReportElement;
/*    */ 
/*    */ public class ColumnReportElement
/*    */   extends ExtDefaultPlugin implements PropertyEditable, ReportElement, ReportContainer, HiddenPlugin {
/* 13 */   Collection<ReportElement> child = null;
/*    */   
/* 15 */   protected String css = "";
/*    */ 
/*    */   
/*    */   public String getName() {
/* 19 */     return "ColumnReportElement";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 24 */     return "1.0.0";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 29 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLabel() {
/* 34 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 39 */     return getClass().getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPropertyOptions() {
/* 44 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getIcon() {
/* 49 */     return "<i class=\"fas fa-columns\"></i>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String render() {
/* 54 */     String html = "";
/* 55 */     Collection<ReportElement> columns = getChildren();
/*    */     
/* 57 */     if (columns != null && !columns.isEmpty()) {
/* 58 */       for (ReportElement column : columns) {
/* 59 */         html = html + column.render();
/* 60 */         String ecss = column.getCSS();
/* 61 */         if (ecss != null && !ecss.isEmpty()) {
/* 62 */           this.css += ecss;
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/* 67 */     return html;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportReportContainer() {
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String preview() {
/* 77 */     return render();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCSS() {
/* 82 */     return this.css;
/*    */   }
/*    */ 
/*    */   
/*    */   public String builderScript() {
/* 87 */     return "{}";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setChildren(Collection<ReportElement> child) {
/* 92 */     this.child = child;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<ReportElement> getChildren() {
/* 97 */     return this.child;
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\ColumnReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */