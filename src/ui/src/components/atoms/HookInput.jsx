import React, { useEffect } from "react";
import { useFormContext } from "react-hook-form";

const HookInput = ({label, placeholder, type, id, styles,validation,setErrors }) => {
    const {register,
        formState: {errors},
    } = useFormContext();
    useEffect(() => setErrors(errors));
  return (
    <div>

<label id={id+"_label"}className="absolute lg:text-[26px] md:text-[20px] lg:font-bold mt-1 ml-2 lg:-translate-y-10 md:-translate-y-7 text-white " >{label}</label>


      <input
        placeholder={placeholder}
        type={type}
        id={id}
        name={id}
        {...register(id,validation)}
        className={` lg:w-[350px] md:w-[250px] h-[50px] mt-2 border-2 border-primary bg-transparent rounded-[20px] pl-3 text-white outline-none focus:border-white ${styles}`}
      />
    </div>
  );
};

export default HookInput;