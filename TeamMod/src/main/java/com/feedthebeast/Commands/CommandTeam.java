package com.feedthebeast.Commands;

import java.util.List;

import com.feedthebeast.TeamMod;
import com.feedthebeast.Network.PacketTypeHandler;
import com.feedthebeast.Network.Packets.PacketTeam;

import cpw.mods.fml.common.network.PacketDispatcher;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandTeam extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "team";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/team set|get playername [team]";
	}
	
	@Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
		switch (par2ArrayOfStr.length)
		{
			case 1:
				return getListOfStringsMatchingLastWord(par2ArrayOfStr,new String[]{"get","set"});
			case 2:
				return getListOfStringsMatchingLastWord(par2ArrayOfStr,getListOfPlayerUsernames());
		}
        return null;
    }
	
    protected String[] getListOfPlayerUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		String senderName = icommandsender.getCommandSenderName();
		if (astring.length < 2)
		{
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(getCommandUsage(icommandsender)));
		}
		else
		{
			String command = astring[0];
			String playerName = astring[1];

			if (command.equals("set"))
			{
				if (astring.length < 3)
				{
					icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(getCommandUsage(icommandsender)));
					return;
				}
				
				TeamMod.instance.teamHandler.setPlayerTeam(playerName, Integer.parseInt(astring[2]));
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Set " + playerName + " team to " + astring[2]));
				
				
			}
			else if (command.equals("get"))
			{
				int playerTeam = TeamMod.instance.teamHandler.getPlayerTeam(senderName);
				if (playerTeam <= -1)
				{
					icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(playerName + " is not in team"));
				}
				else
				{
					icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(playerName + " is in team " + playerTeam));
				}
			}

		}

	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
