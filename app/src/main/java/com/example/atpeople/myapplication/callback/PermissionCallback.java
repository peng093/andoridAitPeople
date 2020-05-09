package com.example.atpeople.myapplication.callback;

import java.util.List;

/**
 * Create by peng on 2020/5/9
 */
public interface PermissionCallback {
    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
