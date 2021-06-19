package in.hocg.boot.changelog.autoconfiguration.core;

import in.hocg.boot.changelog.autoconfiguration.compare.ChangeLogDto;
import in.hocg.boot.changelog.autoconfiguration.compare.FieldChangeDto;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2021/1/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ChangeLogBervice {

    @Transactional(rollbackFor = Exception.class)
    void insert(ChangeLogDto dto);

    @Transactional(rollbackFor = Exception.class)
    default void updateLog(@NonNull String refType, @NonNull Long refId, List<FieldChangeDto> changes, Long userId) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeLogDto.ChangeType.Modify);
        result.setCreatedAt(LocalDateTime.now());
        result.setRefId(refId);
        result.setRefType(refType);
        result.setCreator(userId);
        result.setChange(changes);
        this.insert(result);
    }

    @Transactional(rollbackFor = Exception.class)
    default void deleteLog(@NonNull String refType, @NonNull Long refId, Long userId) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeLogDto.ChangeType.Delete);
        result.setCreatedAt(LocalDateTime.now());
        result.setRefId(refId);
        result.setRefType(refType);
        result.setCreator(userId);
        result.setChange(Collections.emptyList());
        this.insert(result);
    }

    @Transactional(rollbackFor = Exception.class)
    default void insertLog(@NonNull String refType, @NonNull Long refId, Long userId) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeLogDto.ChangeType.Insert);
        result.setCreatedAt(LocalDateTime.now());
        result.setRefId(refId);
        result.setRefType(refType);
        result.setCreator(userId);
        result.setChange(Collections.emptyList());
        this.insert(result);
    }

}
