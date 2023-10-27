import React, { useState, useEffect } from "react";
import axios, { all } from "axios";
import styles from "../../../style";
import authHeader from "../../services/auth-header";
import Loader from "../atoms/Loader";
import AfterLoginNavbar from "../molecules/AfterLoginNavbar";
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
} from "chart.js";
import { Bar, Pie } from "react-chartjs-2";
import testPageService from "../../services/testPage.service";
import examService from "../../services/exam.service";
import ReviewExam from "./ReviewExam";
import { levels } from "../../assets/constants";
import filterService, { comparators } from "../../services/filter.service";
import annotationPlugin from "chartjs-plugin-annotation";

ChartJS.register(
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  annotationPlugin,
  LinearScale,
  BarElement,
  Title
);

const statsFilters = [
  {
    type: "level",
    value: "",
    filter: "equals",
  },
  {
    type: "category",
    value: "",
    filter: "equals",
  },
  {
    type: "passed",
    value: "",
    filter: "greaterOrEqualThan",
  },
  {
    type: "dateGreater",
    value: "",
    filter: "greaterOrEqualThan",
  },
];

const Stats = () => {
  const [exams, setExams] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(false);
  const [focusedExam, setFocusedExam] = useState(null);
  const [isVisible, setIsVisible] = useState(false);
  const [filters, setFilters] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedLevel, setSelectedLevel] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");

  //DATA    WE CALL HERE filter method------------------------------------------------------------------------
  const examThreshold = 75;
  const numberOfExams = exams.length;
  const filteredExams = !isLoading
    ? filters.length > 0
      ? filterService.filter(filters, exams)
      : exams
    : [];

  const examsScores = filteredExams.map((exam) => {
    return examService.calculateScore(exam.answersDTO);
  });

  //=--------------------------------------------------------------------
  const examsPercentages = examsScores.map((score, index) => {
    const numberOfQuestions = filteredExams[index].answersDTO.length;
    return examService.calculatePercentage(score, numberOfQuestions);
  });
  //=--------------------------------------------------------------------
  const passed = examsPercentages.reduce((count, percentage) => {
    if (percentage >= examThreshold) return count + 1;
    return count;
  }, 0);
  //=--------------------------------------------------------------------
  const dateLabels = filteredExams.map((exam) => {
    return exam.examDate
      .toLocaleString("en-GB", { timeZone: "Europe/Warsaw" })
      .substring(0, 17);
  });
  //=DATA FOR TAGS CHARTS-------------------------------------------------------------------

  const allQuestionsWithTagsAndAnswers = filteredExams.flatMap((exam) =>
    exam.answersDTO.map((question) => ({
      questionCategory: question.questionResponseDTO.category,
      questionTags: question.questionResponseDTO.tags,
      isAnsweredCorrectly: examService.isAnswerCorrect(
        question.givenAnswer,
        question.questionResponseDTO.correctAnswers
      ),
    }))
  );

  //=--------------------------------------------------------------------
  //Creates new Set out of array - with flatMap we do nested map and then flat results to single array (from array of arrays),
  //then with new Set we create Set from this array - removing duplicates in process. After that we spread set data into new array

  const transformedList = allQuestionsWithTagsAndAnswers.reduce(
    (result, item) => {
      const existingCategory = result.find(
        (category) => category.questionCategory === item.questionCategory
      );

      if (existingCategory) {
        item.questionTags.forEach((tag) => {
          const existingTag = existingCategory.tags.find(
            (existingTag) => existingTag.tagName === tag
          );
          if (existingTag) {
            existingTag.numberOfQuestionsWithTag++;
            if (item.isAnsweredCorrectly) {
              existingTag.numberOfCorrectlyAnsweredQuestions++;
            }
          } else {
            existingCategory.tags.push({
              tagName: tag,
              numberOfQuestionsWithTag: 1,
              numberOfCorrectlyAnsweredQuestions: item.isAnsweredCorrectly
                ? 1
                : 0,
            });
          }
        });
      } else {
        result.push({
          questionCategory: item.questionCategory,
          tags: item.questionTags.map((tag) => ({
            tagName: tag,
            numberOfQuestionsWithTag: 1,
            numberOfCorrectlyAnsweredQuestions: item.isAnsweredCorrectly
              ? 1
              : 0,
          })),
        });
      }

      return result;
    },
    []
  );

  // const uniqueList = allTags.filter((obj, index, self) => {
  //   return (
  //     index ===
  //     self.findIndex(
  //       (t) => t.tag === obj.tag && t.questionCategory === obj.questionCategory
  //     )
  //   );
  // });
  //dostaje teraz array arrayof arrays for each question category

  // const allTagsWithMoreData = uniqueList.map((tag) => {
  //   let countAll = 0;
  //   let countCorrectAnswers = 0;
  //   allQuestionsWithTagsAndAnswers.forEach((question) => {
  //     if (question.questionTags.includes(tag.tag)) {
  //       countAll++;
  //       if (question.answeredCorrectly) countCorrectAnswers++;
  //     }
  //   });
  //   return {
  //     name: tag,
  //     numberOfQuestionsWithTag: countAll,
  //     numberOfQuestionsCorrectlyAnswered: countCorrectAnswers,
  //   };
  // });

  //=--------------------------------------------------------------------

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/exam/allExamsFromUser", {
        headers: authHeader(),
      })
      .then((response) => {
        if (response.data) {
          const formattedExams = response.data.map((exam) => {
            return {
              id: exam.id,
              examDate: new Date(exam.examDate),
              category:
                exam.answersDTO[0].questionResponseDTO.category.toLowerCase(),
              level: exam.answersDTO[0].questionResponseDTO.level,
              answersDTO: exam.answersDTO,
            };
          });

          setExams(formattedExams);
          setIsLoading(false);
        }
      })
      .catch((error) => {
        setError(error.response.data);
        setIsLoading(false);
      });
  }, []);

  useEffect(() => {
    if (focusedExam !== null) {
      setIsVisible(true);
    } else {
      setIsVisible(false);
    }
  }, [focusedExam]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/questions/allCategories", {
        headers: authHeader(),
      })
      .then((response) => {
        setCategories(response.data); // Update the state with fetched data
      })
      .catch((error) => {
        console.error(error.response.data);
      });
  }, []);

  // //PIE -----------------------------------------------------------------------------------

  const data2 = {
    labels: ["Failed exams", "Passed Exams"],
    datasets: [
      {
        label: "# of Exams",
        data: [examsScores.length - passed, passed],
        backgroundColor: ["rgba(255, 99, 132, 0.2)", "rgba(54, 162, 235, 0.2)"],
      },
    ],
  };

  //Data For Bar percentage score chart-----------------------------------------------------------------
  const dataForPassedExamsBar = {
    labels: dateLabels,
    datasets: [
      {
        label: "Percentage Scores",
        borderColor: "rgba(255, 99, 132, 1)",
        borderWidth: 0,
        backgroundColor: (context) => {
          const value = examsPercentages[context.dataIndex];
          return value >= examThreshold
            ? "rgba(100, 200, 132, 0.4)"
            : "rgba(255, 99, 132, 0.4)";
        },
        barThickness: "flex",
        hoverBorderWidth: 1,
        data: examsPercentages,
      },
    ],
  };

  //-------------------------------------------------------------

  const percentagesForTagBar = transformedList.flatMap((category) =>
    category.tags.map((tag) =>
      tag.numberOfQuestionsWithTag === 0
        ? 0
        : (tag.numberOfCorrectlyAnsweredQuestions /
            tag.numberOfQuestionsWithTag) *
          100
    )
  );

  const dataForTags = {
    labels: transformedList.flatMap((category) =>
      category.tags.map((tag) => tag.tagName)
    ),
    datasets: [
      {
        label: "Percentage of Correct Answers",
        data: percentagesForTagBar,
        backgroundColor: (context) => {
          const value = percentagesForTagBar[context.dataIndex];
          return value >= examThreshold
            ? "rgba(100, 200, 132, 0.4)"
            : "rgba(255, 99, 132, 0.4)";
        },
      },
    ],
  };

  const optionsForTags = {
    scales: {
      y: {
        beginAtZero: true,
        min: 0,
        max: 100,
      },
    },
    plugins: {
      annotation: {
        annotations: {
          line1: {
            type: "line",
            yMin: examThreshold,
            yMax: examThreshold,
            borderColor: "green",
            borderWidth: 1,
            label: {
              content: "Exam Threshold (75%)",
              backgroundColor: "rgba(100, 200, 132, 0.4)",
              display: true,
              position: "end",
              font: {
                size: 12,
                weight: "bold",
              },
            },
          },
        },
      },
    },
  };

  //--------------------------------------------------------

  const options = {
    events: ["click", "mousemove", "mouseout"],
    onClick: function (event, elements) {
      if (elements[0]) {
        const index = elements[0].index;
        setFocusedExam(index);
      }
    },
    scales: {
      y: {
        min: 0,
        max: 100,
      },
    },
    animation: true,
    plugins: {
      annotation: {
        annotations: {
          line1: {
            type: "line",
            yMin: examThreshold,
            yMax: examThreshold,
            borderColor: "green",
            borderWidth: 1,
            label: {
              content: "Exam Threshold (75%)",
              backgroundColor: "rgba(100, 200, 132, 0.4)",
              display: true,
              position: "end",
              font: {
                size: 12,
                weight: "bold",
              },
            },
          },
        },
      },
      legend: {
        display: true,
      },
      tooltip: {
        enabled: true,
      },
    },
  };

  //--------------------------------------------------------------------------------------------

  return (
    <>
      <div
        className={`${styles.paddingX} ${styles.flexCenter} justify-center relative `}
      >
        <div
          className={`${styles.boxWidth} flex flex-col items-center justify-between p-10`}
        >
          <h1 className="text-white text-[50px] mb-10">Statistics</h1>
          {focusedExam !== null && (
            <div
              className={`${styles.boxWidth} h-[800px] absolute left-0 top-0 ${
                isVisible ? "review-visible" : "opacity-0"
              }`}
            >
              <ReviewExam
                exam={exams[focusedExam]}
                closeWindow={setFocusedExam}
              />
            </div>
          )}
          {isLoading && <Loader isLoading={isLoading} />}
          <h2 className="text-white text-[30px] mb-2">Filters:</h2>
          <div
            id="filters-container"
            className="flex w-[450px] justify-between mb-6"
          >
            <div id="category-stats-container" className="text-center">
              <label
                htmlFor="category-stats-Dropdown"
                className="text-[20px] text-white font-poppins mb-2 "
              >
                Category
              </label>
              <div className="testBox_select mt-2">
                {/*Dodaje name do pola i biore je e.target.name*/}
                <select
                  className="w-[200px] h-[40px] rounded-[20px] text-white border-[1px] border-primary transition-all bg-transparent pl-3 outline-none focus:border-[#7d35ea] appearance-none"
                  id="category-stats-Dropdown"
                  name="category"
                  value={selectedCategory}
                  data-filter="equals"
                  onChange={(e) => {
                    filterService.onInputChange(
                      e,
                      filters,
                      statsFilters,
                      setSelectedCategory,
                      setFilters
                    );
                  }}
                >
                  <option value="">All</option>
                  {categories.map((category, index) => {
                    return (
                      <option key={index} value={category.toLowerCase()}>
                        {category}
                      </option>
                    );
                  })}
                </select>
              </div>
            </div>
            <div id="level-stats-container" className="text-center">
              <label
                htmlFor="level-stats-Dropdown"
                className="text-[20px] text-white font-poppins mb-2 "
              >
                Difficulty
              </label>
              <div className="testBox_select mt-2">
                {/*Dodaje name do pola i biore je e.target.name*/}
                <select
                  className="w-[200px] h-[40px] rounded-[20px] text-white border-[1px] border-primary transition-all bg-transparent pl-3 outline-none focus:border-[#7d35ea] appearance-none"
                  id="level-stats-Dropdown"
                  name="level"
                  value={selectedLevel}
                  onChange={(e) => {
                    filterService.onInputChange(
                      e,
                      filters,
                      statsFilters,
                      setSelectedLevel,
                      setFilters
                    );
                  }}
                >
                  <option value="">All</option>
                  {levels.map((level, index) => {
                    return (
                      <option key={index} value={level.toLowerCase()}>
                        {level}
                      </option>
                    );
                  })}
                </select>
              </div>
            </div>
          </div>
          <div className="xl:w-[800px] lg:w-[700px] w-[680px] xl:h-[400px] h-[400px] mb-20">
            <h1 className="text-[26px] text-white text-center mb-2">
              Your most recent exams
            </h1>
            <Bar data={dataForPassedExamsBar} options={options} />
          </div>
          <div className="xl:w-[800px] lg:w-[700px] w-[680px] xl:h-[400px] h-[400px] mb-20">
            <h1 className="text-[26px] text-white text-center mb-2">
              Pecentage of correct answers for each category of question
            </h1>
            <Bar data={dataForTags} options={optionsForTags} />
          </div>
          <div className="w-[400px] h-[400px]">
            <h1 className="text-[26px] text-white text-center mb-2">
              Pass rate of your exams
            </h1>
            <Pie data={data2} />
          </div>
        </div>
      </div>
    </>
  );
};

export default Stats;
