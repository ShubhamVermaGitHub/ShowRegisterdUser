package b.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SessionManager {

    private Context context;
    private static final String REMEMBER_PWD = "password";
    private static final String REMEMBER_EMAIL = "email";
    private static final String AUTH_TOKEN = "auth_token";

    // private Activity activity;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences.Editor editorRem;

    private static final String IS_LOGGEDIN = "isLoggedIn";
    public SharedPreferences myprefRemember;
    private static final String PREF_NAMELAN = "lan";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();

        myprefRemember = this.context.getSharedPreferences(PREF_NAMELAN, Context.MODE_PRIVATE);
        editorRem = myprefRemember.edit();
        editorRem.apply();
    }


    public  RegisterModal createSession(RegisterModal registerModal){
        editorRem.putString("name", registerModal.name);
        editorRem.putString("email", registerModal.email);
        editorRem.putString("id", registerModal.id);
        editorRem.putString("mobile", registerModal.mobile);
        editorRem.putString("deviceTocan", registerModal.deviceToken);
        editorRem.putString("password", registerModal.password);
        editorRem.putBoolean("isRegister", true);
        editorRem.commit();
        return  registerModal;
    }


    public Boolean isRegister() {
        return sharedPreferences.getBoolean("isRegister", false);
    }


    /*public void sessionForId(String id) {
        editorRem.putString("id", id);
        editorRem.commit();
    }

    public String getId() {
        return sharedPreferences.getString("id", "");
    }
*/
    public void sessionForTocan(String tocken) {
        editorRem.putString("tocken", tocken);
        editorRem.commit();
    }

    public String getTocan() {
        return sharedPreferences.getString("tocken", "");
    }


    public void logout() {
        editorRem.clear();
        editorRem.apply();
        context.startActivity(new Intent(context, LoginActivity.class));
        ((Activity) context).finish();
    }


}
