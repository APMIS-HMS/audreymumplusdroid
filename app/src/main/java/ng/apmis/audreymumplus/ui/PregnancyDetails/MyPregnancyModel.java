package ng.apmis.audreymumplus.ui.PregnancyDetails;

public class MyPregnancyModel {
    String viewId ;
    String babyProgcontentHead;
    String weeklyHeader;
    String weeklyContent1;
    String weekContent2;

    public MyPregnancyModel(String mViewId, String mBabtprogcontentHead, String mWeeklyHeader, String mWeeklyContent1, String mWeekContent2){
        viewId = mViewId;
        babyProgcontentHead = mBabtprogcontentHead;
        weeklyHeader = mWeeklyHeader;
        weeklyContent1 = mWeeklyContent1;
        weekContent2 = mWeekContent2;
    }

    public String getViewId() {
        return viewId;
    }

    public String getBabyProgcontentHead() {
        return babyProgcontentHead;
    }

    public String getWeeklyHeader() {
        return weeklyHeader;
    }

    public String getWeeklyContent1() {
        return weeklyContent1;
    }

    public String getWeekContent2() {
        return weekContent2;
    }
}
