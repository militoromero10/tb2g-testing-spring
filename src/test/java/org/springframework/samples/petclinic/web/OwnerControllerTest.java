package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml","classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController controller;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> argumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() {
        reset(clinicService);
    }

    @Test
    void processUpdateOwnerFormValidTest() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit",22)
                    .param("firstName","Camilo")
                    .param("lastName","Romero")
                    .param("address", "Av Siempre Viva 123")
                    .param("city", "Sogamoso")
                    .param("telephone","3196569733"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    void processUpdateOwnerFormNotValidTest() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit",22)
                .param("firstName","Camilo")
                .param("lastName","Romero")
//                    .param("address", "Av Siempre Viva 123")
//                    .param("city", "Sogamoso")
                .param("telephone","3196569733"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner","address"))
                .andExpect(model().attributeHasFieldErrors("owner","city"))
                .andExpect(view().name(OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
    }

    @Test
    void testNewOnwerPostValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("firstName","Camilo")
                    .param("lastName","Romero")
                    .param("address", "Av Siempre Viva 123")
                    .param("city", "Sogamoso")
                    .param("telephone","3196569733"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testNewOnwerPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("firstName","Camilo")
                    .param("lastName","Romero")
//                    .param("address", "Av Siempre Viva 123")
//                    .param("city", "Sogamoso")
                    .param("telephone","3196569733"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner","address"))
                .andExpect(model().attributeHasFieldErrors("owner","city"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    public void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    public void processFindFormTest() throws Exception {
        mockMvc.perform(get("/owners").param("lastName","no hay nadie con este nombre"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    public void testReturnListOfOwners() throws Exception {
        given(clinicService.findOwnerByLastName("")).willReturn(Lists.newArrayList(new Owner(), new Owner()));

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("selections"))
                .andExpect(view().name("owners/ownersList"));

        then(clinicService).should().findOwnerByLastName(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualToIgnoringCase("");
    }
    @Test
    public void testReturnListOfOwnersSingle() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        final String findJustOne = "FindJustOne";
        owner.setLastName(findJustOne);
        given(clinicService.findOwnerByLastName(findJustOne)).willReturn(Lists.newArrayList(owner));

        mockMvc.perform(get("/owners")
                            .param("lastName", findJustOne))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/"+owner.getId()));

        then(clinicService).should().findOwnerByLastName(anyString());

    }
}