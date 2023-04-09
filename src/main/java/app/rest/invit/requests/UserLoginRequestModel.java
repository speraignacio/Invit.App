package app.rest.invit.requests;

public class UserLoginRequestModel {
    private String email;
    private String password;
//    private int userState;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
//    public int getUserState() {
//		return userState;
//	}
//
//	public void setUserState(int userState) {
//		this.userState = userState;
//	}
}
