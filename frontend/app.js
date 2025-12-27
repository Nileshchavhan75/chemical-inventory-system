const API_BASE_URL = 'http://localhost:8080/api';

// Show message
function showMessage(text, type = 'info') {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = text;
    messageDiv.className = `alert alert-${type}`;
    messageDiv.style.display = 'block';
    setTimeout(() => messageDiv.style.display = 'none', 3000);
}

// Load all products
async function loadProducts() {
    try {
        const response = await fetch(`${API_BASE_URL}/products`);
        const products = await response.json();
        displayProducts(products);
    } catch (error) {
        showMessage('Error loading products', 'danger');
    }
}

// Display products
function displayProducts(products) {
    const container = document.getElementById('productsList');
    container.innerHTML = '';
    
    if (products.length === 0) {
        container.innerHTML = '<p class="text-muted">No products found</p>';
        return;
    }
    
    const list = document.createElement('ul');
    list.className = 'list-group';
    
    products.forEach(product => {
        const item = document.createElement('li');
        item.className = 'list-group-item d-flex justify-content-between';
        item.innerHTML = `
            <div>
                <strong>${product.productName}</strong> (${product.casNumber}) - ${product.unit}
            </div>
            <button class="btn btn-sm btn-danger" onclick="deleteProduct(${product.id})">Delete</button>
        `;
        list.appendChild(item);
    });
    
    container.appendChild(list);
}

// Load inventory
async function loadInventory() {
    try {
        const response = await fetch(`${API_BASE_URL}/inventory`);
        const inventory = await response.json();
        displayInventory(inventory);
    } catch (error) {
        showMessage('Error loading inventory', 'danger');
    }
}

// Display inventory
function displayInventory(inventory) {
    const tbody = document.getElementById('inventoryBody');
    tbody.innerHTML = '';
    
    if (inventory.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">No inventory items</td></tr>';
        return;
    }
    
    inventory.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.product.productName}</td>
            <td>${item.product.casNumber}</td>
            <td>${item.currentStock}</td>
            <td>${item.product.unit}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="showStockUpdate(${item.product.id}, '${item.product.productName}')">
                    Update Stock
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Add new product
document.getElementById('addProductForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const product = {
        productName: document.getElementById('productName').value,
        casNumber: document.getElementById('casNumber').value,
        unit: document.getElementById('unit').value
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/products`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(product)
        });
        
        if (response.ok) {
            showMessage('Product added successfully', 'success');
            document.getElementById('addProductForm').reset();
            loadProducts();
            loadInventory();
        } else {
            const error = await response.text();
            showMessage(error, 'danger');
        }
    } catch (error) {
        showMessage('Error adding product', 'danger');
    }
});

// Delete product
async function deleteProduct(productId) {
    if (!confirm('Are you sure you want to delete this product?')) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/products/${productId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showMessage('Product deleted successfully', 'success');
            loadProducts();
            loadInventory();
        } else {
            const error = await response.text();
            showMessage(error, 'danger');
        }
    } catch (error) {
        showMessage('Error deleting product', 'danger');
    }
}

// Show stock update modal
function showStockUpdate(productId, productName) {
    const quantity = prompt(`Enter quantity to update stock for ${productName}:\n(Positive for IN, Negative for OUT)`);
    if (!quantity || isNaN(quantity)) return;
    
    const movementType = parseFloat(quantity) > 0 ? 'IN' : 'OUT';
    const absQuantity = Math.abs(parseFloat(quantity));
    
    updateStock(productId, movementType, absQuantity);
}

// Update stock
async function updateStock(productId, movementType, quantity) {
    try {
        const response = await fetch(
            `${API_BASE_URL}/inventory/${productId}/stock?movementType=${movementType}&quantity=${quantity}`, 
            { method: 'POST' }
        );
        
        if (response.ok) {
            showMessage(`Stock ${movementType.toLowerCase()} updated successfully`, 'success');
            loadInventory();
        } else {
            const error = await response.text();
            showMessage(error, 'danger');
        }
    } catch (error) {
        showMessage('Error updating stock', 'danger');
    }
}

// Initialize
loadProducts();
loadInventory();