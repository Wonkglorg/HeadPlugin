package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.utils.builder.ItemBuilder;
import com.wonkglorg.utilitylib.utils.inventory.Button;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.inventory.PaginationGui;
import com.wonkglorg.utilitylib.utils.item.ItemUtility;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utils.HeadMenuUtility;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
	private final LangManager lang = Heads.getPluginManager().getLangManager();
	private final Stack<String> pathStack = new Stack<>();
	List<MobHeadData> mobHeadData;
	private String mainPath;
	private String selectedFile;
	private boolean confirmed = false;
	
	public HeadConfigGui(HeadMenuUtility menuUtility, Config config, String path, String name)
	{
		super(new InventoryGUI(54, name == null ? "Head Config" : name, Heads.getInstance(), menuUtility)
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
				checkArrowButtons();
			}
		};
	}
	
	private Button backwards()
	{
		ItemStack icon = new ItemBuilder(Material.ARROW).setName("Previous Page").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				
				prevPage();
				checkArrowButtons();
			}
		};
	}
	
	private void handleNextButtons(Config config, String path)
	{
		boolean added = false;
		gui.removeButton(49);
		gui.removeButton(53);
		if(mainPath.equalsIgnoreCase("heads"))
		{
			for(MobHeadData mobHeadData1 : mobHeadData)
			{
				addPagedButton(BasePathButton(mobHeadData1));
			}
			gui.removeButton(45);
			checkArrowButtons();
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
					gui.addButton(53, remove());
					added = true;
				}
				
				addPagedButton(HeadConfigButton(new MobHeadData(testPath, config, 1), subPath));
			} else
			{
				MobHeadData mobHead = MobHeadData.getFirstValidConfigHeadData(config, testPath);
				if(mobHead == null)
				{
					continue;
				}
				addPagedButton(SubPathButton(testPath, mobHead.getTexture(), subPath, " "));
			}
		}
		checkArrowButtons();
		updatePage();
	}
	
	private Button SubPathButton(String testPath, String texture, String name, String description)
	{
		setPage(1);
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
		setPage(1);
		return new Button(icon)
		{
			
			@Override
			public void onClick(InventoryClickEvent e)
			{
				if(e.getClick() == ClickType.SHIFT_LEFT)
				{
					setItem(ItemUtility.setName(icon, mobHeadData.getName() + " ~ Selected"));
					gui.update();
					String[] stringArray = mobHeadData.getPath().split("\\.");
					StringBuilder builder = new StringBuilder();
					builder.append(stringArray[0]);
					for(int i = 1; i < stringArray.length - 1; i++)
					{
						builder.append(".");
						builder.append(stringArray[i]);
					}
					mainPath = builder.toString();
					selectedFile = fileName;
					return;
				}
				mainPath = mobHeadData.getPath();
				clear();
				setPage(1);
				menuUtility.setMobHeadData(mobHeadData);
				new HeadConfigurationPage(menuUtility, false).open();
				gui.destroy();
			}
		};
	}
	
	private Button remove()
	{
		ItemStack icon = new ItemBuilder(Material.BARRIER).setName("Remove head").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				if(selectedFile == null)
				{
					return;
				}
				
				if(selectedFile.equalsIgnoreCase("default"))
				{
					gui.destroy();
					gui.getInventory().close();
					new HeadConfigGui(menuUtility, config, mainPath, ChatColor.RED + "Default can not be removed");
					return;
				}
				
				if(!confirmed)
				{
					setItem(ItemUtility.setName(icon, "Confirm?"));
					gui.update();
					confirmed = true;
					return;
				}
				config.set(mainPath + "." + selectedFile, null);
				config.silentSave();
				confirmed = false;
				gui.destroy();
				gui.getInventory().close();
				new HeadConfigGui(menuUtility, config, mainPath, null);
			}
		};
	}
	
	private Button BasePathButton(MobHeadData mobHeadData)
	{
		setPage(1);
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
				//setItem(new ItemBuilder(Material.LIME_CONCRETE).setName("Feature not yet available :(").build());
				Player player = menuUtility.getOwner();
				Message.msgPlayer(player, lang.getValue(player, "command-request-set-file-name"));
				Message.msgPlayer(player, lang.getValue(player, "command-request-cancel"));
				menuUtility.setLastPath(mainPath);
				menuUtility.setDataVariables(MenuDataVariables.FILENAME);
				Heads.add(player);
				gui.destroy();
				gui.getInventory().close();
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
	
	private void checkArrowButtons()
	{
		System.out.println("Checking buttons");
		gui.removeButton(47);
		gui.removeButton(51);
		System.out.println("Max page: " + getMaxPage());
		if(getPage() < getMaxPage())
		{
			System.out.println("Added forward");
			gui.addButton(forward(), 51);
		}
		if(getPage() > 1)
		{
			System.out.println("Added backward");
			gui.addButton(backwards(), 47);
		}
		
		gui.update();
	}
	
}