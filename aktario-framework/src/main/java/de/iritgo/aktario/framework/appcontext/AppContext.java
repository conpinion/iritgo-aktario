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

package de.iritgo.aktario.framework.appcontext;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.tools.EasyHashMap;
import de.iritgo.aktario.framework.user.User;
import java.util.Locale;


/**
 *
 */
public class AppContext extends BaseObject
{
	static private AppContext appContext;

	/** I'm a server? */
	private boolean server;

	/** I'm a client? */
	private boolean client;

	/** The server IP/URL. */
	private String serverIP;

	/** The user */
	private User user;

	/** This objects syncs the CommandProcessor and the ActionProcessor. */
	private Object lockObject;

	/** Is the client connected with the Server? */
	private boolean connectedWithServer;

	/* The channel number. */
	private double channelNumber;

	/* A DataObject that can be used as an application entry point. */
	private IObject applicationObject;

	/** The current language. */
	private Locale locale;

	/** Custom user data (not used by the framework). */
	protected EasyHashMap data;

	private String userPassword;

	private String userName;

	private String appId;

	/**
	 * Standard constructor
	 */
	public AppContext ()
	{
		super ("appcontext");
		lockObject = new Object ();
		user = null;
		connectedWithServer = false;
		locale = new Locale ("de");
		data = new EasyHashMap ();
	}

	/**
	 * Set the channelnumber.
	 *
	 * @param channelNumber The channelNumber.
	 */
	public void setChannelNumber (double channelNumber)
	{
		this.channelNumber = channelNumber;
	}

	/**
	 * Get the channelnumber.
	 *
	 * @return channelnumber.
	 */
	public double getChannelNumber ()
	{
		return channelNumber;
	}

	/**
	 * Set the State of the Connection.
	 */
	public void setConnectionState (boolean connectedWithServer)
	{
		this.connectedWithServer = connectedWithServer;
	}

	/**
	 * Get the State of the Connection.
	 *
	 * @return state.
	 */
	public boolean isConnectedWithServer ()
	{
		return connectedWithServer;
	}

	/**
	 * Is the User logged in?
	 *
	 * @return loggedIn.
	 */
	public boolean isUserLoggedIn ()
	{
		if (user == null)
		{
			return false;
		}

		return user.isOnline ();
	}

	/**
	 * Set the ServerIP.
	 *
	 * @param serverIP The current serverIP.
	 */
	public void setServerIP (String serverIP)
	{
		this.serverIP = serverIP;
	}

	/**
	 * Get the ServerIP.
	 *
	 * @return The current serverIP.
	 */
	public String getServerIP ()
	{
		return serverIP;
	}

	/**
	 * Set the User.
	 *
	 * @param user The current user.
	 */
	public void setUser (User user)
	{
		this.user = user;
	}

	/**
	 * Get the User.
	 *
	 * @return The current user.
	 */
	public User getUser ()
	{
		return user;
	}

	/**
	 * Set the Server.
	 *
	 * @param server The current server.
	 */
	public void setServer (boolean server)
	{
		this.server = server;
	}

	/**
	 * Get the Server.
	 *
	 * @return The current server.
	 */
	public boolean getServer ()
	{
		return server;
	}

	/**
	 * Set the Client.
	 *
	 * @param client The current client.
	 */
	public void setClient (boolean client)
	{
		this.client = client;
	}

	/**
	 * Get the Client.
	 *
	 * @return The current client.
	 */
	public boolean getClient ()
	{
		return client;
	}

	/**
	 * Get the ApplicationDataObject.
	 *
	 * @param applicationObject The new ApplicationDataObject.
	 */
	public void setApplicationObject (IObject applicationObject)
	{
		this.applicationObject = applicationObject;
	}

	/**
	 * Set the ApplicationDataObject.
	 */
	public IObject getAppObject ()
	{
		return applicationObject;
	}

	public synchronized Object getLockObject ()
	{
		return lockObject;
	}

	static public AppContext instance ()
	{
		if (appContext != null)
		{
			return appContext;
		}

		appContext = new AppContext ();

		return appContext;
	}

	/**
	 * Get the current language.
	 *
	 * @return The current language.
	 */
	public Locale getLocale ()
	{
		return locale;
	}

	/**
	 * Set the current language.
	 *
	 * @param locale The new language.
	 */
	public void setLocale (Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * Put an integer attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put (String key, int value)
	{
		data.put (key, value);
	}

	/**
	 * Put a long attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put (String key, long value)
	{
		data.put (key, value);
	}

	/**
	 * Put a float attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put (String key, float value)
	{
		data.put (key, value);
	}

	/**
	 * Put a double attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put (String key, double value)
	{
		data.put (key, value);
	}

	/**
	 * Put a boolean attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put (String key, boolean value)
	{
		data.put (key, value);
	}

	/**
	 * Put a string attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put (String key, String value)
	{
		data.put (key, value);
	}

	/**
	 * Put an object attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put (String key, Object value)
	{
		data.put (key, value);
	}

	/**
	 * Get an integer attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public int getInt (String key)
	{
		return data.getInt (key);
	}

	/**
	 * Get a long attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public long getLong (String key)
	{
		return data.getLong (key);
	}

	/**
	 * Get a float attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public float getFloat (String key)
	{
		return data.getLong (key);
	}

	/**
	 * Get a double attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public double getDouble (String key)
	{
		return data.getLong (key);
	}

	/**
	 * Get a boolean attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public boolean getBoolean (String key)
	{
		return data.getBoolean (key);
	}

	/**
	 * Get a string attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public String getString (String key)
	{
		return data.getString (key);
	}

	/**
	 * Get an object attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public Object getObject (String key)
	{
		return data.getObject (key);
	}

	/**
	 * Remove an attribute.
	 *
	 * @param key The attribute key.
	 */
	public void remove (String key)
	{
		data.remove (key);
	}

	/**
	 * Save the current decoded user password
	 *
	 * @param password Password
	 */
	public void setUserPassword (String password)
	{
		this.userPassword = password;
	}

	/**
	 * Get the current decoded user password
	 *
	 * @return The user password
	 */
	public String getUserPassword ()
	{
		return userPassword;
	}

	/**
	 * Describe method setUserName() here.
	 *
	 * @param userName
	 */
	public void setUserName (String userName)
	{
		this.userName = userName;
	}

	/**
	 * Describe method getUserName() here.
	 *
	 * @return
	 */
	public String getUserName ()
	{
		return userName;
	}

	/**
	 * Describe method setAppId() here.
	 *
	 * @param appId
	 */
	public void setAppId (String appId)
	{
		this.appId = appId;
	}

	/**
	 * Describe method getAppId() here.
	 *
	 * @return
	 */
	public String getAppId ()
	{
		return appId;
	}
}
