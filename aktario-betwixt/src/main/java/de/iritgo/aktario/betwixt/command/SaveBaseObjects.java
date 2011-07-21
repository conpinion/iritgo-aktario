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


import de.iritgo.aktario.betwixt.baseobjectsave.BaseObjectMapping;
import de.iritgo.aktario.betwixt.baseobjectsave.Context;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.base.BaseRegistry;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import org.apache.commons.betwixt.io.BeanWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;


/**
 * This command save the user and all objects in the baseregistry, but this is a fast simple way to do this. Later we need
 * a refactoring of this part.
 */
public class SaveBaseObjects extends Command
{
	private String path;

	public SaveBaseObjects ()
	{
		super ("savebaseobjects");
		path = Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator () + "data"
						+ Engine.instance ().getFileSeparator ();
	}

	public SaveBaseObjects (String path)
	{
		this ();
		this.path = path;
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	@Override
	public void setProperties (Properties properties)
	{
		path = (String) properties.get ("path");

		if (path == null)
		{
			path = Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator ();
		}
	}

	@Override
	public void perform ()
	{
		BeanWriter writer = null;

		BaseRegistry baseRegistry = Engine.instance ().getBaseRegistry ();
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			try
			{
				User user = (User) i.next ();

				writer = new BeanWriter (new BufferedOutputStream (new FileOutputStream (path + user.getTypeId () + "."
								+ user.getUniqueId ())));

				writer.getXMLIntrospector ().setAttributesForPrimitives (true);
				writer.enablePrettyPrint ();
				//				writer.getBindingConfiguration ().setMapIDs (false);
				writer.write (user);
				writer.close ();
			}
			catch (Exception x)
			{
				x.printStackTrace ();
			}
		}

		for (Iterator i = baseRegistry.iterator (); i.hasNext ();)
		{
			try
			{
				BaseObject baseObject = (BaseObject) i.next ();

				if (baseObject instanceof DataObject)
				{
					DataObject dataObject = (DataObject) baseObject;

					writer = new BeanWriter (new BufferedOutputStream (new FileOutputStream (path
									+ baseObject.getTypeId () + "." + baseObject.getUniqueId ())));
					writer.getXMLIntrospector ().setAttributesForPrimitives (true);
					writer.enablePrettyPrint ();

					//					writer.getBindingConfiguration ().setMapIDs (false);
					BaseObjectMapping save = new BaseObjectMapping ();

					for (Iterator j = dataObject.getAttributes ().keySet ().iterator (); j.hasNext ();)
					{
						String key = (String) j.next ();
						Object object = dataObject.getAttribute (key);

						// SCHEISSE (SHIT) (BULLSHIT)
						if (object instanceof Integer)
						{
							save.addInteger (new Context (key, String.valueOf (object)));
						}

						if (object instanceof Long)
						{
							save.addLong (new Context (key, String.valueOf (object)));
						}

						if (object instanceof Double)
						{
							save.addDouble (new Context (key, String.valueOf (object)));
						}

						if (object instanceof String)
						{
							save.addString (new Context (key, (String) object));
						}

						if (object instanceof IObjectList)
						{
							IObjectList proxyLinkedList = (IObjectList) object;
							Context context = new Context (key, proxyLinkedList.getOwner ().getTypeId (), String
											.valueOf (proxyLinkedList.getOwner ().getUniqueId ()));

							for (Iterator l = proxyLinkedList.iterator (); l.hasNext ();)
							{
								BaseObject pObject = (BaseObject) l.next ();

								context.addChild (new Context (pObject.getTypeId (), String.valueOf (pObject
												.getUniqueId ())));
							}

							save.addProxyLinkedList (context);
						}

						if (object instanceof DataObject)
						{
							save.addDataObject (new Context (key, String.valueOf (object)));
						}
					}

					save.setId (dataObject.getTypeId ());
					save.setUniqueId (dataObject.getUniqueId ());

					writer.write (save);
				}
			}
			catch (Exception x)
			{
				x.printStackTrace ();
			}

			try
			{
				writer.close ();
			}
			catch (Exception x)
			{
			}
		}
	}
}
