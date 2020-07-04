import java.io.Serializable;

public class Respond implements Serializable {
    private String msg;
    private boolean isLogin = true;

    public Respond(String msg) {
        this.msg = msg;
    }

    public Respond() {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
