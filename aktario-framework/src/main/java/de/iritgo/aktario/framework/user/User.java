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


import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.framework.base.DataObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class User extends DataObject implements IObject
{
	private double networkChannel;

	private boolean online;

	private int pingInterval = 5;

	private long pingTime;

	private long lastRealPingTime;

	private long oldPingTime;

	private long allPingTime;

	private int pingCounter;

	private Map newObjectsMapping;

	private Date loggedOutDate = new Date();

	/**
	 * Standard constructor
	 */
	public User()
	{
		super("user");
		newObjectsMapping = new HashMap();
		addAttribute("email", "");
		addAttribute("userName", "");
		addAttribute("password", "");
	}

	/**
	 * Standard constructor
	 */
	public User(String userName, String email, long id, String password, double networkChannel)
	{
		this();
		this.uniqueId = id;
		setEmail(email);
		setName(userName);
		setPassword(password);
		this.networkChannel = networkChannel;
	}

	/**
	 * Standard constructor
	 */
	public User(String userName, String password)
	{
		this();
		this.uniqueId = 10000000;
		setName(userName);
		setPassword(password);
		networkChannel = 0.0;
	}

	/**
	 * Get the id of the iritgo object.
	 */
	public String getTypeId()
	{
		return "user";
	}

	/**
	 * Set the OnlineState
	 */
	public void setOnline(boolean online)
	{
		this.online = online;
	}

	/**
	 * Get the OnlineState.
	 */
	public boolean getOnline()
	{
		return online;
	}

	/**
	 * Is the User Online?
	 */
	public boolean isOnline()
	{
		return online;
	}

	/**
	 * Set the LoggedOutDate
	 */
	public void setLoggedOutDate(Date loggedOutDate)
	{
		this.loggedOutDate = loggedOutDate;
	}

	/**
	 * Get the LoggedOutDate.
	 */
	public Date getLoggedOutDate()
	{
		return loggedOutDate;
	}

	/**
	 * Get the Timestamp of the prototypeable
	 */
	public double getTimeStamp()
	{
		return 0.0;
	}

	/**
	 * Set the Timestamp of the prototypeable
	 */
	public void setTimeStamp(double timeStamp)
	{
	}

	/**
	 * Get the ping time for the user.
	 */
	public long getLastRealPingTime()
	{
		return lastRealPingTime;
	}

	/**
	 * Get the ping time for the user.
	 */
	public long getPingTime()
	{
		return pingTime;
	}

	/**
	 * Set the ping time for this user.
	 */
	public void setPingTime(long pingTime)
	{
		this.pingTime = pingTime;
	}

	/**
	 * Add ping time for ping interval calc.
	 */
	public void addPingTime(long pingTime)
	{
		lastRealPingTime = pingTime;
		allPingTime += pingTime;
		++pingCounter;

		if (pingCounter == pingInterval)
		{
			this.pingTime = allPingTime / pingCounter;
			allPingTime = 0;
			pingCounter = 0;

			return;
		}
	}

	/**
	 * Get the ping time for the user.
	 */
	public long getOldPingTime()
	{
		return oldPingTime;
	}

	/**
	 * Set the ping time for this user.
	 */
	public void setOldPingTime(long pingTime)
	{
		this.oldPingTime = pingTime;
	}

	/**
	 * Create a instance of the iritgo object.
	 */
	public IObject create()
	{
		return new User();
	}

	/**
	 * Add an id
	 */
	public void putNewObjectsMapping(Long id, Long newId)
	{
		newObjectsMapping.put(id, newId);
	}

	/**
	 * Get the a new mapping from the newobjectmapping
	 *
	 * @return The id
	 */
	public Long getNewObjectsMapping(Long id)
	{
		return (Long) newObjectsMapping.get(id);
	}

	/**
	 * Clean up the new objects mapping, will called by a logoff.
	 */
	public void clearNewObjectsMapping()
	{
		newObjectsMapping.clear();
	}

	/**
	 * Read the attributes from the given stream.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void readObject(InputStream stream) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		super.readObject(stream);

		DataInputStream dataStream = new DataInputStream(stream);

		networkChannel = dataStream.readDouble();
		online = dataStream.readBoolean();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject(OutputStream stream) throws IOException
	{
		super.writeObject(stream);

		DataOutputStream dataStream = new DataOutputStream(stream);

		dataStream.writeDouble(networkChannel);
		dataStream.writeBoolean(online);
	}

	/**
	 * Set the Username.
	 */
	public void setName(String userName)
	{
		setAttribute("userName", userName);
	}

	/**
	 * Get the Username.
	 */
	public String getName()
	{
		return getStringAttribute("userName");
	}

	/**
	 * Get the Username.
	 */
	public String getPassword()
	{
		return getStringAttribute("password");
	}

	/**
	 * Set the password.
	 */
	public void setPassword(String password)
	{
		setAttribute("password", password);
	}

	/**
	 * Get the Username.
	 */
	public String getEmail()
	{
		return getStringAttribute("email");
	}

	/**
	 * Get the Username.
	 */
	public void setEmail(String email)
	{
		setAttribute("email", email);
	}

	/**
	 * Set the NetworkChannel.
	 */
	public void setNetworkChannel(double networkChannel)
	{
		this.networkChannel = networkChannel;
	}

	/**
	 * Get the NetworkChannel.
	 */
	public double getNetworkChannel()
	{
		return networkChannel;
	}

	/**
	 * Serialize the object type information on this object
	 */
	public IObject writeTypeInformations(OutputStream stream, IObject iObject)
	{
		return this;
	}

	/**
	 * Read Serialize type information a given stream
	 * and do some things...
	 */
	public IObject readTypeInformations(InputStream stream, IObject iObject)
	{
		return this;
	}

	/**
	 * Return a dump form the current object.
	 *
	 * @return String The current dump
	 */
	public String dump()
	{
		return toString();
	}

	@Override
	public String toString()
	{
		return getName() + "->" + super.toString();
	}
}
