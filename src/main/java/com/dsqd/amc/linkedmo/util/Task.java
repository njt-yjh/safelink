package com.dsqd.amc.linkedmo.util;

import java.util.Map;

public interface Task {
    void executeTask(Map<String, Object> params, int triggerId);

}