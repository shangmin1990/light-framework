package com.le.bigdata.core.util;

/**
 * 根据经纬度计算两点距离
 */
public class DistanceUtil {

    private DistanceUtil(){

    }

    // 地球半径 km
    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 获取两点间的距离
     * @param srcLatitude 起点纬度
     * @param srcLongitude 起点经度
     * @param descLatitude 终点纬度
     * @param descLongitude 终点经度
     * @return
     */
    public static double getDistance(double srcLatitude, double srcLongitude, double descLatitude, double descLongitude){

        double radLat1 = rad(srcLatitude);
        double radLat2 = rad(descLatitude);
        double a = radLat1 - radLat2;
        double b = rad(srcLongitude) - rad(descLongitude);

        double s = 2 * Math.sin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static void main(String... args){
        double distance = getDistance(39.1, 119.3, 24.5, 200);
        System.out.println(distance);
    }
}
