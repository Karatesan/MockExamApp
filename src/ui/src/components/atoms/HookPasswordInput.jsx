import React, { useEffect, useState } from 'react';
import { useFormContext } from 'react-hook-form';
import { FaEye, FaEyeSlash } from 'react-icons/fa'; // Import the eye icons from react-icons

function HookPasswordInput({name,id,validation,placeholder,setErrors,styles}) {
    const [passwordVisible, setPasswordVisible] = useState(false);

    const {register,
        formState: {errors},
    } = useFormContext();
    useEffect(() => setErrors(errors));
  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  return (

    <div className="password-input lg:w-[350px] md:w-[250px] mt-2 flex relative">
      <input
        className={`lg:w-[350px] md:w-[250px] h-[50px] border-2 border-primary bg-transparent rounded-[20px] pl-3 text-white outline-none focus:border-white ${styles}`}
        type={passwordVisible ? 'text' : 'password'}
        id={id}
        name={name}
        {...register}
        placeholder={placeholder}
        {...register(id,validation)}
        
        inputMode='none'
        
      />
      <button
        type="button"
        className="toggle-button"
        tabIndex={-1}
        onClick={togglePasswordVisibility}
      >
        {passwordVisible ? <FaEyeSlash  className='absolute top-[17px] right-[17px] fill-white' /> : <FaEye  className='absolute top-[17px] right-[17px] fill-white' />}
      </button>
    </div>
  );
}

export default HookPasswordInput;