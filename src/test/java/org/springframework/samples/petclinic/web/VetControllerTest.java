package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    ClinicService service;

    @Mock
    Map<String, Object> model;

    @InjectMocks
    VetController controller;

    List<Vet> vets = new ArrayList<>();

    @BeforeEach
    void setUp() {
        vets.add(new Vet());
        given(service.findVets()).willReturn(vets);
    }

    @Test
    void showVetList() {
        String view = "vets/vetList";
        String value = controller.showVetList(model);

        then(service).should().findVets();
        then(model).should().put(anyString(), any());
        assertThat(view).isGreaterThanOrEqualTo(value);
    }

    @Test
    void showResourcesVetList() {
        Vets list = controller.showResourcesVetList();

        then(service).should().findVets();
        assertThat(list.getVetList()).hasSize(1);
    }
}