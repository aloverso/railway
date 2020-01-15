package com.library.contracts;

import com.library.LibraryApplication;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibraryApplication.class)
@ActiveProfiles("contracts")
public abstract class ContractVerifierBase {

    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setUp() {
        RestAssuredMockMvc.webAppContextSetup(webAppContext);
    }
}