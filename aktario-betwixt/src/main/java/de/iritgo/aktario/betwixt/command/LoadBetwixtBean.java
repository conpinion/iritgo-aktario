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
import org.apache.commons.betwixt.io.BeanReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * This is a simple command to load a bean with betwixt.
 */
public class LoadBetwixtBean extends Command
{
	private Object bean;

	private String path;

	private String filename;

	public LoadBetwixtBean()
	{
		super("loadbetwixtbean");
	}

	public LoadBetwixtBean(Object bean)
	{
		this(bean, Engine.instance().getSystemDir() + Engine.instance().getFileSeparator(), bean.getClass().getName());
	}

	public LoadBetwixtBean(Object bean, String path)
	{
		this(bean, path, bean.getClass().getName());
	}

	public LoadBetwixtBean(Object bean, String path, String filename)
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
	public void setProperties(Properties properties)
	{
		super.setProperties(properties);

		bean = (Object) properties.get("bean");

		path = (String) properties.get("path");

		if (path == null)
		{
			path = Engine.instance().getSystemDir() + Engine.instance().getFileSeparator();
		}

		filename = (String) properties.get("filename");

		if (filename == null)
		{
			filename = bean.getClass().getName();
		}
	}

	@Override
	public void perform()
	{
		BufferedReader xmlReader = null;

		try
		{
			xmlReader = new BufferedReader(new InputStreamReader(new FileInputStream(path + filename)));

			BeanReader beanReader = new BeanReader();

			beanReader.getXMLIntrospector().setAttributesForPrimitives(true);
			//			beanReader.getBindingConfiguration ().setMapIDs (false);
			beanReader.registerBeanClass(bean.getClass());
			properties.put("bean", beanReader.parse(xmlReader));
		}
		catch (Exception x)
		{
		}

		try
		{
			xmlReader.close();
		}
		catch (Exception x)
		{
		}
	}
}
