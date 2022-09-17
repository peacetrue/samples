package com.github.peacetrue.sample.echo;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author peace
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        EchoServiceAutoConfiguration.class,
        EchoControllerAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        MockMvcAutoConfiguration.class,
})
class EchoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void echo() throws Exception {
        String input = RandomStringUtils.randomAlphanumeric(10);
        this.mockMvc.perform(get("/echo?input={0}", input))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(input))
        ;
    }
}
