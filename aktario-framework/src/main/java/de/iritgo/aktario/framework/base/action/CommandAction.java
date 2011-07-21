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

package de.iritgo.aktario.framework.base.action;


import de.iritgo.aktario.framework.command.CommandTools;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;


/**
 *
 */
public class CommandAction extends FrameworkAction
{
	private Properties properties;

	private String command;

	/**
	 * Standard constructor
	 */
	public CommandAction ()
	{
	}

	/**
	 * Standard constructor
	 */
	public CommandAction (String command, Properties properties)
	{
		this.properties = properties;
		this.command = command;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		command = stream.readUTF ();

		ObjectInputStream s = new ObjectInputStream (stream);

		properties = (Properties) s.readObject ();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF (command);

		ObjectOutputStream s = new ObjectOutputStream (stream);

		s.writeObject (properties);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		CommandTools.performSimple (command, properties);
	}
}
