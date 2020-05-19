/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.joget.apps.app.dao.DatalistDefinitionDao;
/*     */ import org.joget.apps.app.model.AppDefinition;
/*     */ import org.joget.apps.app.model.DatalistDefinition;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.apps.datalist.model.DataList;
/*     */ import org.joget.apps.datalist.model.DataListCollection;
/*     */ import org.joget.apps.datalist.model.DataListColumn;
/*     */ import org.joget.apps.datalist.model.DataListColumnFormat;
/*     */ import org.joget.apps.datalist.service.DataListService;
/*     */ import org.joget.commons.util.LogUtil;
/*     */ import org.joget.commons.util.StringUtil;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.base.PluginManager;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ 
/*     */ public class ListReportElement extends ExtDefaultPlugin implements PropertyEditable, ReportElement {
/*  35 */   private DataList cachedDataList = null;
/*  36 */   private DataListCollection cachedDataListData = null;
/*  37 */   private int colSize = 0;
/*     */   private boolean hasFormulaColumns = false;
/*     */   private boolean hasFooter = false;
/*     */   private boolean hasFormatter = false;
/*  41 */   private Collection<DataListColumnFormat> nestedListFormatter = new ArrayList<>();
/*  42 */   private Map<String, Object> footers = new HashMap<>();
/*     */ 
/*     */   
/*     */   public String getName() {
/*  46 */     return "ListReportElement";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  51 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  56 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  61 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  66 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  71 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/ListReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  76 */     return "<i class=\"fas fa-table\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  81 */     if (getPropertyString("datalistId").isEmpty() || getDataList() == null) {
/*  82 */       return "";
/*     */     }
/*     */     
/*  85 */     Map<Object, Object> model = new HashMap<>();
/*  86 */     model.put("element", this);
/*  87 */     model.put("dataList", getDataList());
/*  88 */     model.put("rows", getDataListData());
/*     */     
/*  90 */     PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
/*  91 */     String content = pluginManager.getPluginFreeMarkerTemplate(model, getClass().getName(), "/templates/listReportElement.ftl", "messages/ReportElements");
/*  92 */     return content;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/* 102 */     setProperty("elementPreview", "true");
/* 103 */     return render();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCSS() {
/* 108 */     return "";
/*     */   }
/*     */   
/*     */   public boolean hasFooter() {
/* 112 */     return this.hasFooter;
/*     */   }
/*     */   
/*     */   protected DataList getDataList() {
/* 116 */     if (this.cachedDataList == null) {
/* 117 */       DataListService dataListService = (DataListService)AppUtil.getApplicationContext().getBean("dataListService");
/* 118 */       DatalistDefinitionDao datalistDefinitionDao = (DatalistDefinitionDao)AppUtil.getApplicationContext().getBean("datalistDefinitionDao");
/* 119 */       AppDefinition appDef = AppUtil.getCurrentAppDefinition();
/* 120 */       DatalistDefinition datalistDefinition = (DatalistDefinition)datalistDefinitionDao.loadById(getPropertyString("datalistId"), appDef);
/* 121 */       if (datalistDefinition != null) {
/* 122 */         this.cachedDataList = dataListService.fromJson(datalistDefinition.getJson());
/*     */         
/* 124 */         Object footers = getProperty("footer");
/* 125 */         Map<String, Map> footerMap = new HashMap<>();
/* 126 */         if (footers != null && footers instanceof Object[] && ((Object[])footers).length > 0)
/*     */         {
/* 128 */           for (Object footer : (Object[])footers) {
/* 129 */             Map fMap = (Map)footer;
/* 130 */             footerMap.put((String)fMap.get("fieldid"), fMap);
/*     */           } 
/*     */         }
/*     */         
/* 134 */         for (DataListColumn c : this.cachedDataList.getColumns()) {
/* 135 */           if (isVisible(c)) {
/* 136 */             if (footerMap.containsKey(c.getName())) {
/* 137 */               this.hasFooter = true;
/* 138 */               c.setProperty("footer", ((Map)footerMap.get(c.getName())).get("operator").toString());
/* 139 */               c.setProperty("numberFormat", ((Map)footerMap.get(c.getName())).get("numberFormat").toString());
/*     */             } 
/* 141 */             if (c.getFormats() != null && !c.getFormats().isEmpty()) {
/* 142 */               DataListColumnFormat format = c.getFormats().iterator().next();
/* 143 */               if (format != null) {
/* 144 */                 this.hasFormatter = true;
/* 145 */                 if ("org.joget.plugin.enterprise.NestedDatalistFormatter".equalsIgnoreCase(format.getClassName()) && format
/* 146 */                   .getPropertyString("exportOptions").contains("pdf")) {
/* 147 */                   this.nestedListFormatter.add(format);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 152 */             this.colSize++;
/*     */           } 
/*     */         } 
/*     */         
/* 156 */         Object formulaColumns = getProperty("formulaColumns");
/* 157 */         if (formulaColumns != null && formulaColumns instanceof Object[] && ((Object[])formulaColumns).length > 0) {
/* 158 */           this.hasFormulaColumns = true;
/* 159 */           List<DataListColumn> columns = new ArrayList<>();
/* 160 */           columns.addAll(Arrays.asList(this.cachedDataList.getColumns()));
/*     */ 
/*     */           
/* 163 */           int c = 0;
/* 164 */           for (Object fc : (Object[])formulaColumns) {
/* 165 */             Map fcMap = (Map)fc;
/*     */             
/* 167 */             DataListColumn formulaColumn = new DataListColumn("formulaColumn-" + c, fcMap.get("label").toString(), false);
/* 168 */             formulaColumn.setProperties(fcMap);
/* 169 */             formulaColumn.setWidth(fcMap.get("width").toString());
/*     */             
/* 171 */             if (!fcMap.get("footer").toString().isEmpty()) {
/* 172 */               this.hasFooter = true;
/*     */             }
/*     */             
/* 175 */             columns.add(formulaColumn);
/* 176 */             c++;
/* 177 */             this.colSize++;
/*     */           } 
/* 179 */           this.cachedDataList.setColumns(columns.<DataListColumn>toArray(new DataListColumn[0]));
/*     */         } 
/*     */       } 
/*     */     } 
/* 183 */     return this.cachedDataList;
/*     */   }
/*     */   
/*     */   protected DataListCollection getDataListData() {
/* 187 */     if (this.cachedDataListData == null) {
/* 188 */       boolean isPreview = false;
/* 189 */       if ("true".equals(getPropertyString("elementPreview"))) {
/* 190 */         this.cachedDataListData = getDataList().getRows(Integer.valueOf(3), null);
/* 191 */         isPreview = true;
/*     */       } else {
/* 193 */         Object filterParamsProperty = getProperty("filterParams");
/* 194 */         if (filterParamsProperty != null && filterParamsProperty instanceof Object[] && ((Object[])filterParamsProperty).length > 0) {
/* 195 */           Map<String, String[]> params = getDataList().getRequestParamMap();
/* 196 */           if (params == null) {
/* 197 */             params = (Map)new HashMap<>();
/*     */           }
/*     */           
/* 200 */           for (Object param : (Object[])filterParamsProperty) {
/* 201 */             Map paramMap = (Map)param;
/*     */             
/* 203 */             params.put(paramMap.get("param").toString(), new String[] { paramMap.get("value").toString() });
/*     */           } 
/* 205 */           getDataList().setRequestParamMap(params);
/*     */         } 
/*     */         
/* 208 */         this.cachedDataListData = getDataList().getRows();
/*     */       } 
/* 210 */       this.cachedDataListData = preparedataListCollection(getDataList(), this.cachedDataListData, isPreview);
/*     */     } 
/* 212 */     return this.cachedDataListData;
/*     */   }
/*     */   
/*     */   public DataListCollection preparedataListCollection(DataList dataList, DataListCollection<Map<String, Object>> data, boolean isPreview) {
/* 216 */     if (dataList != null && !data.isEmpty()) {
/* 217 */       if (this.hasFormatter || this.hasFormulaColumns || this.hasFooter) {
/* 218 */         DataListCollection<Map<String, Object>> newData = new DataListCollection();
/* 219 */         Object value = null;
/* 220 */         Map<String, Object> newRow = null;
/* 221 */         for (Map<String, Object> row : data) {
/* 222 */           newRow = new HashMap<>();
/* 223 */           for (DataListColumn c : dataList.getColumns()) {
/* 224 */             if (isVisible(c)) {
/* 225 */               value = DataListService.evaluateColumnValueFromRow(row, c.getName());
/* 226 */               if (c.getFormats() != null && !c.getFormats().isEmpty()) {
/* 227 */                 for (DataListColumnFormat f : c.getFormats()) {
/* 228 */                   if (f != null) {
/* 229 */                     value = f.format(dataList, c, row, value);
/*     */                   }
/*     */                 } 
/*     */               } else {
/*     */                 
/* 234 */                 value = getFormulaValue(value, c, row);
/*     */               } 
/* 236 */               calculateFooter(c, value);
/*     */               
/* 238 */               if (value != null && value instanceof String) {
/* 239 */                 value = StringUtil.stripHtmlRelaxed(value.toString());
/*     */               }
/* 241 */               newRow.put(c.getName(), value);
/*     */             } 
/*     */           } 
/* 244 */           newData.add(newRow);
/*     */         } 
/* 246 */         data = newData;
/*     */       }
/*     */     
/* 249 */     } else if (dataList != null && data.isEmpty() && isPreview) {
/* 250 */       DataListCollection<Map<String, Object>> newData = new DataListCollection();
/* 251 */       Map<String, Object> newRow = new HashMap<>();
/* 252 */       for (DataListColumn c : dataList.getColumns()) {
/* 253 */         if (isVisible(c)) {
/* 254 */           newRow.put(c.getName(), "");
/*     */         }
/*     */       } 
/* 257 */       newData.add(newRow);
/* 258 */       newData.add(newRow);
/* 259 */       newData.add(newRow);
/* 260 */       data = newData;
/*     */     } 
/* 262 */     return data;
/*     */   }
/*     */   
/*     */   public static boolean isVisible(DataListColumn c) {
/* 266 */     return ((c.isHidden() && "true".equalsIgnoreCase(c.getPropertyString("include_export"))) || (!c.isHidden() && !"true".equalsIgnoreCase(c.getPropertyString("exclude_export"))));
/*     */   }
/*     */   
/*     */   public String getHeaderStyle() {
/* 270 */     String css = "";
/* 271 */     if (!getPropertyString("headerColor").isEmpty()) {
/* 272 */       css = css + "background:" + getPropertyString("headerColor") + ";";
/*     */     }
/* 274 */     if (!getPropertyString("headerTextColor").isEmpty()) {
/* 275 */       css = css + "color:" + getPropertyString("headerTextColor") + ";";
/*     */     }
/* 277 */     return css;
/*     */   }
/*     */   
/*     */   public String getRowExtra(Object row) {
/* 281 */     String html = "";
/* 282 */     if (!this.nestedListFormatter.isEmpty()) {
/* 283 */       for (DataListColumnFormat f : this.nestedListFormatter) {
/* 284 */         ListReportElement nl = new ListReportElement();
/* 285 */         nl.setProperty("datalistId", f.getPropertyString("listId"));
/* 286 */         if ("true".equalsIgnoreCase(f.getPropertyString("customHeaderColor"))) {
/* 287 */           nl.setProperty("headerColor", f.getPropertyString("headerColor"));
/* 288 */           nl.setProperty("headerTextColor", f.getPropertyString("headerFontColor"));
/*     */         } 
/* 290 */         Object requestParamsProperty = f.getProperty("requestParams");
/* 291 */         if (requestParamsProperty != null && requestParamsProperty instanceof Object[]) {
/* 292 */           List<Map> filters = new ArrayList<>();
/* 293 */           for (Object param : (Object[])requestParamsProperty) {
/* 294 */             Map<String, String> paramMap = (Map<String, String>)param;
/* 295 */             Object tempValue = DataListService.evaluateColumnValueFromRow(row, ((String)paramMap.get("hrefColumn")).toString());
/* 296 */             paramMap.put("value", (tempValue != null) ? tempValue.toString() : ((String)paramMap.get("defaultValue")).toString());
/* 297 */             filters.add(paramMap);
/*     */           } 
/* 299 */           nl.setProperty("filterParams", filters.toArray(new Object[0]));
/*     */         } 
/* 301 */         nl.setProperty("IS_PREVIEW", Boolean.valueOf(false));
/* 302 */         html = html + "<tr style=\"padding:2px 0px 5px;\"><td colspan=\"" + this.colSize + "\">" + nl.render() + "</td></tr>";
/*     */       } 
/*     */     }
/* 305 */     return html;
/*     */   }
/*     */   
/*     */   public String getFooter(DataListColumn c) {
/* 309 */     Object value = null;
/* 310 */     String type = c.getPropertyString("footer");
/* 311 */     if (!type.isEmpty()) {
/* 312 */       Set<Object> distinct; double count; double sum; double count2; double squares; switch (type) {
/*     */         case "DistinctCount":
/* 314 */           distinct = (HashSet)this.footers.get(c.getName() + ":distinct");
/* 315 */           if (distinct != null) {
/* 316 */             value = Integer.valueOf(distinct.size()); break;
/*     */           } 
/* 318 */           value = Integer.valueOf(0);
/*     */           break;
/*     */         
/*     */         case "First":
/* 322 */           value = this.footers.get(c.getName() + ":first");
/*     */           break;
/*     */         case "Highest":
/* 325 */           value = this.footers.get(c.getName() + ":max");
/*     */           break;
/*     */         case "Lowest":
/* 328 */           value = this.footers.get(c.getName() + ":min");
/*     */           break;
/*     */         case "Sum":
/* 331 */           value = this.footers.get(c.getName() + ":sum");
/*     */           break;
/*     */         case "Average":
/* 334 */           count = ((Double)this.footers.get(c.getName() + ":count")).doubleValue();
/* 335 */           if (count > 0.0D) {
/* 336 */             value = Double.valueOf(((Double)this.footers.get(c.getName() + ":sum")).doubleValue() / count); break;
/*     */           } 
/* 338 */           value = Integer.valueOf(0);
/*     */           break;
/*     */         
/*     */         case "Count":
/* 342 */           value = this.footers.get(c.getName() + ":count");
/*     */           break;
/*     */         case "Variance":
/*     */         case "StandardDeviation":
/* 346 */           sum = ((Double)this.footers.get(c.getName() + ":sum")).doubleValue();
/* 347 */           count2 = ((Double)this.footers.get(c.getName() + ":count")).doubleValue();
/* 348 */           squares = ((Double)this.footers.get(c.getName() + ":squares")).doubleValue();
/* 349 */           if (count2 > 1.0D) {
/* 350 */             value = Double.valueOf((squares - sum * sum / count2) / (count2 - 1.0D));
/* 351 */             if ("StandardDeviation".equals(type))
/* 352 */               value = Double.valueOf(Math.sqrt(((Double)value).doubleValue())); 
/*     */             break;
/*     */           } 
/* 355 */           value = Integer.valueOf(0);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 360 */     if (value != null && !c.getPropertyString("numberFormat").isEmpty()) {
/* 361 */       value = formatNumber(value.toString(), c.getPropertyString("numberFormat"));
/*     */     }
/* 363 */     if (value == null) {
/* 364 */       value = "";
/*     */     }
/* 366 */     return value.toString();
/*     */   }
/*     */   
/*     */   protected void calculateFooter(DataListColumn c, Object value) {
/* 370 */     String type = c.getPropertyString("footer");
/* 371 */     if (!type.isEmpty()) {
/* 372 */       Set<Object> distinct; double max, min, sum, count, squares, num = 0.0D;
/* 373 */       if (!(value instanceof Number)) {
/*     */         try {
/* 375 */           num = Double.parseDouble(value.toString());
/* 376 */         } catch (Exception exception) {}
/*     */       } else {
/* 378 */         num = ((Double)value).doubleValue();
/*     */       } 
/*     */       
/* 381 */       switch (type) {
/*     */         case "DistinctCount":
/* 383 */           distinct = null;
/* 384 */           if (!this.footers.containsKey(c.getName() + ":distinct")) {
/* 385 */             distinct = new HashSet();
/*     */           } else {
/* 387 */             distinct = (HashSet)this.footers.get(c.getName() + ":distinct");
/*     */           } 
/* 389 */           distinct.add(value);
/* 390 */           this.footers.put(c.getName() + ":distinct", distinct);
/*     */           break;
/*     */         case "First":
/* 393 */           if (!this.footers.containsKey(c.getName() + ":first")) {
/* 394 */             this.footers.put(c.getName() + ":first", value);
/*     */           }
/*     */           break;
/*     */         case "Highest":
/* 398 */           max = Double.MIN_VALUE;
/* 399 */           if (this.footers.containsKey(c.getName() + ":max")) {
/* 400 */             max = ((Double)this.footers.get(c.getName() + ":max")).doubleValue();
/*     */           }
/* 402 */           if (num > max) {
/* 403 */             this.footers.put(c.getName() + ":max", Double.valueOf(num));
/*     */           }
/*     */           break;
/*     */         case "Lowest":
/* 407 */           min = Double.MAX_VALUE;
/* 408 */           if (this.footers.containsKey(c.getName() + ":min")) {
/* 409 */             min = ((Double)this.footers.get(c.getName() + ":min")).doubleValue();
/*     */           }
/* 411 */           if (num < min) {
/* 412 */             this.footers.put(c.getName() + ":min", Double.valueOf(num));
/*     */           }
/*     */           break;
/*     */         case "Sum":
/*     */         case "Average":
/*     */         case "Count":
/*     */         case "StandardDeviation":
/*     */         case "Variance":
/* 420 */           sum = 0.0D;
/* 421 */           count = 0.0D;
/* 422 */           squares = 0.0D;
/* 423 */           if (this.footers.containsKey(c.getName() + ":sum")) {
/* 424 */             sum = ((Double)this.footers.get(c.getName() + ":sum")).doubleValue();
/* 425 */             count = ((Double)this.footers.get(c.getName() + ":count")).doubleValue();
/* 426 */             squares = ((Double)this.footers.get(c.getName() + ":squares")).doubleValue();
/*     */           } 
/* 428 */           sum += num;
/* 429 */           count++;
/* 430 */           squares += num * num;
/* 431 */           this.footers.put(c.getName() + ":sum", Double.valueOf(sum));
/* 432 */           this.footers.put(c.getName() + ":count", Double.valueOf(count));
/* 433 */           this.footers.put(c.getName() + ":squares", Double.valueOf(squares));
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object getFormulaValue(Object value, DataListColumn c, Object row) {
/* 441 */     if (!c.getPropertyString("formula").isEmpty()) {
/* 442 */       value = formatValue(c.getPropertyString("formula"), row);
/*     */       
/* 444 */       if (!c.getPropertyString("numberFormat").isEmpty()) {
/* 445 */         value = formatNumber(value.toString(), c.getPropertyString("numberFormat"));
/*     */       }
/*     */     } 
/* 448 */     return value;
/*     */   }
/*     */   
/*     */   protected String formatNumber(String value, String format) {
/* 452 */     if (value == null) {
/* 453 */       value = "0";
/*     */     }
/*     */     try {
/* 456 */       DecimalFormat decimalFormat = new DecimalFormat(format);
/* 457 */       return decimalFormat.format(Double.parseDouble(value.toString()));
/* 458 */     } catch (Exception e) {
/* 459 */       LogUtil.error(getClassName(), e, "");
/*     */       
/* 461 */       return value;
/*     */     } 
/*     */   }
/*     */   protected String formatValue(String format, Object row) {
/* 465 */     Map<Object, Object> variables = new HashMap<>();
/* 466 */     format = prepareExpression(format, row, variables);
/* 467 */     Object result = evaluateExpression(format, variables);
/*     */     
/* 469 */     if (result != null) {
/* 470 */       return result.toString();
/*     */     }
/* 472 */     return "";
/*     */   }
/*     */   
/*     */   protected String prepareExpression(String expr, Object row, Map<Object, Object> variables) {
/* 476 */     Pattern pattern = Pattern.compile("\\{([a-zA-Z0-9_\\.]+)\\}");
/* 477 */     Matcher matcher = pattern.matcher(expr);
/*     */     
/* 479 */     while (matcher.find()) {
/* 480 */       String key = matcher.group(1);
/* 481 */       Object value = DataListService.evaluateColumnValueFromRow(row, key);
/* 482 */       if (value != null) {
/* 483 */         String newKey = key.replaceAll(StringUtil.escapeRegex("."), "__");
/* 484 */         if (value instanceof String) {
/*     */           try {
/* 486 */             value = Double.valueOf(Double.parseDouble(value.toString()));
/* 487 */           } catch (Exception exception) {}
/*     */         }
/* 489 */         variables.put(newKey, value);
/* 490 */         expr = expr.replaceAll(StringUtil.escapeRegex("{" + key + "}"), newKey);
/*     */       } 
/*     */     } 
/*     */     
/* 494 */     return expr;
/*     */   }
/*     */   
/*     */   protected Object evaluateExpression(String expr, Map variables) {
/* 498 */     Context cx = Context.enter();
/* 499 */     Scriptable scope = cx.initStandardObjects(null);
/*     */ 
/*     */     
/*     */     try {
/* 503 */       prepareContext(scope, variables);
/* 504 */       Object eval = cx.evaluateString(scope, expr, "", 1, null);
/* 505 */       return eval;
/* 506 */     } catch (Exception e) {
/* 507 */       LogUtil.error(getClassName(), e, expr);
/*     */     } finally {
/* 509 */       Context.exit();
/*     */     } 
/* 511 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void prepareContext(Scriptable scope, Map variables) throws Exception {
/* 516 */     Iterator<Map.Entry> iter = variables.entrySet().iterator();
/* 517 */     while (iter.hasNext()) {
/* 518 */       Map.Entry me = iter.next();
/* 519 */       String key = me.getKey().toString();
/* 520 */       Object value = me.getValue();
/* 521 */       scope.put(key, scope, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\ListReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */