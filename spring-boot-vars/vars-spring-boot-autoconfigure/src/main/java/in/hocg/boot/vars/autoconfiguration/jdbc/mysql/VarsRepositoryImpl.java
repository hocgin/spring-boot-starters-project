package in.hocg.boot.vars.autoconfiguration.jdbc.mysql;

import in.hocg.boot.vars.autoconfiguration.core.VarsRepository;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

/**
 * Created by hocgin on 2021/6/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class VarsRepositoryImpl implements VarsRepository {
    private final DataSource dataSource;
}
