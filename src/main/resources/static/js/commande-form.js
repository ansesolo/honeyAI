/**
 * Commande Form - Dynamic Product Lines Handler
 * Vanilla JavaScript for Story 2.6
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize existing rows
    initializeExistingRows();

    // Add ligne button handler
    document.getElementById('addLigneBtn').addEventListener('click', addNewLigne);

    // Calculate totals on page load
    calculateAllTotals();
});

/**
 * Initialize existing rows (from server-side rendering)
 */
function initializeExistingRows() {
    var rows = document.querySelectorAll('#lignesBody .ligne-row');
    rows.forEach(function(row) {
        var productSelect = row.querySelector('.product-select');
        var prixInput = row.querySelector('.prix-input');

        // Store original price for override detection
        if (productSelect && productSelect.value) {
            var option = productSelect.options[productSelect.selectedIndex];
            if (option && option.dataset.price) {
                prixInput.dataset.originalPrice = option.dataset.price;
            }
        }

        calculateSubtotal(row.querySelector('.quantite-input'));
    });
}

/**
 * Add a new product line to the table
 */
function addNewLigne() {
    var tbody = document.getElementById('lignesBody');
    var rowCount = tbody.querySelectorAll('.ligne-row').length;

    // Create new row
    var newRow = document.createElement('tr');
    newRow.className = 'ligne-row';

    newRow.innerHTML = `
        <td>
            <select class="form-select product-select"
                    name="lignes[${rowCount}].productId"
                    onchange="updatePrice(this)">
                <option value="">-- Choisir un produit --</option>
                ${getProductOptionsHtml()}
            </select>
        </td>
        <td>
            <input type="number"
                   class="form-control text-center quantite-input"
                   name="lignes[${rowCount}].quantite"
                   min="1"
                   value="1"
                   placeholder="1"
                   onchange="calculateSubtotal(this)">
        </td>
        <td>
            <div class="input-group">
                <input type="number"
                       class="form-control text-end prix-input"
                       name="lignes[${rowCount}].prixUnitaire"
                       step="0.01"
                       min="0"
                       placeholder="0,00"
                       onchange="calculateSubtotal(this); checkPriceOverride(this)">
                <span class="input-group-text">EUR</span>
            </div>
            <small class="text-warning price-override-indicator" style="display: none;">
                <i class="fas fa-exclamation-triangle"></i> Prix modifie
            </small>
        </td>
        <td class="text-end">
            <span class="subtotal-display fw-bold">0,00</span> EUR
        </td>
        <td class="text-center">
            <button type="button" class="btn btn-sm btn-outline-danger remove-ligne-btn"
                    onclick="removeLigne(this)" title="Supprimer">
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
        var priceStr = product.price ? product.price.toFixed(2).replace('.', ',') : '';
        var label = product.price
            ? product.name + ' - ' + priceStr + ' EUR'
            : product.name + ' - Prix non defini';
        return `<option value="${product.id}" data-price="${product.price || ''}">${label}</option>`;
    }).join('');
}

/**
 * Remove a product line
 */
function removeLigne(button) {
    var row = button.closest('.ligne-row');
    var tbody = document.getElementById('lignesBody');

    // Only remove if more than one row exists
    if (tbody.querySelectorAll('.ligne-row').length > 1) {
        row.remove();
        reindexRows();
        calculateTotal();
    } else {
        // Clear the row instead of removing it
        var productSelect = row.querySelector('.product-select');
        var quantiteInput = row.querySelector('.quantite-input');
        var prixInput = row.querySelector('.prix-input');

        productSelect.value = '';
        quantiteInput.value = '';
        prixInput.value = '';
        prixInput.dataset.originalPrice = '';

        row.querySelector('.subtotal-display').textContent = '0,00';
        row.querySelector('.price-override-indicator').style.display = 'none';

        calculateTotal();
    }
}

/**
 * Re-index row names after removal
 */
function reindexRows() {
    var rows = document.querySelectorAll('#lignesBody .ligne-row');
    rows.forEach(function(row, index) {
        row.querySelector('.product-select').name = 'lignes[' + index + '].productId';
        row.querySelector('.quantite-input').name = 'lignes[' + index + '].quantite';
        row.querySelector('.prix-input').name = 'lignes[' + index + '].prixUnitaire';
    });
}

/**
 * Update price when product is selected
 */
function updatePrice(selectElement) {
    var row = selectElement.closest('.ligne-row');
    var prixInput = row.querySelector('.prix-input');
    var option = selectElement.options[selectElement.selectedIndex];

    if (option && option.dataset.price && option.dataset.price !== '') {
        var price = parseFloat(option.dataset.price);
        prixInput.value = price.toFixed(2);
        prixInput.dataset.originalPrice = price.toFixed(2);
    } else {
        prixInput.value = '';
        prixInput.dataset.originalPrice = '';
    }

    // Hide override indicator
    row.querySelector('.price-override-indicator').style.display = 'none';

    // Set default quantity if empty
    var quantiteInput = row.querySelector('.quantite-input');
    if (!quantiteInput.value) {
        quantiteInput.value = 1;
    }

    calculateSubtotal(prixInput);
}

/**
 * Calculate subtotal for a row
 */
function calculateSubtotal(inputElement) {
    var row = inputElement.closest('.ligne-row');
    var quantite = parseInt(row.querySelector('.quantite-input').value) || 0;
    var prix = parseFloat(row.querySelector('.prix-input').value) || 0;

    var subtotal = quantite * prix;
    row.querySelector('.subtotal-display').textContent = formatNumber(subtotal);

    calculateTotal();
}

/**
 * Calculate and display total for all lines
 */
function calculateTotal() {
    var total = 0;
    document.querySelectorAll('#lignesBody .ligne-row').forEach(function(row) {
        var quantite = parseInt(row.querySelector('.quantite-input').value) || 0;
        var prix = parseFloat(row.querySelector('.prix-input').value) || 0;
        total += quantite * prix;
    });

    document.getElementById('totalCommande').textContent = formatNumber(total);
}

/**
 * Calculate all subtotals and total
 */
function calculateAllTotals() {
    document.querySelectorAll('#lignesBody .ligne-row').forEach(function(row) {
        var quantiteInput = row.querySelector('.quantite-input');
        if (quantiteInput) {
            calculateSubtotal(quantiteInput);
        }
    });
}

/**
 * Check if price has been manually overridden
 */
function checkPriceOverride(prixInput) {
    var row = prixInput.closest('.ligne-row');
    var indicator = row.querySelector('.price-override-indicator');
    var originalPrice = prixInput.dataset.originalPrice;
    var currentPrice = prixInput.value;

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
