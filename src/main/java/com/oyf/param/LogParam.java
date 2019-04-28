package com.oyf.param;

import lombok.*;

/**
 * Create Time: 2019年04月01日 11:20
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogParam {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private String fromTime;

    private String toTime;


}
