/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.api.ReportContainer;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ 
/*     */ public class ColumnsReportElement
/*     */   extends ExtDefaultPlugin implements PropertyEditable, ReportElement, ReportContainer {
/*  13 */   Collection<ReportElement> child = null;
/*     */   
/*  15 */   protected String css = "";
/*     */ 
/*     */   
/*     */   public String getName() {
/*  19 */     return "ColumnsReportElement";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  24 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  29 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  34 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  39 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  44 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/ColumnsReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  49 */     return "<i class=\"fas fa-columns\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  54 */     String html = "";
/*  55 */     Collection<ReportElement> columns = getChildren();
/*  56 */     String marginBottom = "";
/*  57 */     if (!getPropertyString("marginBottom").isEmpty()) {
/*  58 */       marginBottom = marginBottom + "margin-bottom:" + getPropertyString("marginBottom") + ";";
/*     */     }
/*     */     
/*  61 */     String width = "50%";
/*  62 */     String type = getPropertyString("type");
/*  63 */     if (null != type) switch (type) {
/*     */         case "2":
/*  65 */           width = "30%";
/*     */           break;
/*     */         case "3":
/*  68 */           width = "70%";
/*     */           break;
/*     */         case "4":
/*  71 */           width = "33.3333%";
/*     */           break;
/*     */         case "5":
/*  74 */           width = "25%";
/*     */           break;
/*     */       } 
/*     */     
/*     */     
/*     */     
/*  80 */     if (columns != null && !columns.isEmpty()) {
/*  81 */       html = html + "<table class=\"columns\" style=\"width:100%;border-collapse: collapse;border-spacing: 0;" + marginBottom + "\"><tr style=\"vertical-align:top;\">";
/*  82 */       boolean isFirst = true;
/*  83 */       for (ReportElement column : columns) {
/*  84 */         if (!getPropertyString("gap").isEmpty()) {
/*  85 */           if (isFirst) {
/*  86 */             isFirst = false;
/*     */           } else {
/*  88 */             html = html + "<td class=\"gap\" style=\"width:" + getPropertyString("gap") + ";\"></td>";
/*     */           } 
/*     */         }
/*     */         
/*  92 */         html = html + "<td class=\"column\" style=\"width:" + width + ";\"><div>" + column.render() + "</div></td>";
/*  93 */         String ecss = column.getCSS();
/*  94 */         if (ecss != null && !ecss.isEmpty()) {
/*  95 */           this.css += ecss;
/*     */         }
/*     */         
/*  98 */         if ("30%".equals(width)) {
/*  99 */           width = "70%"; continue;
/* 100 */         }  if ("70%".equals(width)) {
/* 101 */           width = "30%";
/*     */         }
/*     */       } 
/* 104 */       html = html + "</tr></table>";
/*     */     } 
/*     */     
/* 107 */     return html;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/* 117 */     return render();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCSS() {
/* 122 */     return this.css;
/*     */   }
/*     */ 
/*     */   
/*     */   public String builderScript() {
/* 127 */     return AppUtil.readPluginResource(getClass().getName(), "/scripts/columns.js", null, false, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChildren(Collection<ReportElement> child) {
/* 132 */     this.child = child;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ReportElement> getChildren() {
/* 137 */     return this.child;
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\ColumnsReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */