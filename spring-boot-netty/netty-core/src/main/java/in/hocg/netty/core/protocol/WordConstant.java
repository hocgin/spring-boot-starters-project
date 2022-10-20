package in.hocg.netty.core.protocol;

/**
 * Created by hocgin on 2019/3/5.
 * email: hocgin@gmail.com
 * 字宽
 * <p>
 * 魔数(4) | 版本号(1) | 序列化算法(1)| 模块(1) | 指令(1) | 数据长度(4) | 数据(n)
 *
 * @author hocgin
 */
public interface WordConstant {

    interface Width {
        int MAGIC_NUMBER = 4;
        int VERSION = 1;
        int SERIALIZER_ALGORITHM = 1;
        int MODULE = 1;
        int COMMEND = 1;
        int DATA_LENGTH = 4;

        int LENGTH_FIELD_OFFSET = Width.MAGIC_NUMBER + Width.VERSION + Width.SERIALIZER_ALGORITHM + Width.MODULE + Width.COMMEND;
        int LENGTH_FIELD_LENGTH = Width.DATA_LENGTH;
    }

    interface Content {
        int MAGIC_NUMBER_CONTENT = 0x1234;
    }
}
