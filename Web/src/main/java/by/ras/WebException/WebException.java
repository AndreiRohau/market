package by.ras.WebException;

public class WebException extends Exception {

    private static final long serialVersionUID = 1L;

    public WebException() {
        super();
    }

    public WebException(String message) {
        super(message);
    }

    public WebException(Exception e) {
        super(e);
    }

    public WebException(String message, Exception e) {
        super(message, e);
    }
}
