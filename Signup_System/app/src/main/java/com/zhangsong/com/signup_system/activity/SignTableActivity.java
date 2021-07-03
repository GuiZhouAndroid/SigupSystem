package com.zhangsong.com.signup_system.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.utils.DensityUtils;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.tabledata.Student;

import java.util.ArrayList;
import java.util.List;

public class SignTableActivity extends MySwipeBackActivity {
    private RelativeLayout rl_table_show;
    private SmartTable<Student> table;
    private List<Student> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_table);
        initView();
        initState();
        initTable();
    }
    /**
     * 初始化View
     */
    private void initView() {
        table = (SmartTable<Student>)findViewById(R.id.table);
        rl_table_show = (RelativeLayout) findViewById(R.id.rl_table_show);

    }
    /**
     * 提示查询成功
     */
    private void initState() {
        Snackbar snackbar = Snackbar.make(rl_table_show,  R.string.main_Query_success, Snackbar.LENGTH_LONG);
        //设置Snackbar上提示的字体颜色
        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
        snackbar.show();
    }
    /**
     * 初始化Table
     */
    private void initTable() {
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15));//设置表格字体
        Intent intent = getIntent();
            if (intent != null) {
                String getBmobObjectId = intent.getStringExtra("BmobObjectId");//Bmob凭证号
                String getBmobName = intent.getStringExtra("BmobName");//Bmob姓名
                String getBmobSex = intent.getStringExtra("BmobSex");//Bmob性别
                String getBmobNation = intent.getStringExtra("BmobNation");//Bmob民族
                String getBmobidcard = intent.getStringExtra("Bmobidcard");//Bmob身份证号
                String getBmobDegreeEducation = intent.getStringExtra("BmobDegreeEducation");//Bmob文化程度
                String getBmobPoliticalOutlook = intent.getStringExtra("BmobPoliticalOutlook");//Bmob政治面貌
                String getBmobContactNumber = intent.getStringExtra("BmobContactNumber");//Bmob联系电话
                String getBmobOccupation = intent.getStringExtra("BmobOccupation");//Bmob申报职业
                String getBmobDeclarationLevel = intent.getStringExtra("BmobDeclarationLevel");//Bmob申报级别
                String getBmobWhetherTwoExam = intent.getStringExtra("BmobWhetherTwoExam");//Bmob是否二考
                String getBmobExaminationTime = intent.getStringExtra("BmobExaminationTime");//Bmob考试时间
                String getBmobWorkUnit = intent.getStringExtra("BmobWorkUnit");//Bmob工作单位
                String getBmobCreatTime = intent.getStringExtra("BmobCreatTime");//Bmob报名时间
                list.add(new Student(getBmobObjectId,getBmobName,getBmobSex,getBmobNation,getBmobidcard,
                        getBmobDegreeEducation,getBmobPoliticalOutlook,getBmobContactNumber,getBmobOccupation,
                        getBmobDeclarationLevel,getBmobWhetherTwoExam,getBmobExaminationTime,getBmobWorkUnit,getBmobCreatTime));

        }
        table.setData(list);
        table.getConfig().setShowTableTitle(true);
        table.getConfig().setShowXSequence(true);
        table.getConfig().setShowYSequence(true);

        table.setZoom(true, 2, 0.2f);
        //table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
        //固定指定列
        //Y序号列
        table.getConfig().setFixedYSequence(true);
        //X序号列
        table.getConfig().setFixedXSequence(true);
        //列标题
        table.getConfig().setFixedCountRow(true);
        //统计行
        table.getConfig().setFixedTitle(true);
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                return ContextCompat.getColor(SignTableActivity.this, R.color.Yellow);
            }
        });

    }
    /**
     * 设置Snackbar上提示的字体颜色
     * @param snackbar
     * @param color
     */
    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color){
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
