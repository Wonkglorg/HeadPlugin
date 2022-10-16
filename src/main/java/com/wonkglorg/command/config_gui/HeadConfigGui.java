package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.utils.builder.ItemBuilder;
import com.wonkglorg.utilitylib.utils.inventory.Button;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.inventory.PaginationGui;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import com.wonkglorg.utils.HeadMenuUtility;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class HeadConfigGui extends PaginationGui
{
	private final Config config;
	private final HeadMenuUtility menuUtility;
	private final Stack<String> pathStack = new Stack<>();
	List<MobHeadData> mobHeadData;
	private String mainPath;
	
	public HeadConfigGui(HeadMenuUtility menuUtility, Config config, String path)
	{
		super(new InventoryGUI(54, "Head Config", Heads.getInstance(), menuUtility)
		{
			@Override
			public void addComponents()
			{
			
			}
		});
		//pathStack.push()
		this.mainPath = path;
		this.config = config;
		this.menuUtility = menuUtility;
		mobHeadData = MobHeadData.getFirstOfAllValidConfigHeadData(config, "Heads");
		setPage(1);
		
		addSlots(1, 1, 8, 4);
		
		handleNextButtons(config, mainPath);
		
		gui.addButton(51, forward());
		gui.addButton(47, backwards());
		gui.open();
		updatePage();
	}
	
	private Button forward()
	{
		ItemStack icon = new ItemBuilder(Material.ARROW).setName("Next Page").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				nextPage();
			}
		};
	}
	
	private Button backwards()
	{
		ItemStack icon = new ItemBuilder(Material.ARROW).setName("Previos Page").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				prevPage();
			}
		};
	}
	
	private void handleNextButtons(Config config, String path)
	{
		boolean added = false;
		gui.removeButton(49);
		if(mainPath.equalsIgnoreCase("heads"))
		{
			for(MobHeadData mobHeadData1 : mobHeadData)
			{
				addPagedButton(BasePathButton(mobHeadData1));
			}
			gui.removeButton(45);
			updatePage();
			return;
		} else
		{
			gui.addButton(45, returnButton());
		}
		
		for(String subPath : config.getSection(path, false))
		{
			String testPath = path + "." + subPath;
			if(MobHeadData.isValidHeadPath(config, testPath))
			{
				if(!added)
				{
					gui.addButton(49, addNew());
					added = true;
				}
				addPagedButton(HeadConfigButton(new MobHeadData(testPath, config, 1), subPath));
				continue;
			}
			MobHeadData mobHead = MobHeadData.getFirstValidConfigHeadData(config, testPath);
			if(mobHead == null)
			{
				continue;
			}
			addPagedButton(SubPathButton(testPath, mobHead.getTexture(), subPath, " "));
		}
		updatePage();
	}
	
	private Button SubPathButton(String testPath, String texture, String name, String description)
	{
		return new Button(ItemUtility.createCustomHead(texture, name.toLowerCase(), description))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				mainPath = testPath;
				setPage(1);
				clear();
				handleNextButtons(config, mainPath);
			}
		};
	}
	
	private Button HeadConfigButton(MobHeadData mobHeadData, String fileName)
	{
		ItemStack icon = ItemUtility.addLore(mobHeadData.createHeadItemWithInfoDesc(), ChatColor.Reset + ChatColor.AQUA + "File name: " + fileName);
		return new Button(icon)
		{
			
			@Override
			public void onClick(InventoryClickEvent e)
			{
				if(e.getClick() == ClickType.SHIFT_LEFT)
				{
					setItem(ItemUtility.setName(icon, icon.displayName() + " ~ Selected"));
					/*
					menuUtility.setSelectedButton(this);
					menuUtility.getSelectedButton().setItem(ItemUtility.setName(icon, icon.displayName() + "~ Selected"));
					
					 */
					 
					 
				}
				
				clear();
				setPage(1);
				menuUtility.setMobHeadData(mobHeadData);
				new HeadConfigurationPage(menuUtility, false).open();
			}
		};
	}
	
	private Button BasePathButton(MobHeadData mobHeadData)
	{
		
		String baseName = mobHeadData.getOriginalName();
		String capitalLetter = baseName.substring(0, 1).toUpperCase();
		
		return new Button(ItemUtility.createCustomHead(mobHeadData.getTexture(), "&r" + capitalLetter + baseName.substring(1)))
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				mainPath = mainPath + "." + mobHeadData.getOriginalName();
				clear();
				handleNextButtons(config, mainPath);
				
				updatePage();
			}
		};
	}
	
	private Button addNew()
	{
		ItemStack icon = new ItemBuilder(Material.LIME_CONCRETE).setName("Add new Head").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				ItemUtility.setName(icon, "Feature not yet available :(");
				gui.update();
			}
		};
	}
	
	private Button returnButton()
	{
		
		//remove button if the path is less than x
		return new Button(new ItemBuilder(Material.BARRIER).setName("Back").build())
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				String[] splitPath = (mainPath.split("\\."));
				if(splitPath.length < 2)
				{
					return;
				}
				String[] trimmedPath = Arrays.copyOf(splitPath, splitPath.length - 1);
				
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(trimmedPath[0]);
				for(int i = 1; i < trimmedPath.length; i++)
				{
					stringBuilder.append(".").append(trimmedPath[i]);
				}
				mainPath = stringBuilder.toString();
				clear();
				setPage(1);
				handleNextButtons(config, mainPath);
			}
		};
	}
}