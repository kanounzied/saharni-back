package Saharni.Saharni_back.service.password_check;

public class PasswordRegVerif {
    public static boolean validate(String password){
        //length = 8 at least min maj special char
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";
        return password.matches(pattern);
    }
}