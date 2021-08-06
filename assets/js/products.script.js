let cartProducts = {},
    totalProductsCount = 0;
    priceFormatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'INR',
    });

window.onload = () => {
    document.getElementById('urlEditor').addEventListener('focusout', (event) => {
        let imageUrl = document.getElementById('urlEditor').value;
        if(!imageUrl) return;
        document.getElementById('product-image-demo').src = imageUrl;
        document.getElementById('urlEditor-container').style.display = "none";
        document.getElementById('product-image-demo').style.display = "block";
    })
    document.getElementById('productPrice').addEventListener('focusout', (event) => {
        let productPrice = document.getElementById('productPrice').value;
        if(!productPrice) return;
        document.getElementById('hiddenProductPrice').value = priceFormatter.format(productPrice).substr(1);
    })
}

function editImage() {
    document.getElementById('urlEditor').value = "";
    document.getElementById('product-image-demo').src = null;
    document.getElementById('urlEditor-container').style.display = "block";
    document.getElementById('product-image-demo').style.display = "none";
}

function showAddProduct() {
    document.getElementById('product-editor').style.display = "block";
}

function hideAddProduct() {
    document.getElementById('product-editor').style.display = "none";
}

function addToCart(productId, productName, productAssetUrl, productPrice) {
    if(productId in cartProducts) {
        increaseProductQuantity(productId);
    } else {
        cartProducts[productId] = {};
        cartProducts[productId].productId = productId;
        cartProducts[productId].productName = productName;
        cartProducts[productId].productAssesUrl = productAssetUrl;
        cartProducts[productId].productPrice = productPrice;
        cartProducts[productId].nos = 1;
        document.getElementById('cartBadge').innerText = ++totalProductsCount;
        document.getElementById('cartBadge').style.display = "flex";
        let mainCartProductContainer = document.createElement('div');
        mainCartProductContainer.classList.add('cart-product');
        mainCartProductContainer.id = "PDI83" + productId;
        let productRemoveIconContainer = document.createElement('div');
        productRemoveIconContainer.classList.add('product-remove-icon-container');
        let removeIcon = document.createElement('span');
        removeIcon.classList.add('material-icons');
        removeIcon.classList.add('product-remove-icon');
        removeIcon.innerHTML = "close";
        removeIcon.addEventListener('click', (event) => {
            removeFromCart(productId);
        })
        productRemoveIconContainer.appendChild(removeIcon);
        mainCartProductContainer.appendChild(productRemoveIconContainer);
        let cartProductImageContainer = document.createElement('div');
        cartProductImageContainer.classList.add('cart-product-image');
        let cartProductImage = document.createElement('img');
        cartProductImage.src = productAssetUrl;
        cartProductImageContainer.appendChild(cartProductImage);
        mainCartProductContainer.appendChild(cartProductImageContainer);
        let cartProductDetailcontainer = document.createElement('div');
        cartProductDetailcontainer.classList.add('cart-product-detail');
        let cartProductDetailName = document.createElement('span');
        cartProductDetailName.classList.add('cart-product-name');
        cartProductDetailName.innerText = productName;
        cartProductDetailcontainer.appendChild(cartProductDetailName);
        let cartProductDetailPrice = document.createElement('span');
        cartProductDetailPrice.classList.add('cart-product-quantity');
        cartProductDetailPrice.innerText = "Quantity: " + cartProducts[productId].nos + " @ " + productPrice + "/-";
        cartProductDetailcontainer.appendChild(cartProductDetailPrice);
        let cartProductQuantityButtonsContainer = document.createElement('span');
        cartProductQuantityButtonsContainer.innerHTML = 
        `<button class="cart-quantity-button" onclick="decreaseProductQuantity(` + productId + `)">-</button>
        <button class="cart-quantity-button" onclick="increaseProductQuantity(` + productId + `)">+</button>`
        cartProductDetailcontainer.appendChild(cartProductQuantityButtonsContainer);
        mainCartProductContainer.appendChild(cartProductDetailcontainer);
        let cartProductPriceContainer = document.createElement('div');
        cartProductPriceContainer.classList.add('cart-product-price');
        let cartProductPrice = document.createElement('span');
        cartProductPrice.innerText = priceFormatter.format(productPrice.replaceAll(',', ''));
        cartProductPriceContainer.appendChild(cartProductPrice);
        mainCartProductContainer.appendChild(cartProductPriceContainer);
        document.getElementsByClassName('cart-products')[0].appendChild(mainCartProductContainer);
    }
    if(totalProductsCount === 1) {
        removeNoProductContainer();
    }
}

