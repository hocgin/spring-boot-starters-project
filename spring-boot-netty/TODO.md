channel 连接后，分配唯一标识(channelId)
channel 登录后，分配唯一用户标识(channelId, userId)

消息发送时，生成唯一消息编号，检查目标channelId是否在本机，是则从本地channel池获取并发送，否则广播消息。消息消费后原路发送回执。
消费广播消息，检查目标channelId是否在本机，是则发送，否则丢弃。（发送后返回回执）

### 备注

这边可以根据回执维护 channel 路由，发送时优先根据channel路由指定消息通道。channel路由需要拥有自动维护能力。

message -> packet -> data
