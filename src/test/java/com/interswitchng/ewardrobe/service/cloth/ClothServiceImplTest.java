package com.interswitchng.ewardrobe.service.cloth;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.data.model.User;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.repository.ClothRepository;
import com.interswitchng.ewardrobe.repository.UserRepository;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClothServiceImplTest {
    @InjectMocks
    private ClothServiceImpl clothService;
    @Mock
    private UserRepository userRepository;
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
        clothService = new ClothServiceImpl(userRepository, cloudinary, userUtil, cloudinaryUtil, clothRepository);
    }


    @Test
    public void testUploadImage() throws IOException, UserNotFoundException {
        String userEmail = "test@example.com";
        when(userUtil.getAuthenticatedUserEmail()).thenReturn(userEmail);

        User user = new User();
        user.setUserId("64f9d5794f37e93f128bb3c0");
        when(userRepository.findUserByEmailIgnoreCase(userEmail)).thenReturn(Optional.of(user));

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
}
