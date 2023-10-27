/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: "#7518ff",
        orangeDark: "#f45106",
        orangeLight: "#fa8907",
        cream: "#f7f5f2",
        gray: "#3F3F46"
      },
      fontFamily: {
        poppins: ["Poppins", "sans-serif"],
      },
      backgroundColors:{
        lightHover:"#ffffff7c",
        darkHover:"#ffffff54",
      }
    },
    screens: {
      xxs: "320px",
      xs: "425px",
      ss: "620px",
      sm: "768px",
      md: "1024px",
      lg: "1440px",
      xl: "1700px",
    },
  },
  plugins: [],
}

