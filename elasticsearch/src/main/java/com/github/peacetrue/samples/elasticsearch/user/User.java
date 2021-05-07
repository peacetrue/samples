package com.github.peacetrue.samples.elasticsearch.user;

import com.github.peacetrue.core.CreateModify;
import com.github.peacetrue.util.DateTimeFormatterUtils;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author xiayx
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
@Document(indexName = "user-#{T(com.github.peacetrue.samples.elasticsearch.user.User).now()}")
public class User implements Serializable, CreateModify<Long> {

    private static final long serialVersionUID = 0L;

    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatterUtils.SHORT_DATE_TIME);
    }

    /** 主键 */
    @Id
    @Field(type = FieldType.Keyword, fielddata = true)
    private String id;
    /** 用户名 */
    @Field(type = FieldType.Keyword)
    private String username;
    /** 密码 */
    @Field(type = FieldType.Keyword)
    private String password;
    /** 创建者主键 */
    @Field(type = FieldType.Long)
    private Long creatorId;
    /** 创建时间 */
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private LocalDateTime createdTime;
    /** 修改者主键 */
    @Field(type = FieldType.Long)
    private Long modifierId;
    /** 最近修改时间 */
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private LocalDateTime modifiedTime;

}
