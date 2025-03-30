package com.myorg.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.model.Product;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GetProductById implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    private final static String DATA_FILE = "products.json";

    private Map<String, Object> createResponse(int statusCode, String body) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", statusCode);
        response.put("body", body);
        response.put("headers", Map.of("Content-Type", "application/json"));
        return response;
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        try {
            context.getLogger().log("Nhận yêu cầu: " + input);

            // Đọc file JSON
            InputStream jsonData = getClass().getClassLoader().getResourceAsStream(DATA_FILE);
            if (jsonData == null) throw new RuntimeException("File products.json không tồn tại!");

            ObjectMapper objectMapper = new ObjectMapper();
            Product[] products = objectMapper.readValue(jsonData, Product[].class);

            // ✅ Lấy pathParameters đúng cách
            Map<String, Object> pathParameters = (Map<String, Object>) input.get("pathParameters");
            if (pathParameters == null || !pathParameters.containsKey("id")) {
                throw new RuntimeException("ID không tồn tại trong pathParameters!");
            }

            String idString = (String) pathParameters.get("id");
            int productId = Integer.parseInt(idString);

            // Tìm sản phẩm theo ID
            Product product = null;
            for (Product p : products) {
                if (p.getId() == productId) {
                    product = p;
                    break;
                }
            }

            if (product == null) {
                return createResponse(404, "Sản phẩm không tồn tại!");
            }

            // Trả về JSON đúng định dạng
            return createResponse(200, objectMapper.writeValueAsString(product));

        } catch (Exception e) {
            context.getLogger().log("Lỗi xử lý: " + e.getMessage());
            return createResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
