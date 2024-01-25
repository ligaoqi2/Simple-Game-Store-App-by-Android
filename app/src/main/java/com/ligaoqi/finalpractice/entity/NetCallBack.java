package com.ligaoqi.finalpractice.entity;

public interface NetCallBack<T> {
    void onSuccess(T data);
    void onFailure(int code, String  msg);
}
