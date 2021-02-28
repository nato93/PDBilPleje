package dk.aau.hr.pdbilpleje.Homepage.Model;

public class BookingEventModel {

    private String bookinEventID;
    private String date;
    private String userId;
    private String title;
    private String desc;

    public BookingEventModel() {
        bookinEventID = "null";
        date = "null";
        title = "null";
        desc = "null";
        userId = "null";
    }

    public BookingEventModel(String bookinEventID, String date, String userId, String title,
                             String desc, int startingHour, int endingHour, String startingHourM, String endingHourM) {
        this.bookinEventID = bookinEventID;
        this.date = date;
        this.userId = userId;
        this.title = title;
        this.desc = desc;
    }

    public String getBookinEventID() {
        return bookinEventID;
    }

    public void setBookinEventID(String bookinEventID) {
        this.bookinEventID = bookinEventID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
