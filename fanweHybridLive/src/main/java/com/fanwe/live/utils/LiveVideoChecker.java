package com.fanwe.live.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.span.SDPatternUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.Video_check_statusActModel;

import java.util.List;

/**
 * Created by L on 2016/8/28.
 */
public class LiveVideoChecker
{
    private Activity activity;
    private int roomId;
    private String strPrivateKey;
    private boolean isPrivateKeyMode;

    public LiveVideoChecker(Activity activity)
    {
        this.activity = activity;
    }

    public void check(int roomId)
    {
        if (roomId <= 0)
        {
            return;
        }
        this.roomId = roomId;
        isPrivateKeyMode = false;
        check();
    }

    public void check(String copyContent)
    {
        if (TextUtils.isEmpty(copyContent))
        {
            return;
        }

        String tag = LiveConstant.LIVE_PRIVATE_KEY_TAG;
        List<Integer> listPosition = SDPatternUtil.findPosition(copyContent, tag);
        if (listPosition == null)
        {
            return;
        }
        if (listPosition.size() != 2)
        {
            return;
        }

        String key = copyContent.substring(copyContent.indexOf(tag) + tag.length(), copyContent.lastIndexOf(tag));
        if (TextUtils.isEmpty(key))
        {
            return;
        }

        this.strPrivateKey = key;
        SDOtherUtil.copyText("");

        isPrivateKeyMode = true;
        check();
    }

    private void check()
    {
        if (activity == null)
        {
            return;
        }

        tryCheck();
    }

    protected void tryCheck()
    {
        CommonInterface.requestCheckVideoStatus(roomId, strPrivateKey, new AppRequestCallback<Video_check_statusActModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    int newRoomId = actModel.getRoom_id();
                    int oldRoomId = LiveInformation.getInstance().getRoomId();

                    if (actModel.getLive_in() == 1)
                    {
                        if (oldRoomId > 0)
                        {
                            if (LiveInformation.getInstance().isCreater())
                            {
                                // ??????????????????
                                if (isPrivateKeyMode)
                                {
                                    // ???????????????????????????
                                } else
                                {
                                    // ???????????????????????????
                                    SDToast.showToast("?????????????????????????????????????????????????????????");
                                }
                            } else
                            {
                                // ????????????????????????
                                if (oldRoomId == newRoomId)
                                {
                                    if (isPrivateKeyMode)
                                    {
                                        // ???????????????????????????
                                    } else
                                    {
                                        // ???????????????????????????
                                        SDToast.showToast("?????????????????????");
                                    }
                                } else
                                {
                                    showCheckDialog(actModel);
                                }
                            }
                        } else
                        {
                            showCheckDialog(actModel);
                        }
                    } else
                    {
                        showCheckDialog(actModel);
                    }
                }
            }
        });
    }

    private void showCheckDialog(final Video_check_statusActModel actModel)
    {
        AppDialogConfirm dialog = new AppDialogConfirm(activity);
        dialog.setTextContent(actModel.getContent()).setTextCancel("??????");
        if (actModel.getLive_in() == 1)
        {
            dialog.setTextConfirm("????????????");
        } else
        {
            dialog.setTextConfirm("????????????");
        }
        dialog.setCallback(new ISDDialogConfirm.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {

            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog)
            {
                clickConfirm(actModel);
            }
        }).show();
    }

    protected void clickConfirm(Video_check_statusActModel actModel)
    {
        if (actModel.getLive_in() == 1)
        {
            // TODO ??????????????????
            JoinLiveData data = new JoinLiveData();
            data.setRoomId(actModel.getRoom_id());
            data.setGroupId(actModel.getGroup_id());
            data.setCreaterId(actModel.getUser_id());
            data.setLoadingVideoImageUrl(actModel.getLive_image());
            data.setPrivateKey(strPrivateKey);
            data.setSdkType(actModel.getSdk_type());
            AppRuntimeWorker.joinLive(data, activity);
        } else
        {
            // TODO ??????????????????
            Intent intent = new Intent(activity, LiveUserHomeActivity.class);
            intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, actModel.getUser_id());
            activity.startActivity(intent);
        }
    }


}
