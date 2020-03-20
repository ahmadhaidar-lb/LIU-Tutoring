public class UploadImage {
    String name;
    String imageUrl;

    public UploadImage(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public UploadImage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
