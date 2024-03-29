package com.eying.pcss.workflow.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by
 */
@Data
@MappedSuperclass
public abstract class IdEntity {
    //    @Id
//    @GenericGenerator(name="idGenerator", strategy="uuid")
//    @GeneratedValue(generator="idGenerator")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 50)
    private Long id;
    @CreatedDate
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @LastModifiedDate
    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime updatedTime;
}
