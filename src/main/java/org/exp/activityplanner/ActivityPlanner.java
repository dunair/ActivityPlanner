package org.exp.activityplanner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


public class ActivityPlanner implements RequestHandler<Request, Response>{
    public ActivityPlanner () {

    }
    @Override
    public Response handleRequest(Request request, Context context) {
        Response climateCondition = null;
        if(request.cityName == null || request.cityName.isEmpty()) {
            climateCondition = new org.exp.activityplanner.Response();
            climateCondition.setTemp(0);
            climateCondition.setPlace("");
            climateCondition.setDescription("Unable to find climate condition");
            return climateCondition;
        }

        try {
            String REST_URI =
                    "http://api.openweathermap.org/data/2.5/weather?q=" + request.cityName + ",IN&cnt=1&APPID=9139efd101dda5a189334ddef1e5719a&units=metric";
            Client client = ClientBuilder.newClient();

            CityClimate res = client.target(REST_URI).request(MediaType.APPLICATION_JSON).get(CityClimate.class);
            Response climateCondition2 = processCityClimateData(res);
            return climateCondition2;

        } catch (Exception e) {

            climateCondition.setDescription(e.getMessage());
            e.printStackTrace();
        }

        return climateCondition;
    }

    private org.exp.activityplanner.Response processClimateData(Climate climate) {
        int temp = climate.getList().get(0).getMain().getTemp();
        String place = climate.getList().get(0).getName();
        String desc = climate.getList().get(0).getWeather().get(0).getDescription();

        org.exp.activityplanner.Response cCondition = new org.exp.activityplanner.Response();
        cCondition.setTemp(temp);
        cCondition.setPlace(place);
        cCondition.setDescription(desc);
        return  cCondition;
    }
    private org.exp.activityplanner.Response processCityClimateData(CityClimate climate) {
        int temp = climate.getMain().getTemp();
        String place = climate.getName();
        String desc = climate.getWeather().get(0).getDescription();

        org.exp.activityplanner.Response cCondition = new org.exp.activityplanner.Response();
        cCondition.setTemp(temp);
        cCondition.setPlace(place);
        cCondition.setDescription(desc);
        return  cCondition;
    }
}

