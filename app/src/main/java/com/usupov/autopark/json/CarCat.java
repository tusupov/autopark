package com.usupov.autopark.json;

import android.content.Context;

import com.usupov.autopark.config.CatalogRestURIConstants;
import com.usupov.autopark.http.HttpHandler;
import com.usupov.autopark.model.CatalogBrand;
import com.usupov.autopark.model.CatalogModel;
import com.usupov.autopark.model.CatalogYear;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CarCat {

    public static List<CatalogBrand> getBradList(Context context) {
		
        HttpHandler handler = new HttpHandler();
		
        try {
			
            String jsonString = handler.doHttpGet(CatalogRestURIConstants.BRAND_GET_ALL, context).getBodyString();

            JSONArray jsonArray = new JSONArray(jsonString);
            List<CatalogBrand> brandList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                CatalogBrand carBrand = new CatalogBrand();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                carBrand.setId(jsonObject.getInt("id"));
                carBrand.setName(jsonObject.getString("name"));
                carBrand.setCode(jsonObject.getString("code"));
                brandList.add(carBrand);
            }
            return brandList;
			
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        return null;

    }
    public static List<CatalogModel> getModels ( int brandId, Context context){

        String url = String.format(CatalogRestURIConstants.MODEL_GET_ALL, brandId);
        HttpHandler handler = new HttpHandler();

        try {

            String jsonString = handler.doHttpGet(url, context).getBodyString();
            JSONArray jsonArray = new JSONArray(jsonString);
            List<CatalogModel> modelList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CatalogModel catalogModel = new CatalogModel();
                catalogModel.setId(jsonObject.getInt("id"));
                catalogModel.setName(jsonObject.getString("name"));
                modelList.add(catalogModel);
            }
            return modelList;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static List<CatalogYear> getYears ( int brandId, int modelId, Context context){

        String url = String.format(CatalogRestURIConstants.YEAR_GET_ALL, brandId, modelId);
        HttpHandler handler = new HttpHandler();

        try {
            List<CatalogYear> yearList = new ArrayList<>();
            String jsonString = handler.doHttpGet(url, context).getBodyString();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CatalogYear catalogYear = new CatalogYear();
                catalogYear.setId(jsonObject.getInt("id"));
                catalogYear.setModelId(jsonObject.getInt("modelId"));
                catalogYear.setName(jsonObject.getString("name"));
                yearList.add(catalogYear);
            }
            return yearList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}