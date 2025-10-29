import React, { useState } from "react";
import Products from "./components/products/Products";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./components/home/Home";
import NavBar from "./components/shared/Navbar";
import About from "./components/About";
import Contact from "./components/Contact";
import { Toaster } from "react-hot-toast";
import Cart from "./components/cart/Cart";
import LogIn from "./components/auth/LogIn";
import PrivateRoute from "./components/PrivateRoute";
import Register from "./components/auth/Register";

export default function App() {
  const [count, setCount] = useState(0);
  return (
    <React.Fragment>
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/products" element={<Products />} />
          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/cart" element={<Cart />} />

          <Route path="/" element={<PrivateRoute publicPage />}>
            <Route path="/login" element={<LogIn />} />
            <Route path="/register" element={<Register />} />
          </Route>
        </Routes>
      </Router>
      <Toaster position="bottom-center" />
    </React.Fragment>
  );
}
