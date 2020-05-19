/*    */ package org.joget.plugin.report.lib;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.joget.apps.app.service.AppPluginUtil;
/*    */ import org.joget.apps.app.service.AppUtil;
/*    */ import org.joget.plugin.base.ExtDefaultPlugin;
/*    */ import org.joget.plugin.property.model.PropertyEditable;
/*    */ import org.joget.plugin.report.api.ReportElement;
/*    */ 
/*    */ public class HtmlReportElement
/*    */   extends ExtDefaultPlugin
/*    */   implements PropertyEditable, ReportElement
/*    */ {
/*    */   public String getName() {
/* 16 */     return "HtmlReportElement";
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
/* 41 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/HtmlReportElement.json", null, true, "messages/ReportElements");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getIcon() {
/* 46 */     return "<i class=\"fas fa-code\"></i>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String render() {
/* 51 */     return "<div>" + getPropertyString("html") + "</div>";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportReportContainer() {
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String preview() {
/* 61 */     return render();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCSS() {
/* 66 */     String css = "";
/*    */     
/* 68 */     Pattern pattern = Pattern.compile("<style[^>]*>(.*?)</style>", 32);
/* 69 */     Matcher matcher = pattern.matcher(getPropertyString("html"));
/* 70 */     while (matcher.find()) {
/* 71 */       css = css + matcher.group(1);
/*    */     }
/* 73 */     return css;
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\HtmlReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */