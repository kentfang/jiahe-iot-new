package com.jiahe.iot.common.constant;

public interface ResultCode {

    int suc = 200;
    int fail = 400;

    int token_not_have = 401;
    int token_out_of_time = 402;
    int user_is_not_exit = 403;
    int user_password_is_not_equals = 404;
    int user_is_already_exit = 405;
    int user_or_password_is_null = 406;

    int area_name_is_null = 410;
    int area_name_is_exit = 411;
    int area_id_is_exit = 412;
    int area_is_not_exit = 413;

    int product_param_miss = 420;
    int product_is_exit = 421;


    int device_secret_is_not_exit = 430;
    int device_decrypt_is_not_suc = 431;
    int device_decrypt_data_is_err = 432;
    int device_id_is_not_err = 433;


    int request_too_more = 433;


}
