package com.mjsoftking.wxlibrary.enumeration;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

public enum ShareType {

    /*
        SendMessageToWX.Req.WXSceneTimeline 朋友圈， SendMessageToWX.Req.WXSceneSession 好友, SendMessageToWX.Req.WXSceneFavorite 收藏
     */
    WXSceneTimeline(SendMessageToWX.Req.WXSceneTimeline, "朋友圈"),
    WXSceneSession(SendMessageToWX.Req.WXSceneSession, "好友"),
    WXSceneFavorite(SendMessageToWX.Req.WXSceneFavorite, "收藏");

    private final int type;
    private final String desc;

    ShareType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
