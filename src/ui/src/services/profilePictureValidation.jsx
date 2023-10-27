import changeUserService from "./changeUser.service";

class profilePictureValidation{
    validateFile(profile_picture){

        const maxFileSize = 2000000;

        const isFileSizeValid = profile_picture.size<maxFileSize;
        const isFormatValid = changeUserService.isImage(profile_picture);
        
        if(!isFormatValid){
            return "Input must be image file."
        }

        if(!isFileSizeValid){
            return "File size cannot exceed 2mb.";
        }
        return true;
      }
}
export default profilePictureValidation;