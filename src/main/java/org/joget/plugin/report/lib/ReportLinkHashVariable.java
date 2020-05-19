/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import net.sf.ehcache.Cache;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.joget.apps.app.dao.BuilderDefinitionDao;
/*     */ import org.joget.apps.app.model.AppDefinition;
/*     */ import org.joget.apps.app.model.BuilderDefinition;
/*     */ import org.joget.apps.app.model.DefaultHashVariablePlugin;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.apps.app.service.CustomBuilderUtil;
/*     */ import org.joget.commons.util.DynamicDataSourceManager;
/*     */ import org.joget.commons.util.LogUtil;
/*     */ import org.joget.commons.util.SecurityUtil;
/*     */ import org.joget.commons.util.StringUtil;
/*     */ import org.joget.plugin.base.PluginWebSupport;
/*     */ import org.joget.plugin.report.ReportBuilder;
/*     */ import org.joget.workflow.util.WorkflowUtil;
/*     */ 
/*     */ public class ReportLinkHashVariable
/*     */   extends DefaultHashVariablePlugin implements PluginWebSupport {
/*  30 */   protected ReportBuilder builder = null;
/*     */ 
/*     */   
/*     */   public String getName() {
/*  34 */     return "ReportLinkHashVariable";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/*  39 */     return "reportLink";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  44 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  49 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  54 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  59 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  64 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String processHashVariable(String variableKey) {
/*  70 */     String params = null;
/*  71 */     if (variableKey.contains("[") && variableKey.contains("]")) {
/*  72 */       params = variableKey.substring(variableKey.indexOf("[") + 1, variableKey.indexOf("]"));
/*  73 */       variableKey = variableKey.substring(0, variableKey.indexOf("["));
/*     */     } 
/*     */     
/*  76 */     return getUrl(variableKey, params);
/*     */   }
/*     */   
/*     */   protected String getUrl(String id, String params) {
/*  80 */     if (params == null) {
/*  81 */       params = "";
/*     */     }
/*     */     
/*  84 */     AppDefinition appDef = AppUtil.getCurrentAppDefinition();
/*  85 */     BuilderDefinitionDao builderDefinitionDao = (BuilderDefinitionDao)AppUtil.getApplicationContext().getBean("builderDefinitionDao");
/*  86 */     BuilderDefinition builderDefinition = builderDefinitionDao.loadById(id, appDef);
/*  87 */     if (builderDefinition != null) {
/*  88 */       HttpServletRequest request = WorkflowUtil.getHttpServletRequest();
/*  89 */       if (request != null) {
/*  90 */         HttpServletRequest req = WorkflowUtil.getHttpServletRequest();
/*  91 */         String scheme = req.getScheme();
/*  92 */         String serverName = req.getServerName();
/*  93 */         int serverPort = req.getServerPort();
/*     */ 
/*     */         
/*  96 */         StringBuilder url = new StringBuilder();
/*  97 */         url.append(scheme).append("://").append(serverName);
/*     */         
/*  99 */         if (serverPort != 80 && serverPort != 443) {
/* 100 */           url.append(":").append(serverPort);
/*     */         }
/*     */         
/* 103 */         url.append(req.getContextPath())
/* 104 */           .append("/web/json/app/")
/* 105 */           .append(appDef.getAppId())
/* 106 */           .append("/")
/* 107 */           .append(appDef.getVersion())
/* 108 */           .append("/plugin/")
/* 109 */           .append(getClassName())
/* 110 */           .append("/service?_rid=" + StringUtil.escapeString(id, "url", null));
/*     */         
/* 112 */         if (!params.isEmpty()) {
/* 113 */           url.append("&_params=" + StringUtil.escapeString(SecurityUtil.encrypt(params), "url", null));
/*     */         }
/*     */         
/* 116 */         String nonce = SecurityUtil.generateNonce(new String[] { id, params }, 8);
/* 117 */         url.append("&_nonce=" + StringUtil.escapeString(nonce, "url", null));
/*     */         
/* 119 */         url.append("&_s=" + StringUtil.escapeString(SecurityUtil.encrypt(request.getSession().getId()), "url", null));
/*     */         
/* 121 */         return url.toString();
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> availableSyntax() {
/* 130 */     Collection<String> syntax = new ArrayList<>();
/* 131 */     syntax.add("reportLink.ID");
/* 132 */     syntax.add("reportLink.ID[PARAM_NAME1=PARAM_VALUE1&PARAM_NAME2=PARAM_VALUE2]");
/* 133 */     return syntax;
/*     */   }
/*     */ 
/*     */   
/*     */   public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 138 */     String params = "";
/*     */     
/* 140 */     if (request.getParameter("_params") != null) {
/*     */       try {
/* 142 */         params = SecurityUtil.decrypt(request.getParameter("_params"));
/* 143 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 146 */     String reportId = request.getParameter("_rid");
/* 147 */     String nonce = request.getParameter("_nonce");
/* 148 */     String s = SecurityUtil.decrypt(request.getParameter("_s"));
/*     */     
/* 150 */     if (checkNonce(nonce, s)) {
/* 151 */       AppDefinition appDef = AppUtil.getCurrentAppDefinition();
/* 152 */       BuilderDefinitionDao builderDefinitionDao = (BuilderDefinitionDao)AppUtil.getApplicationContext().getBean("builderDefinitionDao");
/* 153 */       BuilderDefinition builderDefinition = builderDefinitionDao.loadById(reportId, appDef);
/* 154 */       if (builderDefinition != null) {
/* 155 */         String json = builderDefinition.getJson();
/*     */ 
/*     */         
/* 158 */         Map<String, String> rparams = new HashMap<>();
/* 159 */         Map<String, String[]> parameters = null;
/* 160 */         if (params != null && !params.isEmpty()) {
/* 161 */           parameters = StringUtil.getUrlParams(params);
/* 162 */           for (String key : parameters.keySet()) {
/* 163 */             rparams.put(key, StringUtils.join((Object[])parameters.get(key), ";"));
/*     */           }
/*     */         } 
/*     */         
/*     */         try {
/* 168 */           response.setHeader("Content-Type", "application/pdf");
/* 169 */           response.setHeader("Content-Disposition", "attachment; filename=" + builderDefinition.getName() + ".pdf; filename*=UTF-8''" + builderDefinition.getName() + ".pdf");
/* 170 */           response.getOutputStream().write(getReport(json, rparams));
/* 171 */         } catch (IOException ex) {
/* 172 */           LogUtil.error(getClassName(), ex, null);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 176 */       response.setStatus(400);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected byte[] getReport(String json, Map<String, String> params) {
/* 181 */     Map<String, Object> config = new HashMap<>();
/* 182 */     config.put("REPORT_PARAMS", params);
/*     */     
/* 184 */     return (byte[])getBuilder().getBuilderResult(json, config);
/*     */   }
/*     */   
/*     */   protected ReportBuilder getBuilder() {
/* 188 */     if (this.builder == null) {
/* 189 */       this.builder = (ReportBuilder)CustomBuilderUtil.getBuilder("report");
/*     */     }
/* 191 */     return this.builder;
/*     */   }
/*     */   
/*     */   protected boolean checkNonce(String nonce, String s) {
/* 195 */     String cacheKey = DynamicDataSourceManager.getCurrentProfile() + "_NonceGenerator_" + nonce + "_" + s;
/* 196 */     Cache cache = (Cache)AppUtil.getApplicationContext().getBean("nonceCache");
/* 197 */     if (cache.get(cacheKey) != null) {
/* 198 */       return true;
/*     */     }
/* 200 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\ReportLinkHashVariable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */