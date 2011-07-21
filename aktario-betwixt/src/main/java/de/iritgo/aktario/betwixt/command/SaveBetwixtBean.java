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
import de.iritgo.aktario.framework.base.DataObject;
import org.apache.commons.betwixt.io.BeanWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * This is a simple command to save a bean with betwixt.
 */
public class SaveBetwixtBean extends Command
{
	private Object bean;

	private String path;

	private String filename;

	public SaveBetwixtBean ()
	{
		super ("savebetwixtbean");
	}

	public SaveBetwixtBean (Object bean)
	{
		this (bean, Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator (), bean.getClass ()
						.getName ());
	}

	public SaveBetwixtBean (Object bean, String path)
	{
		this (bean, path, bean.getClass ().getName ());
	}

	public SaveBetwixtBean (Object bean, String path, String filename)
	{
		this.bean = bean;
		this.path = path;
		this.filename = filename;
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	@Override
	public void setProperties (Properties properties)
	{
		bean = (Object) properties.get ("bean");

		path = (String) properties.get ("path");

		if (path == null)
		{
			path = Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator ();
		}

		filename = (String) properties.get ("filename");

		if (filename == null)
		{
			filename = bean.getClass ().getName ();
		}
	}

	@Override
	public void perform ()
	{
		BeanWriter writer = null;

		try
		{
			writer = new BeanWriter (new BufferedOutputStream (new FileOutputStream (path + filename)));
			writer.getXMLIntrospector ().setAttributesForPrimitives (true);
			writer.enablePrettyPrint ();
			//			writer.getBindingConfiguration ().setMapIDs (false);
			writer.write ((DataObject) bean);
		}
		catch (Exception x)
		{
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
