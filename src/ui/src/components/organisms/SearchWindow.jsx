import axios from "axios";
import { useEffect, useState } from "react";
import authHeader from "../../services/auth-header";
import { GiCancel } from "react-icons/gi";
import { FaSearch } from "react-icons/fa";
import questionService from "../../services/question.service";
import { FormProvider, useForm, useFormContext } from "react-hook-form";
import { Checkbox, FormControl, FormControlLabel, FormLabel, Radio, RadioGroup } from "@mui/material";
import ReactPaginate from "react-paginate";
import '../../assets/pagination.css';
import ErrorComponent from "../atoms/ErrorComponent";
import { useNavigate } from "react-router";
import Button from "../atoms/Button";
import ConfirmWindow from "../atoms/ConfirmWindow";
import { useDispatch, useSelector } from "react-redux";
import { setConfirmationWindow, setQuestionToDelete } from "../../utils/features/deleteQuestionSlice";
import ConfirmDeleteQuestion from "../atoms/ConfirmDeleteQuestion";

const initialData = {
    category: "",
    searchFields: ["question_content"],
    searchText: "",
    level: "",
    tag: "",
    page: 1

}
const itemsPerPage = 5;



const SearchWindow = ({ setSearchWindow }) => {
    const [currentPage, setCurrentPage] = useState("1");
    const methods = useForm({
        defaultValues: {
            searchText: "",
            category: "",
            level: "",
            searchFields: ["question_content"],
            tag: "",
            page: 1
        }
    });

    const confirmationWindow = useSelector((state) => state.deleteQuestion.confirmationWindow);
    const id_question_to_delete = useSelector((state) => state.deleteQuestion.question_id)


    const [categories, setCategories] = useState([]);
    const [resultQuestions, setResultQuestions] = useState([]);
    const [error, setError] = useState(null);
    const [searchResults, setSearchResults] = useState(false);
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const searchRequestSubmit = methods.handleSubmit(async (data) => {
        questionService.searchQuestions(data).then((response) => {
            console.log(response.data)
            setResultQuestions(response.data);
            setSearchResults(true);
            setError(null)
        }).catch((error) => {
            setSearchResults(false);
            setResultQuestions([]);
            setError(error.response);
        })
    })
    const handlePageClick = (e) => {
        setCurrentPage(e.selected + 1);
        console.log(e.selected + 1);
        const offset = (e.selected + 1) * itemsPerPage;
        methods.setValue("page", e.selected + 1);
        searchRequestSubmit()
    }

    const deleteQuestion = (id) => {
        questionService.deleteQuestion(id).then((data) => {
            dispatch(setQuestionToDelete(""));
            dispatch(setConfirmationWindow(false))
            searchRequestSubmit();


        }
        )
    }

    const updateQuestion = (id) => {
        try { navigate("/updateQuestion/" + id) }
        catch (error) {
            console.log(error);
        }
    }
    useEffect(() => {
        console.log(confirmationWindow)
    ,[confirmationWindow]}
    )
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

    return (
        <div className="bg-[#FFFFFF22] w-full p-4 h-[78.5vh] rounded-[20px] flex flex-col relative">
            <button
            
                className="absolute right-[20px] top-[20px] text-primary text-[30px] z-20"
                title={"Cancel"}
                onClick={() => setSearchWindow(false)}
            >
                <GiCancel />
            </button>

            <FormProvider {...methods}>

                <form
                    onSubmit={e => e.preventDefault()}
                    noValidate
                    autoComplete="off"
                    className="flex"
                >
                    <div id="searchForm" className="flex mt-12">
                        <select
                            className="bg-[#FFFFFF22] w-[200px] h-[50px] rounded-[20px] text-white border-[1px] border-primary transition-all bg-transparent pl-3 outline-none focus:border-[#7d35ea] appearance-none mr-4"
                            id="dropdown"

                            {...methods.register("category")}
                        >
                            <option value="">All exams</option>
                            {categories.map((category, index) => {
                                return (
                                    <option key={index} value={category}>
                                        {category}
                                    </option>
                                );
                            })}
                        </select>
                        <div className="flex items-center">
                            <div className="flex space-x-1">
                                <input
                                    type="text"
                                    className=" w-[200px] h-[50px] rounded-[20px] text-white border-[1px] border-primary transition-all bg-transparent pl-3 outline-none focus:border-[#7d35ea] appearance-none mr-4"
                                    placeholder="Search questions..."
                                    {...methods.register("searchText")}
                                />
                                <button className="relative button-hover1 px-8 text-white bg-[#7d35ea] rounded-full "
                                    type="submit"
                                    onClick={searchRequestSubmit}>
                                    
                                    <div className="flex items-center justify-between">
                                        <FaSearch className="z-10"/>
                                        <p className=" relative z-10 text-white ml-2" >Search</p>
                                    </div>

                                </button>
                            </div>

                        </div>

                    </div>

                </form>
                <div className="flex flex-row">
                    <div className="flex flex-row m-4 mt-6"><p className="text-white font-size">Search in: </p>
                    </div>
                    <FormControlLabel control={
                        <Checkbox
                            defaultChecked
                            value={"question_content"}
                            {...methods.register("searchFields")}
                            style={{
                                color: "#7d35ea",
                                transform: "scale(1.4)"
                            }} />}
                        label="Question content"
                        componentsProps={{ typography: { color: "white" } }}
                    />
                    <FormControlLabel control={
                        <Checkbox
                            value={"answers"}
                            {...methods.register("searchFields")}
                            style={{
                                color: "#7d35ea",
                                transform: "scale(1.4)"
                            }} />}
                        label="Answers"
                        componentsProps={{ typography: { color: "white" } }}
                    />
                    <FormControlLabel control={
                        <Checkbox
                            value={"feedback"}
                            {...methods.register("searchFields")}
                            style={{
                                color: "#7d35ea",
                                transform: "scale(1.4)"
                            }} />}
                        label="Feedback"
                        componentsProps={{ typography: { color: "white" } }}
                    />
                    <FormControlLabel control={
                        <Checkbox
                            value={"tags"}
                            {...methods.register("searchFields")}
                            style={{
                                color: "#7d35ea",
                                transform: "scale(1.4)"
                            }} />}
                        label="Tags"
                        componentsProps={{ typography: { color: "white" } }}
                    />
                    

                </div>
                <div className="flex flex-row">
                    <div className="flex m-4 my-4"><p className="text-white font-size">Choose Difficulty: </p>
                    </div>
                    <FormControl className="flex">

                        <RadioGroup
                            id="radio-buttons-group"
                            name="radio-buttons-group"
                            defaultValue="All"
                            row
                            className="flex flex-row m-2"
                        >
                            <FormControlLabel

                                value=""
                                control={
                                    <Radio  {...methods.register("level")}
                                        style={{
                                            color: "#7d35ea",
                                            transform: "scale(1.4)"
                                        }} />}
                                label="All"
                                componentsProps={{ typography: { color: "white" } }}
                            />
                            <FormControlLabel
                                value="BEGINNER"
                                control={<Radio {...methods.register("level")}
                                    style={{
                                        color: "#7d35ea",
                                        transform: "scale(1.4)"
                                    }}
                                />}
                                label="Beginner"
                                componentsProps={{ typography: { color: "white" } }}
                            />
                            <FormControlLabel
                                value="INTERMEDIATE"
                                control={<Radio {...methods.register("level")}
                                    style={{
                                        color: "#7d35ea",
                                        transform: "scale(1.4)"
                                    }}
                                />}
                                label="Intermediate"
                                componentsProps={{ typography: { color: "white" } }}
                            />
                            <FormControlLabel
                                value="EXPERT"
                                control={<Radio {...methods.register("level")}
                                    style={{
                                        color: "#7d35ea",
                                        transform: "scale(1.4)"
                                    }}
                                />}
                                label="Expert"
                                componentsProps={{ typography: { color: "white" } }}
                            />
                        </RadioGroup>
                    </FormControl>

                </div>


                {searchResults && !error &&
                    <div className="text-white my-2  w-[1200px]">
                        <div id="results table" className="flex text-white my-2">
                            <table className="rounded-[20px] table-fixed w-[1200px] border-[1px] border-primary focus:border-[#7d35ea] border-separate border-spacing-0">
                                <thead className="rounded-[20px] border-left-none border-[1px] border-primary focus:border-[#7d35ea] border-spacing-0">
                                    <tr className="border-spacing-0">
                                        <th className="w-3/4 border-[1px] border-primary focus:border-[#7d35ea] border-x-0 border-t-0 border-collapse">Question Content</th>
                                        <th className="w-1/8 border-primary focus:border-[#7d35ea] border-x-0 border-t-0 border-collapse">Edit</th>
                                        <th className="w-1/8 border-primary focus:border-[#7d35ea] border-x-0 border-t-0 border-collapse">Delete</th>
                                    </tr>
                                </thead>
                                <tbody className="border-[1px] border-primary focus:border-[#7d35ea]">
                                    {resultQuestions.questions.map((question) => (
                                        <tr className="" key={question.id}>
                                            <td className="border-[1px] border-primary focus:border-[#7d35ea] border-b-0 border-x-0">{question.questionContent}</td>
                                            <td className="border-[1px] border-primary focus:border-[#7d35ea] border-b-0 border-x-0">
                                                <Button
                                                    handleClick={() => updateQuestion(question.id)}
                                                    title="Edit"
                                                ></Button>
                                            </td>
                                            <td className="border-[1px] border-primary focus:border-[#7d35ea] border-b-0 border-x-0">
                                                <Button
                                                    handleClick={() => {dispatch(setConfirmationWindow(true))
                                                                        dispatch(setQuestionToDelete(question.id))
                                                    }}
                                                    title="Delete"
                                                ></Button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                        { confirmationWindow && <ConfirmDeleteQuestion
                            message={"Delete question?"}
                            cancel={() => dispatch(setConfirmationWindow(false))}
                            confirm={() => deleteQuestion(id_question_to_delete)}
                            dark_and_blurry = {true}
                        />
                        }
                        <ReactPaginate
                            {...methods.register("page")}
                            pageCount={Math.ceil(resultQuestions.totalNumber / itemsPerPage)}
                            onPageChange={handlePageClick}
                            pageRangeDisplayed={2}
                            marginPagesDisplayed={2}
                            containerClassName={'pagination '}
                            activeClassName={'flex justify-center items-center active relative'}
                            breakClassName={'item break-me'}
                            nextClassName={"item next"}
                            nextLinkClassName={"button-hover3"}
                            pageClassName={'item pagination-page'}
                            previousClassName={"item previous"}
                            previousLinkClassName={"button-hover3"}
                            pageLinkClassName={"item flex justify-center items-center button-hover3 relative"}

                        />
                    </div>}
            </FormProvider>
            {
                error && !searchResults && <div className="w-max text-center ">
                    <p className="text-red-700 text-[18px]">{error.data}</p>
                </div>
            }

        </div>
    )
}
export default SearchWindow;
