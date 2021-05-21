package in.hocg.boot.changelog.autoconfiguration.core;

import com.google.common.collect.Lists;
import in.hocg.boot.changelog.autoconfiguration.compare.ChangeLogDto;
import in.hocg.boot.changelog.autoconfiguration.compare.FieldChangeDto;
import in.hocg.boot.utils.enums.ICode;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by hocgin on 2021/1/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ChangeLogService {

    @Transactional(rollbackFor = Exception.class)
    void insert(ChangeLogDto dto);

    @Transactional(rollbackFor = Exception.class)
    default void updateLog(@NonNull ICode refType, @NonNull Long refId, List<FieldChangeDto> changes, Long userId) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeLogDto.ChangeType.Update);
        result.setCreatedAt(LocalDateTime.now());
        result.setRefId(refId);
        result.setRefType(String.valueOf(refType.getCode()));
        result.setCreator(userId);
        result.setChange(changes);
        this.insert(result);
    }

    @Transactional(rollbackFor = Exception.class)
    default void deleteLog(@NonNull ICode refType, @NonNull Long refId, Long userId) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeLogDto.ChangeType.Delete);
        result.setCreatedAt(LocalDateTime.now());
        result.setRefId(refId);
        result.setRefType(String.valueOf(refType.getCode()));
        result.setCreator(userId);
        result.setChange(Lists.newArrayList());
        this.insert(result);
    }

    @Transactional(rollbackFor = Exception.class)
    default void insertLog(@NonNull ICode refType, @NonNull Long refId, Long userId) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeLogDto.ChangeType.Insert);
        result.setCreatedAt(LocalDateTime.now());
        result.setRefId(refId);
        result.setRefType(String.valueOf(refType.getCode()));
        result.setCreator(userId);
        result.setChange(Lists.newArrayList());
        this.insert(result);
    }

}
