/**
 * This file is part of the Iritgo/Aktario Framework.
 *
 * Copyright (C) 2005-2011 Iritgo Technologies.
 * Copyright (C) 2003-2005 BueroByte GbR.
 *
 * Iritgo licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iritgo.aktario.core.config;


public class DatasourceConfig
{
	private String id;

	private String dataSourceClass = "org.apache.commons.dbcp.BasicDataSource";

	private String driverClass;

	private String user;

	private String password;

	private String url;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDataSourceClass()
	{
		return dataSourceClass;
	}

	public void setDataSourceClass(String dataSourceClass)
	{
		this.dataSourceClass = dataSourceClass;
	}

	public String getDriverClass()
	{
		return driverClass;
	}

	public void setDriverClass(String driverClass)
	{
		this.driverClass = driverClass;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
}
