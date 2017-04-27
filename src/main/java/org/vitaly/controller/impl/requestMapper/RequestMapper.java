package org.vitaly.controller.impl.requestMapper;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vitaly on 2017-04-27.
 */
public interface RequestMapper<T> {
    T map(HttpServletRequest request);
}
