package in.hocg.boot.netty.sample.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author hocgin
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class PacketRo implements Serializable {
    private String title;
}
