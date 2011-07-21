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


import java.util.ArrayList;
import java.util.List;


public class Configuration
{
	private NetworkConfig network = new NetworkConfig ();

	private ThreadPoolConfig threadPool = new ThreadPoolConfig ();

	private List<DatasourceConfig> dataSources = new ArrayList<DatasourceConfig> ();

	public List<DatasourceConfig> getDataSources ()
	{
		return dataSources;
	}

	public void addDataSource (DatasourceConfig dataSource)
	{
		dataSources.add (dataSource);
	}

	public NetworkConfig getNetwork ()
	{
		return network;
	}

	public void setNetwork (NetworkConfig network)
	{
		this.network = network;
	}

	public ThreadPoolConfig getThreadPool ()
	{
		return threadPool;
	}

	public void setThreadPool (ThreadPoolConfig threadPool)
	{
		this.threadPool = threadPool;
	}
}
