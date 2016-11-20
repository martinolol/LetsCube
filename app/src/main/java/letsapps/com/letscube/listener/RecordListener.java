package letsapps.com.letscube.listener;

import java.util.ArrayList;

import letsapps.com.letscube.util.DatabaseTime;

public interface RecordListener {
    void newRecords(DatabaseTime time, ArrayList<Integer> recordsTypeId, ArrayList<Integer> previousRecordTime);
    void removedRecord(DatabaseTime time, int recordTypeId);
}
