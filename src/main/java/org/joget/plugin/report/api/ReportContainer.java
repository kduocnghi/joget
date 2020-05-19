package org.joget.plugin.report.api;

import java.util.Collection;

public interface ReportContainer {
  String builderScript();
  
  void setChildren(Collection<ReportElement> paramCollection);
  
  Collection<ReportElement> getChildren();
}


/* Location:              C:\Users\AnhPD\Downloads\Microsoft.SkypeApp_kzf8qxf38zg5c!App\All\ExtendReportBuilder-1.0.0.jar!\org\joget\plugin\report\api\ReportContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */