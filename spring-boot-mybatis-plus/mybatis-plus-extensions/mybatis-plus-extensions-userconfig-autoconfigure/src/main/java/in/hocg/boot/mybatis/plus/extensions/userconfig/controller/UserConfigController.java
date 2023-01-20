package in.hocg.boot.mybatis.plus.extensions.userconfig.controller;


import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.ClearRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.DeleteRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.QueryRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.SetRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.service.UserConfigService;
import in.hocg.boot.utils.StringPoolUtils;
import in.hocg.boot.utils.context.UserContextHolder;
import in.hocg.boot.utils.struct.KeyValue;
import in.hocg.boot.utils.struct.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * [BOOT] 用户配置表 前端控制器
 * </p>
 *
 * @author hocgin
 * @since 2023-01-20
 */
@Api(tags = "[BOOT] 用户配置表")
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@RequestMapping("/user-config")
public class UserConfigController {
    private final UserConfigService service;

    /**
     * 增加或修改
     * [{key, value}]
     *
     * @return [{key, value}]
     */
    @ApiOperation("增加或修改")
    @PutMapping("/set")
    public Result<List<KeyValue>> set(@Valid @RequestBody SetRo ro, @RequestHeader(StringPoolUtils.HEADER_SOURCE) String source) {
        ro.setOptUserId(UserContextHolder.getUserIdThrow());
        ro.setSource(source);
        return Result.success(service.set(ro));
    }

    /**
     * 删除
     * keys=[]
     *
     * @return [{key, value}]
     */
    @ApiOperation("删除")
    @DeleteMapping("/delete")
    public Result<List<KeyValue>> delete(@Valid @RequestBody DeleteRo ro, @RequestHeader(StringPoolUtils.HEADER_SOURCE) String source) {
        ro.setOptUserId(UserContextHolder.getUserIdThrow());
        ro.setSource(source);
        return Result.success(service.delete(ro));
    }

    /**
     * 清除
     * scope
     *
     * @return [{key, value}]
     */
    @ApiOperation("清除")
    @DeleteMapping("/clear")
    public Result<List<KeyValue>> clear(@Valid @RequestBody(required = false) ClearRo ro, @RequestHeader(StringPoolUtils.HEADER_SOURCE) String source) {
        ro.setOptUserId(UserContextHolder.getUserIdThrow());
        ro.setSource(source);
        return Result.success(service.clear(ro));
    }

    /**
     * 查询
     * keys=[]
     *
     * @return [{key, value}]
     */
    @ApiOperation("查询")
    @PostMapping("/query")
    public Result<List<KeyValue>> query(@Valid @RequestBody QueryRo ro, @RequestHeader(StringPoolUtils.HEADER_SOURCE) String source) {
        ro.setOptUserId(UserContextHolder.getUserIdThrow());
        ro.setSource(source);
        return Result.success(service.query(ro));
    }
}

