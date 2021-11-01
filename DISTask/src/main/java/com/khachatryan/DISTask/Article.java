package com.khachatryan.DISTask;

import java.util.Date;

public class Article {
        private Long id;
        private Long productId;
        private String name;
        private String content;
        private String createDate;

    public Article(Long id, Long productId, String name, String content, String createDate){
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.content = content;
        this.createDate = createDate;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
