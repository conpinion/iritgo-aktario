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

package de.iritgo.aktario.core.plugin;


import de.iritgo.aktario.core.base.BaseObject;


/**
 * Environmnent information for each plugin.
 */
public class PluginContext extends BaseObject
{
	/** The class loader for the plugin. */
	private ClassLoader classLoader;

	/** The plugin itself. */
	private Plugin plugin;

	/**
	 * Create a new PluginContext.
	 *
	 * @param plugin The plugin.
	 * @param classLoader The class loader.
	 */
	public PluginContext(Plugin plugin, ClassLoader classLoader)
	{
		this.plugin = plugin;
		this.classLoader = classLoader;
	}

	/**
	 * Get the class loader.
	 *
	 * @return The class loader.
	 */
	public ClassLoader getClassLoader()
	{
		return classLoader;
	}

	/**
	 * Get the plugin.
	 *
	 * @return The plugin.
	 */
	public Plugin getPlugin()
	{
		return plugin;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PluginContext@" + this.hashCode() + "[" + plugin.getName() + "]";
	}
}
