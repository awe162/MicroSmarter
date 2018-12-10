package com.impinj1.microsmarter.constants;

/**
 * Created by Administrator on 2017/8/24.
 */

public class Net  {

  //  public static final String LOGIN_IP = "http://58.213.83.82:60090/ttms/admin/login";

  // public static final String BASIC = "http://58.213.83.82:38080/dwz_tireinfo"; //扬子服务器

  //  public static final String BASIC = "http://58.213.83.82:18180/dwz_tireinfo"; //崇明服务器

    public static final String BASIC = "http://58.213.83.82:48080/dwz_jiangnan"; //江南服务器

  public static final String LOGIN_IP = BASIC+"/login/companyLogin";

    //public static final String LOGIN_COMPANY = "http://58.213.83.82:60090/ttms/company";
    public static final String LOGIN_COMPANY = BASIC+"/login/companyList";

    //public static final String MAIN_QUERY_FLEET = "http://localhost:8080/dwz_tireInfo/login/companyList";


  //  public static final String BASIC = "http://58.213.83.82:60090/newChange";


 //  public static final String BASIC = "http://192.168.1.234:8080/newChange";
    public static final String OVERHAUL = BASIC+"/carBusInfoApp/updateTireInfo";

    public static final String IS_STORAGE = BASIC+"/cjw";

   public static final String ADD = BASIC+"/add";
 //public static final String ADD = BASIC+"/add11";
 public static final String ADDTYRE = BASIC+"/addFactoryCode";

   // public static final String REPLACE = BASIC+"/update";
   public static final String REPLACE = BASIC+"/carBusInfoApp/insertUninstall";

    public static final String QUERY = BASIC+"/test";


    public static final String STORAGE_BRAND = "http://58.213.83.82:60090/ttms/tireLog/tireBrandList.do";
}