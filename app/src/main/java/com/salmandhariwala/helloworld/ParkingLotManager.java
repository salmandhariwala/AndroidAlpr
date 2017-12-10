package com.salmandhariwala.helloworld;

import android.content.Context;
import android.content.SharedPreferences;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ParkingLotManager {

    private static String parkingContentSharedPref = "parking_content";
    private static Long perMinChargeInRs = 1l;

    public static void processEntry(Context context, String vehicleNumber) {
        addVehicleInParkingLot(context, vehicleNumber);
    }

    public static Double processExit(Context context, String vehicleNumber) {
        Double timelapse = getTimeLapse(context, vehicleNumber);
        Double charges = timelapse * perMinChargeInRs;

        deleteVehicleInParkingLot(context, vehicleNumber);
        return charges;
    }

    public static boolean isVehiclePresentInParking(Context context, String vehicleNumber) {
        SharedPreferences pref = context.getSharedPreferences(parkingContentSharedPref, Context.MODE_PRIVATE);

        if (pref.contains(vehicleNumber)) {
            return true;
        } else {
            return false;
        }

    }

    public static Map<String, String> getParkingLotStatuString(Context context) {

        Map<String, String> result = new HashMap<>();

        SharedPreferences pref = context.getSharedPreferences(parkingContentSharedPref, Context.MODE_PRIVATE);

        Map<String, ?> allEntries = pref.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String VehicleNum = entry.getKey();
            Long since = (Long) entry.getValue();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


            result.put(VehicleNum, sdf.format(new Date(since)));

        }

        return result;
    }

    public static void clearParkingLot(Context context) {
        SharedPreferences pref = context.getSharedPreferences(parkingContentSharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    private static Double getTimeLapse(Context context, String vehicleNumber) {
        SharedPreferences pref = context.getSharedPreferences(parkingContentSharedPref, Context.MODE_PRIVATE);

        long milisecpassed = pref.getLong(vehicleNumber, 0);

        Double timelapseMin = milisecpassed / (1000d * 60);

        return timelapseMin;
    }


    private static void deleteVehicleInParkingLot(Context context, String vehicleNumber) {
        SharedPreferences pref = context.getSharedPreferences(parkingContentSharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(vehicleNumber);
        editor.commit();
    }


    private static void addVehicleInParkingLot(Context context, String vehicleNumber) {
        SharedPreferences pref = context.getSharedPreferences(parkingContentSharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(vehicleNumber, new Date().getTime());
        editor.commit();
    }


}
