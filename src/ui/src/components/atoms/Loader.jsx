import React from 'react'
import PacmanLoader from "react-spinners/ClipLoader";

const Loader = ({isLoading, size=100}) => {
  return (
    <div className=" flex justify-center items-center">
              <PacmanLoader
                color={`#FFFFFF33`}
                size={size}
                loading={isLoading}
              />
            </div>
  )
}

export default Loader