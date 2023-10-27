package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.dto.ChangeFirstNameRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeLastNameRequestDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.exceptions.EmailAlreadyExistsException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.UserImage;
import com.fdmgroup.MockExam.repositories.UserRepository;
import com.fdmgroup.MockExam.utils.ImageConverter;

@ExtendWith(MockitoExtension.class)
public class test_UserService {
	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;
	@Mock
	private JwtService jwtService;	
	@Mock
	private NotificationService notificationService;
	
	private User user;
	private User actual;
	List<User> users;

	@BeforeEach
	public void setUp() {
		users = new ArrayList<>();
		user = new User();
		user.setId(1);
		user.setFirstName("testName");
		actual = new User();
		actual.setId(2);
users.add(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void testGenerateToken() {
		String token = userService.generateToken();

		assertNotNull(token);
		assertTrue(token.length() > 0);
		assertTrue(token.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_save_checkRepositorySaving() {
		doReturn(user).when(userRepository).save(user);

		actual = userService.save(user);

		assertEquals(user, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_verificateUser_checkRepositorySavingDuringVerification() {
		doReturn(user).when(userRepository).save(user);

		actual = userService.verificateUser(user);

		assertEquals(user, actual);

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_register_NewUser_Success() {
		User newUser = new User();
		newUser.setEmail("newuser@example.com");

		when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(newUser)).thenReturn(newUser);

		User result = userService.register(newUser);

		assertNotNull(result);
		assertEquals(newUser, result);
		verify(userRepository, times(1)).findByEmail(newUser.getEmail());
		verify(userRepository, times(1)).save(newUser);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_register_UserAlreadyExists_Exception() {
		User existingUser = new User();
		existingUser.setEmail("existing@example.com");

		when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

		assertThrows(EmailAlreadyExistsException.class, () -> userService.register(existingUser));
		verify(userRepository, times(1)).findByEmail(existingUser.getEmail());
		verify(userRepository, times(0)).save(existingUser);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByEmail_UserFound() {
		String email = "test@example.com";
		User user = new User();
		user.setEmail(email);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		Optional<User> result = userService.findByEmail(email);

		assertTrue(result.isPresent());
		assertEquals(user, result.get());
		verify(userRepository, times(1)).findByEmail(email);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByEmail_UserNotFound() {
		String email = "nonexistent@example.com";

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		Optional<User> result = userService.findByEmail(email);

		assertFalse(result.isPresent());
		verify(userRepository, times(1)).findByEmail(email);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_enableUser_Success() {
		String email = "test@example.com";

		when(userRepository.enableUser(email)).thenReturn(1);

		int result = userService.enableUser(email);

		assertEquals(1, result);
		verify(userRepository, times(1)).enableUser(email);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_updatePassword_NewPassword_Success() {
		User user = new User();
		user.setPassword("oldPassword");
		String newPassword = "newPassword";

		when(userRepository.save(user)).thenReturn(user);

		User result = userService.updatePassword(newPassword, user);

		assertNotNull(result);
		assertEquals(newPassword, result.getPassword());
		verify(userRepository, times(1)).save(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteUser() {
		User user = new User();

		userService.delete(user);

		verify(userRepository, times(1)).delete(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changeFirstName_success() {
		String userEmail = "test@example.com";
		String newFirstName = "NewFirstName";
		ChangeFirstNameRequestDTO request = new ChangeFirstNameRequestDTO();
		request.setFirstname(newFirstName);

		when(jwtService.extractUserEmailFromHeader(anyString())).thenReturn(userEmail);
		when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

		ChangeNameResponseDTO response = userService.changeFirstName(request, "mockedHeader");

		assertEquals("First name change to: "+newFirstName+" has been requested.", response.getConfirmationMessage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changeLastName_success() {
		String userEmail = "test@example.com";
		String newLastName = "NewLastName";
		ChangeLastNameRequestDTO request = new ChangeLastNameRequestDTO();
		request.setLastname(newLastName);

		when(jwtService.extractUserEmailFromHeader(anyString())).thenReturn(userEmail);
		when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

		ChangeNameResponseDTO response = userService.changeLastName(request, "mockedHeader");

		assertEquals("Last name change to: "+newLastName+" has been requested.", response.getConfirmationMessage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_addImage() {
		UserImage image = new UserImage();
		image.setName("Added image");
		String userEmail = "test@example.com";

		when(jwtService.extractUserEmailFromHeader(anyString())).thenReturn(userEmail);
		when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

		ConfirmationResponseDTO response = userService.addImage("mockHeader", image);

		assertEquals("Image has been added.", response.getConfirmationMessage());
		assertEquals(image, user.getUserImage());
		verify(userRepository, times(1)).save(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteImage() {
		UserImage image = new UserImage();
		image.setName("Added image");
		String userEmail = "test@example.com";
		user.setUserImage(image);

		when(jwtService.extractUserEmailFromHeader(anyString())).thenReturn(userEmail);
		when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

		ConfirmationResponseDTO response = userService.deleteImage("mockHeader");

		assertEquals("Image has been removed.", response.getConfirmationMessage());
		assertEquals(null, user.getUserImage());
		verify(userRepository, times(1)).save(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_getImage() {
		String userEmail = "test@example.com";

		UserImage image = new UserImage();
		long size = 2;
		image.setSize(size);
		image.setName("Added image");
		String imageName = image.getName();
		byte[] bytes = (imageName != null) ? imageName.getBytes() : new byte[0];
		image.setData(bytes);
		user.setUserImage(image);

		ImageResponseDTO convertedImage = ImageConverter.convertImageToResponseImage(image);

		when(jwtService.extractUserEmailFromHeader(anyString())).thenReturn(userEmail);
		when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

		ImageResponseDTO response = userService.getImage("mockHeader");

		assertEquals(convertedImage, response);
	}

	// ---------------------------------------------------------------------------------------------

	 @Test
	    public void test_blockMyAccount_Success() {
	        String header = "exampleHeader";
	        String password = "examplePassword";
	        User user = new User(); 
	        Optional<User> optUser = Optional.of(user);
	        when(userRepository.findByEmail(null)).thenReturn(optUser);

	        ConfirmationResponseDTO response = userService.blockMyAccount(header, password);

	        assertTrue(user.getLocked());
	        verify(userRepository, times(1)).save(user);
	        assertEquals("Account has been locked.\nPlease wait for administration to delete.", response.getConfirmationMessage());
	    }

		// ---------------------------------------------------------------------------------------------

	    @Test
	    public void test_blockMyAccount_UserNotFound() {
	        String header = "nonExistentUserHeader";
	        String password = "examplePassword";
	        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

	    	assertThrows(NoSuchElementException.class, () -> {
	    		userService.blockMyAccount(header, password);
			});
	    }

		// ---------------------------------------------------------------------------------------------

		@Test
		public void test_findByFirstNameContainsIgnoreCase_checkRepositoryFinding() {
			doReturn(users).when(userRepository).findByFirstNameContainsIgnoreCase("abc");

			List<User> actual = userService.findByFirstNameContainsIgnoreCase("abc");

			assertEquals(users, actual);
		}

		// ---------------------------------------------------------------------------------------------

		@Test
		public void test_findByLastNameontainsIgnoreCase_checkRepositoryFinding() {
			doReturn(users).when(userRepository).findByLastNameContainsIgnoreCase("abc");

			List<User> actual = userService.findByLastNameContainsIgnoreCase("abc");

			assertEquals(users, actual);
		}

		// ---------------------------------------------------------------------------------------------

		@Test
		public void test_findByEmailContainsIgnoreCase_checkRepositoryFinding() {
			doReturn(users).when(userRepository).findByEmailContainsIgnoreCase("abc");

			List<User> actual = userService.findByEmailContainsIgnoreCase("abc");

			assertEquals(users, actual);
		}

		// ---------------------------------------------------------------------------------------------

		@Test
		public void test_findByTags_checkRepositoryFinding() {
			doReturn(users).when(userRepository).findByTags("abc");

			List<User> actual = userService.findByTags("abc");

			assertEquals(users, actual);
		}

		// ---------------------------------------------------------------------------------------------

		 @Test
		    public void testFindById() {
		  
		        when(userRepository.findById(1)).thenReturn(Optional.of(user));

		        User foundUser = userService.findById(1);

		        assertNotNull(foundUser);
		        assertEquals(1, foundUser.getId());
		        assertEquals("testName", foundUser.getFirstName());
		    }

			// ---------------------------------------------------------------------------------------------

		    @Test
		    public void test_giveAccessToUsersWithList() {
		        int userId1 = 1;
		        int userId2 = 2;
		        List<Integer> userIdList = new ArrayList<>();
		        userIdList.add(userId1);
		        userIdList.add(userId2);
		        Category category = new Category("TestCategory");

		        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
		        when(userRepository.findById(userId2)).thenReturn(Optional.of(actual));

		        ConfirmationResponseDTO response = userService.giveAccessToUsers(userIdList, category);

		        assertNotNull(response);
		        assertEquals("Access to exam has been granted.", response.getConfirmationMessage());

		        verify(notificationService, times(2)).giveAccessToUser(any(), eq(category));
		    }

			// ---------------------------------------------------------------------------------------------

		    @Test
		    public void test_giveAccessToUsersWithCategory() {
		    	user.setRole(Role.TRAINEE);
		    	actual.setRole(Role.TRAINEE);
		       users.add(actual);
		        Category category = new Category("TestCategory");

		        when(userRepository.findAll()).thenReturn(users);

		        ConfirmationResponseDTO response = userService.giveAccessToUsers(category);

		        assertNotNull(response);
		        assertEquals("Access to exam has been granted.", response.getConfirmationMessage());
		    }
}
