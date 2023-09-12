package com.interswitchng.ewardrobe.dto;

import com.interswitchng.ewardrobe.data.model.Category;
import com.interswitchng.ewardrobe.data.model.ClothType;
import com.interswitchng.ewardrobe.data.model.CollectionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllClothesDto {
    private String clothType;
    private String category;
    private String collectionType;
    private String clothId;

}
