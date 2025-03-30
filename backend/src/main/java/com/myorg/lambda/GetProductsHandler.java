package com.myorg.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.model.Product;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetProductsHandler implements RequestHandler<Object, Map<String, Object>> {

    private final static String Data_file = "products.json";
    @Override
    public Map<String, Object> handleRequest(Object input, Context context) {
        try {
            // Đọc file JSON
            InputStream jsonData = getClass().getClassLoader().getResourceAsStream("products.json");
            if (jsonData == null) {
                throw new RuntimeException("File products.json không tồn tại!");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Product> products = objectMapper.readValue(
                    jsonData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class)
            );

            // Log số lượng sản phẩm
            context.getLogger().log("Số lượng sản phẩm: " + products.size());

            // Trả về JSON đúng định dạng
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 200);
            response.put("body", objectMapper.writeValueAsString(products));
            response.put("headers", Map.of("Content-Type", "application/json"));

            return response;
        } catch (Exception e) {
            context.getLogger().log("Lỗi xử lý: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("body", "Internal Server Error");
            return errorResponse;
        }
    }

}
