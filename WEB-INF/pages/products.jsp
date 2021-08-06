<%@ page import="java.util.*" import="java.io.*"%>
<%
    LinkedHashMap<String, LinkedHashSet<String>> productsData = (LinkedHashMap<String, LinkedHashSet<String>>)request.getAttribute("products");
%>
<html>
    <head>
        <title>
            Shopping Cart
        </title>
        <link href="https://fonts.googleapis.com/css2?family=Festive&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link href="assets/css/products.css" rel="stylesheet">
        <script src="assets/js/products.script.js"></script>
    <head>
    <header>
        <nav>
            <div class="logo-container">
                <span class="heading-logo">Shopping App</span>
            </div>
            <div class="nav-elements">
                <%
                    HttpSession httpSession = request.getSession();
                    if(httpSession.getAttribute("username") != null) {
                %>
                    <a class="nav-link" onclick="showAddProduct()">
                        <span class="material-icons nav-icon">add</span>
                        Add Product
                    </a>
                    <div class="cart-nav">
                        <a onclick="showCart();" class="nav-link">
                            <span id="cartBadge" class="badge" style="display: none;"></span>
                            <span class="material-icons nav-icon">shopping_cart</span>
                            Cart
                        </a>
                        <div class="product-cart hide">
                            <div class="product-cart-header">
                                Products
                            </div>
                            <div class="cart-products">
                                <div class="no-cart-product">
                                    <span>
                                        No products added! Start adding now.
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <a class="nav-link" href="logout">
                        <span class="material-icons nav-icon">logout</span>
                        Logout
                    </a>
                <%
                    } else {
                %>
                    <a class="nav-link" href="do-auth">
                        <span class="material-icons nav-icon">login</span>
                        Login / SignUp
                    </a>
                <%
                    }
                %>
            </div>
        </nav>
    </header>
    <body>
        <div class="master-container">
            <div class="page-heading">
                <span class="page-heading-span">
                    All Products
                </span>
            </div>
            <div class="products-container">
                <form action="products" method="POST" id="product-editor">
                    <div style="height: 100%;" class="product">
                        <div class="image-editor-icons">
                            <span class="material-icons image-editor-icon" onclick="hideAddProduct()">close</span>
                        </div>
                        <div class="product-img-container">
                            <div class="image-overlay" onclick="editImage()">
                                <span class="material-icons image-editor-icon">edit</span>
                            </div>
                            <img id="product-image-demo" class="product-img" style="height: 130px; display: none;">
                        </div>
                        <div class="product-details">
                            <div class="product-details-header">
                                <span class="product-name full-width">
                                    <input type="text" class="full-width" placeholder="Product Name" name="productName" required>
                                </span>
                            </div>
                            <div id="urlEditor-container" class="product-image-editor-container">
                                <span class="product-name full-width">
                                    <input id="urlEditor" type="url" class="full-width" placeholder="Product Image Url" name="productAssetUrl" required>
                                </span>
                            </div>
                            <div class="product-price">
                                <div class="product-price-editor">
                                    <span class="r-symbol">&#8377</span>
                                    <input type="number" class="full-width" placeholder="Product Price" id="productPrice" name="productPrice" required>
                                </div>
                            </div>
                            <input type="hidden" name="hiddenProductPrice" id="hiddenProductPrice">
                            <div class="product-actions"> 
                                <button type="submit" class="full-width">Add product</button>
                                <!-- <button class="bn">Buy now</button> -->
                            </div>
                        </div>
                    </div>
                </form>
                <%
                    for(Map.Entry<String, LinkedHashSet<String>> mapEntry: productsData.entrySet()) {
                        String[] productDetailsArray = new String[4];
                        productDetailsArray = mapEntry.getValue().toArray(productDetailsArray);
                %>
                    <div class="product <%= productDetailsArray[0] %>PND38">
                        <div class="product-img-container">
                            <img class="product-img" src="<%= productDetailsArray[2] %>">
                        </div>
                        <div class="product-details">
                            <div class="product-details-header">
                                <span class="product-name"><%= productDetailsArray[1] %></span>
                                <span class="material-icons rating-icon">star</span>
                            </div>
                            <div class="product-price">
                                <span class="product-price-span">&#8377 <%= productDetailsArray[3] %></span>
                            </div>
                            <div class="product-actions"> 
                                <%
                                    if(httpSession.getAttribute("username") != null) {
                                %>
                                    <button onclick="addToCart('<%= productDetailsArray[0] %>', '<%= productDetailsArray[1] %>', '<%= productDetailsArray[2] %>', '<%= productDetailsArray[3] %>')">Add to cart</button>
                                <%
                                    } else {
                                %>
                                    <a href="do-auth" class="redirect-link">
                                        <button>
                                            Add to cart
                                        </button>
                                    </a>
                                <%
                                    }
                                %>
                                <button class="bn">Buy now</button>
                            </div>
                        </div>
                    </div>
                <%
                    }
                %>
            </div>
        </div>
    </body>
</html>
