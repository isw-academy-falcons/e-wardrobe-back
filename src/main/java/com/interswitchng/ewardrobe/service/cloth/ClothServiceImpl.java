package com.interswitchng.ewardrobe.service.cloth;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.interswitchng.ewardrobe.data.model.*;
import com.interswitchng.ewardrobe.dto.ClothListDto;
import com.interswitchng.ewardrobe.dto.GetAllClothesDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.repository.ClothRepository;
import com.interswitchng.ewardrobe.service.user.UserService;
import com.interswitchng.ewardrobe.utils.CloudinaryUtil;
import com.interswitchng.ewardrobe.utils.UserUtil;
import jakarta.mail.Header;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class ClothServiceImpl implements ClothService {

    private final UserService userService;
    private final Cloudinary cloudinary;
    private final UserUtil userUtil;
    private final CloudinaryUtil cloudinaryUtil;
    private final ClothRepository clothRepository;
    private final RestTemplate restTemplate;

    @Override
    public void saveCloth(Cloth cloth) {
        clothRepository.save(cloth);
    }

    @Override
    public Optional<Cloth> findByPictureUrl(String pictureUrl) {
        return clothRepository.findByImageUrl(pictureUrl);
    }

    @Override
    public List<Cloth> findClothByUserId(String userId) {
        return clothRepository.findClothByUserId(userId);
    }

    @Override
    public Cloth findClothByUserIdAndImageUrl(String userId, String pictureUrl) {
        return clothRepository.findClothByUserIdAndImageUrl(userId, pictureUrl);
    }

    @Override
    public List<String> uploadImage(List<MultipartFile> files, String category, String clothType)
            throws IOException, UserNotFoundException {
        String email = userUtil.getAuthenticatedUserEmail();
        User user = userService.findUserByEmail(email);
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            byte[] fileBytes = file.getBytes();
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = cloudinaryUtil.generatedFileName(originalFileName);
            Map<String, String> uploadOptions = ObjectUtils.asMap("public_id", uniqueFileName);

            Map<String, String> uploadResult = cloudinary.uploader().upload(fileBytes, uploadOptions);
            String imageUrl = uploadResult.get("secure_url");

            Cloth cloth = new Cloth();
            cloth.setClothType(ClothType.valueOf(clothType));
            cloth.setCategory(Category.valueOf(category));
            cloth.setCollectionType(CollectionType.UPLOADED);
            cloth.setUserId(user.getUserId());
            cloth.setImageUrl(imageUrl);
            clothRepository.save(cloth);
            imageUrls.add(cloth.getImageUrl());
        }
        return imageUrls.stream().toList();
    }

    @Override
    public Page<Cloth> getAllClothes(GetAllClothesDto getAllClothesDto, String userId, Pageable pageable) throws UserNotFoundException {
        User foundUser = userService.findById(userId);
        List<Cloth> clothByUserId = clothRepository.findClothByUserId(foundUser.getUserId());
        List<Cloth> filteredClothes = new ArrayList<>();

        for (Cloth cloth : clothByUserId) {
            boolean includeCloth = true;

            if (getAllClothesDto.getCollectionType() != null && !getAllClothesDto.getCollectionType().isEmpty() &&
                    !getAllClothesDto.getCollectionType().equals(cloth.getCollectionType().name())) {
                includeCloth = false;
            }

            if (getAllClothesDto.getClothType() != null && !getAllClothesDto.getClothType().isEmpty() &&
                    !getAllClothesDto.getClothType().equals(cloth.getClothType().name())) {
                includeCloth = false;
            }

            if (getAllClothesDto.getCategory() != null && !getAllClothesDto.getCategory().isEmpty() &&
                    !getAllClothesDto.getCategory().equals(cloth.getCategory().name())) {
                includeCloth = false;
            }

            if (includeCloth) {
                filteredClothes.add(cloth);
            } else {
                filteredClothes.add(cloth);
            }
        }


        return new PageImpl<>(filteredClothes, pageable, filteredClothes.size());
    }


    @Override
    public void deleteCloth(String clothId) throws UserNotFoundException, EWardRobeException {
        Cloth cloth = clothRepository.findById(clothId).orElseThrow(
                () -> new EWardRobeException("Cloth not found"));
        clothRepository.delete(cloth);
    }

    @Override
    public List<Cloth> getAllUserClothes(String userId) {
        return clothRepository.findClothByUserId(userId).stream().toList();
    }

    @Override
    public List<Cloth> getAllUploadedUserClothes(String userId) {
        return clothRepository.findClothByUserId(userId)
                .stream()
                .filter(cloth -> cloth.getCollectionType() == CollectionType.UPLOADED)
                .toList();
    }

    private ClothListDto sendClothesToModel(String userid, Category category) {
        List<Cloth> clothes = clothRepository.findClothsByUserIdAndCategory(userid, category)
                .stream()
                .filter(cloth -> cloth.getCollectionType() == CollectionType.UPLOADED)
                .toList();

        Map<ClothType, List<Cloth>> clothesByType = clothes.stream()
                .collect(Collectors.groupingBy(Cloth::getClothType));

        ClothListDto clothListDto = new ClothListDto();
        clothListDto.setTops(extractImageUrl(clothesByType.getOrDefault(ClothType.TOP, Collections.emptyList())));
        clothListDto.setBottoms(extractImageUrl(clothesByType.getOrDefault(ClothType.BOTTOM, Collections.emptyList())));
        clothListDto.setDresses(extractImageUrl(clothesByType.getOrDefault(ClothType.DRESS, Collections.emptyList())));
        return clothListDto;
    }

    private List<String> extractImageUrl(List<Cloth> clothes) {
        return clothes.stream()
                .map(Cloth::getImageUrl)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cloth> getAllUnsplashUserClothes(String userId) {
        return clothRepository.findClothByUserId(userId)
                .stream()
                .filter(cloth -> cloth.getCollectionType() == CollectionType.UNSPLASH)
                .toList();
    }

    @Override
    public List<String> generateOutfit(String id, Category category) throws EWardRobeException {
        ClothListDto cloths = sendClothesToModel(id, category);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<?> http = new HttpEntity<>(cloths, header);
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:5000/matches", POST, http, String.class);

        if (responseEntity.getStatusCode() != OK) {
            throw new EWardRobeException("Failed to generate outfit: " + responseEntity.getStatusCode());
        }
        return List.of(Objects.requireNonNull(responseEntity.getBody()).split(","));
    }
}