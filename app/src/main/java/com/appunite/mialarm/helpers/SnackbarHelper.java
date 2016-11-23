package com.appunite.mialarm.helpers;

import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.appunite.mialarm.R;

import javax.annotation.Nonnull;

public class SnackbarHelper {

    public static void showSnackBar(View rootView, String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBar(View rootView, @StringRes int textId) {
        Snackbar.make(rootView, textId, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLongSnackBar(View rootView, String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showLongSnackBar(View rootView, @StringRes int textId) {
        Snackbar.make(rootView, textId, Snackbar.LENGTH_LONG).show();
    }

    public static void showErrorSnackBar(final @Nonnull View containerView,
                                         final @Nonnull String message) {
        final Snackbar snackbar = Snackbar.make(containerView, message, Snackbar.LENGTH_LONG);
        final View view = snackbar.getView();
        view.setBackgroundColor(ActivityCompat.getColor(containerView.getContext(), R.color.error_snackbar));
        final TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(ActivityCompat.getColor(containerView.getContext(), R.color.accent));
        snackbar.show();
    }

    public static void showErrorSnackBar(final @Nonnull View containerView,
                                         @StringRes int textId) {
        final Snackbar snackbar = Snackbar.make(containerView, textId, Snackbar.LENGTH_LONG);
        final View view = snackbar.getView();
        view.setBackgroundColor(ActivityCompat.getColor(containerView.getContext(), R.color.error_snackbar));
        final TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(ActivityCompat.getColor(containerView.getContext(), R.color.accent));
        snackbar.show();
    }

    public static void showErrorSnackBarWithOk(final @Nonnull View containerView,
                                               @StringRes int textId) {
        final Snackbar snackbar = Snackbar.make(containerView, textId, Snackbar.LENGTH_INDEFINITE);
        final View view = snackbar.getView();
        view.setBackgroundColor(ActivityCompat.getColor(containerView.getContext(), R.color.error_snackbar));
        final TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(ActivityCompat.getColor(containerView.getContext(), R.color.accent));
        snackbar
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public static void showErrorSnackBarWithOk(final @Nonnull View containerView,
                                               String text) {
        final Snackbar snackbar = Snackbar.make(containerView, text, Snackbar.LENGTH_INDEFINITE);
        final View view = snackbar.getView();
        view.setBackgroundColor(ActivityCompat.getColor(containerView.getContext(), R.color.error_snackbar));
        snackbar
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public static Snackbar showErrorSnackInfinite(final @Nonnull View containerView,
                                                  @StringRes int textId) {
        final Snackbar snackbar = Snackbar.make(containerView, textId, Snackbar.LENGTH_INDEFINITE);
        final View view = snackbar.getView();
        view.setBackgroundColor(ActivityCompat.getColor(containerView.getContext(), R.color.error_snackbar));
        return snackbar;
    }
}
