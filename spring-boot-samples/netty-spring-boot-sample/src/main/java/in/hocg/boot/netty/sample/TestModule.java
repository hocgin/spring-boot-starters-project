package in.hocg.boot.netty.sample;

/**
 * Created by hocgin on 2019/3/17.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@SuppressWarnings("all")
public interface TestModule {
    byte MODULE_VALUE = 1;

    /**
     * 指令
     */
    byte TEST_REQUEST = 1;
    byte TEST_REQUEST_2 = 2;
    byte TEST_RESPONSE = 2;
}

