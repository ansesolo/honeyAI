/**
 * Order Form - Dynamic Product Lines Handler
 * Vanilla JavaScript for Story 2.6
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize existing rows
    initializeExistingRows();

    // Add line button handler
    document.getElementById('addLineBtn').addEventListener('click', addNewLine);

    // Calculate totals on page load
    calculateAllTotals();
});

/**
 * Initialize existing rows (from server-side rendering)
 */
function initializeExistingRows() {
    var rows = document.querySelectorAll('#linesBody .line-row');
    rows.forEach(function(row) {
        var productSelect = row.querySelector('.product-select');
        var priceInput = row.querySelector('.price-input');

        // Store original price for override detection
        if (productSelect && productSelect.value) {
            var option = productSelect.options[productSelect.selectedIndex];
            if (option && option.dataset.price) {
                priceInput.dataset.originalPrice = option.dataset.price;
            }
        }

        calculateSubtotal(row.querySelector('.quantity-input'));
    });
}

/**
 * Add a new product line to the table
 */
function addNewLine() {
    var tbody = document.getElementById('linesBody');
    var rowCount = tbody.querySelectorAll('.line-row').length;

    // Create new row
    var newRow = document.createElement('tr');
    newRow.className = 'line-row';

    newRow.innerHTML = `
        <td>
            <select class="form-select product-select"
                    name="lines[${rowCount}].productId"
                    onchange="updatePrice(this)">
                <option value="">-- Choisir un produit --</option>
                ${getProductOptionsHtml()}
            </select>
        </td>
        <td>
            <input type="number"
                   class="form-control text-center quantity-input"
                   name="lines[${rowCount}].quantity"
                   min="1"
                   value="1"
                   placeholder="1"
                   onchange="calculateSubtotal(this)">
        </td>
        <td>
            <div class="input-group">
                <input type="number"
                       class="form-control text-end price-input"
                       name="lines[${rowCount}].unitPrice"
                       step="0.01"
                       min="0"
                       placeholder="0,00"
                       onchange="calculateSubtotal(this); checkPriceOverride(this)">
                <span class="input-group-text">EUR</span>
            </div>
            <small class="text-warning price-override-indicator" style="display: none;">
                <i class="fas fa-exclamation-triangle"></i> Prix modifié
            </small>
        </td>
        <td class="text-end">
            <span class="subtotal-display fw-bold">0,00</span> EUR
        </td>
        <td class="text-center">
            <button type="button" class="btn btn-sm btn-outline-danger remove-line-btn"
                    onclick="removeLine(this)" title="Supprimer">
                <i class="fas fa-trash"></i>
            </button>
        </td>
    `;

    tbody.appendChild(newRow);

    // Focus on the new product select
    newRow.querySelector('.product-select').focus();
}

/**
 * Generate options HTML from products data
 */
function getProductOptionsHtml() {
    if (!productsData || productsData.length === 0) {
        return '';
    }

    return productsData.map(function(product) {
        return `<option value="${product.id}" data-price="${product.price || ''}">${product.displayLabel}</option>`;
    }).join('');
}

/**
 * Remove a product line
 */
function removeLine(button) {
    var row = button.closest('.line-row');
    var tbody = document.getElementById('linesBody');

    // Only remove if more than one row exists
    if (tbody.querySelectorAll('.line-row').length > 1) {
        row.remove();
        reindexRows();
        calculateTotal();
    } else {
        // Clear the row instead of removing it
        var productSelect = row.querySelector('.product-select');
        var quantityInput = row.querySelector('.quantity-input');
        var priceInput = row.querySelector('.price-input');

        productSelect.value = '';
        quantityInput.value = '';
        priceInput.value = '';
        priceInput.dataset.originalPrice = '';

        row.querySelector('.subtotal-display').textContent = '0,00';
        row.querySelector('.price-override-indicator').style.display = 'none';

        calculateTotal();
    }
}

/**
 * Re-index row names after removal
 */
function reindexRows() {
    var rows = document.querySelectorAll('#linesBody .line-row');
    rows.forEach(function(row, index) {
        row.querySelector('.product-select').name = 'lines[' + index + '].productId';
        row.querySelector('.quantity-input').name = 'lines[' + index + '].quantity';
        row.querySelector('.price-input').name = 'lines[' + index + '].unitPrice';
    });
}

/**
 * Update price when product is selected
 */
function updatePrice(selectElement) {
    var row = selectElement.closest('.line-row');
    var priceInput = row.querySelector('.price-input');
    var option = selectElement.options[selectElement.selectedIndex];

    if (option && option.dataset.price && option.dataset.price !== '') {
        var price = parseFloat(option.dataset.price);
        priceInput.value = price.toFixed(2);
        priceInput.dataset.originalPrice = price.toFixed(2);
    } else {
        priceInput.value = '';
        priceInput.dataset.originalPrice = '';
    }

    // Hide override indicator
    row.querySelector('.price-override-indicator').style.display = 'none';

    // Set default quantity if empty
    var quantityInput = row.querySelector('.quantity-input');
    if (!quantityInput.value) {
        quantityInput.value = 1;
    }

    calculateSubtotal(priceInput);
}

/**
 * Calculate subtotal for a row
 */
function calculateSubtotal(inputElement) {
    var row = inputElement.closest('.line-row');
    var quantity = parseInt(row.querySelector('.quantity-input').value) || 0;
    var price = parseFloat(row.querySelector('.price-input').value) || 0;

    var subtotal = quantity * price;
    row.querySelector('.subtotal-display').textContent = formatNumber(subtotal);

    calculateTotal();
}

/**
 * Calculate and display total for all lines
 */
function calculateTotal() {
    var total = 0;
    document.querySelectorAll('#linesBody .line-row').forEach(function(row) {
        var quantity = parseInt(row.querySelector('.quantity-input').value) || 0;
        var price = parseFloat(row.querySelector('.price-input').value) || 0;
        total += quantity * price;
    });

    document.getElementById('totalOrder').textContent = formatNumber(total);
}

/**
 * Calculate all subtotals and total
 */
function calculateAllTotals() {
    document.querySelectorAll('#linesBody .line-row').forEach(function(row) {
        var quantityInput = row.querySelector('.quantity-input');
        if (quantityInput) {
            calculateSubtotal(quantityInput);
        }
    });
}

/**
 * Check if price has been manually overridden
 */
function checkPriceOverride(priceInput) {
    var row = priceInput.closest('.line-row');
    var indicator = row.querySelector('.price-override-indicator');
    var originalPrice = priceInput.dataset.originalPrice;
    var currentPrice = priceInput.value;

    if (originalPrice && currentPrice &&
        parseFloat(originalPrice).toFixed(2) !== parseFloat(currentPrice).toFixed(2)) {
        indicator.style.display = 'block';
    } else {
        indicator.style.display = 'none';
    }
}

/**
 * Format number as French currency (comma decimal separator)
 */
function formatNumber(value) {
    return value.toFixed(2).replace('.', ',');
}
