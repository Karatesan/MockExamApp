import React from "react";

const HookErrorComponent = (errorMessage) => {
    return (
        <div  className="w[500px] text-center ">
            <p id={errorMessage.id+"_error"} className="text-red-700 text-[18px]">{errorMessage.errorMessage}</p>
        </div>
    );

};

export default HookErrorComponent;