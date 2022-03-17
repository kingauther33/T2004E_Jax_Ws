import Card from '../UI/Card';
import classes from './Cart.module.css';
import CartItem from './CartItem';
import { useSelector, useDispatch } from 'react-redux';
import { clearCartData } from 'store/cart-actions';

const Cart = (props) => {
	const cartItems = useSelector((state) => state.cart.items);
	const dispatch = useDispatch();

	const clearItemHandler = () => {
		dispatch(clearCartData());
	};

	return (
		<Card className={classes.cart}>
			<div>
				<h2>Your Shopping Cart</h2>
				<button onClick={clearItemHandler}>Clear</button>
			</div>
			<ul>
				{cartItems.map((item) => (
					<CartItem
						key={item.productId}
						item={{
							id: item.productId,
							title: item.productName,
							quantity: item.quantity,
							total: item.quantity * item.unitPrice,
							price: item.unitPrice,
						}}
					/>
				))}
			</ul>
		</Card>
	);
};

export default Cart;
