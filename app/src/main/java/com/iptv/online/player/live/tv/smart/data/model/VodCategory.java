package com.iptv.online.player.live.tv.smart.data.model;

//import androidx.media3.extractor.text.ttml.TtmlNode;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes5.dex */
public class VodCategory implements Serializable {
    private String categoryId;
    private String categoryName;
    private int parentId;

    public VodCategory() {
    }

    public VodCategory(String str, String str2) {
        this.categoryId = str;
        this.categoryName = str2;
    }

    public String getCategoryId() {
        String str = this.categoryId;
        return str != null ? str : "all";
    }

    public void setCategoryId(String str) {
        this.categoryId = str;
    }

    public String getCategoryName() {
        String str = this.categoryName;
        return (str == null || str.trim().isEmpty()) ? "All" : this.categoryName.trim();
    }

    public void setCategoryName(String str) {
        this.categoryName = str;
    }

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int i) {
        this.parentId = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(getCategoryId(), ((VodCategory) obj).getCategoryId());
    }

    public int hashCode() {
        return Objects.hash(getCategoryId());
    }
}
