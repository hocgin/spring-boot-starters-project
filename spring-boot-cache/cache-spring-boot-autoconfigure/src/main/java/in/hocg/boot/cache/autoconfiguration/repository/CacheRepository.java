package in.hocg.boot.cache.autoconfiguration.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface CacheRepository {

    /**
     * 添加到带有过期时间的缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间(单位秒)
     */
    void setExpire(final byte[] key, final byte[] value, final long time);

    /**
     * 添加到带有过期时间的缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间(单位秒)
     */
    void setExpire(final String key, final Object value, final long time);

    /**
     * 批量添加到带有过期时间的缓存
     *
     * @param keys   redis主键
     * @param values 值
     * @param time   过期时间(单位秒)
     */
    void setExpire(final String[] keys, final Object[] values, final long time);

    /**
     * 一次性添加数组到过期时间的缓存，不用多次连接，节省开销
     *
     * @param keys   the keys
     * @param values the values
     */

    void set(final String[] keys, final Object[] values);

    /**
     * 添加到缓存
     *
     * @param key   the key
     * @param value the value
     */
    void set(final String key, final Object value);

    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the byte [ ]
     */
    byte[] get(final byte[] key);


    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the string
     */
    Object get(final String key);

    /**
     * (HASH操作)
     * 对HashMap操作
     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    void putHashValue(String key, String hashKey, Object hashValue);

    /**
     * (HASH操作)
     * 获取单个field对应的值
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the hash values
     */
    Object getHashValues(String key, String hashKey);

    /**
     * (HASH操作)
     * 根据key值删除
     *
     * @param key      the key
     * @param hashKeys the hash keys
     */
    void delHashValues(String key, Object... hashKeys);

    /**
     * (HASH操作)
     * key只匹配map
     *
     * @param key the key
     * @return the hash value
     */
    Map<String, Object> getHashValue(String key);

    /**
     * (HASH操作)
     * 批量添加
     *
     * @param key the key
     * @param map the map
     */
    void putHashValues(String key, Map<String, Object> map);

    /**
     * 判断某个主键是否存在
     *
     * @param key the key
     * @return the boolean
     */
    boolean exists(final String key);


    /**
     * 删除key
     *
     * @param keys the keys
     * @return the long
     */
    long del(final String... keys);

    /**
     * (List操作)
     * redis List数据结构 : 将一个或多个值 value 插入到列表 key 的表头
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    Long leftPush(String key, Object value);

    /**
     * (List操作)
     * redis List数据结构 : 移除并返回列表 key 的头元素
     *
     * @param key the key
     * @return the string
     */
    Object leftPop(String key);

    /**
     * (List操作)
     * redis List数据结构 :将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    Long in(String key, Object value);

    /**
     * (List操作)
     * redis List数据结构 : 移除并返回列表 key 的末尾元素
     *
     * @param key the key
     * @return the string
     */
    Object rightPop(String key);

    /**
     * (List操作)
     * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
     *
     * @param key the key
     * @return the long
     */
    Long length(String key);

    /**
     * (List操作)
     * redis List数据结构 : 根据参数 i 的值，移除列表中与参数 value 相等的元素
     *
     * @param key   the key
     * @param i     the
     * @param value the value
     */
    void remove(String key, long i, Object value);

    /**
     * (List操作)
     * redis List数据结构 : 将列表 key 下标为 index 的元素的值设置为 value
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    void set(String key, long index, Object value);

    /**
     * (List操作)
     * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
     *
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the list
     */
    List<Object> getList(String key, int start, int end);

    /**
     * (List操作)
     * redis List数据结构 : 批量存储
     *
     * @param key  the key
     * @param list the list
     * @return the long
     */
    Long leftPushAll(String key, List<String> list);

    /**
     * (List操作)
     * redis List数据结构 : 将值 value 插入到列表 key 当中，位于值 index 之前或之后,默认之后。
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    void insert(String key, long index, Object value);

    /**
     * 迭代哈希表中的键值对
     *
     * @param key
     * @param pattern
     * @param count
     * @return
     */
    Set<Map.Entry<Object, Object>> hScan(String key, String pattern, int count);

    /**
     * 迭代set中的键值对
     *
     * @param key
     * @param pattern
     * @param count
     * @return
     */
    Set<Object> sScan(String key, String pattern, int count);


    /**
     * 迭代zset中的键值对
     *
     * @param key
     * @param pattern
     * @param count
     * @return
     */
    Set<Object> zScan(String key, String pattern, int count);


    /**
     * scan命令
     *
     * @param pattern 查询条件
     * @param count   count是每次扫描的key个数，并不是结果集个数。count要根据扫描数据量大小而定，Scan虽然无锁，但是也不能保证在超过百万数据量级别搜索效率；
     *                count不能太小，网络交互会变多，count要尽可能的大。在搜索结果集1万以内，建议直接设置为与所搜集大小相同
     * @return
     */
    Set<String> scan(String pattern, int count);
}
