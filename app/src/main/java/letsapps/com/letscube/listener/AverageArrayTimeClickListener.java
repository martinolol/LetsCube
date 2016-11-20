package letsapps.com.letscube.listener;

import letsapps.com.letscube.util.DatabaseTime;

public interface AverageArrayTimeClickListener {
    /**
     * Prevents that a time is clicked on the averageArrayView.
     *
     * @param concernedTimes the times that constitutes the mean or average or single(Mo1)
     */
    void OnClickTime(DatabaseTime[] concernedTimes);
}
