import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ProductsServlet extends HttpServlet {
    LinkedHashMap<String, LinkedHashSet<String>> productsData = new LinkedHashMap<>();
    LinkedHashSet<String> productDetails = new LinkedHashSet<>();

    public void init() {
        productDetails.add("1");
        productDetails.add("Itel IT2163");
        productDetails.add("https://rukminim1.flixcart.com/image/312/312/kingqkw0/mobile/z/n/v/itel-it2163-it2163-original-imafye3ngxdgya75.jpeg?q=70");
        productDetails.add("7,999.00");
        productDetails.add("buy-now?id=1");
        productsData.put("1", new LinkedHashSet<>(productDetails));
        productDetails.clear();
        productDetails.add("2");
        productDetails.add("Itel Ace Young");
        productDetails.add("https://rukminim1.flixcart.com/image/312/312/ko0d6kw0/mobile/q/e/r/ace-young-it2161-itel-original-imag2k79myhnfaug.jpeg?q=70");
        productDetails.add("4,899.00");
        productDetails.add("buy-now?id=2");
        productsData.put("2", new LinkedHashSet<>(productDetails));
        productDetails.clear();
        productDetails.add("3");
        productDetails.add("Itel Power 400");
        productDetails.add("https://rukminim1.flixcart.com/image/312/312/jtq47m80/mobile/n/f/v/itel-it5617-it5617-original-imaffyh3hvhymdym.jpeg?q=70");
        productDetails.add("3,599.00");
        productDetails.add("buy-now?id=3");
        productsData.put("3", new LinkedHashSet<>(productDetails));
        productDetails.clear();
        productDetails.add("4");
        productDetails.add("Itel ACE");
        productDetails.add("https://rukminim1.flixcart.com/image/312/312/kingqkw0/mobile/s/g/g/itel-it2163-it2163-original-imafye3nmafgfacb.jpeg?q=70");
        productDetails.add("5,699.00");
        productDetails.add("buy-now?id=4");
        productsData.put("4", new LinkedHashSet<>(productDetails));
        productDetails.clear();
        productDetails.add("5");
        productDetails.add("Itel HRE");
        productDetails.add("https://rukminim1.flixcart.com/image/312/312/kiew3gw0/mobile/j/s/7/itel-ace-it2161-original-imafy7rney8ykzqk.jpeg?q=70");
        productDetails.add("3,499.00");
        productDetails.add("buy-now?id=5");
        productsData.put("5", new LinkedHashSet<>(productDetails));
        productDetails.clear();
        productDetails.add("6");
        productDetails.add("Itel U10");
        productDetails.add("https://rukminim1.flixcart.com/image/312/312/kiew3gw0/mobile/h/7/v/itel-ace-it2161-original-imafy7rnyspwayeb.jpeg?q=70");
        productDetails.add("2,999.00");
        productDetails.add("buy-now?id=6");
        productsData.put("6", new LinkedHashSet<>(productDetails));
        productDetails.clear();
        productDetails.add("7");
        productDetails.add("Itel Power 100 New");
        productDetails.add("https://rukminim1.flixcart.com/image/312/312/jtq47m80/mobile/q/e/k/itel-it5617-it5617-original-imaffyzh6z8s53w3.jpeg?q=70");
        productDetails.add("4,999.00");
        productDetails.add("buy-now?id=7");
        productsData.put("7", new LinkedHashSet<>(productDetails));
        productDetails.clear();
        productDetails.add("8");
        productDetails.add("Itel IT2173");
        productDetails.add("https://rukminim1.flixcart.com/image/312/312/kingqkw0/mobile/z/n/v/itel-it2163-it2163-original-imafye3ngxdgya75.jpeg?q=70");
        productDetails.add("4,000.00");
        productDetails.add("buy-now?id=8");
        productsData.put("8", new LinkedHashSet<>(productDetails));
        productDetails.clear();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("products", productsData);
        request.getRequestDispatcher("/WEB-INF/pages/products.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String productId = (productsData.size() + 1) + "";
            String productName = request.getParameter("productName");
            String productPrice = request.getParameter("hiddenProductPrice");
            String productAssetUrl = request.getParameter("productAssetUrl");
            productDetails.add(productId);
            productDetails.add(productName);
            productDetails.add(productAssetUrl);
            productDetails.add(productPrice);
            productsData.put(productId, new LinkedHashSet<>(productDetails));
            productDetails.clear();
            response.sendRedirect("products");
        } catch (Exception e) {
            response.setStatus(500);
        }
    }

    public void destroy() {
    }
}