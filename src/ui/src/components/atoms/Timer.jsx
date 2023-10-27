import React, { useState, useEffect } from "react";

const Timer = ({ initialTimeInSeconds, className, onTimerEnd }) => {
  const [timeInSeconds, setTimeInSeconds] = useState(initialTimeInSeconds);

  useEffect(() => {
    // Update the timer every second
    const interval = setInterval(() => {
      if (timeInSeconds > 0) {
        setTimeInSeconds(timeInSeconds - 1);
      }
     else {
      clearInterval(interval);
      if (typeof onTimerEnd === "function") {
        onTimerEnd();
      }
    }
    }, 1000);

    // Cleanup the interval when the component unmounts
    return () => clearInterval(interval);
  }, [timeInSeconds]);

  // Convert seconds to minutes and seconds
  const minutes = Math.floor(timeInSeconds / 60);
  const seconds = timeInSeconds % 60;

  return (
    <div className={`text-white text-[17px] ${className}`}>
      Timer: {minutes < 10 ? "0" : ""}
      {minutes}:{seconds < 10 ? "0" : ""}
      {seconds}
    </div>
  );
};

export default Timer;
