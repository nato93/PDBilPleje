package dk.aau.hr.pdbilpleje;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dk.aau.hr.pdbilpleje.Homepage.BookingEventCallback;
import dk.aau.hr.pdbilpleje.Homepage.Model.BookingEventModel;

public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {

    private MainActivity mActivity;
    private BookingEventCallback bec;

    private static final String TAG = "ApiAsyncTask";

    ApiAsyncTask(MainActivity activity, BookingEventCallback bec) {
        this.mActivity = activity;
        this.bec = bec;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
//            mActivity.clearResultsText();
//            mActivity.updateResultsText(getDataFromApi());
            saveBookingEvent();
        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            Log.e(TAG, "doInBackground: availabilityException");
            mActivity.showGooglePlayServicesAvailabilityErrorDialog(availabilityException.getConnectionStatusCode());
        } catch (UserRecoverableAuthIOException userRecoverableException) {
            Log.e(TAG, "doInBackground: userRecoverableException");
            mActivity.startActivityForResult(userRecoverableException.getIntent(), MainActivity.REQUEST_AUTHORIZATION);
        } catch (IOException e) {
            mActivity.updateStatus("The following error occurred: " + e.getMessage());
        }
        return null;
    }

    private List<String> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Events events = mActivity.mService.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        if (items.isEmpty()) {
            Log.e(TAG, "getDataFromApi: event items are empty");
        }

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
//            eventStrings.add(String.format("%s (%s)", event.getSummary(), start));
        }
        return eventStrings;
    }


    private void saveBookingEvent() throws IOException {

        Log.e(TAG, "saveBookingEvent: ==============================");

        BookingEventModel bem = new BookingEventModel();

        String summary = "Car Wash booking";
        String location = "Los angles Street 10-v";
        String desc = "Car wash on 22 January, 2021.";

        bem.setTitle(summary);
        bem.setDesc(desc);
        bem.setDate(new Date().toString());

        Event event = new Event().setSummary(summary).setLocation(location).setDescription(desc);

        DateTime startDateTime = new DateTime("2021-01-22T00:00:00-07:00");
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2021-01-22T00:00:00-07:00");
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

//        EventAttendee[] attendees = new EventAttendee[]{
//                new EventAttendee().setEmail("170427@students.au.edu.pk"),
//                new EventAttendee().setEmail("170247@students.au.edu.pk")
////                new EventAttendee().setEmail("sbrin@example.com"),
//        };
//        event.setAttendees(Arrays.asList(attendees));

//        EventReminder[] reminderOverrides = new EventReminder[]{
//                new EventReminder().setMethod("email").setMinutes(24 * 60),
//                new EventReminder().setMethod("popup").setMinutes(10),
//        };
//        Event.Reminders reminders = new Event.Reminders()
//                .setUseDefault(false)
//                .setOverrides(Arrays.asList(reminderOverrides));
//        event.setReminders(reminders);

        String calendarId = "primary";

        event = mActivity.mService.events().insert(calendarId, event).execute();

        System.out.printf("Event created: %s\n", event.getHtmlLink());

        bec.onBookingEventReceive(bem);

        Log.e(TAG, "saveBookingEvent: ==============================");

    }


}
