import React from "react";

// Names of the props we expect to receive
const keys = ["title", "description", "price", "rating", "category"];

const SearchQuestionResult = (props) => (
  <div>
    {keys.map((key, index) => (
      <span key={index}>
        {key.charAt(0) + key.slice(1)}: {props[key]}
      </span>
    ))}
  </div>
);

export default SearchQuestionResult;
