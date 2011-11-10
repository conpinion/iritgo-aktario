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

package de.iritgo.aktario.betwixt.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import java.util.Properties;


/**
 * This command load all Dataobjects into the baseregistry
 */
public class LoadBaseObjects extends Command
{
	private String path;

	public LoadBaseObjects()
	{
		super("loadbaseobjects");
		path = Engine.instance().getSystemDir() + Engine.instance().getFileSeparator() + "data"
						+ Engine.instance().getFileSeparator();
	}

	public LoadBaseObjects(String path)
	{
		this();
		this.path = path;
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	@Override
	public void setProperties(Properties properties)
	{
		path = (String) properties.get("path");

		if (path == null)
		{
			path = Engine.instance().getSystemDir() + Engine.instance().getFileSeparator();
		}
	}

	@Override
	public void perform()
	{
		// 		BeanWriter writer = null;
		// 		BaseRegistry baseRegistry = Engine.instance ().getBaseRegistry ();
		// 		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		// 		final File dataDir = new File(path);
		// 		File[] dataFiles =
		// 			dataDir.listFiles (
		// 				new FilenameFilter()
		// 				{
		// 					public boolean accept (File dir, String name)
		// 					{
		// 						return true;
		// 					}
		// 				});
		// 		if (dataFiles == null)
		// 		{
		// 			Log.log ("persist", "LoadBasicObjects", "No files to load in: " + path, Log.WARN);
		// 			return;
		// 		}
		// 		for (int i = 0; i < dataFiles.length; ++i)
		// 		{
		// 			String filename = dataFiles[i].getName ();
		// 			BufferedReader xmlReader = null;
		// 			try
		// 			{
		// 				xmlReader =
		// 					new BufferedReader(new InputStreamReader(new FileInputStream(path + filename)));
		// 				BeanReader beanReader = new BeanReader();
		// 				beanReader.getXMLIntrospector ().setAttributesForPrimitives (true);
		// 				beanReader.getBindingConfiguration ().setMapIDs (false);
		// 				beanReader.registerBeanClass (BaseObjectMapping.class);
		// 				beanReader.registerBeanClass (User.class);
		// 				BaseObjectMapping baseObjectMapping = null;
		// 				Object object = (Object) beanReader.parse (xmlReader);
		// 				if (object == null)
		// 				{
		// 					continue;
		// 				}
		// 				if (object instanceof User)
		// 				{
		// 					User user = (User) object;
		// 					Server.instance ().getUserRegistry ().addUser ((User) object);
		// 					Engine.instance ().getProxyRegistry ().addProxy (new FrameworkProxy(user));
		// 					baseRegistry.add (user);
		// 					xmlReader.close ();
		// 					continue;
		// 				}
		// 				baseObjectMapping = (BaseObjectMapping) object;
		// 				DataObject dataObject = null;
		// 				try
		// 				{
		// 					dataObject =
		// 						(DataObject) Engine.instance ().getIObjectFactory ().newInstance (
		// 							baseObjectMapping.getId ());
		// 					dataObject.setUniqueId (baseObjectMapping.getUniqueId ());
		// 				}
		// 				catch (NoSuchIObjectException x)
		// 				{
		// 					// TODO: This is a hardcore bug! You have forgotten to register the DataObject!
		// 					Log.log (
		// 						"persist", "LoadBasicObects.perform",
		// 						"DataObject not registred: " + baseObjectMapping.getId (), Log.FATAL);
		// 					continue;
		// 				}
		// 				for (Iterator j = baseObjectMapping.getIntegers ().iterator (); j.hasNext ();)
		// 				{
		// 					Context context = (Context) j.next ();
		// 					dataObject.putAttribute (context.getId (), new Integer(context.getString ()));
		// 				}
		// 				for (Iterator j = baseObjectMapping.getLongs ().iterator (); j.hasNext ();)
		// 				{
		// 					Context context = (Context) j.next ();
		// 					dataObject.putAttribute (context.getId (), new Long(context.getString ()));
		// 				}
		// 				for (Iterator j = baseObjectMapping.getDoubles ().iterator (); j.hasNext ();)
		// 				{
		// 					Context context = (Context) j.next ();
		// 					dataObject.putAttribute (context.getId (), new Double(context.getString ()));
		// 				}
		// 				for (Iterator j = baseObjectMapping.getStrings ().iterator (); j.hasNext ();)
		// 				{
		// 					Context context = (Context) j.next ();
		// 					dataObject.putAttribute (context.getId (), context.getString ());
		// 				}
		// 				baseRegistry.add (dataObject);
		// 				Engine.instance ().getProxyRegistry ().addProxy (new FrameworkProxy(dataObject));
		// 			}
		// 			catch (Exception x)
		// 			{
		// 				x.printStackTrace ();
		// 			}
		// 			try
		// 			{
		// 				xmlReader.close ();
		// 			}
		// 			catch (Exception x)
		// 			{
		// 				x.printStackTrace ();
		// 			}
		// 		}
		// 		// Load the ProxyLinkedLists
		// 		for (int i = 0; i < dataFiles.length; ++i)
		// 		{
		// 			String filename = dataFiles[i].getName ();
		// 			BufferedReader xmlReader = null;
		// 			try
		// 			{
		// 				xmlReader =
		// 					new BufferedReader(new InputStreamReader(new FileInputStream(path + filename)));
		// 				BeanReader beanReader = new BeanReader();
		// 				beanReader.getXMLIntrospector ().setAttributesForPrimitives (true);
		// 				beanReader.getBindingConfiguration ().setMapIDs (false);
		// 				beanReader.registerBeanClass ("BaseObjectMapping", BaseObjectMapping.class);
		// 				beanReader.registerBeanClass ("User", User.class);
		// 				BaseObjectMapping baseObjectMapping = null;
		// 				Object object = (Object) beanReader.parse (xmlReader);
		// 				if (object == null)
		// 				{
		// 					continue;
		// 				}
		// 				if (object instanceof User)
		// 				{
		// 					continue;
		// 				}
		// 				baseObjectMapping = (BaseObjectMapping) object;
		// // 				DataObject dataObject =
		// // 					(DataObject) baseRegistry.get (
		// // 						new Long(baseObjectMapping.getUniqueId ()).longValue (), "none");
		// // 				for (Iterator j = baseObjectMapping.getProxyLinkedLists ().iterator ();
		// // 					j.hasNext ();)
		// // 				{
		// // 					Context context = (Context) j.next ();
		// // 					IObject owner =
		// // 						(IObject) baseRegistry.get (
		// // 							new Long(baseObjectMapping.getUniqueId ()).longValue (), "none");
		// // 					List list = (List) context.getChilds ();
		// // 					if (list.size () == 0)
		// // 					{
		// // 						xmlReader.close ();
		// // 						continue;
		// // 					}
		// // 					Context childContext = (Context) list.get (0);
		// // 					if (childContext == null)
		// // 					{
		// // 						continue;
		// // 					}
		// // 					IObjectList proxyLinkedList =
		// // 						dataObject.getIObjectListAttribute (context.getProxyLinkedListKey ());
		// // 					for (Iterator k = context.getChilds ().iterator (); k.hasNext ();)
		// // 					{
		// // 						childContext = (Context) k.next ();
		// // 						proxyLinkedList.add (
		// // 							baseRegistry.get (new Long(childContext.getString ()).longValue (), "none"));
		// // 					}
		// // 					//					dataObject.putAttribute (context.getProxyLinkedListKey (), proxyLinkedList);
		// 				}
		// 			}
		// 			catch (Exception x)
		// 			{
		// 				x.printStackTrace ();
		// 			}
		// 			try
		// 			{
		// 				xmlReader.close ();
		// 			}
		// 			catch (Exception x)
		// 			{
		// 			}
		// 		}
	}
}
