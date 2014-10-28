package com.gameotaku.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gameotaku.app.R;
import com.gameotaku.app.db.dao.WebGame;
import com.gameotaku.app.service.Constants;
import com.gameotaku.app.ui.activity.BrowserActivity;
import com.gameotaku.app.util.AjustImageUtils;

import java.util.List;

/**
 * Created by Vincent on 8/29/14.
 */
public class WebGameAdapter extends ArrayAdapter<WebGame> {

    private Context mContext;
    private int layoutResourceId;

    public WebGameAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        layoutResourceId = resource;
    }

    public void updateData(List<WebGame> data) {
        clear();
        if (data != null) {
            for (WebGame webGame : data) {
                add(webGame);
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
            viewHolder.rank = (TextView) viewItem.findViewById(R.id.textView_rank);
            viewHolder.icon = (ImageView) viewItem.findViewById(R.id.iconImageView);
            viewHolder.title = (TextView) viewItem.findViewById(R.id.titleTextView);
            viewHolder.player_count = (TextView) viewItem.findViewById(R.id.countTextView);
            viewHolder.play = (Button) viewItem.findViewById(R.id.button_play);
            viewHolder.desc = (LinearLayout) viewItem.findViewById(R.id.list_item_desc);
            viewHolder.desc_content = (TextView) viewItem.findViewById(R.id.list_item);

            viewItem.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) viewItem.getTag();
        }

        final WebGame webGame = getItem(position);

        viewHolder.rank.setText(String.valueOf(position + 1));

        AjustImageUtils.getInstance()
                .loadImage(webGame.getItem_id(), webGame.getIcon(), viewHolder.icon, "w_icon", 60);

        viewHolder.title.setText(webGame.getName());
        viewHolder.player_count.setText(webGame.getPlayer_count() + " 人在玩");

        viewHolder.desc.setVisibility(View.GONE);

        viewHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BrowserActivity.class);
                intent.putExtra(Constants.Intent.BROWSER_ITEM_ID, webGame.getItem_id());
                intent.putExtra(Constants.Intent.BROWSER_ITEM_NAME, webGame.getName());
                intent.putExtra(Constants.Intent.BROWSER_ITEM_URL, webGame.getRequest_url());
                intent.putExtra(Constants.Intent.BROWSER_ITEM_ICON, webGame.getIcon());
                mContext.startActivity(intent);
            }
        });

        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BrowserActivity.class);
                intent.putExtra(Constants.Intent.BROWSER_ITEM_ID, webGame.getItem_id());
                intent.putExtra(Constants.Intent.BROWSER_ITEM_NAME, webGame.getName());
                intent.putExtra(Constants.Intent.BROWSER_ITEM_URL, webGame.getRequest_url());
                intent.putExtra(Constants.Intent.BROWSER_ITEM_ICON, webGame.getIcon());
                mContext.startActivity(intent);
            }
        });

        return viewItem;
    }

    private static class ViewHolder {
        public TextView rank;
        public ImageView icon;
        public TextView title;
        public TextView player_count;
        public Button play;
        public LinearLayout desc;
        public TextView desc_content;
    }
}
