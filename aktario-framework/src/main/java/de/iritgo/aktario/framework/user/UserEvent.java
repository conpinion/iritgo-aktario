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

package de.iritgo.aktario.framework.user;


import de.iritgo.aktario.core.event.Event;


/**
 * UserEvent, contains the user object.
 */
public class UserEvent implements Event
{
	public static int USER_UNKNOWN_STATE = 0;

	public static int USER_LOGGED_IN = 1;

	public static int USER_LOGGED_OUT = 2;

	private User user;

	private int logged;

	private String plainPassword;

	/**
	 * Standard constructor
	 *
	 * @param user The userobject.
	 */
	public UserEvent (User user)
	{
		this.user = user;
	}

	/**
	 * Standard constructor
	 *
	 * @param user The userobject.
	 */
	public UserEvent (User user, int logged)
	{
		this.user = user;
		this.logged = logged;
	}

	/**
	 *
	 */
	public UserEvent (User user, int logged, String plainPassword)
	{
		this (user, logged);
		this.plainPassword = plainPassword;
	}

	/**
	 * @return The plainPassword.
	 */
	public String getPlainPassword ()
	{
		return plainPassword;
	}

	public boolean isLoggedIn ()
	{
		return logged == USER_LOGGED_IN;
	}

	public boolean isLoggedOut ()
	{
		return logged == USER_LOGGED_OUT;
	}

	public User getUser ()
	{
		return user;
	}
}
