package com.zhangsong.com.signup_system.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.BezierPagerIndicator;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.CommonNavigator;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.CommonNavigatorAdapter;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.ExamplePagerAdapter;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.IPagerIndicator;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.IPagerTitleView;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.MagicIndicator;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.ScaleTransitionPagerTitleView;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.SimplePagerTitleView;
import com.zhangsong.com.signup_system.base.TabLayoutViewPage.ViewPagerHelper;

import java.util.Arrays;
import java.util.List;

public class ManiClassificationFragment extends Fragment {
    private static final String[] CHANNELS = new String[]{"office2010",
            "PhotoShopCS5", "ASP.net", "AutoCAD2010(建筑)",
            "AutoCAD2005(建筑)", "AutoCAD2010(机械)"};

    private List<String> mDataList = Arrays.asList(CHANNELS);
    private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);
    private ViewPager mViewPager;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classification, container, false);
        mViewPager=view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mExamplePagerAdapter);
        initMagicIndicator6();
        return view;
    }

    private void initMagicIndicator6() {
        MagicIndicator magicIndicator = view.findViewById(R.id.magic_indicator6);
//        magicIndicator.setBackgroundColor(Color.parseColor("#336699"));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.WHITE);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.yellow));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(Color.parseColor("#ff4a42"),
                        Color.parseColor("#fcde64"),
                        Color.parseColor("#73e8f4"),
                        Color.parseColor("#76b0ff"),
                        Color.parseColor("#c683fe"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
}