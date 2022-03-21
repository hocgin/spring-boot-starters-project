package in.hocg.boot.utils.exception;

/**
 * Created by hocgin on 2021/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ToolsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ToolsException(String message) {
        super(message);
    }

    public ToolsException(Throwable throwable) {
        super(throwable);
    }

    public ToolsException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
