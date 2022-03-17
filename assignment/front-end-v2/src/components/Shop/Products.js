import ProductItem from './ProductItem';
import classes from './Products.module.css';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { API } from 'API';

const Products = (props) => {
	const [products, setProducts] = useState([]);

	useEffect(() => {
		const fetchProducts = async () => {
			await axios
				.get(API.findAllProducts.url, API.config)
				.then((res) => {
					setProducts(res.data);
				})
				.catch((err) => console.error(err));
		};

		fetchProducts();
	}, []);

	return (
		<section className={classes.products}>
			<h2>Buy your favorite products</h2>
			<ul>
				{products?.map((product) => (
					<ProductItem
						key={product.id}
						id={product.id}
						name={product.name}
						price={product.price}
						status={product.status}
					/>
				))}
			</ul>
		</section>
	);
};

export default Products;
