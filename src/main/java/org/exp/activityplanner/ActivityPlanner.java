package org.exp.activityplanner;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.Table;

import org.joda.time.DateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MediaType;


public class ActivityPlanner implements RequestHandler<Request, Response>{
    public ActivityPlanner () {

    }

    @Override
    public Response handleRequest(Request request, Context context) {
        Response activityData = new Response();
        List<ActivityResponse> activityList = new ArrayList<>();

        if(request.getAction() != null && request.getAction().equals("d")) {
            //Mock response if action passed is d
            List<ActivityResponse> aRs = getDummy();
            activityData.setActivities(aRs);
            return activityData;
        }

        try {
            if(request.getCityName() != null && !request.getCityName().isEmpty()) {
                SearchResponse apiReponseActivities = searchActivities(request.getCityName());
                if (apiReponseActivities != null) {
                    if (request.getPreferences() == null ||
                            request.getPreferences().isEmpty()) {
                        request.setPreferences(Preferences.ALL);
                    }

                    activityData =
                            getPreferredTopActivities(apiReponseActivities, request.getCityName(), request.getPreferences(), request.getAction());
                } else {
                    activityData.setErrorMessage("No activities found");
                    activityData.setCount(0);
                }
            } else {
                activityData.setErrorMessage("Location not found");
                activityData.setCount(0);
            }
        } catch (Exception e) {
            activityData.setCount(0);
            e.printStackTrace();
        }
        return activityData;
    }

    private SearchResponse searchActivities(String cityName) {
        DateTime today = new DateTime();
        DateTime tomorrow = today.plusDays(1);

        String todayStr = today.toString("yyyy-mm-dd");
        String tomorrowStr = tomorrow.toString("yyyy-mm-dd");
        String REST_LX_API =
                "https://apim.expedia.com/x/activities/search?location="+ cityName +"startDate=" + todayStr +"&endDate=" + tomorrowStr;
        Client lxApiClient = ClientBuilder.newClient();

        MultivaluedMap<String, Object> map = new MultivaluedHashMap<String, Object>();
        map.add("key", "4f8ce657-ee06-4527-a8d8-4b207f8f0d62");
        map.add("Content-Type", "application/json");
        SearchResponse activities = lxApiClient.target(REST_LX_API).request(MediaType.APPLICATION_JSON).headers(map).get(SearchResponse.class);

        return activities;
    }

    private Response getPreferredTopActivities(SearchResponse activities, String locationName, String userPrefs, String action) {
        Response activityData = new Response();
        List<ActivityResponse> activityList = new ArrayList<>();

        StringTokenizer tokens = new StringTokenizer(userPrefs, ",");
        Set<String> prefs = new HashSet<String>();
        while (tokens.hasMoreTokens()) {
            prefs.add(tokens.nextToken());
        }

        PreferenceCalcEngine pCalc = new PreferenceCalcEngine();
        Set<String> preferences = pCalc.getPreferences(prefs);
        List<Activity> allFiltered = getFilteredActivities(activities.getActivities(), preferences);
        List<Activity> filtered = findTheTopActivity(allFiltered);

        activities = new SearchResponse();
        activities.setActivities(filtered);
        String errorMessage = persistData(filtered, locationName, preferences);
        activityData.setErrorMessage(errorMessage);

        if (filtered.size() > 0) {
            for (int i = 0; i < 3 && i < filtered.size(); i++) {
                ActivityResponse aR = new ActivityResponse();
                aR.setActivityId(filtered.get(i).getId());
                aR.setActivityName(filtered.get(i).getTitle());
                aR.setFromPrice(filtered.get(i).getFromPrice());
                aR.setDuration(filtered.get(i).getDuration());
                aR.setScore(filtered.get(i).getScoreOutOf5());
                aR.setFreeCancellation(filtered.get(i).isFreeCancellation());
                aR.setImageUrl("https:" + filtered.get(i).getImageUrl());

                if(action != null && action.equals("email")) {
                    //Pull more info for email action.
                    aR = fillActivityDetails(filtered.get(i).getId(), aR);

                    StringTokenizer latlng = new StringTokenizer(filtered.get(i).getLatLng(), ",");
                    List<String> latlngList = new ArrayList<>();

                    while (latlng.hasMoreTokens()) {
                        latlngList.add(latlng.nextToken());
                    }

                    String REST_URI = "";

                    //Pull weather info.
                    if (latlngList.size() == 2) {
                        REST_URI =
                                "http://api.openweathermap.org/data/2.5/weather?q=" + locationName + "&cnt=1&APPID=9139efd101dda5a189334ddef1e5719a&units=metric";

                    } else {
                        REST_URI =
                                "http://api.openweathermap.org/data/2.5/weather?lat=" + latlngList.get(0) + "&lon=" + latlngList.get(1) + "&cnt=1&APPID=9139efd101dda5a189334ddef1e5719a&units=metric";

                    }
                    Client client = ClientBuilder.newClient();

                    CityClimate climate = client.target(REST_URI).request(MediaType.APPLICATION_JSON).get(CityClimate.class);
                    if (climate != null) {
                        WeatherResponse wR = new WeatherResponse();
                        wR.setTemp(climate.getMain().getTemp());
                        wR.setDescription(climate.getWeather().get(0).getDescription());
                        aR.setWeather(wR);
                    }

                }
                activityList.add(aR);
            }

            activityData.setActivities(activityList);
            activityData.setCount(filtered.size());
            if(action != null && action.equals("email")) {
                sendMail(activityData);
            }
        }

        return activityData;
    }


    private List<Activity> getFilteredActivities(List<Activity> activities, Set<String> prefs) {
        long start = System.currentTimeMillis();
        List<Activity> filteredActivities = new ArrayList<>();
        if(prefs.isEmpty() || prefs.contains(Preferences.ALL)) {
            return activities;
        }
        else {
            for(Activity activity: activities) {

                for(String cat: activity.getCategories()) {
                    if(prefs.contains(cat)) {
                        String aDuration = activity.getDuration();
                        long duration = calculateDuration(aDuration);
                        if(prefs.contains(Preferences.SHORT)) {
                            if(aDuration.contains("h")) {
                                if(duration <= 3) {
                                    String durationWord = parseDuration(duration, 'h');
                                    activity.setDuration(durationWord);
                                    filteredActivities.add(activity);
                                }
                            } else if(aDuration.contains("m")) {
                                String durationWord = parseDuration(duration, 'm');
                                activity.setDuration(durationWord);
                                filteredActivities.add(activity);
                            }

                        } else {
                            String durationWord = "can't parse duration";
                            if(aDuration.contains("h")) {
                                durationWord = parseDuration(duration, 'h');

                            } else if(aDuration.contains("d")){
                                durationWord = parseDuration(duration, 'd');
                            } else if(aDuration.contains("m")) {
                                durationWord = parseDuration(duration, 'm');
                            }
                            activity.setDuration(durationWord);
                            filteredActivities.add(activity);
                        }
                    }
                }
            }
            return filteredActivities;
        }
    }

    private String parseDuration(long duration, char type) {
        if(type == 'h') {
            if(duration == 0) {
                return "not provided";
            }
            else if(duration == 1){
                return "1 hour";
            }
            else {
                return duration + " hours";
            }
        } else if(type == 'd'){
            if(duration == 0) {
                return "not provided";
            }
            else if(duration == 1){
                return "1 day";
            }
            else {
                return duration + " days";
            }
        } else if(type == 'm') {
            if(duration == 0) {
                return "not provided";
            }
            else if(duration == 1){
                return "1 minute";
            }
            else {
                return duration + " minutes";
            }
        } else {
            return "can't parse duration";
        }

    }

    private long calculateDuration(String aDuration) {
        if(aDuration.contains("h") || aDuration.contains("d") || aDuration.contains("m")) {
            Pattern p = Pattern.compile("^(\\d+)");
            Matcher m = p.matcher(aDuration);
            long duration = 0;
            if (m.find()) {
                duration = Integer.parseInt(m.group(1));
            }
            return duration;
        }
        else {
           return 0;
        }
    }

    private List<Activity> findTheTopActivity(List<Activity> activities) {
        List<Activity> metReviewCount = new ArrayList<>();

        for(Activity activity: activities) {
           if(activity.isReviewScoreThresholdMet()) {
               metReviewCount.add(activity);
           }
        }

        if(metReviewCount.size() < 3) {
            Collections.sort(activities, new ActivityComparator());
            for(int i = 0 ; i < activities.size() && metReviewCount.size() < 3; i++) {
                metReviewCount.add(activities.get(i));
            }
        }

        List<Activity> topActivities = getTopNActivities(metReviewCount, 3);

        Collections.sort(topActivities, new ActivityComparator());
        return metReviewCount;
    }

    private List<Activity> getTopNActivities(List<Activity> activities, int n) {
        List<Activity> topActivities = new ArrayList<>();
        for(int i = 0; i < n && i < activities.size(); i++) {
            topActivities.add(activities.get(i));
        }

        return topActivities;
    }

    private String persistData(List<Activity> activities, String cityName, Set<String> preferences) {
        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            DynamoDB dynamoDB = new DynamoDB(client);
            Table table = dynamoDB.getTable("ActivityDetails");

            StringBuilder prefs = new StringBuilder();
            for(String p : preferences) {
                prefs.append(p);
                prefs.append(",");
            }
            String prefStr = prefs.toString();
            if(prefStr != null && prefs.length() > 0) {
                prefStr = prefs.toString().substring(0, prefStr.length() - 1);
            }
            for (int i = 0; i < 3 && i < activities.size(); i++) {
                Item item = new Item().withPrimaryKey("id", activities.get(i).getId())
                        .with("title", activities.get(i).getTitle())
                        .with("fromPrice", activities.get(i).getFromPrice())
                        .with("duration", activities.get(i).getDuration())
                        .with("latLng", activities.get(i).getLatLng())
                        .with("scoreOutOf5", activities.get(i).getScoreOutOf5())
                        .with("imageUrl", activities.get(i).getImageUrl())
                        .with("reviewScoreThresholdMet", activities.get(i).isReviewScoreThresholdMet())
                        .with("cityName", cityName)
                        .with("preferences", prefStr);

                table.putItem(item);
            }
        }
        catch (Exception e) {
            return e.getMessage();
        }

        return "";
    }

    private ActivityResponse fillActivityDetails(Long id, ActivityResponse aR) {
        MultivaluedMap<String, Object> map = new MultivaluedHashMap<String, Object>();
        map.add("key", "4f8ce657-ee06-4527-a8d8-4b207f8f0d62");
        map.add("Content-Type", "application/json");

        String REST_LX_API = "https://apim.expedia.com/x/activities/activity?activityId="+ id;
        Client lxApiClient = ClientBuilder.newClient();

        SimpleActivity simpleActivity = lxApiClient.target(REST_LX_API).request(MediaType.APPLICATION_JSON).headers(map).get(SimpleActivity.class);
        if(simpleActivity != null) {
            aR.setFreeCancellation(simpleActivity.isFreeCancellation());
            aR.setInclusions(simpleActivity.getInclusions());
            aR.setStaticMapUrl("https:" + simpleActivity.getStaticMapUrl());
        }
        return aR;
    }

    private void sendMail(Response resValue) {
        String REST_MAIL_API = "https://1jnild4xx1.execute-api.us-east-1.amazonaws.com/test";
        Client lxApiClient = ClientBuilder.newClient();
        javax.ws.rs.core.Response res = lxApiClient.target(REST_MAIL_API).
                request(MediaType.APPLICATION_JSON).post(Entity.entity(resValue, MediaType.APPLICATION_JSON));
        int a = 0;
    }

    private List<ActivityResponse> getDummy() {
        List<ActivityResponse> aRs = new ArrayList<ActivityResponse>();
        ActivityResponse aR = new ActivityResponse();
        aR.setActivityId(330802L);
        aR.setActivityName("Camden Pub Crawl");
        aR.setFromPrice("$16");
        aR.setDuration("4 hours");
        aR.setScore("2.7");
        aR.setImageUrl("https:" + "https://a.travel-assets.com/lxweb/media-vault/330802_m.jpeg?v=106335");
        WeatherResponse w = new WeatherResponse();
        w.setDescription("broken clouds");
        w.setTemp(20);
        aR.setWeather(w);
        List<String> incl = new ArrayList<>();
        incl.add("<p>Guided pub crawl of Camden</p>");
        incl.add("<p>Skip-the-Line VIP entrance to all venues</p>");
        aR.setInclusions(incl);
        aR.setStaticMapUrl("https://maps.googleapis.com/maps/api/staticmap?client=gme-expedia&channel=expedia-LXInformation&size=528x297&maptype=roadmap&sensor=false&format=png8&scale=2&center=51.5390111,-0.1425553&markers=color:red%7C51.5390111,-0.1425553&markers=color:blue%7C51.5360463,-0.1396272&signature=J2ycLyt9e5oRfS9h0cV9r6lBhYs=");
        aR.setFreeCancellation(true);
        aRs.add(aR);
        return aRs;
    }
}

