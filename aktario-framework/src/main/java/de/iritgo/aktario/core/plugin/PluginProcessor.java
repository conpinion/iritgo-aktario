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


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;


/**
 * PluginProcessor, very bad code, and very unoptimized, please rewrite this!
 */
public class PluginProcessor
{
	public static int FORWARD = 0;

	public static int BACKWARD = 1;

	private List plugins;

	private List initedPlugins;

	private List sortedPlugins;

	public PluginProcessor (List plugins)
	{
		this.plugins = plugins;
		initedPlugins = new LinkedList ();
	}

	public void doPlugins (PluginProcess process, int direction)
	{
		sortedPlugins = generateSortedList ();

		if (direction == FORWARD)
		{
			Iterator i = sortedPlugins.iterator ();

			while (i.hasNext ())
			{
				doPlugin (process, (PluginContext) i.next ());
			}
		}
		else
		{
			ListIterator i = sortedPlugins.listIterator (sortedPlugins.size ());

			while (i.hasPrevious ())
			{
				doPlugin (process, (PluginContext) i.previous ());
			}
		}
	}

	public void doPlugin (PluginProcess process, PluginContext pluginContext)
	{
		Thread.currentThread ().setContextClassLoader (pluginContext.getClassLoader ());

		Plugin plugin = pluginContext.getPlugin ();

		process.doPlugin (plugin);
		initedPlugins.add (plugin.getName ());

		ClassLoader parentloader = pluginContext.getClassLoader ().getParent ();

		Thread.currentThread ().setContextClassLoader (parentloader);
	}

	public LinkedList generateSortedList ()
	{
		int lastDiv = - 1;

		initedPlugins.add ("FIRST");

		int pluginsSize = plugins.size () + 1;
		LinkedList sortedList = new LinkedList ();

		while (true)
		{
			if (lastDiv == (pluginsSize - (initedPlugins.size ())))
			{
				initedPlugins.add ("LAST");
			}

			lastDiv = pluginsSize - (initedPlugins.size ());

			if (pluginsSize == initedPlugins.size ())
			{
				return sortedList;
			}

			Iterator i = plugins.iterator ();

			while (i.hasNext ())
			{
				PluginContext pluginContext = (PluginContext) i.next ();

				Thread.currentThread ().setContextClassLoader (pluginContext.getClassLoader ());

				Plugin plugin = pluginContext.getPlugin ();

				if (! checkIsDependencyOK (plugin.getDependency ()))
				{
					continue;
				}

				// True if plugin allready initialized...
				if (initedPlugins.contains (plugin.getName ()))
				{
					continue;
				}

				sortedList.add (pluginContext);
				initedPlugins.add (plugin.getName ());

				ClassLoader parentloader = pluginContext.getClassLoader ().getParent ();

				Thread.currentThread ().setContextClassLoader (parentloader);
			}
		}
	}

	public boolean checkIsDependencyOK (String dependency)
	{
		StringTokenizer st = new StringTokenizer (dependency, ",");

		while (st.hasMoreTokens ())
		{
			if (! initedPlugins.contains (st.nextToken ()))
			{
				return false;
			}
		}

		return true;
	}
}
