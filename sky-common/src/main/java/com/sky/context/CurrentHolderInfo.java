package com.sky.context;

/**
 * 记录当前登录员工
 */
public class CurrentHolderInfo {

    private static final ThreadLocal<Long> CURRENT_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Long> REQUEST_INITIATOR = new ThreadLocal<>();

    public static final Long MANAGEMENT_SIDE = 1L;
    public static final Long USER_SIDE = 2L;

    /**
     * 设置当前请求账户信息
     *
     * @param empId            当前登录账户ID
     * @param requestInitiator 当前登录账户前端类型（管理端、用户端）
     */
    public static void setCurrentHolder(Long empId, Long requestInitiator) {
        CURRENT_HOLDER.set(empId);
        REQUEST_INITIATOR.set(requestInitiator);
    }

    /**
     * 获取登录账户ID
     *
     * @return 返回当前登录账户ID
     */
    public static Long getCurrentHolder() {
        return CURRENT_HOLDER.get();
    }

    /**
     * 获取登录账户所使用客户端
     *
     * @return 客户端代码
     */
    public static Long getRequestInitiator() {
        return REQUEST_INITIATOR.get();
    }

    /**
     * 移除ThreadLocal
     */
    public static void removeCurrentHolder() {
        CURRENT_HOLDER.remove();
        REQUEST_INITIATOR.remove();
    }
}
