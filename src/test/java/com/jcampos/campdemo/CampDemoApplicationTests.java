package com.jcampos.campdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "test")
@SpringBootTest
class CampDemoApplicationTests {

  @Test
  void contextLoads() {
  }

}
