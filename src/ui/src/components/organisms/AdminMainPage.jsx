import React from "react";
import styles from "../../../style";
import SearchBar from "../molecules/SearchBar";

const AdminMainPage = () => {
  return (
    <div className={`${styles.paddingX} ${styles.flexCenter} justify-center`}>
      <div className={`${styles.boxWidth}`}>
        <SearchBar />


      </div>
    </div>
  );
};

export default AdminMainPage;
