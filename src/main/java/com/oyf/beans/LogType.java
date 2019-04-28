package com.oyf.beans;

import lombok.Getter;

/**
 * Create Time: 2019年04月01日 11:26
 * Create Author: 欧阳飞
 **/

@Getter
public enum LogType {

    TYPE_DEPT(1),
    TYPE_USER(2),
    TYPE_ACL_MODULE(3),
    TYPE_ACL(4),
    TYPE_ROLE(5),
    TYPE_ROLE_ACL(6),
    TYPE_ROLE_USER(7);

    private  int type;

    LogType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static LogType getByValue(int value) {
        for (LogType logType : values()) {
            if (logType.getType() == value) {
                return logType;
            }
        }
        return null;
    }
}
