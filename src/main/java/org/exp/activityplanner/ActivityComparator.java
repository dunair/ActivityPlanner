package org.exp.activityplanner;

import java.util.Comparator;
/**
 * Created by a-7775 on 7/10/18.
 */
public class ActivityComparator implements Comparator<Activity>{
    public int compare(Activity a, Activity b) {
        return a.getScoreOutOf5().compareTo(b.getScoreOutOf5());
    }
}
