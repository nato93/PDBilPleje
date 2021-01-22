package dk.aau.hr.pdbilpleje.Homepage;

import dk.aau.hr.pdbilpleje.Homepage.Model.BookingEventModel;

public interface BookingEventCallback {

    void onBookingEventReceive(BookingEventModel bem);

}
