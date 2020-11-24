package org.springframework.samples.petclinic.sfg.junit5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.sfg.BaseConfig;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.samples.petclinic.sfg.LaurelConfig;
import org.springframework.samples.petclinic.sfg.LaurelWordProducer;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = {HearingInterpreterInnerClassTest.InnerConfiguration.class})
class HearingInterpreterInnerClassTest {

    @Configuration
    static class InnerConfiguration {
        @Bean
        HearingInterpreter getHearingInterpreter(){
            return new HearingInterpreter( new LaurelWordProducer());
        }

    }

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIHeard() {
        String value = hearingInterpreter.whatIHeard();
        assert value.equals("Laurel");
    }
}