package in.hocg.boot.excel.sample;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import in.hocg.boot.excel.sample.vo.ExcelDataVo;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro.PageRo;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    public IPage<ExcelDataVo> paging(PageRo ro) {
        int maxCount = 20;
        if (ro.getPage() <= maxCount) {
            IPage<ExcelDataVo> page = PageUtils.emptyPage(ro);
            return page.setRecords(mock(ro.getSize()));
        }
        return PageUtils.emptyPage(ro);
    }

    private List<ExcelDataVo> mock(int size) {
        List<ExcelDataVo> result = Lists.newArrayList();

        for (int i = 0; i < size; i++) {
            ExcelDataVo item = new ExcelDataVo();
            item.setTitle(StrUtil.format("标题 {}", i));
            result.add(item);
        }

        return result;
    }

    public void handle(@Validated List<ExcelDataVo> data) {
        log.info("处理数据: {} 条", data.size());
    }

}
