package in.hocg.netty.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by hocgin on 2019/3/2.
 * email: hocgin@gmail.com
 * <p>
 * 拆包器
 * - 拒绝非本协议
 * - 解决粘包问题
 *
 * @author hocgin
 */
@Slf4j
public class Splitter extends LengthFieldBasedFrameDecoder {

    public Splitter() {
        super(Integer.MAX_VALUE, WordConstant.Width.LENGTH_FIELD_OFFSET, WordConstant.Width.LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        if (in.getInt(in.readerIndex()) != WordConstant.Content.MAGIC_NUMBER_CONTENT) {
            Channel channel = ctx.channel();
            channel.close();
            return null;
        }
        log.debug("正常放行");

        return super.decode(ctx, in);
    }
}
