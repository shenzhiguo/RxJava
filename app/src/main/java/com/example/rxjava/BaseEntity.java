package com.example.rxjava;

/**
 * Created by shenzhiguo on 2016/9/18.
 */

public interface BaseEntity {
    public static final int BASIC_MODE = 1;
    public static final int FILTER_MODE = 2;

    public static final int EXT_MODE = 100;
    public static final int JUST_MODE = 101;
    public static final int FROM_MODE = 102;
    public static final int DEFER_MODE = 103;
    public static final int MAP_MODE = 104;
    public static final int FLAT_MAP_MODE = 105;

    public static final int EXT_FILTER_MODE = 200;
    public static final int TAKE_MODE = 201;
    public static final int TAKE_LAST_MODE = 202;
    public static final int DISTINCT_MODE = 203;
    public static final int FIRST_MODE = 204;
    public static final int LAST_MODE = 205;
    public static final int SKIP_MODE = 206;
    public static final int SKIP_LAST_MODE = 207;
    public static final int ELEMENT_AT_MODE = 208;
    public static final int SAMPLING_MODE = 209;
    public static final int TIMEOUT_MODE = 210;
    public static final int DEBOUNCE = 211;
}
