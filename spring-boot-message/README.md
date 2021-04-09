## 消息类型
- 异步消息
- 同步消息
  - 普通消息
  - 事务消息




```bash
MessageFactory.






MessageFactory.syncSend(消息, 消息轨迹, 是否持久, 超时时间)

MessageFactory.local().syncSend(message, isTransactional, timeout);
MessageFactory.mq().syncSend(message, isTransactional, timeout);


MessageFactory.syncSend(message, isTransactional, timeout);
MessageFactory.syncSend(message, isTransactional);
MessageFactory.syncSend(message);
MessageFactory.asyncSend(message);
```
