package com.gameotaku.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.gameotaku.app.R;
import com.gameotaku.app.db.GameType;

import java.util.List;

/**
 * Created by Vincent on 9/14/14.
 */
public class GameTypeAdapter extends ArrayAdapter<GameType> {

    private Context mContext;
    private int layoutResourceId;
    private TypeChooseListener mListener;

    public GameTypeAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        layoutResourceId = resource;
    }

    public GameTypeAdapter(Context context, int resource, TypeChooseListener listener) {
        super(context, resource);
        mContext = context;
        layoutResourceId = resource;
        mListener = listener;
    }

    public void updateData(List<GameType> data) {
        clear();
        if (data != null) {
            for (GameType gameType : data) {
                add(gameType);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItem = convertView;
        ViewHolder viewHolder;

        if (viewItem == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            viewItem = inflater.inflate(layoutResourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (Button) viewItem.findViewById(R.id.button);

            viewItem.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) viewItem.getTag();
        }

        final GameType gameType = getItem(position);
        viewHolder.title.setText(gameType.getType_name());
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.setTypeChooser(gameType);
                }
            }
        });

        return viewItem;
    }

    public interface TypeChooseListener {
        public void setTypeChooser(GameType gameType);
    }

    private static class ViewHolder {
        public Button title;
    }
}
