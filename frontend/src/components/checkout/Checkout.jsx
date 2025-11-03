import { Step, StepLabel, Stepper } from "@mui/material";
import { useEffect, useState } from "react";
import AddressInfo from "./AddressInfo";
import { useDispatch, useSelector } from "react-redux";
import {
  getUserAddresses,
  createStripePaymentSecret,
} from "../../store/actions";
import { Button } from "@headlessui/react";
import toast from "react-hot-toast";
import ErrorPage from "../shared/ErrorPage";
import PaymentMethod from "./PaymentMethod";
import OrderSummary from "./OrderSummary";
import StripePayment from "./StripePayment";
import PaypalPayment from "./PaypalPayment";

const CheckOut = () => {
  const [activeStep, setActiveStep] = useState(0);
  const dispatch = useDispatch();

  const steps = ["Address", "Payment Method", "Order Summary", "Payment"];

  const { isLoading, errorMessage } = useSelector((state) => state.errors);
  const { cart, totalPrice } = useSelector((state) => state.carts);
  const { address, selectedUserCheckoutAddress } = useSelector(
    (state) => state.auth
  );

  // (optional) if you add payment logic later
  const { paymentMethod } = useSelector((state) => state.payment);

  const handleBack = () => {
    setActiveStep((prevStep) => prevStep - 1);
  };

  const handleNext = () => {
    if (activeStep === 0 && !selectedUserCheckoutAddress) {
      toast.error("Please select checkout address before proceeding.");
      return;
    }

    if (activeStep === 1 && (!selectedUserCheckoutAddress || !paymentMethod)) {
      toast.error("Please select payment method before proceeding.");
      return;
    }

    if (activeStep === 2 && paymentMethod === "Stripe") {
      const sendData = {
        amount: totalPrice,
        currency: "usd",
      };
      dispatch(createStripePaymentSecret(sendData));
    }

    setActiveStep((prevStep) => prevStep + 1);
  };

  useEffect(() => {
    dispatch(getUserAddresses());
  }, [dispatch]);

  return (
    <div className="py-14 pb-28 min-h-[calc(100vh-100px)] bg-gray-50 relative">
      {/* Stepper */}
      <Stepper activeStep={activeStep} alternativeLabel>
        {steps.map((label, index) => (
          <Step key={index}>
            <StepLabel>{label}</StepLabel>
          </Step>
        ))}
      </Stepper>

      {/* Step Content */}
      <div className="mt-5">
        {activeStep === 0 && <AddressInfo address={address} />}
        {activeStep === 1 && <PaymentMethod />}
        {activeStep === 2 && (
          <OrderSummary
            totalPrice={totalPrice}
            cart={cart}
            address={selectedUserCheckoutAddress}
            paymentMethod={paymentMethod}
          />
        )}
        {activeStep === 3 && (
          <>
            {paymentMethod === "Stripe" ? <StripePayment /> : <PaypalPayment />}
          </>
        )}
        {/* You can later add conditional renders for other steps */}
      </div>

      {/* Floating Bottom Bar */}
      <div
        className="flex justify-between items-center px-6 fixed z-9999 h-20 bottom-0 left-0 w-full 
                   backdrop-blur-md bg-white/80 border-t border-gray-300 shadow-[0_-2px_10px_rgba(0,0,0,0.1)]"
      >
        <Button
          variant="outlined"
          disabled={activeStep === 0}
          onClick={handleBack}
          className={`border border-gray-400 text-gray-700 px-4 py-2 rounded-md font-medium
                      ${
                        activeStep === 0
                          ? "opacity-50 cursor-not-allowed"
                          : "hover:bg-gray-100"
                      }`}
        >
          Back
        </Button>

        {activeStep !== steps.length - 1 && (
          <button
            disabled={
              errorMessage ||
              (activeStep === 0
                ? !selectedUserCheckoutAddress
                : activeStep === 1
                ? !paymentMethod
                : false)
            }
            onClick={handleNext}
            className={`bg-blue-600 font-semibold px-6 h-10 rounded-md text-white transition-all duration-200
                       ${
                         errorMessage ||
                         (activeStep === 0 && !selectedUserCheckoutAddress) ||
                         (activeStep === 1 && !paymentMethod)
                           ? "opacity-60 cursor-not-allowed"
                           : "hover:bg-blue-700"
                       }`}
          >
            Proceed
          </button>
        )}
      </div>

      {errorMessage && <ErrorPage message={errorMessage} />}
    </div>
  );
};

export default CheckOut;
