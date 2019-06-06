package com.finance.fund.transfer.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.services.FundTransferService;
import com.finance.fund.transfer.utils.JsonUtils;
import com.finance.fund.transfer.utils.TransactionPayload;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Nilesh
 */
class FundTransferServiceTest {

    @BeforeAll
    static void setUp() {
        FundTransferService.start();
    }

    @AfterAll
    static void tearDown() {
        FundTransferService.stop();
    }

    @Test
    void getAllAccountHoldersDefaultPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:4567/accountholders?limit=10");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"hasMore\": false,\n" +
                        "  \"pageNumber\": 1,\n" +
                        "  \"recordsPerPage\": 10,\n" +
                        "  \"content\": [\n" +
                        "    {\n" +
                        "      \"name\": \"AVS Technologies\",\n" +
                        "      \"id\": 1,\n" +
                        "      \"accountHolderType\": \"CORPORATE\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}", json);
            }
        }
    }

    @Test
    void getAllAccountHoldersSecondPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:4567/accountholders?page=2&limit=20");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"hasMore\": false,\n" +
                        "  \"pageNumber\": 2,\n" +
                        "  \"recordsPerPage\": 20,\n" +
                        "  \"content\": []\n" +
                        "}", json);
            }
        }
    }

    @Test
    void getAllAccountHoldersInvalidPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:4567/accountholders?page=0&limit=20");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Page number should be positive and starts with 1\"\n" +
                        "}", json);
            }
        }
    }

    @Test
    void getAllAccountHoldersWithoutPagination() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:4567/accountholders");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Invalid pagination parameters\"\n" +
                        "}", json);
            }
        }
    }

    @Test
    void transferMoneyWithTheSameAccount() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpPost httpPost = new HttpPost("http://localhost:4567/transactions");
            final TransactionPayload payload = new TransactionPayload(1L, 1L, BigDecimal.valueOf(123456, 2));
            final String jsonPayload = JsonUtils.make().toJson(payload);
            final StringEntity stringEntity = new StringEntity(jsonPayload);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Accounts must be different\"\n" +
                        "}", json);
            }
        }
    }
}