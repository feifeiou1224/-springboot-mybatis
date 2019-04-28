package com.oyf.dto;

import lombok.*;

import java.util.Date;

/**
 * Create Time: 2019年04月01日 16:36
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysLogDto {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private Date fromTime;

    private Date toTime;

}
