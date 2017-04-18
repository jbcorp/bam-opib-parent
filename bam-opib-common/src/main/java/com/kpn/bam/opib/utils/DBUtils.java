package com.kpn.bam.opib.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.google.common.collect.Lists;

public class DBUtils {
	

	public static Connection getConnectionByJNDI(String datasourceJNDI) throws NamingException, SQLException {

		Context context = null;
		DataSource dataSource = null;
		Connection connection = null;
		
		context = new InitialContext();
		dataSource = (DataSource) context.lookup(datasourceJNDI);
		connection = dataSource.getConnection();
		System.out.println("Got Connection from Datasource -> JNDI " + datasourceJNDI + " " + connection);
		return connection;
	}

	// Oracle have limit of 1000 items within IN clause
	public static List<String> getQueryInputBatches(List<String> paramList) {
		List<String> queryStringList = new ArrayList<String>();

		List<List<String>> splitedListOfOrderIdList = Lists.partition(paramList, 950);
		for (List<String> batch : splitedListOfOrderIdList) {
			String queryString = getQueryString(batch);
			queryStringList.add(queryString);
		}
		return queryStringList;
	}

	public static String getQueryString(List<String> batch) {
		String queryString = " ";
		if (!batch.isEmpty()) {
			for (String parameter : batch) {
				queryString = queryString + " '" + parameter + "' , ";
			}
			queryString = queryString.substring(0, queryString.lastIndexOf(","));
		}

		return queryString;
	}

}
