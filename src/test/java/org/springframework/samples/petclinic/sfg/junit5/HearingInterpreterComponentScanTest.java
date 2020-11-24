package org.springframework.samples.petclinic.sfg.junit5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = HearingInterpreterComponentScanTest.InnerConfig.class)
class HearingInterpreterComponentScanTest {
    /**
     * deberia funcionar, al parecer no logra reconocer el bean primario, pues encuentra dos definiciones
     * para usar y no sabe que hacer
     */
    @Configuration
    @ComponentScan("org.springframework.samples.petclinic.sfg")
    static class InnerConfig {

    }

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIHeard() {
        String value = hearingInterpreter.whatIHeard();
        assert value.equals("Laurel");
    }
}