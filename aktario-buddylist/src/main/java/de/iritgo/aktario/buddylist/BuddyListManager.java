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

package de.iritgo.aktario.buddylist;


import java.sql.*;
import java.util.*;
import javax.sql.*;
import org.apache.commons.dbutils.*;
import de.iritgo.aktario.core.*;
import de.iritgo.aktario.core.base.*;
import de.iritgo.aktario.core.iobject.*;
import de.iritgo.aktario.core.logger.*;
import de.iritgo.aktario.core.manager.*;
import de.iritgo.aktario.core.plugin.*;
import de.iritgo.aktario.core.resource.*;
import de.iritgo.aktario.core.user.*;
import de.iritgo.aktario.framework.base.*;
import de.iritgo.aktario.framework.command.*;
import de.iritgo.aktario.framework.dataobject.*;
import de.iritgo.aktario.framework.server.*;
import de.iritgo.aktario.framework.user.*;
import de.iritgo.simplelife.string.*;


public class BuddyListManager extends BaseObject implements Manager, UserListener, PluginEventListener
{
	private BuddyListRegistry buddyListRegistry;

	public BuddyListManager()
	{
		super("BuddyListManager");
	}

	public void init()
	{
		Engine.instance().getEventRegistry().addListener("Plugin", this);

		Engine.instance().getEventRegistry().addListener("User", this);
	}

	public BuddyListRegistry getBuddyListRegistry()
	{
		return buddyListRegistry;
	}

	public void iObjectCreatedEvent(@SuppressWarnings("unused") IObjectCreatedEvent event)
	{
	}

	public void iObjectModifiedEvent(@SuppressWarnings("unused") IObjectModifiedEvent event)
	{
	}

	public void iObjectDeletedEvent(IObjectDeletedEvent event)
	{
		if (event.getDeletedObject() instanceof AktarioUser)
		{
			try
			{
				UserRegistry userRegistry = Server.instance().getUserRegistry();

				User user = userRegistry.getUser(((AktarioUser) event.getDeletedObject()).getUserId());

				DynDataObject participant = null;

				for (Iterator i = Engine.instance().getBaseRegistry().iterator("Participant"); i.hasNext();)
				{
					participant = (DynDataObject) i.next();

					if (participant.getStringAttribute("iritgoUserName").equals(user.getName()))
					{
						break;
					}
				}

				Engine.instance().getEventRegistry().fire("objectremoved",
								new IObjectDeletedEvent(participant, null, null, null));
				Engine.instance().getBaseRegistry().remove(participant);
			}
			catch (Exception x)
			{
				Log.logFatal("system", "ParicipantManager:pluginEvent(delete participant )",
								"Can not delete participant  error.");
			}
		}
	}

	public void pluginEvent(PluginStateEvent event)
	{
		if (event.allPluginsInitialized())
		{
		}
	}

	public void generateGroups(final User user)
	{
		final DataSource dataSource = (DataSource) CommandTools.performSimple("persist.GetDefaultDataSource");
		final BuddyList buddyList = addBuddyList(user);
		final ResourceService resources = Engine.instance().getResourceService();

		QueryRunner query = new QueryRunner(dataSource);
		try
		{
			query
							.query(
											"select * from akteragroup left join akteragroupentry on akteragroup.id = akteragroupentry.groupid left join keelusers on keelusers.uniqid = akteragroupentry.userid where keelusers.username="
															+ "'" + user.getName() + "' and akteragroup.visible = true",
											new ResultSetHandler()
											{
												public Object handle(ResultSet rs) throws SQLException
												{
													while (rs.next())
													{
														try
														{
															long groupId = rs.getLong("groupId");

															String displayName = rs.getString("title");
															if (StringTools.isTrimEmpty(displayName))
															{
																displayName = rs.getString("name");
															}
															displayName = resources
																			.getStringWithoutException(displayName);
															final BuddyListGroup buddyListGroup = addBuddyListGroup(
																			user, buddyList, groupId, displayName);

															QueryRunner query2 = new QueryRunner(dataSource);

															query2
																			.query(
																							"select * from akteragroup left join akteragroupentry on akteragroup.id = akteragroupentry.groupid "
																											+ "left join keelusers on keelusers.uniqid = akteragroupentry.userid where akteragroup.id="
																											+ groupId,
																							new ResultSetHandler()
																							{
																								public Object handle(
																												ResultSet rs)
																									throws SQLException
																								{
																									while (rs.next())
																									{
																										try
																										{
																											addParticipant(
																															rs
																																			.getString("username"),
																															buddyListGroup);
																										}
																										catch (Exception ignored)
																										{
																											Log
																															.logError(
																																			"persist",
																																			"LoadObject",
																																			"NoSuchIObjectException");
																											ignored
																															.printStackTrace();
																										}
																									}

																									return null;
																								}
																							});
														}
														catch (Exception x)
														{
															Log.logError("plugin", "BuddyListManager.generateGroups", x
																			.toString());
														}
													}

													return null;
												}
											});
		}
		catch (Exception x)
		{
			Log.logError("plugin", "BuddyListManager.generateGroups", x.toString());
		}
	}

