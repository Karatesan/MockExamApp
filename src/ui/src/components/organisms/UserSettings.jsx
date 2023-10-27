import { FormProvider, useForm } from "react-hook-form";
import {
  first_name_validation,
  last_name_validation,
} from "../../utils/InputValidations";
import HookInput from "../atoms/HookInput";
import { BiCloudUpload } from "react-icons/bi";
import { useEffect, useState } from "react";
import ChangeForm from "../atoms/ChangeForm";
import { useNavigate } from "react-router";
import Button from "../atoms/Button";
import changeUserService from "../../services/changeUser.service";
import ChangeResponseBox from "../atoms/ChangeResponseBox";
import DeleteAccountBox from "./DeleteAccountBox";
import { RiDeleteBin6Fill } from "react-icons/ri";
import { useSessionStorage } from "../../utils/useSessionStorage";
import { useDispatch, useSelector } from "react-redux";
import { setProfilePicture } from "../../utils/features/userDataSlice";

const UserSettings = () => {
  const profilePic = useSelector((state) => state.userData.profilePicture);
  const [pictureError, setPictureError] = useState(null);
  const [response, setResponse] = useState(null);

  const navigate = useNavigate();

  const dispatch= useDispatch();
  const deleteAccount = () => (
    navigate("/deleteAccount")
  );

  return (
    <div>
      <h2 className=" font-semibold text-center text-[40px] text-white">
        Account Settings
      </h2>
      <div className="flex flex-row items-center">
        <div className="flex flex-col justify-start lg:w-[700px] md:w-[400px] p-4">
          <ChangeForm
            field="Firstname"
            label="First Name"
            validationForm={first_name_validation}
          />
          <ChangeForm
            field="Lastname"
            label="Last Name"
            validationForm={last_name_validation}
          />
          <div className="flex lg:mt-12 md:mt-12 ml-4 flex-col ">
            <label className="absolute lg:text-[26px] md:text-[20px] lg:font-bold ml-2 lg:-translate-y-10 md:-translate-y-7 text-white ">
              {"Role"}
            </label>
            <div
              className={`flex items-center lg:w-[350px] md:w-[250px] lg:h-[50px] md:h-[40px] border-2  border-primary bg-transparent rounded-[20px] pl-3 text-white outline-none focus:border-white `}
            >
              <p>{sessionStorage.getItem("role")}</p>
            </div>
          </div>
          <div className="flex lg:mt-12 md:mt-8 ml-4 flex-col ">
            <label className="absolute lg:font-bold lg:text-[26px] md:text-[20px] mt-3 ml-2 -translate-y-10 text-white">
              {"Password"}
            </label>
            <div className="ml-0">
              <Button
                id={"ChangePassword"}
                title={"Change Password"}
                variation={1}
                buttonSize={1}
                handleClick={() => navigate("/changePassword")}
                type="submit"
              />
            </div>
          </div>
        </div>
        <div className="lg:w-[600px] md:w-[500px] lg:h-[600px] md:h-[450px] flex-col flex lg:justify-center md:justify-end items-center">
          {!profilePic && 
            <div className="lg:w-[300px] md:w-[250px] flex flex-col lg:h-[300px] md:h-[250px] mb-4 border-[2px] rounded-[20px] justify-center items-center border-primary">
              <div>
                <p className="text-[20px] text-center font-bold text-white lg:ml-4">
                  Profile Picture
                </p>
              </div>
              <div>
                <p className="text-[20px] block text-center font-bold text-white lg:ml-4">
                  2mb max
                </p>

                </div>
          </div>}
          {profilePic &&
            <div className="relative">
              <img
                className="lg:w-[300px] md:w-[250px] mb-4 lg:h-[300px] md:h-[250px] rounded-[20px]"
                src={profilePic}
              />
              <div
                className="absolute w-full h-full top-0 opacity-0 hover:opacity-100 flex items-center justify-center cursor-pointer"
                onClick={() =>
                  changeUserService.deleteImage().then(
                    dispatch(setProfilePicture(null)),
                    sessionStorage.removeItem("profile_picture")
                  )
                }
              >
                <RiDeleteBin6Fill
                  size="50"
                  color="#8231fdd4"
                  className=""
                />
              </div>
            </div>
          }
          {pictureError &&
            <ChangeResponseBox
              response={response}
              setResponse={setResponse}
              setError={setPictureError}
              error={pictureError}
            />
          }
          <div
            id="input_image_container"
            className="lg:w-[350px] md:w-[250px] flex justify-center items-center border-[2px] mt-4 border-primary rounded-[30px] pl-4"
          >
            <input
              id="file_input"
              name="file_input"
              type="file"
              accept=".jpeg, .png, .jpg"
              onChange={(event) =>
                changeUserService.changePicture(
                  event,
                  setPictureError
                ).then(response => dispatch(setProfilePicture(response)))
              }
              className={`hidden`}
            />
            <label
              className={`flex justify-center items-center cursor-pointer`}
              htmlFor="file_input"
            >
              <BiCloudUpload className="text-[50px] text-primary hover:text-[#8231fdd4] transition-all" />
              <span className="text-[20px] font-bold text-white ml-4">
                Upload Profile Picture
              </span>
            </label>
          </div>
          <button
            className="relative bg-[#7518ff] button-hover1 lg:mt-3 md:mt-2 lg:mb-3 md:mb-2 lg:w-[260px] md:w-[200px] lg:h-[54px] md:h-[48px] lg:rounded-[30px] md:rounded-[25px]"
            id={"DeleteAccountButton"}
            type="button"
            onClick={deleteAccount}
          >
            <p
              className="relative z-10 text-white font-bold 
                   lg:text-[24px] md:text-[16px] px-4`"
            >
              {"Delete your account"}
            </p>
          </button>
        </div>
      </div>
    </div>
  );
};

export default UserSettings;
