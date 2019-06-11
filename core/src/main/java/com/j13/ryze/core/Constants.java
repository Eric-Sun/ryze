package com.j13.ryze.core;


public class Constants {

    public static String USER_DEFAULT_AVATAR_FILENAME = "1552208882018.jpeg";
    public static String ANON_LOU = "anon_lou.jpeg";
    public static String ANON_XIA = "anon_xia.jpeg";


    public static class ResponseStatus {
        public static int SUCCESS = 0;
        public static int FAILURE = 1;
        public static int UNEXCEPED_FAILURE = 2;
    }

    public static class DB {
        public static int NOT_DELETED = 0;
        public static int DELETED = 1;
    }

    public static class OrderStatus {
        public static int QUERY_ALL_STATUS = -1;
        public static int ORDER_CREATED = 0;
    }

    public static class User {
        public static class Lock {
            public static int NO_LOCK = 0;
            public static int IS_LOCK = 1;
        }

        public static class Gender {
            public static int MAN = 1;
            public static int WOMAN = 2;
            public static int NO = 0;
        }
    }

    public static class OrderRecordActionType {
        // 创建order
        public static int ADD = 0;
        // 修改状态
        public static int UPDATE_STATUS = 1;
        // 取消订单（只能是管理员）
        public static int CANCEL = -1;
        // 上传进度图片
        public static int UPLOAD_IMG = 2;
        // 画家完成画作，在线审核
        public static int FINISH = 3;
    }

    public static class Order {
        public static int NO_PAINTER = -1;
    }

    public static class Wechat {
        public static String APPID = "wxaa4fd4f99543a61a";
        public static String AppSecret = "52af06754a9b58a9b6bb14d45f198d4f";
    }


    public static class Banner {
        public static int ONLINE = 1;
        public static int OFFLINE = 0;

    }

    public static class IMG_TYPE {
        public static int AVATAR = 0;
        public static int AVATAR_URL_FROM_WECHAT = 1;
        public static int POST_IMG = 2;
    }

    // 用户表中的用户类型
    public static class USER_SOURCE_TYPE {
        public static int MACHINE = 0;
        public static int WECHAT = 1;
    }

    public static class POST_STATUS {
        public static int ONLINE = 0;
        public static int OFFLINE = 1;
    }

    public static class POST_ANONYMOUS {
        public static int COMMON = 0;
        public static int ANONYMOUS = 1;
    }

    public static class REPLY_ANONYMOUS {
        public static int COMMON = 0;
        public static int ANONYMOUS = 1;
    }

    /**
     * 帖子类型
     */
    public static class POST_TYPE {
        public static int STORE = 0;   // 故事贴
        public static int DIARY = 1;   // 一日一记
        public static int ALL_TYPE = -1;
    }

    public static class USER_SEX {
        public static int MALE = 0;       // 男
        public static int FEMALE = 1;     // 女
    }


    public static class NOTICE {
        public static class TYPE {
            public static int REPLY_NOTICE = 1;
            public static int POST_NOTICE = 0;
        }

        public static class STATUS {
            public static int NOT_READ = 0;
            public static int READED = 1;

        }
    }

    public static class UER_MEMBER {
    }


    public static class UserLock {

        /**
         * 封号的原因类型
         */
        public static class LockReasonType {
            public static int RUMA = 1;
        }

        /**
         * 封号的操作者类型
         */
        public static class LockOperatorType {
            public static int MEMBER = 1;  // 会员
            public static int ADMIN = 2; // 管理员

        }

        /**
         * 解封的原因类型
         */
        public static class UnlockReasonType {
            public static int LOCKING = 0;  // 现在处于封号状态，默认状态
            public static int TIME_IS_OVER = 1; // 封号时间已到，自动解封状态
            public static int VOTE = 2; // 会员投票决定的
            public static int ADMIN_FORCE = 3; // admin强制解封
        }

        /**
         * 解封操作的操作者类型
         */
        public static class UnlockOperatorType {
            public static int MEMBER = 1;
            public static int ADMIN = 2;
            public static int SYSTEM = 3;
            public static int NOT_SET = 0;
        }

        public static class UnlockReason {
            public static String DEFAULT_TIMEOUT_REASON = "timeout";
            public static String DEFAULT_ADMIN_FORCE_REASON = "";
        }

    }

    public static class Fetcher {
        public static class SourceType {
            public static int TIANYA = 0;
        }

        public static class Status {
            // 未推送到库中
            public static int NOT_PUSH = 0;
            public static int PUSHED = 1;
        }
    }
}
