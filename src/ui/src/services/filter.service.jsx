
//if no filter return ""
//providedUserFilters - custom filters made by user,
//saved filters- filters saved to useState inside component that uses filtering
/*
user filters formula:

filter ={
      {
      type: "nameOfInputField",
      value: "leaveEmpty, it ll be fileld with filter value",
      filter: "comparator type",
    },
}

****List of comparators not yet implemented****
*/

export const comparators = [
  {
    type: "equals",
    comparator: (value, compareTo) => {
      return value === compareTo;
    },
  },
  {
    type: "greaterOrEqualThan",
    comparator: (value, compareTo) => {
      return value >= compareTo;
    },
  },
  {
    type: "lessOrEqualThan",
    comparator: (value, compareTo) => {
      return value <= compareTo;
    },
  },
];

class filterService {
  getComparator(type) {
    return comparators.find((element) => element.type === type);
  }

  getFilter(type, providedUserFilters) {
    return providedUserFilters.find((element) => element.type === type);
  }

  removeFilterIfPresent(filters, type) {
    let filtered = [...filters];
    if (filters.length > 0) {
      filtered = filtered.filter((filter) => {
        return filter.type !== type;
      });
    }
    return filtered;
  }

  updateFiltersArray(savedFilters, providedUserFilters, type, value) {
    if (type) {
      const filteredFilters = this.removeFilterIfPresent(savedFilters, type);
      if (value === "") {
        return filteredFilters;
      } else {
        const filter = this.getFilter(type, providedUserFilters);
        filter.value = value;
        const combinedFilters = [...filteredFilters, filter];
        return combinedFilters;
      }
    }
    return savedFilters;
  }

  filter(filters, array) {
    let newArray = [...array];
    for (const f of filters) {
      newArray = newArray.filter((element) => {
        return this.getComparator(f.filter).comparator(
          element[f.type],
          f.value
        );
      });
    }
    return newArray;
  }

  onInputChange(e, savedFilters, providedUserFilters, setState, setFilters) {
    setState(e.target.value);
    setFilters(
      this.updateFiltersArray(
        savedFilters,
        providedUserFilters,
        e.target.name,
        e.target.value
      )
    );
  }
}

export default new filterService();
