package com.sam_chordas.android.stockhawk.data;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jinal.
 */
public class WidgetStockDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List mCollections = new ArrayList();
    Context mContext = null;
    boolean isConnected;
    private Intent mServiceIntent;
    private QuoteCursorAdapter mCursorAdapter;
    private Cursor mCursor;

    public WidgetStockDataProvider(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {

        initData();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }


        RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                R.layout.list_item_quote_widget);
        mView.setTextViewText(R.id.stock_symbol, mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)));
        mView.setTextViewText(R.id.bid_price, mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));

        Intent intent = new Intent(mContext, MyStocksActivity.class);

        /*intent.putExtra(mContext.getResources().getString(R.string.symbol),
                mCursor.getString(mCursor.getColumnIndex(mContext.getResources().getString(R.string.symbol))));*/

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        mView.setOnClickPendingIntent(R.id.widget_row, pendingIntent);

        return mView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {



        try {

            final long identityToken = Binder.clearCallingIdentity();

            // This is the same query from MyStocksActivity
            mCursor = mContext.getContentResolver().query(
                    QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{
                            QuoteColumns._ID,
                            QuoteColumns.SYMBOL,
                            QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE,
                            QuoteColumns.CHANGE,
                            QuoteColumns.ISUP
                    },
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);
            Binder.restoreCallingIdentity(identityToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= 10; i++) {
            mCollections.add("ListView item " + i);
        }

    }


    public void networkToast() {
        Toast.makeText(mContext, mContext.getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
    }
}
