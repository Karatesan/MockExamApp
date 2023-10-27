import React from "react";

const ErrorComponent = (errorMessage) => {
    return (
        <div className="w-max text-center ">
            <p className="text-red-700 text-[18px]">{errorMessage.errorMessage.errorMessage[0]}</p>
        </div>
    );

};

export default ErrorComponent;