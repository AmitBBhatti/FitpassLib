package com.fitpass.libfitpass.base.http_client;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "Network Connection exception";
    }
}
