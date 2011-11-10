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

package de.iritgo.aktario.framework.user.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import java.io.IOException;


/**
 *
 */
public class RegisterNewUserFailureAction extends FrameworkAction
{
	static public int userNAME_INUSE;

	static public int EMAIL_INUSE;

	private int failure;

	/**
	 * Standard constructor
	 */
	public RegisterNewUserFailureAction()
	{
	}

	/**
	 * Standard constructor
	 */
	public RegisterNewUserFailureAction(int failure)
	{
		this.failure = failure;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		failure = stream.readInt();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeInt(failure);
	}

	/**
	 * Perform the action.
	 */
	public void perform()
	{
		Engine.instance().getFlowControl().ruleFailure("userregisted");
	}
}
