package com.impinj1.microsmarter.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/24.
 */

public class CommonCompany {

    public static Map<String ,Map<String,String>> getCompany(){
        Map<String ,Map<String,String>> mList = new HashMap<String ,Map<String,String>>();

        Map<String,String> beijing = new HashMap<String,String>();
        beijing.put("-1","所有车辆");
        beijing.put("2","车队二");

        Map<String,String> hangzhou = new HashMap<String,String>();
        hangzhou.put("-1","所有车辆");
        hangzhou.put("2","未知");
        hangzhou.put("3","杭州公交第二分公司");
        hangzhou.put("4","杭州公交第三分公司");
        hangzhou.put("5","杭州公交第五分公司");
        hangzhou.put("6","上海奉贤巴士一公司");

        Map<String,String> shanghai = new HashMap<String,String>();
        shanghai.put("-1","所有车辆");
        shanghai.put("2","上海巴士三汽");
        shanghai.put("3","上海巴士二汽");
        shanghai.put("4","上海奉贤巴士一公司");

        Map<String,String> shenzhenxibu = new HashMap<String,String>();
        shenzhenxibu.put("-1","所有车辆");
        // shenzhenxibu.put("0","仓库");

        Map<String,String> zhengzhouyutong = new HashMap<String,String>();
        zhengzhouyutong.put("-1","所有车辆");
        //  zhengzhouyutong.put("0","仓库");

        Map<String,String> yantai = new HashMap<String,String>();
        yantai.put("-1","所有车辆");
        yantai.put("2"," 车队一");

        Map<String,String> qingdao = new HashMap<String,String>();
        qingdao.put("-1","所有车辆");
        //  qingdao.put("0"," 仓库");

        Map<String,String> fuyang = new HashMap<String,String>();
        fuyang.put("-1","所有车辆");
        fuyang.put("5","公交一分公司车队");
        fuyang.put("6","公交二分公司车队");
        fuyang.put("7","公交三分公司车队");

        Map<String,String> yancheng = new HashMap<String,String>();
        yancheng.put("-1","所有车辆");
        //  yancheng.put("0"," 仓库");

        Map<String,String> xining = new HashMap<String,String>();
        xining.put("-1","所有车辆");
        xining.put("2","二车队");

        Map<String,String> guangzhouyiqi = new HashMap<String,String>();
        guangzhouyiqi.put("-1","所有车辆");
        //  guangzhouyiqi.put("0","仓库");

        Map<String,String> zhongqijituan = new HashMap<String,String>();
        zhongqijituan.put("-1","所有车辆");
        // zhongqijituan.put("0","仓库");

        Map<String,String> bengbu = new HashMap<String,String>();
        bengbu.put("-1","所有车辆");
        //  bengbu.put("0","仓库");

        Map<String,String> langfang = new HashMap<String,String>();
        langfang.put("-1","所有车辆");
        langfang.put("2","36路");
        langfang.put("3","16路");
        langfang.put("4","7路");
        langfang.put("5","6路");
        langfang.put("6","12路");
        langfang.put("7","13路");

        Map<String,String> jinzhou = new HashMap<String,String>();
        jinzhou.put("-1","所有车辆");
        //  jinzhou.put("0","仓库");

        Map<String,String> kunming = new HashMap<String,String>();
        kunming.put("-1","所有车辆");
        //    kunming.put("0","仓库");

        Map<String,String> shenzhendongbu = new HashMap<String,String>();
        shenzhendongbu.put("-1","所有车辆");
        shenzhendongbu.put("8","398车队");
        shenzhendongbu.put("9","五一分队");
        shenzhendongbu.put("12","鹅公岭车队");

        Map<String,String> anyang = new HashMap<String,String>();
        anyang.put("-1","所有车辆");


        Map<String,String> yangzi = new HashMap<>();
        yangzi.put("2","禄口修理厂");
        yangzi.put("3","雨花修理厂");
        yangzi.put("4","马鞍山修理厂");
        yangzi.put("5","淳化修理厂");
        yangzi.put("6","大厂修理厂");


        Map<String,String> chongming = new HashMap<String,String>();
        yangzi.put("2","禄口修理厂");
        yangzi.put("3","雨花修理厂");
        yangzi.put("4","马鞍山修理厂");
        yangzi.put("5","淳化修理厂");
        yangzi.put("6","大厂修理厂");

        mList.put("beijing",beijing);
        mList.put("hangzhou",hangzhou);
        mList.put("shanghai",shanghai);
        mList.put("shenzhenxibu",shenzhenxibu);
        mList.put("zhengzhouyutong",zhengzhouyutong);
        mList.put("yantai",yantai);
        mList.put("qingdao",qingdao);
        mList.put("fuyang",fuyang);
        mList.put("yancheng",yancheng);
        mList.put("xining",xining);
        mList.put("guangzhouyiqi",guangzhouyiqi);
        mList.put("zhongqijituan",zhongqijituan);
        mList.put("bengbu",bengbu);
        mList.put("langfang",langfang);
        mList.put("jinzhou",jinzhou);
        mList.put("kunming",kunming);
        mList.put("shenzhendongbu",shenzhendongbu);
        mList.put("anyang",anyang);

        mList.put("yangzi",yangzi);

        mList.put("chongming",chongming);
        return mList;
    }

    public static String getTireBrand(String keyId){
        Map<String,String> map = new HashMap<String,String>();

      //  map.put("米其林","2");
        /*

        map.put("固特异","3");

        map.put("双钱","7");

        map.put("赛轮","8");

        map.put("倍耐力","9");

    //    map.put("佳通","10");

        map.put("其它","11");

        map.put("普利司通","12");

        map.put("马牌","13");

        map.put("德力","15");

        map.put("锦湖","16");*/
//下面是杭州的轮胎品牌ID
      /*  map.put("米其林","1");

        map.put("固特异","2");

        map.put("朝阳","3");*/

//富阳公交
        map.put("佳通","1");
        map.put("韩泰","5");

        String s = map.get(keyId);

        return s;
    }

    public static String getTireName(String keyId){
        Map<String,String> map = new HashMap<String,String>();

        map.put("1","米其林");

        map.put("3","固特异");

        map.put("7","双钱");

        map.put("8","赛轮");

        map.put("9","倍耐力");

        map.put("10","佳通");

        map.put("11","其它");

        map.put("12","普利司通");

        map.put("13","马牌");

        map.put("15","德力");

        map.put("16","锦湖");

        String s = map.get(keyId);

        return s;
    }

    public static String getTyrePosition(String keyId){
        Map<String,String> map = new HashMap<String,String>();

        map.put("1","左前轮");

        map.put("2","右前轮");

        map.put("3","左后外");

        map.put("4","左后内");

        map.put("5","右后内");

        map.put("6","右后外");

        String s = map.get(keyId);
        return s;
    }
}
