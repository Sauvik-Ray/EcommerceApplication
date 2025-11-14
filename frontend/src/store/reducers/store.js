import { configureStore } from "@reduxjs/toolkit";
import { productReducer } from "./ProductReducer";
import { errorRedcer } from "./errorReducer";
import { cartReducer } from "./cartReducer";
import { authReducer } from "./authReducer";
import { paymentMethodReducer } from "./paymentMethodReducer";
import { adminReducer } from "./adminReducer";
import { orderReducer } from "./orderReducer";
import { sellerReducer } from "./sellerReducer";

const user = localStorage.getItem("auth")
  ? JSON.parse(localStorage.getItem("auth"))
  : null;

const cartItems = localStorage.getItem("cartItems")
  ? JSON.parse(localStorage.getItem("cartItems"))
  : [];

const clientSecret = localStorage.getItem("client-secret")
  ? JSON.parse(localStorage.getItem("client-secret"))
  : null;

const selectedUserCheckoutAddress = localStorage.getItem("cartItems")
  ? JSON.parse(localStorage.getItem("CHECKOUT_ADDRESS"))
  : [];

const initialState = {
  auth: {
    user: user,
    clientSecret: clientSecret,
    selectedUserCheckoutAddress,
  },
  carts: { cart: cartItems },
};

export const store = configureStore({
  reducer: {
    products: productReducer,
    errors: errorRedcer,
    carts: cartReducer,
    auth: authReducer,
    payment: paymentMethodReducer,
    admin: adminReducer,
    order: orderReducer,
    seller: sellerReducer,
  },
  preloadedState: initialState,
});
export default store;
