package com.appunite.mialarm.helpers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import com.appunite.mialarm.R;
import com.appunite.mialarm.model.SmallAlarm;

import rx.Observer;

public class DialogHelper {

    public static void showTimePickerDialog(Context context, int minute, int hour,
                                            final Observer<SmallAlarm> observer) {
        final TimePickerDialog timePicker;
        timePicker = new TimePickerDialog(
                context,
                R.style.DefaultPopupStyle,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        observer.onNext(new SmallAlarm(selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
        timePicker.setTitle("");
        timePicker.show();
    }

}

