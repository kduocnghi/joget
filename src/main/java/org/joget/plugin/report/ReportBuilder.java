/*     */ package org.joget.plugin.report;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.joget.apps.app.dao.BuilderDefinitionDao;
/*     */ import org.joget.apps.app.model.AppDefinition;
/*     */ import org.joget.apps.app.model.BuilderDefinition;
/*     */ import org.joget.apps.app.model.CustomBuilder;
/*     */ import org.joget.apps.app.model.CustomBuilderAbstract;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.apps.app.service.CustomBuilderUtil;
/*     */ import org.joget.apps.form.service.FormPdfUtil;
/*     */ import org.joget.commons.util.LogUtil;
/*     */ import org.joget.directory.model.User;
/*     */ import org.joget.plugin.base.Plugin;
/*     */ import org.joget.plugin.base.PluginManager;
/*     */ import org.joget.plugin.base.PluginWebSupport;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.property.service.PropertyUtil;
/*     */ import org.joget.plugin.report.api.ReportContainer;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ import org.joget.workflow.model.service.WorkflowUserManager;
/*     */ import org.joget.workflow.util.WorkflowUtil;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xhtmlrenderer.resource.FSEntityResolver;
/*     */ import org.xhtmlrenderer.swing.Java2DRenderer;
/*     */ import org.xml.sax.EntityResolver;
/*     */ 
/*     */ public class ReportBuilder
/*     */   extends CustomBuilderAbstract
/*     */   implements CustomBuilder, PluginWebSupport {
/*     */   public static final String BUILDER_MESSAGE_PATH = "messages/ReportBuilder";
/*     */   public static final String MESSAGE_PATH = "messages/ReportElements";
/*     */   public static final String IS_PREVIEW = "IS_PREVIEW";
/*     */   public static final String REPORT_PARAMS = "REPORT_PARAMS";
/*     */   public static final String REPORT_OUTPUT = "REPORT_OUTPUT";
/*     */   public static final String REPORT_OUTPUT_HTML = "HTML";
/*     */   public static final String REPORT_OUTPUT_HTML_FULL = "HTML_WITH_HEADER_FOOTER";
/*  53 */   protected static ThreadLocal reportParameters = new ThreadLocal();
/*     */ 
/*     */   
/*     */   public String getName() {
/*  57 */     return "reportBuilder";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  62 */     return "report";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  67 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  72 */     return AppPluginUtil.getMessage("reportBuilder.description", getClassName(), getResourceBundlePath());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  77 */     return "far fa-file-pdf";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColor() {
/*  82 */     return "#dd2c00";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  87 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  92 */     return AppPluginUtil.getMessage("reportBuilder.label", getClassName(), getResourceBundlePath());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getObjectLabel() {
/*  97 */     return AppPluginUtil.getMessage("reportBuilder.report", getClassName(), getResourceBundlePath());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIdPrefix() {
/* 102 */     return "rp_";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/* 107 */     return AppUtil.readPluginResource(getClassName(), "/properties/setting.json", null, true, getResourceBundlePath());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBuilderConfig() {
/* 112 */     return AppUtil.readPluginResource(getClassName(), "/properties/report_builder.json", null, false, getResourceBundlePath());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getResourceBundlePath() {
/* 117 */     return "messages/ReportBuilder";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBuilderJS(String contextPath, String buildNumber) {
/* 122 */     return "<script type=\"text/javascript\" src=\"" + contextPath + "/plugin/org.joget.plugin.report.ReportBuilder/reportbuilder.js?build=" + getVersion() + buildNumber + "\"></script>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBuilderCSS(String contextPath, String buildNumber) {
/* 127 */     String css = "<link href=\"" + contextPath + "/plugin/org.joget.plugin.report.ReportBuilder/reportbuilder.css?build=" + getVersion() + buildNumber + "\" rel=\"stylesheet\" type=\"text/css\" />";
/* 128 */     return css;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBuilderHTML(BuilderDefinition def, String json, HttpServletRequest request, HttpServletResponse response) {
/* 133 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public void builderPreview(String json, HttpServletRequest request, HttpServletResponse response) {
/* 138 */     Map<String, Object> config = new HashMap<>();
/* 139 */     config.put("IS_PREVIEW", Boolean.valueOf(true));
/*     */     
/* 141 */     if ("pdf".equalsIgnoreCase(request.getParameter("outputtype"))) {
/* 142 */       byte[] pdf = (byte[])getBuilderResult(json, config);
/*     */       
/*     */       try {
/* 145 */         response.setHeader("Content-Type", "application/pdf");
/* 146 */         response.setHeader("Content-Disposition", "inline; filename=preview.pdf; filename*=UTF-8''preview.pdf");
/*     */         
/* 148 */         response.getOutputStream().write(pdf);
/* 149 */       } catch (IOException ex) {
/* 150 */         LogUtil.error(getClassName(), ex, null);
/*     */       } 
/*     */     } else {
/* 153 */       config.put("REPORT_OUTPUT", "HTML_WITH_HEADER_FOOTER");
/* 154 */       String html = (String)getBuilderResult(json, config);
/*     */       try {
/* 156 */         response.setContentType("text/html; charset=utf-8");
/* 157 */         response.getWriter().write(html);
/* 158 */       } catch (IOException ex) {
/* 159 */         LogUtil.error(getClassName(), ex, null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 166 */     boolean isAdmin = WorkflowUtil.isCurrentUserInRole("ROLE_ADMIN");
/* 167 */     if (!isAdmin) {
/* 168 */       response.sendError(401);
/*     */       
/*     */       return;
/*     */     } 
/* 172 */     String action = request.getParameter("_action");
/* 173 */     if ("previewElement".equals(action)) {
/* 174 */       String json = request.getParameter("_json");
/* 175 */       String id = request.getParameter("_id");
/* 176 */       AppDefinition appDef = AppUtil.getCurrentAppDefinition();
/*     */       
/* 178 */       String tempJson = json;
/* 179 */       if (tempJson.contains("%%%%") || tempJson.contains("****SECURE_VALUE****-")) {
/* 180 */         BuilderDefinitionDao dao = (BuilderDefinitionDao)AppUtil.getApplicationContext().getBean("builderDefinitionDao");
/* 181 */         BuilderDefinition def = dao.loadById(id, appDef);
/*     */         
/* 183 */         if (def != null) {
/* 184 */           tempJson = PropertyUtil.propertiesJsonStoreProcessing(def.getJson(), tempJson);
/*     */         }
/*     */       } 
/*     */       
/* 188 */       tempJson = AppUtil.processHashVariable(json, null, null, null);
/*     */       
/* 190 */       String html = "";
/*     */       try {
/* 192 */         JSONObject obj = new JSONObject(tempJson);
/* 193 */         if (obj.has("className")) {
/* 194 */           PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
/* 195 */           Plugin plugin = pluginManager.getPlugin(obj.getString("className"));
/* 196 */           if (plugin != null && plugin instanceof ReportElement && plugin instanceof PropertyEditable) {
/* 197 */             ((PropertyEditable)plugin).setProperties(PropertyUtil.getPropertiesValueFromJson(obj.getJSONObject("properties").toString()));
/* 198 */             ((PropertyEditable)plugin).setProperty("IS_PREVIEW", Boolean.valueOf(true));
/* 199 */             html = ((ReportElement)plugin).preview();
/*     */           } 
/*     */         } 
/* 202 */       } catch (Exception ex) {
/* 203 */         LogUtil.error(getClass().getName(), ex, "preview element error!");
/*     */       } 
/* 205 */       response.getWriter().write(html);
/*     */     } else {
/*     */       try {
/* 208 */         JSONArray jsonArray = new JSONArray();
/*     */         
/* 210 */         for (ReportElement e : getAvailableElements()) {
/* 211 */           Map<String, String> option = new HashMap<>();
/* 212 */           option.put("className", e.getClassName());
/* 213 */           option.put("icon", e.getIcon());
/* 214 */           option.put("label", e.getLabel());
/* 215 */           option.put("propertyOptions", e.getPropertyOptions());
/* 216 */           if (e.getPropertyOptions() != null && !e.getPropertyOptions().isEmpty()) {
/* 217 */             option.put("defaultProperties", PropertyUtil.getDefaultPropertyValues(option.get("propertyOptions")));
/*     */           } else {
/* 219 */             option.put("defaultProperties", "{}");
/*     */           } 
/* 221 */           option.put("isContainer", Boolean.toString(e instanceof ReportContainer));
/* 222 */           if (e instanceof ReportContainer) {
/* 223 */             option.put("builderScript", ((ReportContainer)e).builderScript());
/*     */           }
/* 225 */           option.put("isHidden", Boolean.toString(e instanceof org.joget.plugin.base.HiddenPlugin));
/* 226 */           option.put("supportContainer", Boolean.toString(e.supportReportContainer()));
/* 227 */           jsonArray.put(option);
/*     */         } 
/*     */         
/* 230 */         jsonArray.write(response.getWriter());
/* 231 */       } catch (Exception ex) {
/* 232 */         LogUtil.error(getClass().getName(), ex, "Get available report element plugin error!");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getBuilderResult(String json, Map<String, Object> config) {
/* 239 */     setReportParameters((Map<String, String>)config.get("REPORT_PARAMS"));
/* 240 */     HttpServletRequest request = WorkflowUtil.getHttpServletRequest();
/* 241 */     boolean isPreview = false;
/* 242 */     if (config.containsKey("IS_PREVIEW")) {
/* 243 */       isPreview = ((Boolean)config.get("IS_PREVIEW")).booleanValue();
/*     */     }
/*     */     
/* 246 */     json = AppUtil.processHashVariable(json, null, "json", null);
/* 247 */     json = AppUtil.replaceAppMessages(json, "json");
/*     */     
/* 249 */     WorkflowUserManager workflowUserManager = (WorkflowUserManager)AppUtil.getApplicationContext().getBean("workflowUserManager");
/* 250 */     User currentUser = workflowUserManager.getCurrentUser();
/*     */     
/*     */     try {
/* 253 */       JSONObject obj = new JSONObject(json);
/* 254 */       String permissionKey = "default";
/* 255 */       boolean hasPermission = false;
/*     */       
/* 257 */       JSONObject prop = obj.getJSONObject("properties");
/*     */       
/* 259 */       String pageWidth = prop.getString("pageWidth");
/* 260 */       String pageHeight = prop.getString("pageHeight");
/* 261 */       String top = prop.getString("topMargin");
/* 262 */       String bottom = prop.getString("bottomMargin");
/* 263 */       String left = prop.getString("leftMargin");
/* 264 */       String right = prop.getString("rightMargin");
/* 265 */       String orientation = prop.getString("orientation");
/* 266 */       if (!"portrait".equals(orientation)) {
/* 267 */         pageWidth = prop.getString("pageHeight");
/* 268 */         pageHeight = prop.getString("pageWidth");
/*     */       } 
/* 270 */       String bgImg = "";
/* 271 */       if (prop.has("background_image")) {
/* 272 */         bgImg = prop.getString("background_image");
/*     */       }
/* 274 */       String bgColor = "";
/* 275 */       if (prop.has("background_color")) {
/* 276 */         bgColor = prop.getString("background_color");
/*     */       }
/* 278 */       String bgType = "";
/* 279 */       if (prop.has("background_color")) {
/* 280 */         bgType = prop.getString("background_type");
/*     */       }
/*     */       
/* 283 */       String body = "";
/* 284 */       String header = "";
/* 285 */       String footer = "";
/* 286 */       String backgroundCss = "";
/* 287 */       if (!bgColor.isEmpty()) {
/* 288 */         backgroundCss = backgroundCss + "background-color:" + bgColor + ";";
/*     */       } else {
/* 290 */         backgroundCss = backgroundCss + "background-color:#fff;";
/*     */       } 
/* 292 */       if (!bgImg.isEmpty()) {
/* 293 */         if (bgImg.startsWith("/")) {
/* 294 */           bgImg = getURL(request, bgImg);
/*     */         }
/* 296 */         backgroundCss = backgroundCss + "background-image:url('" + bgImg + "');";
/*     */       } 
/* 298 */       if ("cover".equals(bgType)) {
/* 299 */         backgroundCss = backgroundCss + "background-size:cover;background-position:center;";
/* 300 */       } else if ("contain".equals(bgType)) {
/* 301 */         backgroundCss = backgroundCss + "background-size:contain;background-position:center;background-repeat:no-repeat;";
/* 302 */       } else if ("repeat".equals(bgType)) {
/* 303 */         backgroundCss = backgroundCss + "background-repeat:repeat;";
/*     */       } 
/*     */       
/* 306 */       String css = "@page {" + backgroundCss + "  size: " + getPixel(pageWidth) + "px " + getPixel(pageHeight) + "px; margin: " + getPixel(top) + "px  " + getPixel(right) + "px  " + getPixel(bottom) + "px  " + getPixel(left) + "px;}";
/* 307 */       css = css + ".border-table, .border-table td, .border-table th, .border-table tr {border: 1px solid #000;}";
/* 308 */       css = css + "thead {display: table-header-group;}";
/* 309 */       css = css + ".currentpage:before {content: counter(page);}";
/* 310 */       css = css + ".pagenumber:before {content: counter(pages);}";
/*     */       
/* 312 */       config.put("elementCss", "");
/*     */       
/* 314 */       if (!isPreview) {
/* 315 */         if (prop.has("permission_rules")) {
/* 316 */           JSONArray permissionRules = prop.getJSONArray("permission_rules");
/* 317 */           if (permissionRules != null && permissionRules.length() > 0) {
/* 318 */             for (int i = 0; i < permissionRules.length(); i++) {
/* 319 */               JSONObject rule = permissionRules.getJSONObject(i);
/* 320 */               if (rule.has("permission")) {
/* 321 */                 JSONObject permissionObj = rule.optJSONObject("permission");
/* 322 */                 hasPermission = CustomBuilderUtil.getPermisionResult(permissionObj.toString(), request.getParameterMap(), currentUser);
/* 323 */                 if (hasPermission) {
/* 324 */                   permissionKey = rule.getString("permission_key");
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/* 332 */         if (!hasPermission) {
/* 333 */           if (prop.has("permission")) {
/* 334 */             JSONObject permissionObj = prop.getJSONObject("permission");
/* 335 */             hasPermission = CustomBuilderUtil.getPermisionResult(permissionObj.toString(), request.getParameterMap(), currentUser);
/*     */           } else {
/* 337 */             hasPermission = true;
/*     */           } 
/*     */         }
/*     */       } else {
/* 341 */         hasPermission = true;
/*     */       } 
/*     */       
/* 344 */       if (hasPermission) {
/* 345 */         if ("HTML".equalsIgnoreCase((String)config.get("REPORT_OUTPUT"))) {
/* 346 */           if (obj.has("body")) {
/* 347 */             body = renderPart(obj.getJSONObject("body"), permissionKey, request.getParameterMap(), currentUser, isPreview, config);
/*     */           }
/* 349 */         } else if ("HTML_WITH_HEADER_FOOTER".equalsIgnoreCase((String)config.get("REPORT_OUTPUT"))) {
/* 350 */           if (obj.has("header")) {
/* 351 */             body = body + renderPart(obj.getJSONObject("header"), permissionKey, request.getParameterMap(), currentUser, isPreview, config);
/*     */           }
/* 353 */           if (obj.has("body")) {
/* 354 */             body = body + renderPart(obj.getJSONObject("body"), permissionKey, request.getParameterMap(), currentUser, isPreview, config);
/*     */           }
/* 356 */           if (obj.has("footer")) {
/* 357 */             body = body + renderPart(obj.getJSONObject("footer"), permissionKey, request.getParameterMap(), currentUser, isPreview, config);
/*     */           }
/*     */           
/* 360 */           String pageCss = ".border-table, .border-table td, .border-table th, .border-table tr {border: 1px solid #000;}";
/* 361 */           pageCss = pageCss + ".report_page_body{" + backgroundCss + "background-image:none;padding: " + getPixel(top) + "px  " + getPixel(right) + "px  " + getPixel(bottom) + "px  " + getPixel(left) + "px; min-height:" + getPixel(pageHeight) + "px;}";
/*     */           
/* 363 */           config.put("elementCss", pageCss + config.get("elementCss"));
/*     */           
/* 365 */           body = "<div class=\"report_page_body\">" + body + "</div>";
/*     */         } else {
/* 367 */           if (obj.has("header")) {
/* 368 */             header = renderPart(obj.getJSONObject("header"), permissionKey, request.getParameterMap(), currentUser, isPreview, config);
/* 369 */             if (header != null && !header.isEmpty()) {
/* 370 */               int height = getRenderPartHeight(header, config.get("elementCss").toString(), pageWidth);
/* 371 */               css = css + "@page {padding-top: " + height + "px;}";
/* 372 */               css = css + "div.header {padding-top: " + getPixel(top) + "px; padding-left:9px; padding-right:9px;}";
/*     */             } 
/*     */           } 
/* 375 */           if (obj.has("body")) {
/* 376 */             body = renderPart(obj.getJSONObject("body"), permissionKey, request.getParameterMap(), currentUser, isPreview, config);
/*     */           }
/* 378 */           if (obj.has("footer")) {
/* 379 */             footer = renderPart(obj.getJSONObject("footer"), permissionKey, request.getParameterMap(), currentUser, isPreview, config);
/* 380 */             if (footer != null && !footer.isEmpty()) {
/* 381 */               int height = getRenderPartHeight(footer, config.get("elementCss").toString(), pageWidth);
/* 382 */               css = css + "@page {padding-bottom: " + height + "px;}";
/* 383 */               css = css + "div.footer {padding-bottom: " + getPixel(bottom) + "px; padding-left:9px; padding-right:9px;}";
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 389 */       if ("HTML".equalsIgnoreCase((String)config.get("REPORT_OUTPUT")) || "HTML_WITH_HEADER_FOOTER"
/* 390 */         .equalsIgnoreCase((String)config.get("REPORT_OUTPUT"))) {
/* 391 */         return "<style>" + config.get("elementCss") + "</style>" + body;
/*     */       }
/* 393 */       return FormPdfUtil.createPdf(body, header, footer, css + config.get("elementCss"), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false));
/*     */     }
/* 395 */     catch (Exception e) {
/* 396 */       LogUtil.error(getClassName(), e, "");
/*     */     } finally {
/* 398 */       cleanReportParameters();
/*     */     } 
/*     */     
/* 401 */     return null;
/*     */   }
/*     */   
/*     */   protected String renderPart(JSONObject element, String permissionKey, Map requestParams, User currentUser, boolean isPreview, Map<String, Object> config) throws JSONException {
/* 405 */     String result = "";
/*     */     
/* 407 */     JSONArray elementsArray = element.getJSONArray("elements");
/* 408 */     for (int i = 0; i < elementsArray.length(); i++) {
/* 409 */       JSONObject elementObj = (JSONObject)elementsArray.get(i);
/* 410 */       ReportElement reportElement = getReportElement(elementObj, permissionKey, requestParams, currentUser, isPreview);
/* 411 */       if (reportElement != null) {
/* 412 */         result = result + reportElement.render();
/* 413 */         String css = reportElement.getCSS();
/* 414 */         if (css != null && !css.isEmpty()) {
/* 415 */           config.put("elementCss", (new StringBuilder()).append(config.get("elementCss")).append(css).toString());
/*     */         }
/*     */       } 
/*     */     } 
/* 419 */     return result;
/*     */   }
/*     */   
/*     */   protected ReportElement getReportElement(JSONObject element, String permissionKey, Map requestParams, User currentUser, boolean isPreview) {
/* 423 */     if (element.has("className")) {
/*     */       try {
/* 425 */         PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
/* 426 */         Plugin plugin = pluginManager.getPlugin(element.getString("className"));
/* 427 */         if (plugin != null && plugin instanceof ReportElement && plugin instanceof PropertyEditable) {
/* 428 */           boolean visible = true;
/*     */           
/* 430 */           if (!isPreview) {
/*     */             
/* 432 */             JSONObject ruleObj = null;
/* 433 */             if ("default".equals(permissionKey)) {
/* 434 */               ruleObj = element.getJSONObject("properties");
/* 435 */             } else if (element.getJSONObject("properties").has("permission_rules")) {
/* 436 */               JSONObject permissionRules = element.getJSONObject("properties").getJSONObject("permission_rules");
/* 437 */               if (permissionRules != null && permissionRules.has(permissionKey)) {
/* 438 */                 ruleObj = permissionRules.getJSONObject(permissionKey);
/*     */               }
/*     */             } 
/*     */             
/* 442 */             if (ruleObj != null && ruleObj.has("hidden") && "true".equalsIgnoreCase(ruleObj.getString("hidden"))) {
/* 443 */               visible = false;
/*     */             }
/*     */           } 
/*     */           
/* 447 */           if (visible) {
/* 448 */             ((PropertyEditable)plugin).setProperties(PropertyUtil.getPropertiesValueFromJson(element.getJSONObject("properties").toString()));
/* 449 */             ((PropertyEditable)plugin).setProperty("IS_PREVIEW", Boolean.valueOf(isPreview));
/*     */ 
/*     */             
/* 452 */             if (plugin instanceof ReportContainer && element.has("elements")) {
/* 453 */               Collection<ReportElement> childs = new ArrayList<>();
/* 454 */               JSONArray childsArr = element.getJSONArray("elements");
/* 455 */               for (int i = 0; i < childsArr.length(); i++) {
/* 456 */                 JSONObject childObj = (JSONObject)childsArr.get(i);
/* 457 */                 ReportElement c = getReportElement(childObj, permissionKey, requestParams, currentUser, isPreview);
/* 458 */                 if (c != null) {
/* 459 */                   childs.add(c);
/*     */                 }
/*     */               } 
/* 462 */               ((ReportContainer)plugin).setChildren(childs);
/*     */             } 
/* 464 */             return (ReportElement)plugin;
/*     */           } 
/*     */         } 
/* 467 */       } catch (Exception e) {
/* 468 */         LogUtil.error(getClassName(), e, "");
/*     */       } 
/*     */     }
/* 471 */     return null;
/*     */   }
/*     */   
/*     */   public static void setReportParameters(Map<String, String> params) {
/* 475 */     LinkedList<Map<String, String>> stack = (LinkedList<Map<String, String>>) reportParameters.get();
/* 476 */     if (stack == null) {
/* 477 */       stack = new LinkedList<>();
/*     */     }
/* 479 */     if (params == null) {
/* 480 */       params = new HashMap<>();
/*     */     }
/* 482 */     stack.push(params);
/*     */     
/* 484 */     reportParameters.set(stack);
/*     */   }
/*     */   
/*     */   public static Map<String, String> getReportParameters() {
/* 488 */     LinkedList<Map<String, String>> stack = (LinkedList<Map<String, String>>) reportParameters.get();
/* 489 */     if (stack != null && !stack.isEmpty()) {
/* 490 */       return stack.getFirst();
/*     */     }
/* 492 */     return null;
/*     */   }
/*     */   
/*     */   public static void cleanReportParameters() {
/* 496 */     LinkedList<Map<String, String>> stack = (LinkedList<Map<String, String>>) reportParameters.get();
/* 497 */     if (stack != null) {
/* 498 */       stack.pop();
/*     */     }
/* 500 */     if (stack == null || stack.isEmpty()) {
/* 501 */       reportParameters.remove();
/*     */     } else {
/* 503 */       reportParameters.set(stack);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Collection<ReportElement> getAvailableElements() {
/* 508 */     PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
/* 509 */     Collection<ReportElement> list = new ArrayList<>();
/* 510 */     Collection<Plugin> pluginList = pluginManager.list(ReportElement.class);
/* 511 */     for (Plugin plugin : pluginList) {
/* 512 */       if (plugin instanceof ReportElement) {
/* 513 */         list.add((ReportElement)plugin);
/*     */       }
/*     */     } 
/* 516 */     return list;
/*     */   }
/*     */   
/*     */   protected int getRenderPartHeight(String html, String css, String width) {
/* 520 */     int px = getPixel(width);
/*     */     
/*     */     try {
/* 523 */       String htmlMeta = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
/* 524 */       htmlMeta = htmlMeta + "<!DOCTYPE html>";
/* 525 */       htmlMeta = htmlMeta + "<html>";
/* 526 */       htmlMeta = htmlMeta + "<head>";
/* 527 */       htmlMeta = htmlMeta + "<meta http-equiv=\"content-type\" content=\"application/xhtml+xml; charset=UTF-8\" />";
/* 528 */       htmlMeta = htmlMeta + "<style>" + css + "</style>";
/* 529 */       htmlMeta = htmlMeta + "</head><body>";
/* 530 */       htmlMeta = htmlMeta + html;
/* 531 */       htmlMeta = htmlMeta + "</body></html>";
/*     */       
/* 533 */       DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 534 */       documentBuilderFactory.setValidating(false);
/* 535 */       DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
/* 536 */       builder.setEntityResolver((EntityResolver)FSEntityResolver.instance());
/* 537 */       Document xmlDoc = builder.parse(new ByteArrayInputStream(htmlMeta.getBytes("UTF-8")));
/*     */       
/* 539 */       Java2DRenderer renderer = new Java2DRenderer(xmlDoc, px);
/* 540 */       return renderer.getImage().getHeight() - 20;
/* 541 */     } catch (Exception e) {
/* 542 */       LogUtil.error(FormPdfUtil.class.getName(), e, "");
/*     */ 
/*     */       
/* 545 */       return 0;
/*     */     } 
/*     */   }
/*     */   protected int getPixel(String number) {
/* 549 */     int px = 0;
/*     */     try {
/* 551 */       px = (int)Math.round(Double.parseDouble(number) * 3.7795275590551185D);
/* 552 */     } catch (Exception exception) {}
/* 553 */     return px;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getURL(HttpServletRequest req, String link) {
/* 558 */     String scheme = req.getScheme();
/* 559 */     String serverName = req.getServerName();
/* 560 */     int serverPort = req.getServerPort();
/*     */ 
/*     */     
/* 563 */     StringBuilder url = new StringBuilder();
/* 564 */     url.append(scheme).append("://").append(serverName);
/*     */     
/* 566 */     if (serverPort != 80 && serverPort != 443) {
/* 567 */       url.append(":").append(serverPort);
/*     */     }
/*     */     
/* 570 */     if (link != null) {
/* 571 */       url.append(link);
/*     */     }
/* 573 */     return url.toString();
/*     */   }
/*     */ }