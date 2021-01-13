package in.hocg.boot.mybatis.plus.autoconfiguration.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import in.hocg.boot.mybatis.plus.autoconfiguration.ro.PageRo;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;

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
        return page(page.getTotal(), page.getCurrent(), page.getSize(), Collections.emptyList());
    }

    /**
     * 创建空分页对象
     *
     * @param current 当前页数
     * @param size    当前页数量
     * @return 分页对象
     */
    public static <T> IPage<T> emptyPage(long current, long size) {
        return page(0L, current, size, Collections.emptyList());
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
    public static <T> IPage<T> page(long total, long current, long size, List<T> data) {
        return new Page<T>(current, size, total)
            .setRecords(data);
    }
}
