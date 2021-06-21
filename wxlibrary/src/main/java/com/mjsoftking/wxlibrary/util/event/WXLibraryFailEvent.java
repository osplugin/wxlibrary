package com.mjsoftking.wxlibrary.util.event;

public class WXLibraryFailEvent extends WXLibraryBaseEvent {
    private final int wxErrCode;
    private final String wxErrStr;

    public WXLibraryFailEvent(int wxErrCode, String wxErrStr) {
        this.wxErrStr = wxErrStr;
        this.wxErrCode = wxErrCode;
    }

    public int getWxErrCode() {
        return wxErrCode;
    }

    public String getWxErrStr() {
        return wxErrStr;
    }
}
