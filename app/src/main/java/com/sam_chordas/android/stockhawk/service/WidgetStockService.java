package com.sam_chordas.android.stockhawk.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.data.WidgetStockDataProvider;

/**
 * Created by Jinal
 */
public class WidgetStockService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        WidgetStockDataProvider dataProvider = new WidgetStockDataProvider(
                getApplicationContext(), intent);
        return dataProvider;
    }
}
