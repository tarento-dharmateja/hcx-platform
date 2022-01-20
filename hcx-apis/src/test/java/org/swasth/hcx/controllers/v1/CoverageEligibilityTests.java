package org.swasth.hcx.controllers.v1;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.swasth.common.dto.HeaderAudit;
import org.swasth.hcx.controllers.BaseSpec;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class CoverageEligibilityTests extends BaseSpec {




    @Test
    public void check_coverage_eligibility_success_scenario() throws Exception {
      doNothing().when(mockKafkaClient).send(anyString(),anyString(),any());
      String requestBody = getRequestBody();
      MvcResult mvcResult = mockMvc.perform(post("/v1/coverageeligibility/check").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
      MockHttpServletResponse response = mvcResult.getResponse();
      int status = response.getStatus();
      assertEquals(202, status);
    }

    @Test
    public void check_coverage_eligibility_headers_missing_scenario() throws Exception {
        doNothing().when(mockKafkaClient).send(anyString(),anyString(),any());
        String requestBody = getHeadersMissingRequestBody();
        MvcResult mvcResult = mockMvc.perform(post("/v1/coverageeligibility/check").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(400, status);
    }


    @Test
    public void check_coverage_eligibility_client_exception_scenario() throws Exception {
        String requestBody = getBadRequestBody();
        MvcResult mvcResult = mockMvc.perform(post("/v1/coverageeligibility/check").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(400, status);
    }

    @Test
    public void check_coverage_eligibility_exception_scenario() throws Exception {
        String requestBody = "{}";
        MvcResult mvcResult = mockMvc.perform(post("/v1/coverageeligibility/check").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(500, status);
    }

    @Test
    public void on_check_coverage_eligibility_success_scenario() throws Exception {
        doNothing().when(mockKafkaClient).send(anyString(),anyString(),any());
        when(headerAuditService.search(any())).thenReturn(Arrays.asList(new HeaderAudit("26b1060c-1e83-4600-9612-ea31e0ca5091", "1-2799b6a4-cf2d-45fe-a5e1-5f1c82979e0d", "5e934f90-111d-4f0b-b016-c22d820674e1", "1e83-460a-4f0b-b016-c22d820674e1", "2022-01-06T20:35:52.636+0530", "93f908ba", "59cefda2-a4cc-4795-95f3-fb9e82e21cef", "/v1/coverageeligibility/check", new Object(), new Object(), "request.dispatched")));
        String requestBody = getRequestBody();
        MvcResult mvcResult = mockMvc.perform(post("/v1/coverageeligibility/on_check").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(202, status);
    }
    @Test
    public void on_check_coverage_eligibility_client_exception_scenario() throws Exception {
        when(headerAuditService.search(any())).thenReturn(Arrays.asList(new HeaderAudit("26b1060c-1e83-4600-9612-ea31e0ca5091", "1-2799b6a4-cf2d-45fe-a5e1-5f1c82979e0d", "5e934f90-111d-4f0b-b016-c22d820674e1", "1e83-460a-4f0b-b016-c22d820674e1", "2022-01-06T20:35:52.636+0530", "93f908ba", "59cefda2-a4cc-4795-95f3-fb9e82e21cef", "/v1/coverageeligibility/check", new Object(), new Object(), "request.dispatched")));
        String requestBody = getBadRequestBody();
        MvcResult mvcResult = mockMvc.perform(post("/v1/coverageeligibility/on_check").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(400, status);
    }
    @Test
    public void on_check_coverage_eligibility_exception_scenario() throws Exception {
        String requestBody = "{}";
        MvcResult mvcResult = mockMvc.perform(post("/v1/coverageeligibility/on_check").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(500, status);
    }
}
