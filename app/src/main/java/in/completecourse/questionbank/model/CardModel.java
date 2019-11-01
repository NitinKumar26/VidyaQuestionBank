package in.completecourse.questionbank.model;


public class CardModel {

    private String name;
    private int thumbnail;
    private String category_link;
    private String companyPhoto;

    public CardModel(int thumbnail, String name) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory_link() {
        return category_link;
    }

    public void setCategory_link(String category_link) {
        this.category_link = category_link;
    }

    public String getCompanyPhoto() {
        return companyPhoto;
    }

    public void setCompanyPhoto(String companyPhoto) {
        this.companyPhoto = companyPhoto;
    }
}
