import { FormProvider, useForm } from "react-hook-form";
import HookInput from "./HookInput";
import { useEffect, useState } from "react";
import changeUserService from "../../services/changeUser.service";
import ChangeResponseBox from "./ChangeResponseBox";
import { useDispatch, useSelector } from "react-redux";
import { setTokenData } from "../../utils/features/userDataSlice";

const ChangeForm = ({field, label, validationForm}) => {
    const [response, setResponse] = useState(null);
    const [error, setError] = useState(null);
    const dispatch = useDispatch();
    const firstname= useSelector((state) => state.userData.firstname);
    const lastname = useSelector((state) => state.userData.lastname);


    const methods = useForm({defaultValues: {
        firstname: firstname,
        lastname: lastname
    }}
    );
    const [errors, setErrors] = useState([]);
    useEffect(() => {
      (Object.keys(errors).length==0)?
      document.getElementById(field.toLowerCase()).style.outline =  "none"
      :document.getElementById(field.toLowerCase()).style.outline ="2px solid red"
    });
    const resetResponses = () => {
      response && setResponse(null);
    };
    const changeName = async (data,field) => {
      resetResponses();
      if((field.toLowerCase() ==="firstname" && data[field.toLowerCase()] !== firstname) || 
      (field.toLowerCase() ==="lastname" && data[field.toLowerCase()] !== lastname)
      ){
      return changeUserService
          .changeName(data[field.toLowerCase()],field)
          .then((response) => {
            setResponse(
              response.data.confirmationMessage
            );
            dispatch(setTokenData(response.data.token));
          })
          .catch((error) => {
            setError(error.response.data);
          })}
  };

    const submitResettingRequest = methods.handleSubmit(async (data) => {
        changeName(data,field);
        });

    return (
        <div id="form_container" className="flex items-center w-full lg:h-auto md:h-[130px]">
        {(response || error) && (
          <div>
          <ChangeResponseBox
            setResponse={setResponse}
            response={response}
            setError={setError}
            error={error}
            />
          </div>
        )}
        {!(Object.keys(errors).length==0) && 
              <div>
                <p id={field+"_error"} className="absolute my-16 ml-4 text-red-700 text-[16px]">{errors[field.toLowerCase()].message}</p>
                </div>
              }
        <FormProvider {...methods}>
            <div  className="flex mt-16 ml-4" id="input box">
              <form
                onSubmit={e => e.preventDefault()}
                noValidate
                autoComplete="off"

                className="flex lg:flex-row md:flex-col"


              >
              <HookInput setErrors={setErrors} label={label} {...validationForm}/>
              
             
              <button
                  className="relative bg-[#7518ff] button-hover1 lg:mt-0 lg:mt-2 md:mt-2 lg:left-[5px] md:left-0 lg:w-[260px] md:w-[150px] lg:h-[54px] md:h-[40px] rounded-[30px]"
                  id={"Change"+field+ "Button"}
                  type="submit"
                  onClick={submitResettingRequest}
                ><p
                className="relative z-10 text-white font-bold 
                   lg:text-[24px] md:text-[15px] px-4`"
              >
               {"Change " + label}
              </p></button>
              </form>
               
        </div>
        
        </FormProvider>
        
        </div>
    );
};
export default ChangeForm;