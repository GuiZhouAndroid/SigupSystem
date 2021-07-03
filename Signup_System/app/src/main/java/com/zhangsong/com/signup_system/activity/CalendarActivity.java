package com.zhangsong.com.signup_system.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangsong.com.signup_system.MainActivity;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.calendar.CollapseCalendarView;
import com.zhangsong.com.signup_system.calendar.manager.CalendarManager;
import com.zhangsong.com.signup_system.utils.StatusBar.StatusBarUtils;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarActivity extends MySwipeBackActivity implements View.OnClickListener {
    private Switch switch_week_month,switch_hide_show_lunar_calendar;//切换周月+隐藏显示农历
    private LinearLayout ll_back_today;//回到今天
    private TextView tv_show_calendar,tv_show_switch_month;//选中日期显示+月份切换
    private CollapseCalendarView calendarView;
    private CalendarManager mManager;
    private JSONObject json;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");;
    private boolean show = false;
    private Toolbar toolbar_constant;
    private Snackbar sb;
    private TextView tv_toolbar_title;//标题

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();//初始化View
        initToolbar();//初始化Toolbar
        initCalendar();//初始化日历
        setCalendar();//设置日历
        setSwitchState();//设置开关逻辑
    }

    /**
     * 初始化View
     */
    private void initView() {
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(R.string.drawer_menu_see_calendar);
        toolbar_constant= (Toolbar) findViewById(R.id.toolbar_constant);//Toolbar View id
        calendarView = (CollapseCalendarView) findViewById(R.id.calendar);//获取日历类对象
        switch_week_month= (Switch) findViewById(R.id.switch_week_month);//切换周月
        switch_hide_show_lunar_calendar= (Switch) findViewById(R.id.switch_hide_show_lunar_calendar);//显示隐藏农历
        ll_back_today= (LinearLayout) findViewById(R.id.ll_back_today);//回到今天
        ll_back_today.setOnClickListener(this);
        tv_show_calendar= (TextView) findViewById(R.id.tv_show_calendar);//选中时间显示
        tv_show_switch_month= (TextView) findViewById(R.id.tv_show_switch_month);//月份切换显示
    }
    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar_constant);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用Toolbar.setNavigationIcion(null);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    /**
     * 初始化日历
     */
    private void initCalendar() {
        mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().withYear(100),
                LocalDate.now().plusYears(100));
    }
    /**
     * 设置日历
     */
    private void setCalendar() {
        /**
         * 月份切换监听器
         */
        mManager.setMonthChangeListener(new CalendarManager.OnMonthChangeListener() {
            @Override
            public void monthChange(String month, LocalDate mSelected) {
                tv_show_switch_month.setText(month);
            }
        });
        /**
         * 日期选中监听器
         */
        calendarView.setDateSelectListener(new CollapseCalendarView.OnDateSelect() {

            @Override
            public void onDateSelected(LocalDate date) {
                tv_show_calendar.setText(date.toString());
            }
        });
        /**
         * 点击标题
         */
        calendarView.setTitleClickListener(new CollapseCalendarView.OnTitleClickListener() {
            @Override
            public void onTitleClick() {
                sb = Snackbar.make(calendarView, "点宝宝干啥呢，我可没惹你哦", Snackbar.LENGTH_INDEFINITE);
                sb.setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CalendarActivity.this, "点击了确定，可以在这使用其他逻辑", Toast.LENGTH_SHORT).show();
                    }
                });
                sb.setActionTextColor(getResources().getColor(R.color.Yellow));
                View view = sb.getView();
                view.setBackgroundColor(getResources().getColor(R.color.SnackbarBackground));
                TextView tv = view.findViewById(R.id.snackbar_text);
                tv.setTextColor(getResources().getColor(R.color.White));
                sb.show();

            }
        });
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        json = new JSONObject();
        try {
            for (int i = 0; i < 30; i++) {
                JSONObject jsonObject2 = new JSONObject();
                if (i <= 6) {
                    jsonObject2.put("type", "休");
                } else if ( i > 6 && i< 11) {
                    jsonObject2.put("type", "班");
                }
                if (i%3 == 0) {
                    jsonObject2.put("list", new JSONArray());
                }

                json.put(sdf.format(cal.getTime()), jsonObject2);

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //设置数据显示
        calendarView.setArrayData(json);
        //初始化日历管理器
        calendarView.init(mManager);
    }
    /**
     * 开关逻辑——切换周月 + 隐藏，显示农历
     */
    private void setSwitchState() {
        //周月切换
        switch_week_month.setChecked(true);//设置默认true，为月
        switch_week_month.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch_week_month.isChecked()){
                    mManager.toggleView();
                    calendarView.populateLayout();
                }else {
                    mManager.toggleView();
                    calendarView.populateLayout();
                }
            }
        });
        //显示隐藏农历
        switch_hide_show_lunar_calendar.setChecked(true);//设置默认true ，显示农历
        switch_hide_show_lunar_calendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch_week_month.isChecked()){
                    calendarView.showChinaDay(show);
                    show = !show;
                }else {
                    calendarView.showChinaDay(show);
                    show = !show;
                }
            }
        });
    }

    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back_today:
                calendarView.changeDate(LocalDate.now().toString());//回到今天
                break;
        }
    }
    /**
     *  从查看日历返回主页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (sb!=null){
                    sb.dismiss();
                }else {
                    finish();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    /**
     * Toolbar 的Navigation Button 添加点击事件,用于打开或关闭侧滑抽屉
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
