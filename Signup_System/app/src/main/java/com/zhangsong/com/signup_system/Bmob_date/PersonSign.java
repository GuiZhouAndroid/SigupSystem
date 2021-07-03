package com.zhangsong.com.signup_system.Bmob_date;

import cn.bmob.v3.BmobObject;

public class PersonSign extends BmobObject {
    private String Name;//姓名
    private String Sex;//性别
    private String Nation;//民族
    private String IdCard;//身份证号
    private String DegreeEducation;//文化程度
    private String PoliticalOutlook;//政治面貌
    private String ContactNumber;//联系电话
    private String Occupation;//申报职业
    private String DeclarationLevel;//申报级别
    private String WhetherTwoExam;//是否二考
    private String ExaminationTime;//考试时间
    private String WorkUnit;//工作单位



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

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String idCard) {
        IdCard = idCard;
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

    public void setOccupation(String declarationOccupation) {
        Occupation = declarationOccupation;
    }

    public String getDeclarationLevel() {
        return DeclarationLevel;
    }

    public void setDeclarationLevel(String declarationLevel) {
        DeclarationLevel = declarationLevel;
    }

    public String getWhetherTwoExam() {
        return WhetherTwoExam;
    }

    public void setWhetherTwoExam(String whetherTwoExam) {
        WhetherTwoExam = whetherTwoExam;
    }

    public String getExaminationTime() {
        return ExaminationTime;
    }

    public void setExaminationTime(String examinationTime) {
        ExaminationTime = examinationTime;
    }

    public String getWorkUnit() {
        return WorkUnit;
    }

    public void setWorkUnit(String workUnit) {
        WorkUnit = workUnit;
    }

    @Override
    public String toString() {
        return "PersonSign{" +
                "Name='" + Name + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Nation='" + Nation + '\'' +
                ", IdCard='" + IdCard + '\'' +
                ", DegreeEducation='" + DegreeEducation + '\'' +
                ", PoliticalOutlook='" + PoliticalOutlook + '\'' +
                ", ContactNumber='" + ContactNumber + '\'' +
                ", Occupation='" + Occupation + '\'' +
                ", DeclarationLevel='" + DeclarationLevel + '\'' +
                ", WhetherTwoExam='" + WhetherTwoExam + '\'' +
                ", ExaminationTime='" + ExaminationTime + '\'' +
                ", WorkUnit='" + WorkUnit + '\'' +
                '}';
    }
}