	public void addParticipant(String name, BuddyListGroup buddyListGroup)
	{
		for (Iterator i = Engine.instance().getBaseRegistry().iterator("ParticipantState"); i.hasNext();)
		{
			DynDataObject participantState = (DynDataObject) i.next();

			if (participantState.getStringAttribute("iritgoUserName").equals(name))
			{
				buddyListGroup.addParticipant(participantState);
			}
		}
	}

	public BuddyListGroup addBuddyListGroup(User user, BuddyList buddyList, long groupId, String name)
	{
		try
		{
			BuddyListGroup buddyListGroup = (BuddyListGroup) Engine.instance().getIObjectFactory().newInstance(
							"BuddyListGroup");

			buddyListGroup.setName(name);
			buddyListGroup.setIritgoUserName(user.getName());
			buddyListGroup.setUniqueId(groupId);
			registerIObject((IObject) buddyListGroup);

			buddyList.addBuddyListGroup(buddyListGroup);

			return buddyListGroup;
		}
		catch (Exception x)
		{
			Log.logFatal("system", "BuddyListManager:addBuddyList(Create BuddyList)",
							"Can not create buddy list error.");
			x.printStackTrace();
		}

		return null;
	}

	public BuddyList addBuddyList(User user)
	{
		try
		{
			BuddyList buddyList = (BuddyList) Engine.instance().getIObjectFactory().newInstance("BuddyList");

			buddyList.setIritgoUserName(user.getName());
			buddyList.setUniqueId(user.getUniqueId());
			registerIObject((IObject) buddyList);

			return buddyList;
		}
		catch (Exception x)
		{
			Log.logFatal("system", "BuddyListManager:addBuddyList(Create BuddyList)",
							"Can not create buddy list error.");
			x.printStackTrace();
		}

		return null;
	}

	private void registerIObject(IObject iObject)
	{
		IObjectProxy proxy = (IObjectProxy) new FrameworkProxy();

		proxy.setSampleRealObject((IObject) iObject);

		Engine.instance().getBaseRegistry().add((BaseObject) iObject);
		Engine.instance().getProxyRegistry().addProxy(proxy, iObject.getTypeId());
	}

	public void userEvent(UserEvent event)
	{
		User user = event.getUser();

		if (user == null)
		{
			return;
		}

		if (event.isLoggedIn())
		{
			generateGroups(user);
		}

		if (event.isLoggedOut())
		{
			BuddyList buddyList = (BuddyList) Engine.instance().getBaseRegistry().get(user.getUniqueId(), "BuddyList");

			if (buddyList == null)
			{
				return;
			}
			try
			{
				for (Iterator groups = buddyList.buddyListGroupIterator(); groups.hasNext();)
				{
					BuddyListGroup buddyListGroup = (BuddyListGroup) groups.next();
					Engine.instance().getBaseRegistry().remove(buddyListGroup);
				}
			}
			catch (Exception x)
			{
				System.out.println("BuddyListManager error: " + x.getMessage());
			}

			Engine.instance().getBaseRegistry().remove(buddyList);
		}
	}

	public void unload()
	{
	}
}
