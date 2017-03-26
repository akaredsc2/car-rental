package org.vitaly.dao.abstraction;

import org.vitaly.model.location.Location;

/**
 * Created by vitaly on 2017-03-26.
 */
public interface LocationDao extends AbstractDao<Location> {
    int getLocationCount();
}
