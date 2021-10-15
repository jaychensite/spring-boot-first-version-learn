package org.springframework.learn.tx;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

public class MsgConsumerRecordDaoImpl implements MsgConsumerRecordDao {

    @Override
    public void consumeSuccess() {
        this.jdbcTemplate.update("insert into  msg_consumer_record values(?,?) ",
                1, 10);
    }

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
