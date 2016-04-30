package news.caughtup.caughtup.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationUser extends User {

    private String password;
    private String confirm;

    public RegistrationUser(String userName) {
        super(userName);
    }

    public void setPassword(String password, String confirm) {
        this.password = password;
        this.confirm = confirm;
    }

    public boolean confirmName() {
        return name != null && !name.isEmpty();
    }

    public boolean confirmPassword() {
        return password != null && password.equals(confirm) && !password.isEmpty();
    }

    public JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", getName());
        jsonObject.put("password", password);
        jsonObject.put("fullName", getFullName());
        jsonObject.put("email", getEmail());
        return jsonObject;
    }
}
