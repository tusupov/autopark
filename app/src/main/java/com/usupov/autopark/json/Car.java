package com.usupov.autopark.json;

import android.content.Context;

import com.google.gson.Gson;
import com.usupov.autopark.config.CarRestURIConstants;
import com.usupov.autopark.http.HttpHandler;
import com.usupov.autopark.model.CarModel;
import com.usupov.autopark.model.CustomHttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Car {

    public static List<CarModel> getCarList(Context context) {

        HttpHandler handler = new HttpHandler();
        String url = CarRestURIConstants.GET_ALL;
        CustomHttpResponse customHttpResponse = handler.doHttpGet(url, context);
        String jsonStr = customHttpResponse.getBodyString();
        if (jsonStr == null)
            return null;
        List<CarModel> carList = new ArrayList<>();
        JSONArray carsArray = null;
        try {
            carsArray = new JSONArray(jsonStr);
            for (int i = 0; i < carsArray.length(); i++) {
                JSONObject carObject = carsArray.getJSONObject(i);

                Gson gson = new Gson();
                CarModel carModel = gson.fromJson(carObject.toString(), CarModel.class);

                carModel.setFullName();

                carList.add(carModel);
            }
            return carList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static CarModel getCarByVin(String vin, Context context) {

        HttpHandler handler = new HttpHandler();
        String url = String.format(CarRestURIConstants.GET_BY_VIN, vin);
        String jSonString = handler.doHttpGet(url, context).getBodyString();

        return fromJsonToCarModel(jSonString);
    }
    public static CarModel getCarByCatalog(int brandId, int modelId, int yearId, Context context) {
        HttpHandler handler = new HttpHandler();
        String url = String.format(CarRestURIConstants.GET_BY_CATALOG, brandId, modelId, yearId);
        String jsonString = handler.doHttpGet(url, context).getBodyString();

        return fromJsonToCarModel(jsonString);
    }
    private static CarModel fromJsonToCarModel(String jsonString) {
        if (jsonString==null)
            return null;
        try {
            Gson g = new Gson();
            return g.fromJson(jsonString, CarModel.class);
        }
        catch (Exception e){}

        return null;
    }
}