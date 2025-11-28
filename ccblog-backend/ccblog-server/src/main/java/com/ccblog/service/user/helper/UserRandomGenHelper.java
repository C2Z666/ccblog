package com.ccblog.service.user.helper;

import java.util.Random;

/**
 * 头像选择器
 *
 * @author czc
 * @date 2025-11-27
 */
public class UserRandomGenHelper {

    private static final String AVATAR_DEFAULT = "https://i.cetsteam.com/imgs/2025/11/27/a16e212c8b951236.png";
    private static final String AVATAR_TEMPLATE = "https://i.cetsteam.com/imgs/%s.png";
    public static final String[] AVATAR_ITEMS = {
            "2025/11/27/0a5ecce44a8a64a8",
            "2025/11/27/86fdabbc593c8b06",
            "2025/11/27/a16e212c8b951236",
            "2025/11/27/623173e3270eab78",
            "2025/11/27/7cbd1c0f58cb4dc9",
            "2025/11/27/9385ff8d74cdd9a9",
            "2025/11/27/6ff159197419ca28",
            "2025/11/27/2e2c4efa536f99b0",
            "2025/11/27/8a68f729852f07ca",
            "2025/11/27/b621bf0bbeee0cff",
            "2025/11/27/5ae0b2d88cef0574",
            "2025/11/27/18553dd381879e89",
            "2025/11/27/ed6facb252ba6c02",
            "2025/11/27/7a0bc44b4ad1bfdf",
            "2025/11/27/6eedc12d1271beb0",
            "2025/11/27/f5243e130aa61daa",
            "2025/11/27/9499cd502675250e",
            "2025/11/27/64471d0a08522416",
            "2025/11/27/54ffe856a29e8c55",
            "2025/11/27/0192fbe036361d86",
            "2025/11/27/8903e4530dc647bc",
            "2025/11/27/4bbe3d863dc86cdd"
    };
    private static final int AVATAR_NUM = AVATAR_ITEMS.length;

    private static final Random RANDOM = new Random();

    /**
     * 头像自动选择
     *
     * @return
     */
    public static String genAvatar() {
        int idx = RANDOM.nextInt(AVATAR_NUM);
        return String.format(AVATAR_TEMPLATE, AVATAR_ITEMS[idx]);
    }
}
