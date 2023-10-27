import React, { useState } from 'react';
import { FaEye, FaEyeSlash } from 'react-icons/fa'; // Import the eye icons from react-icons

function PasswordInput({password, setPassword}) {
  const [passwordVisible, setPasswordVisible] = useState(false);

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  return (
    <div className="password-input test-border w-[200px] flex relative">
      <input
      className='w-[200px]'
        type={passwordVisible ? 'text' : 'password'}
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        inputMode='none'
      />
      <button
        type="button"
        className="toggle-button"
        onClick={togglePasswordVisibility}
      >
        {passwordVisible ? <FaEyeSlash className='absolute top-[5px] right-[10px]' /> : <FaEye  className='absolute top-[5px] right-[10px]' />}
      </button>
    </div>
  );
}

export default PasswordInput;