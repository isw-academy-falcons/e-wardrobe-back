package com.interswitchng.ewardrobe.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Cloth {
    @Id
    private String clothId;
    private String imageUrl;
    private String description;
    private ClothType clothType;
    private Category category;
    private CollectionType collectionType;
    private String userId;
}

