package seov;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.TimeZone;

@SpringBootTest
class IdentifyServiceApplicationTests {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
	}

	@Test
	void contextLoads() {
	}

}
