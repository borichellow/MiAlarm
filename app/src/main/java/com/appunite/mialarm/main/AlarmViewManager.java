package com.appunite.mialarm.main;

import android.content.res.Resources;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appunite.mialarm.R;
import com.appunite.rx.android.adapter.BaseAdapterItem;
import com.appunite.rx.android.adapter.ViewHolderManager;
import com.jakewharton.rxbinding.view.RxView;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

public class AlarmViewManager implements ViewHolderManager {

    @Nonnull
    private final Resources resources;

    @Inject
    public AlarmViewManager(@Nonnull Resources resources) {
        this.resources = resources;
    }

    @Override
    public boolean matches(@Nonnull BaseAdapterItem baseAdapterItem) {
        return baseAdapterItem instanceof MainPresenter.AlarmItem;
    }

    @Nonnull
    @Override
    public BaseViewHolder createViewHolder(@Nonnull ViewGroup viewGroup, @Nonnull LayoutInflater layoutInflater) {
        return new Holder(layoutInflater.inflate(R.layout.alarm_item, viewGroup, false));
    }

    public class Holder extends BaseViewHolder<MainPresenter.AlarmItem> {

        @BindView(R.id.alarm_item_time)
        TextView time;
        @BindView(R.id.alarm_item_switcher)
        SwitchCompat onSwitcher;
        @BindView(R.id.alarm_item_days)
        View daysView;

        @Nonnull
        private final SerialSubscription subscription = new SerialSubscription();

        public Holder(@Nonnull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onViewRecycled() {
            super.onViewRecycled();

            subscription.set(Subscriptions.empty());
        }

        @Override
        public void bind(@Nonnull MainPresenter.AlarmItem passengerItem) {

            //For now without repeats
            daysView.setVisibility(View.GONE);

            subscription.set(Subscriptions.from(

            ));
        }

    }
}
