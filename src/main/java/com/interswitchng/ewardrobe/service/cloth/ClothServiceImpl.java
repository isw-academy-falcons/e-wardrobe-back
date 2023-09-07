package com.interswitchng.ewardrobe.service.cloth;

import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.repository.ClothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClothServiceImpl implements ClothService{

    @Autowired
    private ClothRepository clothRepository;

    @Override
    public void saveCloth(Cloth cloth) {
        clothRepository.save(cloth);
    }
}
