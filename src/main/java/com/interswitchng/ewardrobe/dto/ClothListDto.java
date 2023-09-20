package com.interswitchng.ewardrobe.dto;

import com.interswitchng.ewardrobe.data.model.Cloth;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClothListDto {
    private List<String> tops;
    private List<String> bottoms;
    private List<String> dresses;
}
