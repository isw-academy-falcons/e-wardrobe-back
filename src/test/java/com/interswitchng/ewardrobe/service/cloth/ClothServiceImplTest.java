package com.interswitchng.ewardrobe.service.cloth;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.dto.GetAllClothesDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.repository.ClothRepository;
import com.interswitchng.ewardrobe.repository.UserRepository;
import com.interswitchng.ewardrobe.service.user.UserService;
import com.interswitchng.ewardrobe.utils.CloudinaryUtil;
import com.interswitchng.ewardrobe.utils.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClothServiceImplTest {
    @InjectMocks
    private ClothServiceImpl clothService;
    @Mock
    private UserService userService;
    @Mock
    private CloudinaryUtil cloudinaryUtil;
    @Mock
    private Cloudinary cloudinary;
    @Mock
    private ClothRepository clothRepository;
    @Mock
    private UserUtil userUtil;

    @BeforeEach
    public void setUp() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        MockitoAnnotations.initMocks(this);
        clothService = new ClothServiceImpl(userService, cloudinary, userUtil, cloudinaryUtil, clothRepository);
    }


    @Test
    public void testUploadImage() throws IOException, UserNotFoundException {
        String userEmail = "test@example.com";
        when(userUtil.getAuthenticatedUserEmail()).thenReturn(userEmail);

        User user = new User();
        user.setUserId("64f9d5794f37e93f128bb3c0");
        when(userService.findUserByEmail(userEmail)).thenReturn(user);

        byte[] fileBytes = "test data".getBytes();
        String originalFileName = "test.jpg";
        String uniqueFileName = "uniqueFileName";
        when(cloudinaryUtil.generatedFileName(originalFileName)).thenReturn(uniqueFileName);

        Uploader uploader = Mockito.mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);

        Map<String, String> uploadResult = Map.of("secure_url", "https://example.com/test.jpg");
        when(uploader.upload(eq(fileBytes), anyMap())).thenReturn(uploadResult);

        MockMultipartFile file = new MockMultipartFile(
                "file", originalFileName, "image/jpeg", fileBytes);

        String imageUrl = clothService.uploadImage(file, "NATIVE", "A stylish t-shirt", "TOP", "UPLOADED");

        verify(clothRepository, times(1)).save(any(Cloth.class));
        assertEquals("https://example.com/test.jpg", imageUrl);
    }


//    @Test
//    public void getPaginatedListOfCloth() throws UserNotFoundException {
//        GetAllClothesDto getAllClothesDto = new GetAllClothesDto();
//        Pageable pageable = Pageable.unpaged();
//        String userId = "testUserId";
//        User foundUser = new User();
//        foundUser.setUserId(userId);
//
//        when(userService.findById(userId)).thenReturn(foundUser);
//
//        Page<Cloth> mockPage = mock(Page.class);
//        when(clothRepository.findAllByCriteria(any(GetAllClothesDto.class), eq(userId), eq(pageable))).thenReturn(mockPage);
//
//        Page<Cloth> result = clothService.getPaginatedClothes(getAllClothesDto, pageable, userId);
//
//        verify(userService, times(1)).findById(userId);
//        verify(clothRepository, times(1)).findAllByCriteria(getAllClothesDto, userId, pageable);
//        assertSame(mockPage, result);
//
//    }

    @Test
    public void testDeleteCloth_Success() throws UserNotFoundException, EWardRobeException {

        String clothId = "cloth123";
        String userId = "user123";

        User foundUser = new User();
        foundUser.setUserId(userId);

        Cloth cloth = new Cloth();
        cloth.setClothId(clothId);
        cloth.setUserId(userId);

        when(userService.findById(userId)).thenReturn(foundUser);

        when(clothRepository.findById(clothId)).thenReturn(java.util.Optional.of(cloth));

        clothService.deleteCloth(clothId, userId);

        verify(userService, times(1)).findById(userId);
        verify(clothRepository, times(1)).findById(clothId);
        verify(clothRepository, times(1)).delete(cloth);
    }

    @Test
    public void testDeleteCloth_ClothNotFoundException() throws UserNotFoundException {

        String clothId = "cloth123";
        String userId = "user123";


        when(userService.findById(userId)).thenReturn(new User());

        when(clothRepository.findById(clothId)).thenReturn(java.util.Optional.empty());


        assertThrows(EWardRobeException.class, () -> clothService.deleteCloth(clothId, userId));
    }

    @Test
    public void testDeleteCloth_UnauthorizedAccessException() throws UserNotFoundException {

        String clothId = "cloth123";
        String userId = "user123";

        User foundUser = new User();
        foundUser.setUserId("otherUser123");

        Cloth cloth = new Cloth();
        cloth.setClothId(clothId);
        cloth.setUserId(userId);

        when(userService.findById(userId)).thenReturn(foundUser);

        when(clothRepository.findById(clothId)).thenReturn(java.util.Optional.of(cloth));


        assertThrows(UserNotFoundException.class, () -> clothService.deleteCloth(clothId, userId));
    }
}
