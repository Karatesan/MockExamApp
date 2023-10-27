import { useEffect } from "react";
import { useFormContext } from "react-hook-form";

const TextArea = ({label, placeholder, type, id, question_id,validation,setErrors }) => {
const {register,
    formState: {errors},
} = useFormContext();
useEffect(() => {
    setErrors(errors)
})
return (
    <div>
    
    <textarea 
    placeholder={placeholder}
    type={type}
    id={id}
    name={id}
    {...register(id,validation)}
    rows={3}
    cols={40}
    className="border-2 border-primary bg-transparent rounded-[20px] text-white p-2 resize-none"
    />
    </div>
)};
export default TextArea;