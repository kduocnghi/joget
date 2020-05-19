/*    */ package org.joget.plugin.report;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import org.joget.commons.util.LogUtil;
/*    */ import org.joget.plugin.base.CustomPluginInterface;
/*    */ import org.joget.plugin.base.PluginManager;
/*    */ import org.joget.plugin.report.api.ReportElement;
/*    */ import org.osgi.framework.BundleActivator;
/*    */ import org.osgi.framework.BundleContext;
/*    */ import org.osgi.framework.ServiceRegistration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Activator
/*    */   implements BundleActivator
/*    */ {
/*    */   protected Collection<ServiceRegistration> registrationList;
/*    */   
/*    */   public void start(BundleContext context) {
/* 37 */     this.registrationList = new ArrayList<>();
/*    */     
/*    */     try {
/* 40 */       PluginManager.registerCustomPluginInterface(new CustomPluginInterface(ReportElement.class, "reportBuilder.element", "messages/ReportBuilder"));
/*    */       
/* 42 */       this.registrationList.add(context.registerService(ReportBuilder.class.getName(), new ReportBuilder(), null));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     }
/* 64 */     catch (Exception e) {
/* 65 */       LogUtil.info(Activator.class.getName(), e.toString());
/*    */     } 
/*    */   }
/*    */   
/*    */   public void stop(BundleContext context) {
/* 70 */     for (ServiceRegistration registration : this.registrationList) {
/* 71 */       registration.unregister();
/*    */     }
/*    */     
/* 74 */     PluginManager.unregisterCustomPluginInterface(ReportElement.class.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\Activator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */