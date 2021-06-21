package com.mjsoftking.wxlibrary.util.event;

public class WXLibrarySucceedEvent extends WXLibraryBaseEvent {

    private final String code;

    public WXLibrarySucceedEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
