import { useEffect, useRef, useState } from "react";
import axios from "axios";
import SearchQuestionResult from "./SearchQuestionResult";

const SearchBar = () => {
  const [value, setValue] = useState(""); 
  const [suggestions, setSuggestions] = useState([]);
  const [hideSuggestions, setHideSuggestions] = useState(true);
  const [result, setResult] = useState(null);

  const findResult = (title) => {
    setResult(suggestions.find((suggestion) => suggestion.title === title));
  };

  const fetchData = async () => {
    try {
      const { data } = await axios.get(
        `https://dummyjson.com/products/search?q=${value}`
      );
      setSuggestions(data.products);
      console.log(data.products);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    //load suggestions only when user types something
    if (value !== "") {
      const getData = setTimeout(() => fetchData(), 1000);
      return () => clearTimeout(getData);
    }
  }, [value]);

//   function debounce(func, delay) {
//     let timeoutId;
//     return function () {
//       const context = this;
//       const args = arguments;
//       clearTimeout(timeoutId);
//       timeoutId = setTimeout(() => {
//         func.apply(context, args);
//       }, delay);
//     };
//   }

  //   const handleInputChange = debounce((value) => {
  //     fetchData();
  //   }, 3000);

  return (
    <>
      <div className="container">
        <input
          onFocus={() => setHideSuggestions(false)}
          onBlur={async () => {
            setTimeout(() => {
              setHideSuggestions(true);
            }, 200);
          }}
          type="text"
          className="textbox"
          placeholder="Search data..."
          value={value}
          onChange={(e) => {
            setValue(e.target.value);
          }}
        />
        <div className={`suggestions ${hideSuggestions && "hidden"}`}>
          {suggestions &&
            suggestions.map((suggestion, index) => (
              <div
                className="suggestion"
                onClick={() => findResult(suggestion.title)}
                key={index}
              >
                {suggestion.title}
              </div>
            ))}
        </div>
      </div>
      {result && <SearchQuestionResult {...result} />}
    </>
  );
};

export default SearchBar;
