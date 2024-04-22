package train;

import java.time.LocalDateTime;

public class TimeTableRow {
    private LocalDateTime scheduledTime;
    private LocalDateTime actualTime;
    private String stationShortCode;
    private String stopType;

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public LocalDateTime getActualTime() {
        return actualTime;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public String getStopType() {
        return stopType;
    }

    public TimeTableRow(LocalDateTime addScheduledTime, LocalDateTime addActualTime, String addStationShortCode,
            String addStopType) {
        this.scheduledTime = addScheduledTime;
        this.actualTime = addActualTime;
        this.stationShortCode = addStationShortCode;
        this.stopType = addStopType;
    }
}
