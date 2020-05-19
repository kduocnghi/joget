/*     */ package org.joget.plugin.report.lib;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Base64;
/*     */ import java.util.List;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.glxn.qrgen.core.scheme.Bookmark;
/*     */ import net.glxn.qrgen.core.scheme.EMail;
/*     */ import net.glxn.qrgen.core.scheme.GeoInfo;
/*     */ import net.glxn.qrgen.core.scheme.Girocode;
/*     */ import net.glxn.qrgen.core.scheme.GooglePlay;
/*     */ import net.glxn.qrgen.core.scheme.KddiAu;
/*     */ import net.glxn.qrgen.core.scheme.MMS;
/*     */ import net.glxn.qrgen.core.scheme.MeCard;
/*     */ import net.glxn.qrgen.core.scheme.SMS;
/*     */ import net.glxn.qrgen.core.scheme.Schema;
/*     */ import net.glxn.qrgen.core.scheme.Telephone;
/*     */ import net.glxn.qrgen.core.scheme.Url;
/*     */ import net.glxn.qrgen.core.scheme.VCard;
/*     */ import net.glxn.qrgen.core.scheme.Wifi;
/*     */ import net.glxn.qrgen.core.scheme.YouTube;
/*     */ import net.glxn.qrgen.javase.QRCode;
/*     */ import org.joget.apps.app.service.AppPluginUtil;
/*     */ import org.joget.apps.app.service.AppUtil;
/*     */ import org.joget.commons.util.LogUtil;
/*     */ import org.joget.plugin.base.ExtDefaultPlugin;
/*     */ import org.joget.plugin.property.model.PropertyEditable;
/*     */ import org.joget.plugin.report.api.ReportElement;
/*     */ 
/*     */ 
/*     */ public class QRcodeReportElement
/*     */   extends ExtDefaultPlugin
/*     */   implements PropertyEditable, ReportElement
/*     */ {
/*     */   public String getName() {
/*  39 */     return "QRcodeReportElement";
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
/*  64 */     return AppUtil.readPluginResource(getClass().getName(), "/properties/report/QRcodeReportElement.json", null, true, "messages/ReportElements");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  69 */     return "<i class=\"fas fa-qrcode\"></i>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String render() {
/*  74 */     int height = 150;
/*  75 */     setProperty("heightInt", Integer.valueOf(height));
/*     */     
/*  77 */     String image = getBase64QRCode();
/*     */     
/*  79 */     if (image != null) {
/*  80 */       String style = "";
/*  81 */       String margin = "";
/*  82 */       if (!getPropertyString("height").isEmpty()) {
/*  83 */         style = style + "width:" + getPropertyString("height") + ";height:" + getPropertyString("height") + ";";
/*     */       }
/*  85 */       if (!getPropertyString("marginBottom").isEmpty()) {
/*  86 */         margin = margin + "margin-bottom:" + getPropertyString("marginBottom") + ";";
/*     */       }
/*  88 */       return "<div style=\"text-align:" + getPropertyString("halign") + ";" + margin + "\"><img style=\"" + style + "\" src=\"data:image/jpeg;base64," + image + "\" /></div>";
/*     */     } 
/*     */     
/*  91 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportReportContainer() {
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String preview() {
/* 101 */     return render();
/*     */   }
/*     */   
/*     */   public String getBase64QRCode() {
/*     */     try {
/* 106 */       BufferedImage image = ImageIO.read(getQRCodeFile());
/*     */       
/* 108 */       String imageString = null;
/* 109 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*     */       
/*     */       try {
/* 112 */         ImageIO.write(image, "jpeg", bos);
/* 113 */         byte[] imageBytes = bos.toByteArray();
/*     */         
/* 115 */         byte[] encodedBytes = Base64.getEncoder().encode(imageBytes);
/* 116 */         imageString = new String(encodedBytes, "UTF-8");
/*     */       } finally {
/*     */         
/* 119 */         bos.close();
/*     */       } 
/*     */       
/* 122 */       return imageString;
/* 123 */     } catch (Exception e) {
/* 124 */       LogUtil.error(getClassName(), e, "");
/*     */       
/* 126 */       return null;
/*     */     } 
/*     */   } protected File getQRCodeFile() {
/* 129 */     YouTube youTube = null;
/* 130 */     Schema schema = null;
/* 131 */     if ("Bookmark".equalsIgnoreCase(getPropertyString("type"))) {
/* 132 */       Bookmark bookmark = new Bookmark();
/* 133 */       bookmark.setUrl(getPropertyString("url"));
/* 134 */       bookmark.setTitel(getPropertyString("title").isEmpty() ? null : getPropertyString("title"));
/* 135 */       Bookmark bookmark1 = bookmark;
/* 136 */     } else if ("Email".equalsIgnoreCase(getPropertyString("type"))) {
/* 137 */       EMail eMail = new EMail(getPropertyString("email"));
/* 138 */     } else if ("GeoInfo".equalsIgnoreCase(getPropertyString("type"))) {
/* 139 */       GeoInfo geoInfo = new GeoInfo();
/* 140 */       List<String> points = new ArrayList<>();
/* 141 */       points.add(getPropertyString("latitude"));
/* 142 */       points.add(getPropertyString("longitude"));
/* 143 */       geoInfo.setPoints(points);
/* 144 */     } else if ("Girocode".equalsIgnoreCase(getPropertyString("type"))) {
/* 145 */       Girocode girocode = new Girocode();
/* 146 */       girocode.setEncoding(Girocode.Encoding.UTF_8);
/* 147 */       girocode.setBic(getPropertyString("bic"));
/* 148 */       girocode.setName(getPropertyString("name"));
/* 149 */       girocode.setIban(getPropertyString("iban"));
/* 150 */       girocode.setAmount(getPropertyString("amount"));
/* 151 */       girocode.setPurposeCode(getPropertyString("purposeCode"));
/* 152 */       girocode.setReference(getPropertyString("reference"));
/* 153 */       girocode.setHint(getPropertyString("hint"));
/* 154 */       Girocode girocode1 = girocode;
/* 155 */     } else if ("GooglePlay".equalsIgnoreCase(getPropertyString("type"))) {
/* 156 */       GooglePlay googlePlay = new GooglePlay();
/* 157 */       googlePlay.setAppPackage(getPropertyString("value"));
/* 158 */     } else if ("KddiAu".equalsIgnoreCase(getPropertyString("type"))) {
/* 159 */       KddiAu kddiAu = new KddiAu();
/* 160 */       kddiAu.setName1(getPropertyString("name"));
/* 161 */       kddiAu.setName2(getPropertyString("name2").isEmpty() ? null : getPropertyString("name2"));
/* 162 */       kddiAu.setAddress(getPropertyString("address").isEmpty() ? null : getPropertyString("address"));
/* 163 */       kddiAu.setEmail1(getPropertyString("email1").isEmpty() ? null : getPropertyString("email1"));
/* 164 */       kddiAu.setEmail2(getPropertyString("email2").isEmpty() ? null : getPropertyString("email2"));
/* 165 */       kddiAu.setEmail3(getPropertyString("email3").isEmpty() ? null : getPropertyString("email3"));
/* 166 */       kddiAu.setTelephone1(getPropertyString("telephone1").isEmpty() ? null : getPropertyString("telephone1"));
/* 167 */       kddiAu.setTelephone2(getPropertyString("telephone2").isEmpty() ? null : getPropertyString("telephone2"));
/* 168 */       kddiAu.setTelephone3(getPropertyString("telephone3").isEmpty() ? null : getPropertyString("telephone3"));
/* 169 */       KddiAu kddiAu1 = kddiAu;
/* 170 */     } else if ("MMS".equalsIgnoreCase(getPropertyString("type"))) {
/* 171 */       MMS mms = new MMS();
/* 172 */       mms.setNumber(getPropertyString("number"));
/* 173 */       mms.setSubject(getPropertyString("subject").isEmpty() ? null : getPropertyString("subject"));
/* 174 */       MMS mMS1 = mms;
/* 175 */     } else if ("MeCard".equalsIgnoreCase(getPropertyString("type"))) {
/* 176 */       MeCard meCard = new MeCard();
/* 177 */       meCard.setName(getPropertyString("name"));
/* 178 */       meCard.setAddress(getPropertyString("address").isEmpty() ? null : getPropertyString("address"));
/* 179 */       meCard.setTelephone(getPropertyString("telephone1").isEmpty() ? null : getPropertyString("telephone1"));
/* 180 */       meCard.setEmail(getPropertyString("email1").isEmpty() ? null : getPropertyString("email1"));
/* 181 */       MeCard meCard1 = meCard;
/* 182 */     } else if ("SMS".equalsIgnoreCase(getPropertyString("type"))) {
/* 183 */       SMS sms = new SMS();
/* 184 */       sms.setNumber(getPropertyString("number"));
/* 185 */       sms.setSubject(getPropertyString("subject").isEmpty() ? null : getPropertyString("subject"));
/* 186 */       SMS sMS1 = sms;
/* 187 */     } else if ("Telephone".equalsIgnoreCase(getPropertyString("type"))) {
/* 188 */       Telephone telephone = new Telephone();
/* 189 */       telephone.setTelephone(getPropertyString("telephone"));
/* 190 */     } else if ("Url".equalsIgnoreCase(getPropertyString("type"))) {
/* 191 */       Url url = new Url();
/* 192 */       url.setUrl(getPropertyString("url"));
/* 193 */     } else if ("VCard".equalsIgnoreCase(getPropertyString("type"))) {
/* 194 */       VCard vcard = new VCard();
/* 195 */       vcard.setName(getPropertyString("name"));
/* 196 */       vcard.setAddress(getPropertyString("address").isEmpty() ? null : getPropertyString("address"));
/* 197 */       vcard.setCompany(getPropertyString("company").isEmpty() ? null : getPropertyString("company"));
/* 198 */       vcard.setEmail(getPropertyString("email1").isEmpty() ? null : getPropertyString("email1"));
/* 199 */       vcard.setTitle(getPropertyString("title").isEmpty() ? null : getPropertyString("title"));
/* 200 */       vcard.setWebsite(getPropertyString("website").isEmpty() ? null : getPropertyString("website"));
/* 201 */       vcard.setPhoneNumber(getPropertyString("telephone1").isEmpty() ? null : getPropertyString("telephone1"));
/* 202 */       vcard.setNote(getPropertyString("note").isEmpty() ? null : getPropertyString("note"));
/* 203 */       VCard vCard1 = vcard;
/* 204 */     } else if ("Wifi".equalsIgnoreCase(getPropertyString("type"))) {
/* 205 */       Wifi wifi = new Wifi();
/* 206 */       wifi.setSsid(getPropertyString("ssid"));
/* 207 */       wifi.setAuthentication(Wifi.Authentication.valueOf(getPropertyString("authentication")));
/* 208 */       wifi.setPsk(getPropertyString("password").isEmpty() ? null : getPropertyString("password"));
/* 209 */       wifi.setHidden("true".equalsIgnoreCase(getPropertyString("hidden")));
/* 210 */       Wifi wifi1 = wifi;
/* 211 */     } else if ("YouTube".equalsIgnoreCase(getPropertyString("type"))) {
/* 212 */       youTube = new YouTube(getPropertyString("value"));
/* 213 */     } else if (!getPropertyString("value").isEmpty()) {
/* 214 */       return QRCode.from(getPropertyString("value")).withSize(((Integer)getProperty("heightInt")).intValue(), ((Integer)getProperty("heightInt")).intValue()).file();
/*     */     } 
/*     */     
/* 217 */     if (youTube != null) {
/* 218 */       return QRCode.from((Schema)youTube).withSize(((Integer)getProperty("heightInt")).intValue(), ((Integer)getProperty("heightInt")).intValue()).file();
/*     */     }
/* 220 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCSS() {
/* 226 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\lib\QRcodeReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */