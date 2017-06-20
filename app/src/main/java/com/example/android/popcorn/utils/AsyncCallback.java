package com.example.android.popcorn.utils;

import android.support.annotation.Nullable;

public interface AsyncCallback<T> {
    void asyncCallback(@Nullable final T data);
}
