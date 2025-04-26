package jkml;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testNow() throws Exception {
		mockMvc.perform(get("/now")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.now").exists());
	}

	@Test
	void testRedirect() throws Exception {
		mockMvc.perform(get("/redirect")).andDo(print())
				.andExpect(status().isFound())
				.andExpect(header().exists(HttpHeaders.LOCATION));
	}

	@Test
	void testEnv() throws Exception {
		mockMvc.perform(get("/env")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(is(not(emptyString()))));
	}

	@Test
	void testProperties() throws Exception {
		mockMvc.perform(get("/properties")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$['java.version']").exists());
	}

	@Test
	@WithMockUser(username = "user1", authorities = { "authority1" })
	void testAuthentication() throws Exception {
		mockMvc.perform(get("/authentication")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.authorities").exists());
	}

}
