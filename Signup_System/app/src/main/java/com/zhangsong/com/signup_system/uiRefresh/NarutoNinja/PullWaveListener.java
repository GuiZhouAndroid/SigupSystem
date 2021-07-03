package com.zhangsong.com.signup_system.uiRefresh.NarutoNinja;

/**
 * 涟漪回调监听
 */
public interface PullWaveListener {
    /**
     * 下拉中
     * @param refreshLayout
     * @param fraction
     */
    void onPulling(RefreshLayout refreshLayout, float fraction);

    /**
     * 下拉松开
     * @param refreshLayout
     * @param fraction
     */
    void onPullReleasing(RefreshLayout refreshLayout, float fraction);
}
