/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.Base64;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.sourceforge.barbecue.Barcode;
/*     */ import net.sourceforge.barbecue.BarcodeFactory;
/*     */ import net.sourceforge.barbecue.BarcodeImageHandler;
/*     */ import net.sourceforge.barbecue.linear.code39.Code39Barcode;
/*     */ import net.sourceforge.barbecue.linear.ean.UCCEAN128Barcode;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.commons.util.LogUtil;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ 
/*     */ public class BarcodeReportElement
/*     */   extends ExtDefaultPlugin
/*     */   implements PropertyEditable, ReportElement
/*     */ {
/*     */   public String getName() {
/*  24 */     return "BarcodeReportElement";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  29 */     return "1.0.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  34 */     return AppPluginUtil.getMessage(getName() + ".desc", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  39 */     return AppPluginUtil.getMessage(getName() + ".label", getClassName(), "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  44 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyOptions() {
/*  49 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/BarcodeReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  54 */     return "<i class=\"fas fa-barcode\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  59 */     String value = getPropertyString("value");
/*  60 */     if (value.isEmpty()) {
/*  61 */       return "";
/*     */     }
/*     */     
/*  64 */     String image = getBarcodeBase64Image(value);
/*     */     
/*  66 */     if (image != null) {
/*  67 */       String style = "";
/*  68 */       String margin = "";
/*  69 */       if (!getPropertyString("width").isEmpty()) {
/*  70 */         style = style + "width:" + getPropertyString("width") + ";";
/*     */       }
/*  72 */       if (!getPropertyString("height").isEmpty()) {
/*  73 */         style = style + "height:" + getPropertyString("height") + ";";
/*     */       }
/*  75 */       if (!getPropertyString("marginBottom").isEmpty()) {
/*  76 */         margin = margin + "margin-bottom:" + getPropertyString("marginBottom") + ";";
/*     */       }
/*  78 */       return "<div style=\"text-align:" + getPropertyString("halign") + ";" + margin + "\"><img style=\"" + style + "\" src=\"data:image/jpeg;base64," + image + "\" /></div>";
/*     */     } 
/*     */     
/*  81 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/*  91 */     return render(); } protected String getBarcodeBase64Image(String text) { try {
/*     */       UCCEAN128Barcode uCCEAN128Barcode2; Barcode barcode2;
/*     */       UCCEAN128Barcode uCCEAN128Barcode1;
/*     */       Barcode barcode1;
/*  95 */       Code39Barcode code39Barcode = null;
/*  96 */       Barcode bc = null;
/*  97 */       boolean showText = "true".equalsIgnoreCase(getPropertyString("showText"));
/*  98 */       boolean checkSum = "true".equalsIgnoreCase(getPropertyString("checkSum"));
/*     */       
/* 100 */       switch (getPropertyString("type")) {
/*     */         case "0":
/* 102 */           bc = BarcodeFactory.create2of7(text);
/*     */           break;
/*     */         case "1":
/* 105 */           bc = BarcodeFactory.create3of9(text, checkSum);
/*     */           break;
/*     */         case "2":
/* 108 */           bc = BarcodeFactory.createBookland(text);
/*     */           break;
/*     */         case "3":
/* 111 */           bc = BarcodeFactory.createCodabar(text);
/*     */           break;
/*     */         case "4":
/* 114 */           bc = BarcodeFactory.createCode128(text);
/*     */           break;
/*     */         case "5":
/* 117 */           bc = BarcodeFactory.createCode128A(text);
/*     */           break;
/*     */         case "6":
/* 120 */           bc = BarcodeFactory.createCode128B(text);
/*     */           break;
/*     */         case "7":
/* 123 */           bc = BarcodeFactory.createCode128B(text);
/*     */           break;
/*     */         case "8":
/* 126 */           bc = BarcodeFactory.createCode39(text, checkSum);
/*     */           break;
/*     */         case "9":
/* 129 */           bc = BarcodeFactory.createEAN128(text);
/*     */           break;
/*     */         case "10":
/* 132 */           bc = BarcodeFactory.createEAN13(text);
/*     */           break;
/*     */         case "11":
/* 135 */           bc = BarcodeFactory.createGlobalTradeItemNumber(text);
/*     */           break;
/*     */         case "12":
/* 138 */           bc = BarcodeFactory.createInt2of5(text, checkSum);
/*     */           break;
/*     */         case "13":
/* 141 */           bc = BarcodeFactory.createMonarch(text);
/*     */           break;
/*     */         case "14":
/* 144 */           bc = BarcodeFactory.createNW7(text);
/*     */           break;
/*     */         case "15":
/* 147 */           bc = BarcodeFactory.createPDF417(text);
/*     */           break;
/*     */         case "16":
/* 150 */           bc = BarcodeFactory.createSCC14ShippingCode(text);
/*     */           break;
/*     */         case "17":
/* 153 */           bc = BarcodeFactory.createShipmentIdentificationNumber(text);
/*     */           break;
/*     */         case "18":
/* 156 */           uCCEAN128Barcode2 = new UCCEAN128Barcode("00", text, checkSum);
/*     */           break;
/*     */         case "19":
/* 159 */           barcode2 = BarcodeFactory.createStd2of5(text, checkSum);
/*     */           break;
/*     */         case "20":
/* 162 */           uCCEAN128Barcode1 = new UCCEAN128Barcode(getPropertyString("applicationIdentifier"), text, checkSum);
/*     */           break;
/*     */         case "21":
/* 165 */           barcode1 = BarcodeFactory.createUPCA(text);
/*     */           break;
/*     */         case "22":
/* 168 */           barcode1 = BarcodeFactory.createUSD3(text, checkSum);
/*     */           break;
/*     */         case "23":
/* 171 */           barcode1 = BarcodeFactory.createUSD4(text);
/*     */           break;
/*     */         case "24":
/* 174 */           barcode1 = BarcodeFactory.createUSPS(text);
/*     */           break;
/*     */         case "25":
/* 177 */           code39Barcode = new Code39Barcode(text, checkSum, true);
/*     */           break;
/*     */       } 
/*     */       
/* 181 */       code39Barcode.setDrawingText(showText);
/* 182 */       BufferedImage image = BarcodeImageHandler.getImage((Barcode)code39Barcode);
/*     */       
/* 184 */       String imageString = null;
/* 185 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*     */       
/*     */       try {
/* 188 */         ImageIO.write(image, "jpeg", bos);
/* 189 */         byte[] imageBytes = bos.toByteArray();
/*     */         
/* 191 */         byte[] encodedBytes = Base64.getEncoder().encode(imageBytes);
/* 192 */         imageString = new String(encodedBytes, "UTF-8");
/*     */       } finally {
/*     */         
/* 195 */         bos.close();
/*     */       } 
/*     */       
/* 198 */       return imageString;
/* 199 */     } catch (Exception e) {
/* 200 */       LogUtil.error(getClassName(), e, text);
/*     */ 
/*     */       
/* 203 */       return null;
/*     */     }  }
/*     */ 
/*     */   
/*     */   public String getCSS() {
/* 208 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\BarcodeReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */