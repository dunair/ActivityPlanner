package org.exp.activityplanner;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
/**
 * Created by a-7775 on 7/8/18.
 */
public class PreferenceCalcEngine {
    public Set<String> getPreferences(Set<String> preferences) {
        Set<String> matchPreferences = new HashSet<>();

        for(String p: preferences) {
            switch (p) {
                case Preferences.FAMILY: matchPreferences.add("Family Friendly");
                break;
                case Preferences.ADVENTURE: matchPreferences.add("Walking & Bike Tours");
                break;
                case Preferences.EXPLORE: matchPreferences.add("Tours & Sightseeing");
                    matchPreferences.add("Day Trips & Excursions");
                    matchPreferences.add("Sightseeing Passes");
                    matchPreferences.add("Hop-on Hop-off");
                    matchPreferences.add("Multi-Day & Extended Tours");
                    matchPreferences.add("Attractions");
                break;
                case Preferences.EAT: matchPreferences.add("Food & Drink");
                break;
                case Preferences.NIGHT: matchPreferences.add("Nightlife");
                break;
                case Preferences.SHOWS: matchPreferences.add("Show & Sport Tickets");
                break;
                case Preferences.WATER: matchPreferences.add("Cruises & Water Tours");
                break;
                case Preferences.SHORT: matchPreferences.add("short");
                break;
                case Preferences.ALL: matchPreferences.add("all");
                break;
            }
        }

        return matchPreferences;
    }
}
