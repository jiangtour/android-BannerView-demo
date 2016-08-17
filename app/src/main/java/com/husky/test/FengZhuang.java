package com.husky.test;

/**
 * @author husky
 */
public class FengZhuang {
    public static final String type_sy = "SY";
    public static final String type_zlx = "ZLX";

    private static volatile FengZhuang instance;

    private FengZhuang(){}

    public static FengZhuang getInstance(){
        if (instance == null){
            synchronized (FengZhuang.class){
                if (instance == null){
                    instance = new FengZhuang();
                }
            }
        }
        return instance;
    }

    public  double yunsuan(double value1,double value2,String type){
        switch (type){
            case type_sy:
                return syYunsuan(value1,value2);
            case type_zlx:
                return zlxYunsuan(value1,value2);
            default:
                return 0;
        }
    }

    private  double zlxYunsuan(double value1, double value2) {
        //运算逻辑
        return 0;
    }

    private  double syYunsuan(double value1, double value2) {
        //运算逻辑
        return 0;
    }
}
