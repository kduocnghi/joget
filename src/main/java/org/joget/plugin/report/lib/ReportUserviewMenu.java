/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.net.URLEncoder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.joget.apps.app.dao.BuilderDefinitionDao;
/*     */ import org.joget.apps.app.model.AppDefinition;
/*     */ import org.joget.apps.app.model.BuilderDefinition;
/*     */ import org.joget.apps.app.model.CustomBuilder;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.apps.app.service.CustomBuilderUtil;
/*     */ import org.joget.apps.userview.model.UserviewMenu;
/*     */ import org.joget.workflow.util.WorkflowUtil;
/*     */ 
/*     */ 
/*     */ public class ReportUserviewMenu
/*     */   extends UserviewMenu
/*     */ {
/*     */   public String getName() {
/*  21 */     return "ReportMenu";
/*     */   }
/*     */   
/*     */   public String getVersion() {
/*  25 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  30 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  35 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  40 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  45 */     return AppUtil.readPluginResource(getClassName(), "/properties/report/reportUserviewMenu.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCategory() {
/*  50 */     return "Enterprise";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  56 */     return "<i class=\"far fa-file-pdf\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHomePageSupported() {
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDecoratedMenu() {
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRenderPage() {
/*  71 */     String html = "";
/*     */     
/*  73 */     BuilderDefinitionDao builderDefinitionDao = (BuilderDefinitionDao)AppUtil.getApplicationContext().getBean("builderDefinitionDao");
/*  74 */     String id = getPropertyString("reportId");
/*  75 */     AppDefinition appDef = AppUtil.getCurrentAppDefinition();
/*  76 */     BuilderDefinition builderDefinition = builderDefinitionDao.loadById(id, appDef);
/*     */     
/*  78 */     if (builderDefinition != null) {
/*  79 */       String json = builderDefinition.getJson();
/*  80 */       CustomBuilder builder = CustomBuilderUtil.getBuilder("report");
/*     */       
/*  82 */       String params = "";
/*  83 */       Map<String, String> rparams = new HashMap<>();
/*  84 */       Object reportParams = getProperty("reportParams");
/*  85 */       if (reportParams != null && reportParams instanceof Object[] && ((Object[])reportParams).length > 0)
/*     */       {
/*  87 */         for (Object param : (Object[])reportParams) {
/*  88 */           Map paramMap = (Map)param;
/*  89 */           String value = paramMap.get("value").toString();
/*  90 */           if (value.isEmpty()) {
/*  91 */             value = paramMap.get("default").toString();
/*     */           }
/*  93 */           rparams.put(paramMap.get("param").toString(), value);
/*     */           
/*  95 */           if (!params.isEmpty()) {
/*  96 */             params = params + "&";
/*     */           }
/*     */           try {
/*  99 */             params = params + paramMap.get("param").toString() + "=" + URLEncoder.encode(value, "UTF-8");
/* 100 */           } catch (Exception exception) {}
/*     */         } 
/*     */       }
/*     */       
/* 104 */       Map<String, Object> config = new HashMap<>();
/* 105 */       config.put("REPORT_PARAMS", rparams);
/* 106 */       config.put("REPORT_OUTPUT", "HTML_WITH_HEADER_FOOTER");
/*     */       
/* 108 */       String quiteEdit = "";
/* 109 */       if (AppUtil.isQuickEditEnabled()) {
/* 110 */         String contextPath = WorkflowUtil.getHttpServletRequest().getContextPath();
/* 111 */         quiteEdit = "<div class=\"quickEdit\">";
/* 112 */         quiteEdit = quiteEdit + "<a href=\"" + contextPath + "/web/console/app/" + appDef.getAppId() + "/" + appDef.getVersion() + "/cbuilder/report/design/" + id + "\" target=\"_blank\">";
/* 113 */         quiteEdit = quiteEdit + "<i class=\"fas fa-pencil-alt\"></i> " + builder.getObjectLabel() + ": " + builderDefinition.getName() + "</a></div>";
/*     */       } 
/*     */       
/* 116 */       html = (String)builder.getBuilderResult(json, config);
/* 117 */       html = "<div class=\"report_content\">" + getLink(id, params) + quiteEdit + html + "</div>";
/*     */     } 
/*     */     
/* 120 */     return getPropertyString("customHeader") + html + getPropertyString("customFooter");
/*     */   }
/*     */   
/*     */   protected String getLink(String id, String params) {
/* 124 */     if (params == null) {
/* 125 */       params = "";
/*     */     }
/* 127 */     if (!params.isEmpty()) {
/* 128 */       params = "[" + params + "]";
/*     */     }
/*     */     
/* 131 */     String hash = "#reportLink." + id + params + "#";
/* 132 */     String label = AppPluginUtil.getMessage("reportMenu.download", getClassName(), "messages/ReportElements");
/*     */     
/* 134 */     return "<p style=\"text-align:right;\"><a class=\"downloadlink\" target=\"_blank\" href=\"" + AppUtil.processHashVariable(hash, null, null, null) + "\"><i class=\"far fa-file-pdf\" style=\"color:#dd2c00;\"></i> " + label + "</a></p>";
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\ReportUserviewMenu.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */