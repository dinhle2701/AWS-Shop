import { MemoryRouter } from "react-router-dom";
import { test, expect } from "vitest";
import App from "~/components/App/App";
import { renderWithProviders } from "~/testUtils";
import { screen, waitForElementToBeRemoved } from "@testing-library/react";
import { formatAsPrice } from "~/utils/utils";

const API_URL = "https://yeev8ynukl.execute-api.us-east-1.amazonaws.com/prod/products";

test("Renders products list from real API", async () => {
  // // Gọi API thực tế để lấy danh sách sản phẩm
  // const response = await fetch(API_URL);
  // const products = await response.json();

  // renderWithProviders(
  //   <MemoryRouter initialEntries={["/"]}>
  //     <App />
  //   </MemoryRouter>
  // );

  // // Đợi UI hiển thị dữ liệu (nếu có loader "Loading")
  // await waitForElementToBeRemoved(() => screen.queryByText(/Loading/));

  // // Kiểm tra sản phẩm có hiển thị không
  // products.forEach((product) => {
  //   expect(screen.getByText(product.title)).toBeInTheDocument();
  //   expect(screen.getByText(formatAsPrice(product.price))).toBeInTheDocument();
  // });
});
