export const password_validation = {
    name: 'password',
    type: 'password',
    id: 'password',
    placeholder: 'Enter your password',
    validation: {
      required: {
        value: true,
        message: 'Password field cannot be left blank!',
      },
      minLength: {
        value: 6,
        message: 'Password has to have at least 6 characters!',
      },
      pattern: {
        value: /(?=.*[A-Z])(?=.*[a-z])(?=.*[\W_])/,
        message: 'Password needs to contain upper case, lower case and a symbol!'
      }
    },
  }

  export const confirm_password_validation = {
    name: 'confirmPassword',
    type: 'password',
    id: 'confirmPassword',
    placeholder: 'Confirm your password',
    validation: {
      required: {
        value: true,
        message: 'Confirm password field cannot be left blank!',
      },
      minLength: {
        value: 6,
        message: 'Confirm password has to have at least 6 characters!',
      },
      pattern: {
        value: /(?=.*[A-Z])(?=.*[a-z])(?=.*[\W_])/,
        message: 'Confirm password needs to contain upper case, lower case and a symbol!'
      }
    },
  }

  export const old_password_validation = {
    name: 'oldPassword',
    type: 'password',
    id: 'oldPassword',
    placeholder: 'Enter your current password',
    validation: {
      required: {
        value: true,
        message: 'Current password field cannot be left blank!',
      },
      minLength: {
        value: 6,
        message: 'Current password has to have at least 6 characters!',
      },
      pattern: {
        value: /(?=.*[A-Z])(?=.*[a-z])(?=.*[\W_])/,
        message: 'Current password contains upper case, lower case and a symbol!'
      }
    },
  }

  export const first_name_validation = {
    name: 'firstname',
    type: 'text',
    id: 'firstname',
    placeholder: 'Enter your first name',
    validation: {
      required: {
        value: true,
        message: 'First name field cannot be left blank!',
      },
      maxLength: {
        value: 50,
        message: 'First names cannot be longer than 50 characters!',
      },
    },
  }

  export const last_name_validation = {
    name: 'lastname',
    type: 'text',
    id: 'lastname',
    placeholder: 'Enter your last name',
    validation: {
      required: {
        value: true,
        message: 'Last name field cannot be left blank!',
      },
      maxLength: {
        value: 100,
        message: 'Last names cannot be longer than 100 characters!',
      },
    },
  }

  export const email_validation = {
    name: 'email',
    type: 'email',
    id: 'email',
    placeholder: 'Enter your e-mail',
    validation: {
      required: {
        value: true,
        message: 'Email field cannot be left blank!',
      },
      pattern: {
        value:
          /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
        message: 'Not a valid email address!',
      },
    },
  }

  export const comment_validation = {
    name: 'comment',
    type: 'text',
    id: 'comment',
    placeholder: 'Why do you want to report this question?',
    validation: {
      required: {
        value: true,
        message: 'Please explain why you want to report the question!',
      },
      minLength: {
        value: 20,
        message: 'Please write a more extensive explanation, 20 characters or more!',
      },
      maxLength: {
        value: 1000,
        message: 'Please keep your explanation under 1000 characters!',
      },
    },
  }