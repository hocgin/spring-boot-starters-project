package in.hocg.boot.mybatis.plus.autoconfiguration.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro.PageRo;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.utils.LangUtils;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hocgin on 2021/1/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class PageUtils {
    /**
     * 创建空分页对象
     *
     * @param ro 分页请求
     * @return 分页对象
     */
    public static <T> IPage<T> emptyPage(PageRo ro) {
        return emptyPage(ro.getPage(), ro.getSize());
    }

    /**
     * 空分页
     *
     * @param page 分页对象
     * @param <T>  对象
     * @return 新分页对象
     */
    public static <T> IPage<T> emptyPage(IPage<T> page) {
        return fillPage(page.getTotal(), page.getCurrent(), page.getSize(), Collections.emptyList());
    }

    /**
     * 创建空分页对象
     *
     * @param current 当前页数
     * @param size    当前页数量
     * @return 分页对象
     */
    public static <T> IPage<T> emptyPage(long current, long size) {
        return fillPage(0L, current, size, Collections.emptyList());
    }

    /**
     * 创建分页对象
     *
     * @param total   总数量
     * @param current 当前页
     * @param size    每页数量
     * @param data    当前页数据
     * @param <T>     对象
     * @return 分页对象
     */
    public static <T> IPage<T> fillPage(long total, long current, long size, List<T> data) {
        return new Page<T>(current, size, total).setRecords(data);
    }

    /**
     * List 转为 IPage
     *
     * @param data 列表
     * @param <T>
     * @return
     */
    public static <T> IPage<T> fillPage(List<T> data) {
        if (CollUtil.isEmpty(data)) {
            return emptyPage(1L, 1L);
        }
        return new Page<T>(1L, data.size(), data.size()).setRecords(data);
    }

    /**
     * 填充滚动对象
     *
     * @param page         分页对象
     * @param nextIdMapper 提取
     * @param <T>          对象
     * @return 滚动对象
     */
    public static <T> IScroll<T> fillScroll(IPage<T> page, Function<T, ? extends Serializable> nextIdMapper) {
        boolean hasMore;
        List<T> records = page.getRecords();
        if (page.searchCount()) {
            hasMore = page.getPages() > page.getCurrent();
        } else {
            hasMore = page.getSize() == records.size();
        }
        return fillScroll(hasMore, records, nextIdMapper);
    }

    /**
     * 填充滚动对象
     *
     * @param hasMore      是否有更多
     * @param records      当前页数据
     * @param nextIdMapper 提取
     * @param <T>          对象
     * @return 滚动对象
     */
    public static <T> IScroll<T> fillScroll(Boolean hasMore, List<T> records, Function<T, ? extends Serializable> nextIdMapper) {
        Serializable nextId = null;
        if (CollUtil.isNotEmpty(records)) {
            nextId = LangUtils.callIfNotNull(records.get(records.size() - 1), nextIdMapper).orElse(null);
        }
        return new IScroll<T>().setHasMore(hasMore).setRecords(records).setNextId(nextId);
    }

    /**
     * 填充滚动对象
     *
     * @param data
     * @param nextIdMapper
     * @param <T>
     * @return
     */
    public static <T> IScroll<T> fillScroll(List<T> data, Function<T, ? extends Serializable> nextIdMapper) {
        return fillScroll(false, data, nextIdMapper);
    }

    /**
     * 填充滚动对象
     *
     * @param <T> 对象
     * @return 滚动对象
     */
    public static <T> IScroll<T> emptyScroll() {
        return fillScroll(false, Collections.emptyList(), null);
    }

    /**
     * 设置层级别
     *
     * @param pageSize
     * @param currentPage
     * @param totalSize
     * @param desc
     * @param data
     * @param indexMapper
     * @param <T>
     * @return
     */
    public static <T> List<T> fillIndex(long pageSize, long currentPage, Long totalSize, List<T> data, boolean desc, SFunction<T, Long> indexMapper) {
        if (CollUtil.isEmpty(data)) {
            return data;
        }

        String indexFieldName = PropertyNamer.methodToProperty(LambdaUtils.extract(indexMapper).getImplMethodName());

        int step = 1;
        long baseSize = pageSize * currentPage;
        long baseIndex = baseSize;
        if (desc) {
            step = -1;
            baseIndex = totalSize - baseSize;
        }

        final int finalStep = step;
        final long[] index = {baseIndex};
        Consumer<T> setIndexConsumer = t -> ReflectUtil.setFieldValue(t, indexFieldName, index[0] += finalStep);
        data.forEach(setIndexConsumer);
        return data;
    }

    public static <T> IPage<T> fillIndex(IPage<T> page, boolean desc, SFunction<T, Long> indexMapper) {
        fillIndex(page.getSize(), page.getCurrent(), page.getTotal(), page.getRecords(), desc, indexMapper);
        return page;
    }

}
