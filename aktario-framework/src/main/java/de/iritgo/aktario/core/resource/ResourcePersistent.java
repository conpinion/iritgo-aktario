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

package de.iritgo.aktario.core.resource;


/**
 *
 */
public class ResourcePersistent extends ResourceNode
{
	private Object resourcePath;

	private ResourceLoader resourceLoader;

	/**
	 * Standard constructor
	 *
	 * @param name The description of the resource.
	 * @param nodeName The node Name
	 */
	public ResourcePersistent(String name, String nodeName, Object resourcePath)
	{
		super(name, nodeName);
		this.resourcePath = resourcePath;
	}

	/**
	 * Set the ResourceLoader.
	 *
	 * @param resourceLoader The resource loader.
	 */
	public void setResourceLoader(ResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Get the ResourceLoader.
	 *
	 * @return The ResourceLoader.
	 */
	public ResourceLoader getResourceLoader()
	{
		return resourceLoader;
	}

	/**
	 * Get the ResourcePath.
	 *
	 * @return The ResourcePath.
	 */
	public Object getResourcePath()
	{
		return resourcePath;
	}
}
