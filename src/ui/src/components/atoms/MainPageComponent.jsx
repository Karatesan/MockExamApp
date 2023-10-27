import { afterLoginNavbarConsts } from "../../assets/constants";

const MainPageComponent = ({elem,text}) => {
    const FaPic = afterLoginNavbarConsts.find(obj => { return obj.id ===elem});

    return (
        <div className="flex w-[400px] h-[200px] border-2 m-2 border-primary rounded-[40px]">
            <div className="flex m-3 items-center ">
            <FaPic.img 
            className="text-[40px] h-[80px] w-[80px] ml-[10px] mr-[15px] text-primary"
            />
            <p  className="text-[18px] text-white">
                {text}
            </p>
            </div>
        </div>
    );
};
export default MainPageComponent;