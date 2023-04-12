package it.develhope.Unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.develhope.Unittest.controller.UserController;
import it.develhope.Unittest.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class UnitTestApplicationTests {

	@Autowired
	UserController userController;

	@Autowired
	MockMvc mockMvc;
@Autowired
private ObjectMapper objectMapper;

	@Test
	void UserControllerLoads() {
		Assertions.assertThat(userController).isNotNull();
	}
	private User getUserFromId(Long id) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/user/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		try {
			String studentJSON = result.getResponse().getContentAsString();
			User user = objectMapper.readValue(studentJSON, User.class);

			Assertions.assertThat(user).isNotNull();
			Assertions.assertThat(user.getId()).isNotNull();

			return user;
		} catch (Exception e) {
			return null;
		}}
	private User createAUser() throws Exception {
		User user = new User();
		user.setName("mirko");
		user.setSurname("calo");
		user.setAge(28);
		return createAUser(user);
	}
	private User createAUser(User user) throws Exception {
		MvcResult result = createAUserRequest(user);
		User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

		Assertions.assertThat(userFromResponse).isNotNull();
		Assertions.assertThat(userFromResponse.getId()).isNotNull();

		return userFromResponse;
	}

	private MvcResult createAUserRequest() throws Exception {
		User user = new User();
		user.setName("mirko");
		user.setSurname("calo");
		user.setAge(28);
		return createAUserRequest(user);
	}

	private MvcResult createAUserRequest(User user) throws Exception {
		if(user == null) return null;
		String userJSON = objectMapper.writeValueAsString(user);
		return this.mockMvc.perform(post("/user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void createAUserTest() throws Exception {
		createAUser();
	}
	@Test
	void readSingleUser() throws Exception {
		User user = createAUser();
		User userFromResponse = getUserFromId(user.getId());
		Assertions.assertThat(userFromResponse.getId()).isEqualTo(user.getId());
	}



	@Test
	void updateUser() throws Exception{
		User user = createAUser();

		String newName = "pino";
		user.setName(newName);

		String userJSON = objectMapper.writeValueAsString(user);

		MvcResult result = this.mockMvc.perform(put("/user/"+user.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

		Assertions.assertThat(userFromResponse.getId()).isEqualTo(user.getId());
		Assertions.assertThat(userFromResponse.getName()).isEqualTo(newName);

		User userFromResponseGet = getUserFromId(user.getId());
		Assertions.assertThat(userFromResponseGet.getId()).isEqualTo(user.getId());
		Assertions.assertThat(userFromResponseGet.getName()).isEqualTo(newName);

	}

	@Test
	void deleteUser()throws Exception{
		User user = createAUser();
		assertThat(user.getId()).isNotNull();

		this.mockMvc.perform(delete("/user/" + user.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

	}
}
