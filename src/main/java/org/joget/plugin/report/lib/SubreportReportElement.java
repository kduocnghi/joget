/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.joget.apps.app.dao.BuilderDefinitionDao;
/*     */ import org.joget.apps.app.model.AppDefinition;
/*     */ import org.joget.apps.app.model.BuilderDefinition;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.apps.app.service.CustomBuilderUtil;
/*     */ import org.joget.apps.datalist.model.DataListBinder;
/*     */ import org.joget.apps.datalist.model.DataListCollection;
/*     */ import org.joget.apps.datalist.service.DataListService;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.base.PluginManager;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.ReportBuilder;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ 
/*     */ public class SubreportReportElement
/*     */   extends ExtDefaultPlugin implements PropertyEditable, ReportElement {
/*  22 */   protected DataListCollection binderdata = null;
/*  23 */   protected ReportBuilder builder = null;
/*     */ 
/*     */   
/*     */   public String getName() {
/*  27 */     return "SubreportReportElement";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  32 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  37 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  42 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  47 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  52 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/SubreportReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  57 */     return "<i class=\"fas fa-file-contract\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  62 */     String html = "";
/*     */     
/*  64 */     if (!getPropertyString("reportId").isEmpty()) {
/*  65 */       AppDefinition appDef = AppUtil.getCurrentAppDefinition();
/*  66 */       BuilderDefinitionDao builderDefinitionDao = (BuilderDefinitionDao)AppUtil.getApplicationContext().getBean("builderDefinitionDao");
/*  67 */       BuilderDefinition builderDefinition = builderDefinitionDao.loadById(getPropertyString("reportId"), appDef);
/*     */       
/*  69 */       if (builderDefinition != null) {
/*  70 */         String json = builderDefinition.getJson();
/*     */         
/*  72 */         if (getData() == null || "true".equals(getPropertyString("elementPreview"))) {
/*  73 */           Map<String, String> rparams = new HashMap<>();
/*  74 */           Object reportParams = getProperty("reportParams");
/*  75 */           if (reportParams != null && reportParams instanceof Object[] && ((Object[])reportParams).length > 0)
/*     */           {
/*  77 */             for (Object param : (Object[])reportParams) {
/*  78 */               Map paramMap = (Map)param;
/*  79 */               String value = paramMap.get("value").toString();
/*  80 */               if (value.isEmpty()) {
/*  81 */                 value = paramMap.get("default").toString();
/*     */               }
/*  83 */               rparams.put(paramMap.get("param").toString(), value);
/*     */             } 
/*     */           }
/*  86 */           html = html + getReport(json, rparams);
/*     */         } else {
/*  88 */           for (Object row : getData()) {
/*  89 */             html = html + getReport(json, row);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     return html;
/*     */   }
/*     */   
/*     */   protected ReportBuilder getBuilder() {
/*  99 */     if (this.builder == null) {
/* 100 */       this.builder = (ReportBuilder)CustomBuilderUtil.getBuilder("report");
/*     */     }
/* 102 */     return this.builder;
/*     */   }
/*     */   
/*     */   protected String getReport(String json, Object row) {
/* 106 */     Map<String, String> rparams = new HashMap<>();
/* 107 */     Object reportParams = getProperty("reportParamMapping");
/* 108 */     if (reportParams != null && reportParams instanceof Object[] && ((Object[])reportParams).length > 0) {
/*     */       
/* 110 */       Object value = null;
/* 111 */       for (Object param : (Object[])reportParams) {
/* 112 */         Map paramMap = (Map)param;
/* 113 */         value = DataListService.evaluateColumnValueFromRow(row, paramMap.get("value").toString());
/* 114 */         if (value == null) {
/* 115 */           value = "";
/*     */         }
/* 117 */         if (value.toString().isEmpty()) {
/* 118 */           value = paramMap.get("default").toString();
/*     */         }
/* 120 */         rparams.put(paramMap.get("param").toString(), value.toString());
/*     */       } 
/*     */     } 
/* 123 */     return getReport(json, rparams);
/*     */   }
/*     */   
/*     */   protected String getReport(String json, Map<String, String> params) {
/* 127 */     Map<String, Object> config = new HashMap<>();
/* 128 */     config.put("REPORT_PARAMS", params);
/* 129 */     config.put("REPORT_OUTPUT", "HTML");
/*     */     
/* 131 */     return (String)getBuilder().getBuilderResult(json, config);
/*     */   }
/*     */   
/*     */   protected DataListCollection getData() {
/* 135 */     if (this.binderdata == null) {
/*     */       
/* 137 */       Object binderData = getProperty("binder");
/* 138 */       if (binderData != null && binderData instanceof Map) {
/* 139 */         Map bdMap = (Map)binderData;
/* 140 */         if (bdMap != null && bdMap.containsKey("className") && !bdMap.get("className").toString().isEmpty()) {
/* 141 */           PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
/* 142 */           DataListBinder binder = (DataListBinder)pluginManager.getPlugin(bdMap.get("className").toString());
/*     */           
/* 144 */           if (binder != null) {
/* 145 */             Map bdProps = (Map)bdMap.get("properties");
/* 146 */             binder.setProperties(bdProps);
/*     */             
/* 148 */             String sort = null;
/* 149 */             Boolean order = null;
/* 150 */             if (!getPropertyString("sortingOrderByColumn").isEmpty()) {
/* 151 */               sort = getPropertyString("sortingOrderByColumn");
/* 152 */               order = Boolean.valueOf("DESC".equals(getPropertyString("sortingOrder")));
/*     */             } 
/*     */             
/* 155 */             this.binderdata = binder.getData(null, bdProps, new org.joget.apps.datalist.model.DataListFilterQueryObject[0], sort, order, null, null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 160 */     return this.binderdata;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/* 171 */     setProperty("elementPreview", "true");
/* 172 */     return render();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCSS() {
/* 177 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\SubreportReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */