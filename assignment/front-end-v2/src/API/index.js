const BASE_SHOPPING_CART_URL = 'http://localhost:8080/api/carts';
const BASE_PRODUCT_URL = 'http://localhost:8080/api/products';

export const API = {
	findAllProducts: {
		url: BASE_PRODUCT_URL,
	},
	getShoppingCart: {
		url: BASE_SHOPPING_CART_URL,
	},
	createShoppingCart: {
		url: BASE_SHOPPING_CART_URL + '/create',
	},
	addToShoppingCart: {
		url: BASE_SHOPPING_CART_URL + '/add',
	},
	deductShoppingCart: {
		url: BASE_SHOPPING_CART_URL + '/deduct',
	},
	clearShoppingCart: {
		url: BASE_SHOPPING_CART_URL + '/clear',
	},
	config: {
		headers: {
			'Access-Control-Allow-Origin': '*',
			'Content-Type': 'application/json',
			Authorization: '1',
		},
	},
};
