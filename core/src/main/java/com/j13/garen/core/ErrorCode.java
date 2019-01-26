package com.j13.garen.core;

public class ErrorCode {

    public static class Common {
        public static int INPUT_PARAMETER_ERROR = 4;
        public static int NEED_T = 5;
    }

    public static class User {
        public static int PASSWORD_NOT_RIGHT = 1001;
        public static int MOBILE_EXISTED = 1002;
        public static int NICKNAME_EXISTED = 1003;    // 已经去掉了
        public static int NEED_LOGIN = 1004;
    }


    public static class Account {
        public static int PASSWORD_NOT_RIGHT = 2001;
        public static int NAME_EXISTED = 2003;
    }

    public static class System {
        public static int SYSTEM_ERROR = 1;
        public static int NOT_FOUND_ACTION = 2;
        public static int ACTION_REFLECT_ERROR = 3;
        public static int REFLECT_ERROR = 4;
        public static int PARSE_REQUEST_POST_DATA_ERROR = 5;
    }


    public static class Item {
        public static int NO_ITEM_RETURN = 4001;
    }

}
