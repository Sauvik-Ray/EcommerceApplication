import { Skeleton } from "@mui/material";
import {
  PaymentElement,
  useElements,
  useStripe,
} from "@stripe/react-stripe-js";
import { useState } from "react";

const PaymentForm = ({ clientSecret, totalPrice }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const PaymentElementOptions = {
    layout: "tabs",
  };
  const isLoading = !clientSecret || !stripe || !elements;
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!stripe || !elements) {
      return;
    }

    const { error: submitError } = await elements.submit();

    const { error } = await stripe.confirmPayment({
      elements,
      clientSecret,
      confirmParams: {
        return_url: `${import.meta.env.VITE_FRONTEND_URL}/order-confirm`,
      },
    });

    if (error) {
      setErrorMessage(error.message);
      return false;
    }
  };

  return (
    <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-4">
      <h2 className="text-xl font-semibold mb-4">Payment Information</h2>
      {loading ? (
        <Skeleton />
      ) : (
        <>
          {clientSecret && <PaymentElement options={PaymentElementOptions} />}
          {errorMessage && (
            <div className="text-red-500 mt-2">{errorMessage}</div>
          )}
          <button
            className="text-white w-full px-5 py-2.5 bg-black mt-2 rounded-md font-bold disabled:opacity-50 disabled:animate-pulse"
            disabled={!stripe || isLoading}
          >
            {!isLoading
              ? `Pay $${Number(totalPrice).toFixed(2)}`
              : "Processing"}
          </button>
        </>
      )}
    </form>
  );
};

export default PaymentForm;
