import { uiActions } from './ui-slice';
import { cartActions } from './cart-slice';
import { API } from 'API';
import axios from 'axios';

export const fetchCartData = () => {
	return async (dispatch) => {
		const fetchData = async () => {
			const response = await axios.get(API.getShoppingCart.url, API.config);
			// const response = await axios.get(API.getShoppingCart.url);

			console.log(response);

			if (response.status !== 200) {
				throw new Error('Could not fetch cart data!');
			}

			const data = await response.data;

			if (!!!data) {
				const responseAddToCart = await axios.get(
					API.createShoppingCart.url,
					API.config
				);

				const dataNewCart = await responseAddToCart.data;

				console.log(dataNewCart);

				return dataNewCart;
			}

			console.log(data);

			return data;
		};

		try {
			const cartData = await fetchData();
			dispatch(
				cartActions.replaceCart({
					items: cartData.cartItems || [],
					totalQuantity: cartData.cartItems.length || 0,
				})
			);
			console.log(cartData);
		} catch (error) {
			console.log(error);
			dispatch(
				uiActions.showNotification({
					status: 'error',
					title: 'Error!',
					message: 'Sending cart data failed!',
				})
			);
		}
	};
};

export const addToCartData = (productId, quantity) => {
	return async (dispatch) => {
		dispatch(
			uiActions.showNotification({
				status: 'pending',
				title: 'Sending.',
				message: 'Sending cart data!',
			})
		);

		const addRequest = async () => {
			const response = await fetch(
				`${API.addToShoppingCart.url}?productId=${productId}&quantity=${quantity}`
			);

			if (!response.ok) {
				throw new Error('Add to cart data failed.');
			}

			const data = await response.json();

			return data;
		};

		try {
			const cartData = await addRequest();

			console.log(cartData);

			dispatch(
				cartActions.replaceCart({
					items: cartData.cartItems || [],
					totalQuantity: cartData.cartItems.length || 0,
				})
			);
		} catch (error) {
			dispatch(
				uiActions.showNotification({
					status: 'error',
					title: 'Error!',
					message: 'Adding cart data failed!',
				})
			);
		}
	};
};

export const sendCartData = (cart) => {
	return async (dispatch) => {
		dispatch(
			uiActions.showNotification({
				status: 'pending',
				title: 'Sending.',
				message: 'Sending cart data!',
			})
		);

		const sendRequest = async () => {
			const response = await fetch(
				'https://react-http-40616-default-rtdb.firebaseio.com/cart.json',
				{
					method: 'PUT',
					body: JSON.stringify({
						items: cart.items,
						totalQuantity: cart.totalQuantity,
					}),
				}
			);

			if (!response.ok) {
				throw new Error('Sending cart data failed.');
			}
		};

		try {
			await sendRequest();

			dispatch(
				uiActions.showNotification({
					status: 'success',
					title: 'Success!',
					message: 'Sent cart data successfully!',
				})
			);
		} catch (error) {
			dispatch(
				uiActions.showNotification({
					status: 'error',
					title: 'Error!',
					message: 'Sending cart data failed!',
				})
			);
		}
	};
};
