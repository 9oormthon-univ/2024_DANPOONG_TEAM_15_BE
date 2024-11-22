package com.ivory.ivory;

import com.amazonaws.services.s3.AmazonS3;
import com.ivory.ivory.jwt.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class IvoryApplicationTests {

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private AmazonS3 amazonS3;

	@Test
	void contextLoads() {
	}

}