function removeFromCart(productId) {
    totalProductsCount = totalProductsCount - cartProducts[productId].nos;
    delete cartProducts[productId];
    document.getElementById("PDI83" + productId).remove();
    // console.log(cartProducts)
    document.getElementById('cartBadge').innerText = totalProductsCount;
    if(totalProductsCount === 0) {
        document.getElementById('cartBadge').style.display = "none";
        addNoProductContainer();
    }
}

function decreaseProductQuantity(productId) {
    cartProducts[productId].nos--;
    document.getElementById('cartBadge').innerText = --totalProductsCount;
    if(cartProducts[productId].nos === 0) {
        delete cartProducts[productId];
        // console.log("PDI83" + productId)
        document.getElementById("PDI83" + productId).remove();
    } else {
        let originalString = document.querySelector("#" + "PDI83" + productId + " .cart-product-quantity").innerText;
        document.querySelector("#" + "PDI83" + productId + " .cart-product-quantity").innerText = "Quantity: " + cartProducts[productId].nos + " @ " + originalString.toString().split('@')[1];
    }
    // console.log(totalProductsCount);
    if(totalProductsCount === 0) {
        document.getElementById('cartBadge').style.display = "none";
        addNoProductContainer();
        return;
    }
    let totalProductPrice = cartProducts[productId].nos * Number.parseInt(cartProducts[productId].productPrice.replaceAll(',', ''));
    document.querySelector("#" + "PDI83" + productId + " .cart-product-price").innerText = priceFormatter.format(totalProductPrice);
}

function increaseProductQuantity(productId) {
    cartProducts[productId].nos++;
    // console.log(cartProducts)
    document.getElementById('cartBadge').innerText = ++totalProductsCount;
    let originalString = document.querySelector("#" + "PDI83" + productId + " .cart-product-quantity").innerText;
    document.querySelector("#" + "PDI83" + productId + " .cart-product-quantity").innerText = "Quantity: " + cartProducts[productId].nos + " @ " + originalString.toString().split('@')[1];
    let totalProductPrice = cartProducts[productId].nos * Number.parseInt(cartProducts[productId].productPrice.replaceAll(',', ''));
    // console.log(totalProductPrice)
    document.querySelector("#" + "PDI83" + productId + " .cart-product-price").innerText = priceFormatter.format(totalProductPrice);
}

function removeNoProductContainer() {
    document.getElementsByClassName('no-cart-product')[0].remove();
}

function addNoProductContainer() {
    let container = document.createElement('div');
    container.classList.add('no-cart-product');
    let span = document.createElement('span');
    span.innerText = "No products added! Start adding now.";
    container.appendChild(span);
    document.getElementsByClassName('cart-products')[0].appendChild(container);
}

function showCart() {
    if(document.getElementsByClassName('product-cart')[0].classList.contains('hide'))
        document.getElementsByClassName('product-cart')[0].classList.replace('hide', 'show');
    else
        document.getElementsByClassName('product-cart')[0].classList.replace('show', 'hide');
}

function hideCart() {
    document.getElementsByClassName('product-cart')[0].classList.add('hide');
}

function getCurrencyFormat(number) {
    return priceFormatter.format(number)
}