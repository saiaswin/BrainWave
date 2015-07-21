package com.code.scanner;

/**
 * Created by saiaswin on 20/7/15.
 */
public interface ScanListener {
    void onSuccess(String scanData);

    void onError(String errorMsg);
}
