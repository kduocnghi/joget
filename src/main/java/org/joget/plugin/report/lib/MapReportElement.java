/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.apps.datalist.model.DataListBinder;
/*     */ import org.joget.apps.datalist.model.DataListCollection;
/*     */ import org.joget.apps.datalist.service.DataListService;
/*     */ import org.joget.commons.util.StringUtil;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.base.PluginManager;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ 
/*     */ public class MapReportElement
/*     */   extends ExtDefaultPlugin
/*     */   implements PropertyEditable, ReportElement {
/*  19 */   private DataListCollection cachedData = null;
/*     */ 
/*     */   
/*     */   public String getName() {
/*  23 */     return "MapReportElement";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  28 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  33 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  38 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  43 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  48 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/MapReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  53 */     return "<i class=\"fas fa-map-marked-alt\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  58 */     if (getPropertyString("latitude").isEmpty() || getPropertyString("longitude").isEmpty() || getPropertyString("apikey").isEmpty()) {
/*  59 */       return "";
/*     */     }
/*     */     
/*  62 */     int width = 640;
/*  63 */     int height = 480;
/*     */     try {
/*  65 */       if (!getPropertyString("width").isEmpty()) {
/*  66 */         width = Integer.parseInt(getPropertyString("width"));
/*     */       }
/*  68 */     } catch (Exception exception) {}
/*     */     try {
/*  70 */       if (!getPropertyString("height").isEmpty()) {
/*  71 */         height = Integer.parseInt(getPropertyString("height"));
/*     */       }
/*  73 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  76 */     String url = "https://maps.googleapis.com/maps/api/staticmap";
/*  77 */     url = url + "?center=" + getPropertyString("latitude") + "," + getPropertyString("longitude") + "&size=" + width + "x" + height;
/*  78 */     if (!getPropertyString("type").isEmpty()) {
/*  79 */       url = url + "&maptype=" + getPropertyString("type");
/*     */     }
/*  81 */     if (!getPropertyString("zoom").isEmpty()) {
/*  82 */       url = url + "&zoom=" + getPropertyString("zoom");
/*     */     }
/*  84 */     if (!getPropertyString("apikey").isEmpty()) {
/*  85 */       url = url + "&key=" + getPropertyString("apikey");
/*     */     }
/*     */ 
/*     */     
/*  89 */     if ("static".equalsIgnoreCase(getPropertyString("markersType"))) {
/*  90 */       Object[] markers = (Object[])getProperty("markers");
/*  91 */       if (markers != null && markers.length > 0) {
/*  92 */         Map marker = null;
/*  93 */         String color = null;
/*  94 */         for (Object o : markers) {
/*  95 */           marker = (HashMap)o;
/*  96 */           color = "red";
/*  97 */           if (!marker.get("color").toString().isEmpty()) {
/*  98 */             color = marker.get("color").toString();
/*     */           }
/* 100 */           url = url + "&markers=color:" + color + "%7Clabel:" + marker.get("label").toString() + "%7C" + marker.get("latitude").toString() + "," + marker.get("longitude").toString();
/*     */         } 
/*     */       } 
/* 103 */     } else if ("binder".equalsIgnoreCase(getPropertyString("markersType"))) {
/* 104 */       DataListCollection data = getData();
/* 105 */       String color = null;
/* 106 */       String label = null;
/* 107 */       String latitude = null;
/* 108 */       String longitude = null;
/* 109 */       for (Object row : data) {
/* 110 */         color = "red";
/* 111 */         label = "";
/* 112 */         latitude = "";
/* 113 */         longitude = "";
/* 114 */         if (!getPropertyString("mapping_color").isEmpty()) {
/* 115 */           color = DataListService.evaluateColumnValueFromRow(row, getPropertyString("mapping_color")).toString();
/*     */         }
/* 117 */         if (!getPropertyString("mapping_label").isEmpty()) {
/* 118 */           label = DataListService.evaluateColumnValueFromRow(row, getPropertyString("mapping_label")).toString();
/*     */         }
/* 120 */         if (!getPropertyString("mapping_latitude").isEmpty()) {
/* 121 */           latitude = DataListService.evaluateColumnValueFromRow(row, getPropertyString("mapping_latitude")).toString();
/*     */         }
/* 123 */         if (!getPropertyString("mapping_longitude").isEmpty()) {
/* 124 */           longitude = DataListService.evaluateColumnValueFromRow(row, getPropertyString("mapping_longitude")).toString();
/*     */         }
/* 126 */         if (!latitude.isEmpty() && !longitude.isEmpty()) {
/* 127 */           url = url + "&markers=color:" + color + "%7Clabel:" + label + "%7C" + latitude + "," + longitude;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     if (url != null) {
/* 133 */       String style = "";
/* 134 */       String margin = "";
/* 135 */       if (!getPropertyString("width").isEmpty()) {
/* 136 */         style = style + "width:" + getPropertyString("width") + ";";
/*     */       }
/* 138 */       if (!getPropertyString("height").isEmpty()) {
/* 139 */         style = style + "height:" + getPropertyString("height") + ";";
/*     */       }
/* 141 */       if (!getPropertyString("marginBottom").isEmpty()) {
/* 142 */         margin = margin + "margin-bottom:" + getPropertyString("marginBottom") + ";";
/*     */       }
/* 144 */       return "<div style=\"text-align:" + getPropertyString("halign") + ";" + margin + "\"><img style=\"" + style + "\" src=\"" + StringUtil.escapeString(url, "xml", null) + "\" /></div>";
/*     */     } 
/*     */     
/* 147 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/* 152 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/* 157 */     return render();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCSS() {
/* 162 */     return "";
/*     */   }
/*     */   
/*     */   protected DataListCollection getData() {
/* 166 */     if (this.cachedData == null) {
/*     */       
/* 168 */       Object binderData = getProperty("binder");
/* 169 */       if (binderData != null && binderData instanceof Map) {
/* 170 */         Map bdMap = (Map)binderData;
/* 171 */         if (bdMap != null && bdMap.containsKey("className") && !bdMap.get("className").toString().isEmpty()) {
/* 172 */           PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
/* 173 */           DataListBinder binder = (DataListBinder)pluginManager.getPlugin(bdMap.get("className").toString());
/*     */           
/* 175 */           if (binder != null) {
/* 176 */             Map bdProps = (Map)bdMap.get("properties");
/* 177 */             binder.setProperties(bdProps);
/*     */             
/* 179 */             this.cachedData = binder.getData(null, bdProps, new org.joget.apps.datalist.model.DataListFilterQueryObject[0], null, null, null, null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 184 */     return this.cachedData;
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\MapReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */