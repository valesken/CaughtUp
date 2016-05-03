package news.caughtup.caughtup.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An object to hold information about the user that is registering for a new account.
 */
public class RegistrationUser extends User {
    private String password;
    private String confirm;

    /**
     * Constructor.
     * @param userName
     */
    public RegistrationUser(String userName) {
        super(userName);
    }

    /**
     * Sets the password for the new user.
     * @param password
     * @param confirm
     */
    public void setPassword(String password, String confirm) {
        this.password = password;
        this.confirm = confirm;
    }

    /**
     * Confirms that the name field is not empty.
     * @return
     */
    public boolean confirmName() {
        return name != null && !name.isEmpty();
    }

    /**
     * Checks that password and the confirmation match.
     * @return
     */
    public boolean confirmPassword() {
        return password != null && password.equals(confirm) && !password.isEmpty();
    }

    /**
     * Creates a JSON object of the class for trasnferring to the backend.
     * @return
     * @throws JSONException
     */
    public JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", getName());
        jsonObject.put("password", password);
        jsonObject.put("fullName", getFullName());
        jsonObject.put("email", getEmail());
        return jsonObject;
    }
}
