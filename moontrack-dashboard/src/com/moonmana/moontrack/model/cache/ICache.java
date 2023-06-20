package com.moonmana.moontrack.model.cache;

import java.util.Date;

public interface ICache {

	Date getRefreshDate();

	void setRefreshDate(Date refreshDate);

}