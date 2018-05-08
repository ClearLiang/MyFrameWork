package com.example.a99794.framework.testfile;

import java.util.ArrayList;

/**
 * @作者 ClearLiang
 * @日期 2018/5/7
 * @描述 @desc
 **/

public class WordDocument implements Cloneable {

    private String mText;
    private ArrayList<String> mImages = new ArrayList<>();

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }

    public WordDocument() {
    }

    @Override
    protected WordDocument clone() {
        try {
            WordDocument wordDocument = (WordDocument) super.clone();
            wordDocument.mText = this.mText;
            wordDocument.mImages = (ArrayList<String>) this.mImages.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
