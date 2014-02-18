package com.feedthebeast.VirtualChest.blocks;

import com.feedthebeast.VirtualChest.Configs;
import com.feedthebeast.VirtualChest.lib.LibBlockNames;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;

public class ModBLocks {

	public static Block VirtualChest;
	
	public static void InitBlocks()
	{
		VirtualChest=new VirtualInventoryBlock(Configs.VirtualChestId);
		
		
		
		
		
		RegisterBlocks();
		InitTileEntities();
	}
	
	public static void RegisterBlocks()
	{
		GameRegistry.registerBlock(VirtualChest, LibBlockNames.VIRTUAL_CHEST);
	}
	
	public static void InitTileEntities()
	{
		
	}
}