package com.ecommerce.bikes.controller;

import com.ecommerce.bikes.DockerConfiguration;
import com.ecommerce.bikes.domain.Like;
import com.ecommerce.bikes.exception.ErrorResponse;
import com.ecommerce.bikes.http.ProductResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductIT extends DockerConfiguration {

    @Autowired
    private TestRestTemplate rest;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(1)
    public void should_return_product_by_id() {
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<ProductResponse> result = this.rest.getForEntity(createUrl() + "api/products/1", ProductResponse.class, request);

        assert result.getBody() != null;
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(productsResponses.get(0), result.getBody());
    }

    @Test
    @Order(9)
    public void should_throw_ProductNotFoundException_when_get_product_by_id() {
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ErrorResponse expectedResponse = new ErrorResponse(404, "The product does not exist");

        ResponseEntity<ErrorResponse> result = this.rest.getForEntity(createUrl() + "api/products/167", ErrorResponse.class, request);

        ErrorResponse errorResponse = result.getBody();

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(expectedResponse, errorResponse);
    }

    @Test
    @Order(2)
    public void should_return_all_products() {
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductResponse>> response = this.rest.exchange(
                createUrl() + "api/products", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        List<ProductResponse> products = response.getBody();

        assert products != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productsResponses, products);
    }

    @Test
    @Order(3)
    public void should_return_all_products_by_type() {
        String expectedType = "road";

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductResponse>> response = this.rest.exchange(
                createUrl() + "api/products/type/" + expectedType, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        List<ProductResponse> products = response.getBody();

        assert products != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(productsResponses.get(0)), products);
    }

    @Test
    @Order(4)
    public void should_return_all_favourites_products() {
        long expectedUserId = 1L;

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductResponse>> response = this.rest.exchange(
                createUrl() + "api/products/favourites/" + expectedUserId, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        List<ProductResponse> products = response.getBody();

        assert products != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(productsResponses.get(0), productsResponses.get(1)), products);
    }

    @Test
    @Order(10)
    public void should_throw_UserNotFoundException_when_get_all_favourites_products() {
        long userId = 198L;

        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ErrorResponse expectedResponse = new ErrorResponse(404, "The user does not exist");

        ResponseEntity<ErrorResponse> response = this.rest.exchange(
                createUrl() + "api/products/favourites/" + userId, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        ErrorResponse errorResponse = response.getBody();

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedResponse, errorResponse);
    }

    @Test
    @Order(5)
    public void should_return_all_products_by_name() {
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        String expectedSearch = "Meth";

        ResponseEntity<List<ProductResponse>> response = this.rest.exchange(
                createUrl() + "api/products/search/" + expectedSearch, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        List<ProductResponse> products = response.getBody();

        assert products != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productsResponses, products);
    }

    @Test
    @Order(6)
    public void should_add_like() {
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductResponse>> response = this.rest.exchange(
                createUrl() + "api/products/likes/2/1", HttpMethod.POST, request, new ParameterizedTypeReference<>() {
                });

        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void should_get_like() {
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<Object> response = this.rest.exchange(
                createUrl() + "api/products/likes/3/2", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        Object result = response.getBody();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(9)
    public void should_throw_LikeDoesNotExistException_when_get_not_existent_like() {
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ErrorResponse expectedResponse = new ErrorResponse(404, "Does not exist a result with this specifications");

        ResponseEntity<ErrorResponse> response = this.rest.exchange(
                createUrl() + "api/products/likes/3/3", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        ErrorResponse errorResponse = response.getBody();

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedResponse, errorResponse);
    }

    @Test
    @Order(8)
    public void should_delete_like() {
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductResponse>> response = this.rest.exchange(
                createUrl() + "api/products/likes/2/1", HttpMethod.DELETE, request, new ParameterizedTypeReference<>() {
                });

        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private static List<ProductResponse> productsResponses = List.of(
            new ProductResponse(
                    1L,
                    "Methanol CV FS 9.3 XT",
                    "road",
                    4707,
                    0,
                    "ULTIMATE CROSS-COUNTRY RACE BIKE",
                    "Bianchi Methanol FS es la joya de doble suspensión de Bianchi. Una btt que te permitirá subir como un cohete y bajar como un rayo, gracias a su geometría renovada y su carbono CV que absorve el 80% de las vibraciones.",
                    emptySet(),
                    emptyList(),
                    List.of(
                            new Like(1L, 1L, 1L)
                    )),
            new ProductResponse(
                    2L,
                    "Methanol CV FS 9.2 XTR",
                    "mtb",
                    6195,
                    0,
                    "ULTIMATE CROSS-COUNTRY RACE BIKE",
                    "Bianchi Methanol FS es la joya de doble suspensión de Bianchi. Una btt que te permitirá subir como un cohete y bajar como un rayo, gracias a su geometría renovada y su carbono CV que absorve el 80% de las vibraciones.",
                    emptySet(),
                    emptyList(),
                    List.of(
                            new Like(2L, 1L, 2L)
                    )
            ),
            new ProductResponse(
                    3L,
                    "Methanol CV FS 9.1 XX1",
                    "mtb",
                    9932,
                    0,
                    "ULTIMATE CROS-COUNTRY RACE BIKE",
                    "Bianchi Methanol FS es la joya de doble suspensión de Bianchi. Una btt que te permitirá subir como un cohete y bajar como un rayo, gracias a su geometría renovada y su carbono CV que absorve el 80% de las vibraciones.",
                    emptySet(),
                    emptyList(),
                    List.of(
                            new Like(3L, 2L, 3L)
                    )
            )
    );
}