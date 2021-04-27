package com.fanwe.live.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDViewHolderAdapter;
import com.fanwe.library.adapter.viewholder.SDViewHolder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.JoinPlayBackData;
import com.fanwe.live.model.LivePlaybackModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class LiveTabFollowAdapter extends SDViewHolderAdapter<Object>
{
    private static final int TOTAL_NEED_FIND_TYPE_COUNT = 2;
    private static SparseArray<Integer> arrTypeFirstPositon = new SparseArray<>();

    public LiveTabFollowAdapter(List<Object> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public SDViewHolder<Object> onCreateVHolder(int position, View convertView, ViewGroup parent)
    {
        int type = getItemViewType(position);
        SDViewHolder viewHolder = null;
        switch (type)
        {
            case 0:
                viewHolder = new ViewHolderLiveRoom();
                break;
            case 1:
                viewHolder = new LiveTabHotViewHolder.ViewHolderPlayback();
                break;
            default:

                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindData(int position, View convertView, ViewGroup parent, Object model, SDViewHolder<Object> holder)
    {

    }

    @Override
    public void setData(List<Object> list)
    {
        arrTypeFirstPositon.clear();
        super.setData(list);
        findFirstPosition();
    }

    private void findFirstPosition()
    {
        int findTypeCount = 0;
        int i = 0;
        for (Object item : getData())
        {
            int type = getItemViewType(i);
            boolean needFind = false;
            if (item instanceof LiveRoomModel)
            {
                needFind = true;
            } else if (item instanceof LivePlaybackModel)
            {
                needFind = true;
            }

            if (needFind)
            {
                Integer typePos = arrTypeFirstPositon.get(type);
                if (typePos == null)
                {
                    arrTypeFirstPositon.put(type, i);
                    findTypeCount++;
                }
            }

            if (TOTAL_NEED_FIND_TYPE_COUNT == findTypeCount)
            {
                break;
            }
            i++;
        }
    }


    @Override
    public int getItemViewType(int position)
    {
        Object model = getItem(position);

        if (model instanceof LiveRoomModel)
        {
            return 0;
        } else if (model instanceof LivePlaybackModel)
        {
            return 1;
        }

        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    /**
     * 直播间
     */
    public static class ViewHolderLiveRoom extends LiveTabHotViewHolder
    {

        View ll_top;

        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent)
        {
            return R.layout.item_live_tab_follow_room;
        }

        @Override
        public void onBindData(int position, View convertView, ViewGroup parent, LiveRoomModel model)
        {
            ll_top = find(R.id.ll_top);

            int type = getAdapter().getItemViewType(position);
            Integer typePosition = arrTypeFirstPositon.get(type);
            if (typePosition != null && typePosition == position)
            {
                SDViewUtil.setVisible(ll_top);
            } else
            {
                SDViewUtil.setGone(ll_top);
            }

            super.onBindData(position, convertView, parent, model);
        }
    }

    public static class LiveTabHotViewHolder extends SDViewHolder<LiveRoomModel> {

        ImageView iv_head;
        ImageView iv_head_small;
        TextView tv_nickname;
        TextView tv_city;
        TextView tv_watch_number;
        ImageView iv_room_image;
        TextView tv_topic;
        TextView tv_live_state;
        TextView tv_live_fee;
        TextView tv_game_state;

        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent) {
            return R.layout.item_live_tab_hot;
        }

        @Override
        public void onInit(int position, View convertView, ViewGroup parent) {
            iv_head = find(R.id.iv_head);
            iv_head_small = find(R.id.iv_head_small);
            tv_nickname = find(R.id.tv_nickname);
            tv_city = find(R.id.tv_city);
            tv_watch_number = find(R.id.tv_watch_number);
            iv_room_image = find(R.id.iv_room_image);
            tv_topic = find(R.id.tv_topic);
            tv_live_state = find(R.id.tv_live_state);
            tv_live_fee = find(R.id.tv_live_fee);
            tv_game_state = find(R.id.tv_game_state);
        }

        @Override
        public void onBindData(int position, View convertView, ViewGroup parent, final LiveRoomModel model) {
            GlideUtil.loadHeadImage(model.getHead_image()).into(iv_head);
            if (!TextUtils.isEmpty(model.getV_icon())) {
                SDViewUtil.setVisible(iv_head_small);
                GlideUtil.load(model.getV_icon()).into(iv_head_small);
            } else {
                SDViewUtil.setGone(iv_head_small);
            }
            SDViewBinder.setTextViewVisibleOrGone(tv_live_state, model.getLive_state());
            SDViewBinder.setTextViewVisibleOrGone(tv_live_fee, model.getLivePayFee());

            SDViewBinder.setTextView(tv_nickname, model.getNick_name());
            SDViewBinder.setTextView(tv_city, model.getCity());
            SDViewBinder.setTextView(tv_watch_number, model.getWatch_number());
            GlideUtil.load(model.getLive_image()).into(iv_room_image);

            if (model.getCate_id() > 0) {
                SDViewBinder.setTextView(tv_topic, model.getTitle());
                SDViewUtil.setVisible(tv_topic);
            } else {
                SDViewUtil.setGone(tv_topic);
            }

            if (model.getIs_gaming() == 1) {
                SDViewUtil.setVisible(tv_game_state);
                SDViewBinder.setTextView(tv_game_state, model.getGame_name());
            } else {
                SDViewUtil.setGone(tv_game_state);
            }

            iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, model.getUser_id());
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, model.getHead_image());
                    getActivity().startActivity(intent);
                }
            });
            iv_room_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppRuntimeWorker.joinRoom(model, getActivity());
                }
            });
        }

        /**
         * 回放
         */
        public static class ViewHolderPlayback extends SDViewHolder<LivePlaybackModel> {

            View ll_top;

            LinearLayout ll_content;
            ImageView iv_head;
            ImageView iv_head_small;
            TextView tv_nickname;
            TextView tv_create_time;
            TextView tv_watch_number;
            TextView tv_topic;

            @Override
            public int getLayoutId(int position, View convertView, ViewGroup parent) {
                return R.layout.item_live_tab_follow_playback;
            }

            @Override
            public void onInit(int position, View convertView, ViewGroup parent) {
                ll_top = find(R.id.ll_top);
                ll_content = find(R.id.ll_content);
                iv_head = find(R.id.iv_head);
                iv_head_small = find(R.id.iv_head_small);
                tv_nickname = find(R.id.tv_nickname);
                tv_create_time = find(R.id.tv_create_time);
                tv_watch_number = find(R.id.tv_watch_number);
                tv_topic = find(R.id.tv_topic);
            }

            @Override
            public void onBindData(int position, View convertView, ViewGroup parent, final LivePlaybackModel model) {
                int type = getAdapter().getItemViewType(position);
                Integer typePosition = arrTypeFirstPositon.get(type);
                if (typePosition != null && typePosition == position) {
                    SDViewUtil.setVisible(ll_top);
                } else {
                    SDViewUtil.setGone(ll_top);
                }

                GlideUtil.loadHeadImage(model.getHead_image()).into(iv_head);
                if (!TextUtils.isEmpty(model.getV_icon())) {
                    SDViewUtil.setVisible(iv_head_small);
                    GlideUtil.load(model.getV_icon()).into(iv_head_small);
                } else {
                    SDViewUtil.setGone(iv_head_small);
                }

                SDViewBinder.setTextView(tv_nickname, model.getNick_name());
                SDViewBinder.setTextView(tv_create_time, model.getBegin_time_format());
                SDViewBinder.setTextView(tv_watch_number, model.getWatch_number_format());
                SDViewBinder.setTextView(tv_topic, model.getTitle());

                ll_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JoinPlayBackData data = new JoinPlayBackData();
                        data.setRoomId(model.getRoom_id());
                        AppRuntimeWorker.joinPlayback(data, getActivity());
                    }
                });
            }
        }
    }
}
