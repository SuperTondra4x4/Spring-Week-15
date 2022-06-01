package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.promineotech.jeep.controller.support.FetchJeepTestSupport;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import lombok.Getter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
    config = @SqlConfig(encoding = "utf-8"))

class FetchJeepTest extends FetchJeepTestSupport {
  @Autowired
  private JdbcTemplate jdbcTemplate;
//  @Test
//  void testDb() {
//    int numrows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "customers");
//    System.out.println("num=" + numrows);
//  }
  
//  @Disabled
//  @Autowired
//  @Getter
//  
//  private TestRestTemplate restTemplate;
//  
//  @LocalServerPort
//  private int serverPort;
//
//  protected String getBaseUri() {
//    return String.format("http://localhost:%d/jeeps", serverPort);
//  }
  @Test
  void testThatAJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
    
    System.out.println("hi");
// given: a valid model, trim and URI
    JeepModel model = JeepModel.WRANGLER;
    String trim = "Sport";
    String uri = 
        String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);

// when: a connection is made to the URI
//    ResponseEntity<Jeep> response = getRestTemplate().getForEntity(uri, Jeep.class);
    ResponseEntity<List<Jeep>> response = getRestTemplate().exchange(uri, 
        HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    
// then: a success (OK - 200) status code is returned
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

//    And: the actual list returned is the same as the expected list
    List<Jeep> actual = response.getBody();
    List<Jeep> expected = buildExpected();
    
    actual.forEach(jeep -> jeep.setModelPK(null));

    assertThat(actual).isEqualTo(expected);
    }
}
  
//    fail("Not yet implemented");

