import { useState } from "react";
import Products from "./components/products/Products";
export default function App() {
  const [count, setCount] = useState(0);
  return <Products />;
}
