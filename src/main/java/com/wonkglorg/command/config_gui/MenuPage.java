package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.MenuDataVariables;
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
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MenuPage extends PaginationGui
{
	private final Config config;
	private final HeadMenuUtility menuUtility;
	private final LangManager lang = Heads.getPluginManager().getLangManager();
	List<MobHeadData> mobHeadData;
	private String mainPath;
	private String selectedFile;
	private boolean confirmed = false;
	
	public MenuPage(HeadMenuUtility menuUtility, Config config, String path, String name)
	{
		super(new InventoryGUI(54, name == null ? "Head Config" : name, Heads.getInstance(), menuUtility)
		{
			@Override
			public void addComponents()
			{
			
			}
		});
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
		if(config.getBoolean(path + ".customPath"))
		{
			gui.addButton(49, addNew());
		}
		if(mainPath.equalsIgnoreCase("heads"))
		{
			for(String s : config.getSection("Heads", false))
			{
				
				//Redo this method to show valid head configs if possible or error otherwise
				addPagedButton(BasePathButton(MobHeadData.getFirstValidConfigHeadData(config, "Heads." + s), s));
			}
			gui.removeButton(45);
			checkArrowButtons();
			updatePage();
			return;
		} else
		{
			gui.addButton(45, returnButton());
		}
		Set<String> pathList = config.getSection(path, false);
		pathList.remove("customPath");
		for(String subPath : pathList)
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
				
				addPagedButton(HeadConfigButton(new MobHeadData(testPath, config, 1),subPath));
			} else
			{
				MobHeadData mobHead = MobHeadData.getFirstValidConfigHeadData(config, testPath);
				if(mobHead == null)
				{
					addPagedButton(SubPathButton(path, null, subPath));
					continue;
				}
				addPagedButton(SubPathButton(path, mobHead, subPath));
			}
		}
		checkArrowButtons();
		updatePage();
	}
	
	private Button SubPathButton(String pathInput, MobHeadData mobHeadData, String name)
	{
		setPage(1);
		ItemStack icon;
		if(mobHeadData == null)
		{
			icon = new ItemBuilder(Material.BEDROCK).setName("&r" + name)
													.addLoreLine(ChatColor.Reset + ChatColor.RED + "No Texture available")
													.build();
		} else
		{
			String baseName = name;
			String capitalLetter = baseName.substring(0, 1).toUpperCase();
			icon = ItemUtility.createCustomHead(mobHeadData.getTexture(), "&r" + capitalLetter + baseName.substring(1));
		}
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				mainPath = pathInput + "." + name;
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
				new ConfigurationPage(menuUtility, false).open();
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
				new MenuPage(menuUtility, config, mainPath, null);
			}
		};
	}
	
	private Button BasePathButton(MobHeadData mobHeadData, String basePathName)
	{
		setPage(1);
		ItemStack icon;
		String name;
		
		if(mobHeadData == null)
		{
			icon = new ItemBuilder(Material.BEDROCK).setName("&r" + basePathName.substring(0, 1).toUpperCase() + basePathName.substring(1))
													.addLoreLine(ChatColor.Reset + ChatColor.RED + "No Texture available")
													.build();
			name = basePathName;
		} else
		{
			String baseName = mobHeadData.getOriginalName();
			String capitalLetter = baseName.substring(0, 1).toUpperCase();
			name = mobHeadData.getOriginalName();
			icon = ItemUtility.createCustomHead(mobHeadData.getTexture(), "&r" + capitalLetter + baseName.substring(1));
		}
		
		String finalName = name;
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				mainPath = mainPath + "." + finalName;
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
				ChangeValueCommand.setPlayerDataChange(player, null);
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
		gui.removeButton(47);
		gui.removeButton(51);
		if(getPage() < getMaxPage())
		{
			gui.addButton(forward(), 51);
		}
		if(getPage() > 1)
		{
			gui.addButton(backwards(), 47);
		}
		
		gui.update();
	}
	
}