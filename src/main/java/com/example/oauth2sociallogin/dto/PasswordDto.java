package com.example.oauth2sociallogin.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class PasswordDto {

    @Size(min = 5, message = "Podane hasło jest za krótkie")
    private String password;

    @Size(min = 5, message = "Podane powtórne hasło jest za krótkie")
    private String rePassword;

    private StringBuilder errors = new StringBuilder();


    public boolean hasErrors(){

        validatePasswords();
        return !errors.toString().isEmpty();
    }

    public String getAllErrors(){
        return errors.toString();
    }

    private void validatePasswords(){

        if(!equalsPassAndRepass()) errors.append("Podane hasła różnią się. ");
        if(!hasUpperCase(password)) errors.append("Hasło powinno zawierać wielką literę. ");
        if(!hasDigits(password)) errors.append("Hasło powinno zawierać cyfry i znaki specjalne.");
    }

    private boolean hasUpperCase(String pwd){

        for (char ch : pwd.toCharArray()){
            if(Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDigits(String pwd){

        for (char ch : pwd.toCharArray()){
            if(Character.isDigit(ch)) {
                return true;
            }
        }
        return false;
    }

    private boolean equalsPassAndRepass(){
        return password.equals(rePassword);
    }

}
