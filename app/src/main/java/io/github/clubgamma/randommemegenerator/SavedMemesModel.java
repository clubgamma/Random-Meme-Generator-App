package io.github.clubgamma.randommemegenerator;

public class SavedMemesModel {
    String memeTitle,memeUrl;

    public SavedMemesModel(String memeTitle, String memeUrl) {
        this.memeTitle = memeTitle;
        this.memeUrl = memeUrl;
    }

    public String getMemeTitle() {
        return memeTitle;
    }

    public void setMemeTitle(String memeTitle) {
        this.memeTitle = memeTitle;
    }

    public String getMemeUrl() {
        return memeUrl;
    }

    public void setMemeUrl(String memeUrl) {
        this.memeUrl = memeUrl;
    }
}
