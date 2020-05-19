/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.commons.util.StringUtil;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ import org.joget.workflow.util.WorkflowUtil;
/*     */ 
/*     */ public class ImageReportElement
/*     */   extends ExtDefaultPlugin
/*     */   implements PropertyEditable, ReportElement
/*     */ {
/*     */   public String getName() {
/*  17 */     return "ImageReportElement";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  22 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  27 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  32 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  37 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  42 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/ImageReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  47 */     return "<i class=\"far fa-image\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  52 */     String path = getPropertyString("path");
/*  53 */     if (path.startsWith("/")) {
/*  54 */       HttpServletRequest request = WorkflowUtil.getHttpServletRequest();
/*  55 */       path = getURL(request, path);
/*     */     } 
/*     */     
/*  58 */     if (!path.isEmpty()) {
/*  59 */       String style = "";
/*  60 */       String margin = "";
/*  61 */       if (!getPropertyString("width").isEmpty()) {
/*  62 */         style = style + "width:" + getPropertyString("width") + ";";
/*     */       }
/*  64 */       if (!getPropertyString("height").isEmpty()) {
/*  65 */         style = style + "height:" + getPropertyString("height") + ";";
/*     */       }
/*  67 */       if (!getPropertyString("marginBottom").isEmpty()) {
/*  68 */         margin = margin + "margin-bottom:" + getPropertyString("marginBottom") + ";";
/*     */       }
/*  70 */       return "<div style=\"text-align:" + getPropertyString("halign") + ";" + margin + "\"><img style=\"" + style + "\" src=\"" + StringUtil.escapeString(path, "xml", null) + "\" /></div>";
/*     */     } 
/*     */     
/*  73 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/*  83 */     return render();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCSS() {
/*  88 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getURL(HttpServletRequest req, String link) {
/*  93 */     String scheme = req.getScheme();
/*  94 */     String serverName = req.getServerName();
/*  95 */     int serverPort = req.getServerPort();
/*     */ 
/*     */     
/*  98 */     StringBuilder url = new StringBuilder();
/*  99 */     url.append(scheme).append("://").append(serverName);
/*     */     
/* 101 */     if (serverPort != 80 && serverPort != 443) {
/* 102 */       url.append(":").append(serverPort);
/*     */     }
/*     */     
/* 105 */     if (link != null) {
/* 106 */       url.append(link);
/*     */     }
/* 108 */     return url.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\ImageReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */