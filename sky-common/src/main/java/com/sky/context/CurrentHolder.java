package com.sky.context;

/**
 * 记录当前登录员工
 */
public class CurrentHolder {
    private static final ThreadLocal<Long> CURRENT_HOLDER = new ThreadLocal<>();

    /**
     * 设置登录账户ID
     *
     * @param empId 当前登录账户ID
     */
    public static void setCurrentHolder(Long empId) {
        CURRENT_HOLDER.set(empId);
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
     * 移除ThreadLocal
     */
    public static void removeCurrentHolder() {
        CURRENT_HOLDER.remove();
    }
}
