import {FaChartLine, FaHome, FaClipboardCheck, FaClipboardList, FaQuestionCircle, FaSearch, FaInbox,FaPray} from 'react-icons/fa'
import { RiSettings4Fill } from 'react-icons/ri';

export const afterLoginNavbarConsts = [
  {
    img: FaInbox,
    title: "NOTIFICATIONS",
    id: "inbox",
    access: ["ADMIN"]
  },
  {
    img: FaHome,
    title: "MAIN PAGE",
    id: "main",
    access: ["TRAINEE"]
  }, 
  {
    img: FaChartLine,
    title: "MY STATISTICS",
    id: "stats",
    access: ["TRAINEE","ADMIN"]
  }, 
  {
    img: FaClipboardCheck,
    title: "COMPLETED EXAMS",
    id: "completed",
    access: ["TRAINEE","ADMIN"]
  }, 
  {
    img: FaClipboardList,
    title: "TAKE EXAM!",
    id: "test",
    access: ["TRAINEE","ADMIN"]
  },
  {
    img: FaPray,
    title: "REQUEST FOR EXAM",
    id: "examRequest",
    access: ["TRAINEE"]
  },
  {
    img: FaQuestionCircle,
    title: "ADD QUESTION",
    id:"question",
    access: ["ADMIN"]
  },
  {
    img: FaSearch,
    title: "SEARCH",
    id: "search",
    access: ["ADMIN"]
  },
  {
    img: RiSettings4Fill,
    title: "SETTINGS",
    id: "settings",
    access: ["TRAINEE,ADMIN"]
  },
  

];
export const mainPageTexts = ["UNIQUE", "PLEASANT", "FUN"];
export const dummyAnswers = [
  {
    QusetionLetter: "A",
    Answer: "Make it rain by Bon Jovi",
  },
  {
    QusetionLetter: "B",
    Answer: "Hey, you free tonight?",
  },
  {
    QusetionLetter: "C",
    Answer: "Cucumbers, tomatoes, coffee, blending, span",
  },
  {
    QusetionLetter: "D",
    Answer: "Simple, restart the computer.",
  },
];

export const questionletters = ["A", "B", "C", "D", "E","F","G","H"];

export const levels = ["Beginner","Intermediate","Expert"]

export const testPageConfirmMessages = [
  {
    type:"unansweredQuestions",
    message:"You haven't answered all the questions. Are you sure you want to finish the exam?"
  },
  {
    type:"finish",
    message:"Are you sure you want to finish the exam?"
  },
  {
    type:"cancel",
    message:"Are you sure you want to cancel the exam? Progress won't be saved."
  },
  {
    type:"unsure",
    message:"You have answer(s) flagged as unsure, unsure questions don't count towards the score. Are you sure you want to finish the exam?"
  },
  {
    type:"unansweredAndUnsureQuestions",
    message:"You haven't answered all the questions and you have answer(s) flagged as unsure. Are you sure you want to finish the exam?"
  }
  
]
