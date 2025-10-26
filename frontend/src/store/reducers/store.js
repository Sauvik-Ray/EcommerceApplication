import { configureStore } from "@reduxjs/toolkit";
import { productReducer } from "./ProductReducer";
import { errorRedcer } from "./errorReducer";

export const store = configureStore({
  reducer: {
    products: productReducer,
    errors: errorRedcer,
  },
  preloadedState: {},
});
export default store;
