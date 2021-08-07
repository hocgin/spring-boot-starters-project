package in.hocg.boot.http.log.autoconfiguration.jdbc.mysql;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.http.log.autoconfiguration.core.HttpLog;
import in.hocg.boot.http.log.autoconfiguration.core.HttpLogRepository;
import in.hocg.boot.http.log.autoconfiguration.jdbc.TableHttpLog;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.utils.db.DbUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class HttpLogRepositoryImpl implements HttpLogRepository {
    private final DataSource dataSource;


    @Override
    @SneakyThrows(SQLException.class)
    public HttpLog create(HttpLog entity) {
        Entity newEntity = Entity.create(TableHttpLog.TABLE_NAME)
            .setIgnoreNull(TableHttpLog.FIELD_URI, entity.getUri())
            .setIgnoreNull(TableHttpLog.FIELD_REQUEST_METHOD, entity.getRequestMethod())
            .setIgnoreNull(TableHttpLog.FIELD_REQUEST_BODY, entity.getRequestBody())
            .setIgnoreNull(TableHttpLog.FIELD_REQUEST_HEADERS, entity.getRequestHeaders())
            .setIgnoreNull(TableHttpLog.FIELD_RESPONSE_HEADERS, entity.getResponseHeaders())
            .setIgnoreNull(TableHttpLog.FIELD_RESPONSE_BODY, entity.getResponseBody())
            .setIgnoreNull(TableHttpLog.FIELD_TITLE, entity.getTitle())
            .setIgnoreNull(TableHttpLog.FIELD_CODE, entity.getCode())
            .setIgnoreNull(TableHttpLog.FIELD_REMARK, entity.getRemark())
            .setIgnoreNull(TableHttpLog.FIELD_FAIL_REASON, entity.getFailReason())
            .setIgnoreNull(TableHttpLog.FIELD_ATTACH, entity.getAttach())
            .setIgnoreNull(TableHttpLog.FIELD_BE_CALLER, entity.getBeCaller())
            .setIgnoreNull(TableHttpLog.FIELD_CALLER, entity.getCaller())
            .setIgnoreNull(TableHttpLog.FIELD_DONE_AT, entity.getDoneAt())
            .setIgnoreNull(TableHttpLog.FIELD_CREATED_AT, entity.getCreatedAt())
            .setIgnoreNull(TableHttpLog.FIELD_CREATOR, entity.getCreator())
            .setIgnoreNull(TableHttpLog.FIELD_STATUS, entity.getStatus())
            .setIgnoreNull(TableHttpLog.FIELD_DIRECTION, entity.getDirection());
        Long id = Db.use(dataSource).insertForGeneratedKey(newEntity);
        newEntity.set(TableHttpLog.FIELD_ID, id);
        return this.asHttpLog(newEntity);
    }

    @Override
    public HttpLog create(String title, String code, String remark, String attach,
                          String caller, String beCaller, String creator,
                          String direction, String uri, Map<String, String> headers, Object body) {
        HttpLog entity = new HttpLog();
        entity.setTitle(title);
        entity.setCode(code);
        entity.setRemark(remark);
        entity.setAttach(attach);
        entity.setCaller(caller);
        entity.setBeCaller(beCaller);
        entity.setCreator(creator);
        entity.setDirection(direction);
        entity.setUri(uri);
        entity.setCreatedAt(LocalDateTime.now());

        if (Objects.nonNull(headers)) {
            entity.setRequestHeaders(JSONUtil.toJsonStr(headers));
        }

        if (Objects.nonNull(body)) {
            String responseBody;
            if (ClassUtil.isBasicType(body.getClass())) {
                responseBody = String.valueOf(body);
            } else {
                responseBody = JSONUtil.toJsonStr(body);
            }
            entity.setResponseBody(responseBody);
        }

        return create(entity);
    }

    @Override
    @SneakyThrows(SQLException.class)
    public void updateById(Long logId, String status, String failReason, String responseBody, String responseHeaders) {
        Entity update = Entity.create(TableHttpLog.TABLE_NAME)
            .set(TableHttpLog.FIELD_STATUS, status)
            .set(TableHttpLog.FIELD_FAIL_REASON, failReason)
            .set(TableHttpLog.FIELD_RESPONSE_BODY, responseBody)
            .set(TableHttpLog.FIELD_RESPONSE_HEADERS, responseHeaders)
            .set(TableHttpLog.FIELD_DONE_AT, LocalDateTime.now());

        Entity where = Entity.create(TableHttpLog.TABLE_NAME)
            .set(TableHttpLog.FIELD_ID, logId)
            .set(TableHttpLog.FIELD_STATUS, TableHttpLog.Status.Executing.getCode());
        Db.use(dataSource).update(update, where);
    }

    private HttpLog asHttpLog(Entity entity) {
        HttpLog result = new HttpLog();
        result.setId(entity.getLong(TableHttpLog.FIELD_ID));
        result.setUri(entity.getStr(TableHttpLog.FIELD_URI));
        result.setRequestMethod(entity.getStr(TableHttpLog.FIELD_REQUEST_METHOD));
        result.setRequestBody(entity.getStr(TableHttpLog.FIELD_REQUEST_BODY));
        result.setRequestHeaders(entity.getStr(TableHttpLog.FIELD_REQUEST_HEADERS));
        result.setResponseHeaders(entity.getStr(TableHttpLog.FIELD_RESPONSE_HEADERS));
        result.setResponseBody(entity.getStr(TableHttpLog.FIELD_RESPONSE_BODY));
        result.setTitle(entity.getStr(TableHttpLog.FIELD_TITLE));
        result.setCode(entity.getStr(TableHttpLog.FIELD_CODE));
        result.setRemark(entity.getStr(TableHttpLog.FIELD_REMARK));
        result.setAttach(entity.getStr(TableHttpLog.FIELD_ATTACH));
        result.setDirection(entity.getStr(TableHttpLog.FIELD_DIRECTION));
        result.setBeCaller(entity.getStr(TableHttpLog.FIELD_BE_CALLER));
        result.setCaller(entity.getStr(TableHttpLog.FIELD_CALLER));
        result.setFailReason(entity.getStr(TableHttpLog.FIELD_FAIL_REASON));
        result.setStatus(entity.getStr(TableHttpLog.FIELD_STATUS));
        result.setCreator(entity.getStr(TableHttpLog.FIELD_CREATOR));
        result.setDoneAt(LangUtils.callIfNotNull(entity.getStr(TableHttpLog.FIELD_DONE_AT), DbUtils::asLocalDateTime).orElse(null));
        result.setCreatedAt(LangUtils.callIfNotNull(entity.getStr(TableHttpLog.FIELD_CREATED_AT), DbUtils::asLocalDateTime).orElse(null));
        return result;
    }
}
