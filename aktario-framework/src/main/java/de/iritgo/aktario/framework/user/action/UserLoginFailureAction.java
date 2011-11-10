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
public class UserLoginFailureAction extends FrameworkAction
{
	/** Failure type if the username or password was wrong. */
	static public int BAD_USERNAME_OR_PASSWORD = 1;

	/** Failure type if the user is already logged in. */
	static public int USER_ALREADY_ONLINE = 2;

	/** Failure type if the user is not allowed to login. */
	static public int LOGIN_NOT_ALLOWED = 3;

	/** Failure type. */
	private int failure;

	/**
	 * Create a new UserLoginFailureAction.
	 */
	public UserLoginFailureAction()
	{
	}

	/**
	 * Create a new UserLoginFailureAction.
	 *
	 * @param failure The failure type.
	 */
	public UserLoginFailureAction(int failure)
	{
		this.failure = failure;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		failure = stream.readInt();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeInt(failure);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		Engine.instance().getFlowControl().ruleFailure("UserLogin", new Integer(failure));
	}

	/**
	 * Get the failure type.
	 *
	 * @return The failure type.
	 */
	public int getFailure()
	{
		return failure;
	}
}
