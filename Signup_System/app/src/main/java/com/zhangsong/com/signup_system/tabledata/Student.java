package com.zhangsong.com.signup_system.tabledata;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name="您的报名信息",count = false)
public class Student {
    @SmartColumn(id =1,name = "凭证号",autoCount = true)
    private String ObjectId;
    @SmartColumn(id =2,name = "姓名",autoCount = true)
    private String Name;
    @SmartColumn(id =3,name = "性别",autoCount = true)
    private String Sex;
    @SmartColumn(id =4,name = "民族",autoCount = true)
    private String Nation;
    @SmartColumn(id =5,name = "身份证号",autoCount = true)
    private String idcard;
    @SmartColumn(id =6,name = "文化程度",autoCount = true)
    private String DegreeEducation;
    @SmartColumn(id =7,name = "政治面貌",autoCount = true)
    private String PoliticalOutlook;
    @SmartColumn(id =8,name = "联系电话",autoCount = true)
    private String ContactNumber;
    @SmartColumn(id =9,name = "申报职业",autoCount = true)
    private String Occupation;
    @SmartColumn(id =10,name = "申报级别",autoCount = true)
    private String DeclarationLevel;
    @SmartColumn(id =11,name = "是否二考",autoCount = true)
    private String YesNoTwoExam;
    @SmartColumn(id =11,name = "考试时间",autoCount = true)
    private String ExamTime;
    @SmartColumn(id =12,name = "工作单位",autoCount = true)
    private String WorkUnit;
    @SmartColumn(id =13,name = "报名时间",autoCount = true)
    private String SignTime;


    public Student(String ObjectId,String Name,String Sex,String Nation,String idcard,
                    String DegreeEducation,String PoliticalOutlook,String ContactNumber,
                    String Occupation,String DeclarationLevel,String YesNoTwoExam,String ExamTime,String WorkUnit,String SignTime){
        this.ObjectId=ObjectId;
        this.Name=Name;
        this.Sex=Sex;
        this.Nation=Nation;
        this.idcard=idcard;
        this.DegreeEducation=DegreeEducation;
        this.PoliticalOutlook=PoliticalOutlook;
        this.ContactNumber=ContactNumber;
        this.Occupation=Occupation;
        this.DeclarationLevel=DeclarationLevel;
        this.YesNoTwoExam=YesNoTwoExam;
        this.ExamTime=ExamTime;
        this.WorkUnit=WorkUnit;
        this.SignTime=SignTime;
    }
    public String getObjectId() {
        return ObjectId;
    }

    public void setObjectId(String objectId) {
        ObjectId = objectId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getNation() {
        return Nation;
    }

    public void setNation(String nation) {
        Nation = nation;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getDegreeEducation() {
        return DegreeEducation;
    }

    public void setDegreeEducation(String degreeEducation) {
        DegreeEducation = degreeEducation;
    }

    public String getPoliticalOutlook() {
        return PoliticalOutlook;
    }

    public void setPoliticalOutlook(String politicalOutlook) {
        PoliticalOutlook = politicalOutlook;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getDeclarationLevel() {
        return DeclarationLevel;
    }

    public void setDeclarationLevel(String declarationLevel) {
        DeclarationLevel = declarationLevel;
    }

    public String getYesNoTwoExam() {
        return YesNoTwoExam;
    }

    public void setYesNoTwoExam(String yesNoTwoExam) {
        YesNoTwoExam = yesNoTwoExam;
    }

    public String getExamTime() {
        return ExamTime;
    }

    public void setExamTime(String examTime) {
        ExamTime = examTime;
    }

    public String getWorkUnit() {
        return WorkUnit;
    }

    public void setWorkUnit(String workUnit) {
        WorkUnit = workUnit;
    }

    public String getSignTime() {
        return SignTime;
    }

    public void setSignTime(String signTime) {
        SignTime = signTime;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ObjectId='" + ObjectId + '\'' +
                ", Name='" + Name + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Nation='" + Nation + '\'' +
                ", idcard='" + idcard + '\'' +
                ", DegreeEducation='" + DegreeEducation + '\'' +
                ", PoliticalOutlook='" + PoliticalOutlook + '\'' +
                ", ContactNumber='" + ContactNumber + '\'' +
                ", Occupation='" + Occupation + '\'' +
                ", DeclarationLevel='" + DeclarationLevel + '\'' +
                ", YesNoTwoExam='" + YesNoTwoExam + '\'' +
                ", ExamTime='" + ExamTime + '\'' +
                ", WorkUnit='" + WorkUnit + '\'' +
                ", SignTime='" + SignTime + '\'' +
                '}';
    }
}
