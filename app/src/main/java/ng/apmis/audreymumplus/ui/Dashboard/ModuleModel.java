package ng.apmis.audreymumplus.ui.Dashboard;

public class ModuleModel {
    private String title;
    private int imageIcon;


    public ModuleModel(String mtitle, int mImage){
        imageIcon = mImage;
        title = mtitle;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public String getTitle() {
        return title;
    }
}
