package org.joget.plugin.report.api;

public interface ReportElement {
  String getLabel();
  
  String getClassName();
  
  String getPropertyOptions();
  
  String getIcon();
  
  String render();
  
  String preview();
  
  String getCSS();
  
  boolean supportReportContainer();
}


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\api\ReportElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */