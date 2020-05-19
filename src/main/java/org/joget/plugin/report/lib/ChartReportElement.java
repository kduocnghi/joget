/*     */ package org.joget.plugin.report.lib;
/*     */ import java.awt.Color;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Base64;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.apps.datalist.model.DataListBinder;
/*     */ import org.joget.apps.datalist.model.DataListCollection;
/*     */ import org.joget.apps.datalist.service.DataListService;
/*     */ import org.joget.commons.util.LogUtil;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.base.PluginManager;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ import org.knowm.xchart.BitmapEncoder;
/*     */ import org.knowm.xchart.BubbleChart;
/*     */ import org.knowm.xchart.BubbleChartBuilder;
/*     */ import org.knowm.xchart.CategoryChart;
/*     */ import org.knowm.xchart.CategoryChartBuilder;
/*     */ import org.knowm.xchart.CategorySeries;
/*     */ import org.knowm.xchart.OHLCChart;
/*     */ import org.knowm.xchart.OHLCChartBuilder;
/*     */ import org.knowm.xchart.OHLCSeries;
/*     */ import org.knowm.xchart.PieChart;
/*     */ import org.knowm.xchart.PieChartBuilder;
/*     */ import org.knowm.xchart.PieSeries;
/*     */ import org.knowm.xchart.XYChart;
/*     */ import org.knowm.xchart.XYChartBuilder;
/*     */ import org.knowm.xchart.XYSeries;
/*     */ import org.knowm.xchart.internal.chartpart.Chart;
/*     */ import org.knowm.xchart.style.BubbleStyler;
/*     */ import org.knowm.xchart.style.CategoryStyler;
/*     */ import org.knowm.xchart.style.OHLCStyler;
/*     */ import org.knowm.xchart.style.PieStyler;
/*     */ import org.knowm.xchart.style.Styler;
/*     */ import org.knowm.xchart.style.XYStyler;
/*     */ 
/*     */ public class ChartReportElement extends ExtDefaultPlugin implements PropertyEditable, ReportElement {
/*  49 */   protected DataListCollection binderdata = null;
/*  50 */   protected int width = 640;
/*  51 */   protected int height = 480;
/*  52 */   protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*     */ 
/*     */   
/*     */   public String getName() {
/*  56 */     return "ChartReportElement";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  61 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  66 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  71 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  76 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  81 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/ChartReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  86 */     return "<i class=\"fas fa-chart-bar\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  91 */     if (!getPropertyString("chartWidth").isEmpty()) {
/*     */       try {
/*  93 */         this.width = Integer.parseInt(getPropertyString("chartWidth"));
/*  94 */       } catch (Exception exception) {}
/*     */     }
/*  96 */     if (!getPropertyString("chartHeight").isEmpty()) {
/*     */       try {
/*  98 */         this.height = Integer.parseInt(getPropertyString("chartHeight"));
/*  99 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 102 */     String image = getChartBase64Image();
/*     */     
/* 104 */     if (image != null) {
/* 105 */       String style = "";
/* 106 */       String margin = "";
/* 107 */       if (!getPropertyString("marginBottom").isEmpty()) {
/* 108 */         margin = margin + "margin-bottom:" + getPropertyString("marginBottom") + ";";
/*     */       }
/* 110 */       return "<div style=\"text-align:" + getPropertyString("halign") + ";" + margin + "\"><img style=\"" + style + "\" src=\"data:image/jpeg;base64," + image + "\" /></div>";
/*     */     } 
/*     */     
/* 113 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/* 123 */     return render();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCSS() {
/* 128 */     return "";
/*     */   }
/*     */   
/*     */   protected String getChartBase64Image() {
/*     */     try {
/* 133 */       if (!getPropertyString("mapping_dateformat").isEmpty()) {
/* 134 */         this.sdf = new SimpleDateFormat(getPropertyString("mapping_dateformat"));
/*     */       }
/*     */ 
/*     */       
/* 138 */       Chart chart = null;
/* 139 */       String type = getPropertyString("chartType");
/* 140 */       switch (type) {
/*     */         case "xy":
/*     */         case "line":
/*     */         case "area":
/*     */         case "bar":
/* 145 */           chart = xyChart();
/*     */           break;
/*     */         case "bubble":
/* 148 */           chart = bubbleChart();
/*     */           break;
/*     */         case "candlestick":
/*     */         case "ohlc":
/* 152 */           chart = ohlcChart();
/*     */           break;
/*     */         case "donut":
/*     */         case "pie":
/* 156 */           chart = pieChart();
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 162 */       if (chart != null) {
/*     */         
/* 164 */         chart.setTitle(getPropertyString("title"));
/* 165 */         chart.getStyler().setChartBackgroundColor(Color.WHITE);
/* 166 */         chart.getStyler().setPlotContentSize(0.8D);
/*     */         
/* 168 */         BufferedImage image = BitmapEncoder.getBufferedImage(chart);
/*     */         
/* 170 */         String imageString = null;
/* 171 */         ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*     */         
/*     */         try {
/* 174 */           ImageIO.write(image, "jpeg", bos);
/* 175 */           byte[] imageBytes = bos.toByteArray();
/*     */           
/* 177 */           byte[] encodedBytes = Base64.getEncoder().encode(imageBytes);
/* 178 */           imageString = new String(encodedBytes, "UTF-8");
/*     */         } finally {
/*     */           
/* 181 */           bos.close();
/*     */         } 
/*     */         
/* 184 */         return imageString;
/*     */       } 
/* 186 */     } catch (Exception e) {
/* 187 */       LogUtil.error(getClassName(), e, "");
/*     */     } 
/*     */     
/* 190 */     return null;
/*     */   }
/*     */   
/*     */   protected DataListCollection getData() {
/* 194 */     if (this.binderdata == null) {
/*     */       
/* 196 */       Object binderData = getProperty("binder");
/* 197 */       if (binderData != null && binderData instanceof Map) {
/* 198 */         Map bdMap = (Map)binderData;
/* 199 */         if (bdMap != null && bdMap.containsKey("className") && !bdMap.get("className").toString().isEmpty()) {
/* 200 */           PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
/* 201 */           DataListBinder binder = (DataListBinder)pluginManager.getPlugin(bdMap.get("className").toString());
/*     */           
/* 203 */           if (binder != null) {
/* 204 */             Map bdProps = (Map)bdMap.get("properties");
/* 205 */             binder.setProperties(bdProps);
/*     */             
/* 207 */             String sort = null;
/* 208 */             Boolean order = null;
/* 209 */             if (!getPropertyString("sortingOrderByColumn").isEmpty()) {
/* 210 */               sort = getPropertyString("sortingOrderByColumn");
/* 211 */               order = Boolean.valueOf("DESC".equals(getPropertyString("sortingOrder")));
/*     */             } 
/*     */             
/* 214 */             this.binderdata = binder.getData(null, bdProps, new org.joget.apps.datalist.model.DataListFilterQueryObject[0], sort, order, null, null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 219 */     return this.binderdata;
/*     */   }
/*     */   protected Chart xyChart() {
/* 222 */     XYChart xYChart = null;
/* 223 */     List<Object> xData = new ArrayList();
/* 224 */     Map<String, List<Number>> data = new HashMap<>();
/*     */     
/* 226 */     Object[] mapping_values = (Object[])getProperty("mapping_ys");
/* 227 */     for (Object mapping_value : mapping_values) {
/* 228 */       Map mapping = (HashMap)mapping_value;
/* 229 */       String name = mapping.get("y").toString();
/* 230 */       data.put(name, new ArrayList<Number>());
/*     */     } 
/*     */     
/* 233 */     for (Object r : getData()) {
/* 234 */       if (getPropertyString("xAxisDisplayAS").isEmpty()) {
/* 235 */         xData.add(getValue(r, getPropertyString("mapping_x")));
/* 236 */       } else if ("number".equals(getPropertyString("xAxisDisplayAS"))) {
/* 237 */         xData.add(getNumber(r, getPropertyString("mapping_x")));
/* 238 */       } else if ("date".equals(getPropertyString("xAxisDisplayAS"))) {
/* 239 */         xData.add(getDate(r, getPropertyString("mapping_x")));
/*     */       } 
/*     */       
/* 242 */       for (String key : data.keySet()) {
/* 243 */         Collection<Number> y = data.get(key);
/* 244 */         y.add(getNumber(r, key));
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     Chart chart = null;
/* 249 */     if (getPropertyString("xAxisDisplayAS").isEmpty() || "bar".equalsIgnoreCase(getPropertyString("chartType"))) {
/* 250 */       CategoryChart categoryChart = ((CategoryChartBuilder)((CategoryChartBuilder)(new CategoryChartBuilder()).width(this.width)).height(this.height)).xAxisTitle(getPropertyString("categoryAxisLabel")).yAxisTitle(getPropertyString("valueAxisLabel")).build();
/* 251 */       if ("area".equalsIgnoreCase(getPropertyString("chartType"))) {
/* 252 */         ((CategoryStyler)categoryChart.getStyler()).setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Area);
/* 253 */       } else if ("xy".equalsIgnoreCase(getPropertyString("chartType"))) {
/* 254 */         ((CategoryStyler)categoryChart.getStyler()).setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
/*     */       } 
/*     */       
/* 257 */       for (Object mapping_value : mapping_values) {
/* 258 */         Map mapping = (HashMap)mapping_value;
/* 259 */         String name = mapping.get("y").toString();
/*     */         
/* 261 */         categoryChart.addSeries(mapping.get("legend").toString(), xData, data.get(name));
/*     */       } 
/*     */       
/* 264 */       if ("true".equalsIgnoreCase(getPropertyString("stack"))) {
/* 265 */         ((CategoryStyler)categoryChart.getStyler()).setStacked(true);
/*     */       }
/*     */       
/* 268 */       if ("true".equalsIgnoreCase(getPropertyString("showValueLabel"))) {
/* 269 */         ((CategoryStyler)categoryChart.getStyler()).setHasAnnotations(true);
/*     */       }
/*     */       
/* 272 */       if (!getPropertyString("dateFormat").isEmpty()) {
/* 273 */         ((CategoryStyler)categoryChart.getStyler()).setDatePattern(getPropertyString("dateFormat"));
/*     */       }
/*     */     } else {
/* 276 */       xYChart = ((XYChartBuilder)((XYChartBuilder)(new XYChartBuilder()).width(this.width)).height(this.height)).xAxisTitle(getPropertyString("categoryAxisLabel")).yAxisTitle(getPropertyString("valueAxisLabel")).build();
/* 277 */       if ("area".equalsIgnoreCase(getPropertyString("chartType"))) {
/* 278 */         ((XYStyler)xYChart.getStyler()).setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
/*     */       }
/*     */       
/* 281 */       for (Object mapping_value : mapping_values) {
/* 282 */         Map mapping = (HashMap)mapping_value;
/* 283 */         String name = mapping.get("y").toString();
/*     */         
/* 285 */         xYChart.addSeries(mapping.get("legend").toString(), xData, data.get(name));
/*     */       } 
/*     */       
/* 288 */       if ("true".equalsIgnoreCase(getPropertyString("showValueLabel"))) {
/* 289 */         ((XYStyler)xYChart.getStyler()).setToolTipType(Styler.ToolTipType.yLabels);
/*     */       }
/*     */       
/* 292 */       if (!getPropertyString("dateFormat").isEmpty()) {
/* 293 */         ((XYStyler)xYChart.getStyler()).setDatePattern(getPropertyString("dateFormat"));
/*     */       }
/*     */     } 
/*     */     
/* 297 */     ((XYStyler)xYChart.getStyler()).setLegendPosition(Styler.LegendPosition.OutsideE);
/*     */     
/* 299 */     Color[] colors = getColors();
/* 300 */     if (colors != null) {
/* 301 */       ((XYStyler)xYChart.getStyler()).setSeriesColors(colors);
/*     */     }
/* 303 */     if (!"true".equalsIgnoreCase(getPropertyString("showLegend"))) {
/* 304 */       ((XYStyler)xYChart.getStyler()).setLegendVisible(false);
/*     */     }
/*     */     
/* 307 */     return (Chart)xYChart;
/*     */   }
/*     */   
/*     */   protected Chart bubbleChart() {
/* 311 */     BubbleChart chart = ((BubbleChartBuilder)((BubbleChartBuilder)(new BubbleChartBuilder()).width(this.width)).height(this.height)).xAxisTitle(getPropertyString("categoryAxisLabel")).yAxisTitle(getPropertyString("valueAxisLabel")).build();
/*     */     
/* 313 */     for (Object r : getData()) {
/* 314 */       List<Number> x = new ArrayList<>();
/* 315 */       x.add(getNumber(r, getPropertyString("mapping_x")));
/* 316 */       List<Number> y = new ArrayList<>();
/* 317 */       y.add(getNumber(r, getPropertyString("mapping_y")));
/* 318 */       List<Number> ra = new ArrayList<>();
/* 319 */       ra.add(getNumber(r, getPropertyString("mapping_r")));
/* 320 */       chart.addSeries(getValue(r, getPropertyString("mapping_label")), x, y, ra);
/*     */     } 
/*     */     
/* 323 */     Color[] colors = getColors();
/* 324 */     if (colors != null) {
/* 325 */       ((BubbleStyler)chart.getStyler()).setSeriesColors(colors);
/*     */     }
/* 327 */     if (!"true".equalsIgnoreCase(getPropertyString("showLegend"))) {
/* 328 */       ((BubbleStyler)chart.getStyler()).setLegendVisible(false);
/*     */     }
/*     */     
/* 331 */     return (Chart)chart;
/*     */   }
/*     */   
/*     */   protected Chart ohlcChart() {
/* 335 */     OHLCChart chart = ((OHLCChartBuilder)((OHLCChartBuilder)(new OHLCChartBuilder()).width(this.width)).height(this.height)).build();
/* 336 */     ((OHLCStyler)chart.getStyler()).setLegendVisible(false);
/* 337 */     ((OHLCStyler)chart.getStyler()).setToolTipsEnabled(true);
/*     */     
/* 339 */     List<Date> date = new ArrayList<>();
/* 340 */     List<Number> open = new ArrayList<>();
/* 341 */     List<Number> high = new ArrayList<>();
/* 342 */     List<Number> low = new ArrayList<>();
/* 343 */     List<Number> close = new ArrayList<>();
/*     */     
/* 345 */     for (Object r : getData()) {
/* 346 */       date.add(getDate(r, getPropertyString("mapping_date")));
/* 347 */       open.add(getNumber(r, getPropertyString("mapping_open")));
/* 348 */       high.add(getNumber(r, getPropertyString("mapping_high")));
/* 349 */       low.add(getNumber(r, getPropertyString("mapping_low")));
/* 350 */       close.add(getNumber(r, getPropertyString("mapping_close")));
/*     */     } 
/*     */     
/* 353 */     if ("candlestick".equalsIgnoreCase(getPropertyString("chartType"))) {
/* 354 */       chart.addSeries("data", date, open, high, low, close)
/* 355 */         .setUpColor(Color.RED)
/* 356 */         .setDownColor(Color.GREEN);
/*     */     } else {
/* 358 */       chart.addSeries("data", date, open, high, low, close)
/* 359 */         .setOhlcSeriesRenderStyle(OHLCSeries.OHLCSeriesRenderStyle.HiLo);
/*     */     } 
/*     */     
/* 362 */     if (!getPropertyString("dateFormat").isEmpty()) {
/* 363 */       ((OHLCStyler)chart.getStyler()).setDatePattern(getPropertyString("dateFormat"));
/*     */     }
/*     */     
/* 366 */     return (Chart)chart;
/*     */   }
/*     */   
/*     */   protected Chart pieChart() {
/* 370 */     PieChart chart = ((PieChartBuilder)((PieChartBuilder)(new PieChartBuilder()).width(this.width)).height(this.height)).build();
/*     */     
/* 372 */     for (Object r : getData()) {
/* 373 */       chart.addSeries(getValue(r, getPropertyString("mapping_legend")), getNumber(r, getPropertyString("mapping_value")));
/*     */     }
/*     */     
/* 376 */     if ("donut".equalsIgnoreCase(getPropertyString("chartType"))) {
/* 377 */       ((PieStyler)chart.getStyler()).setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut);
/*     */     }
/*     */     
/* 380 */     if (!"true".equalsIgnoreCase(getPropertyString("showLegend"))) {
/* 381 */       ((PieStyler)chart.getStyler()).setLegendVisible(false);
/*     */     }
/* 383 */     if ("true".equalsIgnoreCase(getPropertyString("showValue"))) {
/* 384 */       ((PieStyler)chart.getStyler()).setAnnotationType(PieStyler.AnnotationType.Value);
/*     */     }
/* 386 */     ((PieStyler)chart.getStyler()).setPlotBorderVisible(false);
/*     */     
/* 388 */     Color[] colors = getColors();
/* 389 */     if (colors != null) {
/* 390 */       ((PieStyler)chart.getStyler()).setSeriesColors(colors);
/*     */     }
/*     */     
/* 393 */     return (Chart)chart;
/*     */   }
/*     */   
/*     */   protected String getValue(Object o, String name) {
/* 397 */     String value = "";
/*     */     
/*     */     try {
/* 400 */       Object v = DataListService.evaluateColumnValueFromRow(o, name);
/* 401 */       if (v != null) {
/* 402 */         return v.toString();
/*     */       }
/* 404 */     } catch (Exception e) {
/* 405 */       LogUtil.error(getClassName(), e, name);
/*     */     } 
/*     */     
/* 408 */     return value;
/*     */   }
/*     */   
/*     */   protected Number getNumber(Object o, String name) {
/* 412 */     double value = 0.0D;
/*     */     
/*     */     try {
/* 415 */       Object v = DataListService.evaluateColumnValueFromRow(o, name);
/* 416 */       if (v instanceof Number) {
/* 417 */         return (Number)v;
/*     */       }
/* 419 */       value = Double.parseDouble(v.toString());
/*     */     }
/* 421 */     catch (Exception e) {
/* 422 */       LogUtil.error(getClassName(), e, name);
/*     */     } 
/*     */     
/* 425 */     return Double.valueOf(value);
/*     */   }
/*     */   
/*     */   protected Date getDate(Object o, String name) {
/*     */     try {
/* 430 */       Object v = DataListService.evaluateColumnValueFromRow(o, name);
/* 431 */       if (v instanceof Date) {
/* 432 */         return (Date)v;
/*     */       }
/* 434 */       return this.sdf.parse(v.toString());
/*     */     }
/* 436 */     catch (Exception e) {
/* 437 */       LogUtil.error(getClassName(), e, name);
/*     */       
/* 439 */       return null;
/*     */     } 
/*     */   }
/*     */   protected boolean check(String value, String[] list) {
/* 443 */     List<String> valueList = new ArrayList<>();
/* 444 */     valueList.addAll(Arrays.asList(list));
/* 445 */     return valueList.contains(value);
/*     */   }
/*     */   
/*     */   protected String convertDateFormat(String format) {
/* 449 */     format = format.replaceAll("yyyy", "%Y");
/* 450 */     format = format.replaceAll("yy", "%y");
/* 451 */     format = format.replaceAll("MMMMM", "%B");
/* 452 */     format = format.replaceAll("MMM", "%b");
/* 453 */     format = format.replaceAll("MM", "%m");
/* 454 */     format = format.replaceAll("M", "%#m");
/* 455 */     format = format.replaceAll("dd", "%d");
/* 456 */     format = format.replaceAll("HH", "%H");
/* 457 */     format = format.replaceAll("kk", "%H");
/* 458 */     format = format.replaceAll("KK", "%I");
/* 459 */     format = format.replaceAll("hh", "%I");
/* 460 */     format = format.replaceAll("a", "%p");
/* 461 */     format = format.replaceAll("mm", "%M");
/* 462 */     format = format.replaceAll("ss", "%S");
/* 463 */     return format;
/*     */   }
/*     */   
/*     */   protected Color[] getColors() {
/* 467 */     Collection<Color> colors = new ArrayList<>();
/*     */     
/* 469 */     if (!getPropertyString("colors").isEmpty()) {
/* 470 */       String[] colorCodes = getPropertyString("colors").split(",");
/* 471 */       for (String c : colorCodes) {
/*     */         try {
/* 473 */           colors.add(Color.decode(c.trim()));
/* 474 */         } catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 480 */     if (!colors.isEmpty()) {
/* 481 */       return colors.<Color>toArray(new Color[0]);
/*     */     }
/* 483 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\ChartReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */