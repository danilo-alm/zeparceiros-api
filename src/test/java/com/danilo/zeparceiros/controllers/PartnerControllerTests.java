package com.danilo.zeparceiros.controllers;

import com.danilo.zeparceiros.dtos.PartnerRequestDTO;
import com.danilo.zeparceiros.dtos.PartnerResponseDTO;
import com.danilo.zeparceiros.services.PartnerService;
import com.danilo.zeparceiros.utils.PartnerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnerController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PartnerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PartnerService partnerService;

    private PartnerRequestDTO partnerRequestDTO;
    private PartnerResponseDTO partnerResponseDTO;

    public void validateResponseMapIsResponseDTO(Map<String, Object> responseMap, PartnerResponseDTO partnerResponseDTO) throws Exception {
        String expectedCoverageAreaJson = objectMapper.writeValueAsString(partnerResponseDTO.coverageArea());
        String expectedAddressJson = objectMapper.writeValueAsString(partnerResponseDTO.address());

        assertAll(
            () -> assertEquals(partnerResponseDTO.document(), responseMap.get("document")),
            () -> assertEquals(partnerResponseDTO.tradingName(), responseMap.get("tradingName")),
            () -> assertEquals(partnerResponseDTO.ownerName(), responseMap.get("ownerName")),
            () -> assertEquals(expectedCoverageAreaJson, objectMapper.writeValueAsString(responseMap.get("coverageArea"))),
            () -> assertEquals(expectedAddressJson, objectMapper.writeValueAsString(responseMap.get("address")))
        );
    }

    public void validateResponseIsPartnerResponseDTO(ResultActions response, PartnerResponseDTO partnerResponseDTO) throws Exception {
        String jsonResponse = response.andReturn().getResponse().getContentAsString();
        Map<String, Object> responseMap = JsonPath.parse(jsonResponse).read("$");

        this.validateResponseMapIsResponseDTO(responseMap, partnerResponseDTO);
    }

    @BeforeEach
    public void setUp() {
        partnerRequestDTO = new PartnerRequestDTO();
        partnerResponseDTO = new PartnerResponseDTO(PartnerFactory.createPartner());
    }

    @Test
    public void PartnerController_CreatePartner_ReturnPartnerResponseDto() throws Exception {
        given(partnerService.createPartner(any(PartnerRequestDTO.class))).willReturn(partnerResponseDTO);

        ResultActions response = mockMvc.perform(post("/api/partners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partnerRequestDTO)))
            .andExpect(status().isCreated());

        this.validateResponseIsPartnerResponseDTO(response, partnerResponseDTO);
    }

    @Test
    public void PartnerController_CreatePartners_ReturnListOfPartnerResponseDto() throws Exception {
        given(partnerService.createPartners(anyMap())).willReturn(List.of(partnerResponseDTO));

        Map<String, List<PartnerRequestDTO>> content = new HashMap<>();
        content.put("pdvs", List.of(partnerRequestDTO));

        ResultActions response = mockMvc.perform(post("/api/partners/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(content)))
            .andExpect(status().isCreated());

        String jsonResponse = response.andReturn().getResponse().getContentAsString();
        List<Map<String, Object>> responseMapList = JsonPath.parse(jsonResponse).read("$");

        this.validateResponseMapIsResponseDTO(responseMapList.get(0), partnerResponseDTO);
    }

    @Test
    public void PartnerController_GetPartnerById_ReturnPartnerResponseDto() throws Exception {
        given(partnerService.getPartnerById(anyLong())).willReturn(partnerResponseDTO);
        ResultActions response = mockMvc.perform(get("/api/partners/1")).andExpect(status().isOk());
        this.validateResponseIsPartnerResponseDTO(response, partnerResponseDTO);
    }

    @Test
    public void PartnerController_SearchPartner_ReturnPartnerResponseDto() throws Exception {
        given(partnerService.searchPartner(anyDouble(), anyDouble())).willReturn(partnerResponseDTO);

        ResultActions response = mockMvc.perform(get("/api/partners/search")
                .param("lat", "0")
                .param("lon", "0"))
            .andExpect(status().isOk());

        this.validateResponseIsPartnerResponseDTO(response, partnerResponseDTO);
    }
}
