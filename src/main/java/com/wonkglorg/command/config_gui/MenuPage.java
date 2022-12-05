package com.wonkglorg.command.config_gui;

import com.wonkglorg.Heads;
import com.wonkglorg.enums.MenuDataVariables;
import com.wonkglorg.utilitylib.config.Config;
import com.wonkglorg.utilitylib.managers.LangManager;
import com.wonkglorg.utilitylib.utils.builder.ItemBuilder;
import com.wonkglorg.utilitylib.utils.inventory.Button;
import com.wonkglorg.utilitylib.utils.inventory.InventoryGUI;
import com.wonkglorg.utilitylib.utils.inventory.PaginationGui;
import com.wonkglorg.utilitylib.utils.item.ItemUtil;
import com.wonkglorg.utilitylib.utils.message.ChatColor;
import com.wonkglorg.utilitylib.utils.message.Message;
import com.wonkglorg.utils.HeadMenuUtility;
import com.wonkglorg.utils.MobHeadData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class MenuPage extends InventoryGUI
{
	private final Config config;
	private final HeadMenuUtility menuUtility;
	private final PaginationGui pagination;
	private final LangManager lang = Heads.getManager().getLangManager();
	private String mainPath;
	private String selectedFile;
	private boolean confirmed = false;
	
	public MenuPage(HeadMenuUtility menuUtility, Config config, String path, String name, int page)
	{
		super(54, name == null ? "Head Config" : name, Heads.getInstance(), menuUtility);
		this.mainPath = path;
		this.config = config;
		this.menuUtility = menuUtility;
		pagination = new PaginationGui(this);
		pagination.addSlots(1, 1, 8, 5);
		
		pagination.setFillerItem(InventoryGUI.FILLER);
		handleNextButtons(config, mainPath);
		pagination.setPage(page);
		pagination.updatePage();
	}
	
	@Override
	public void addComponents()
	{
	
	}
	
	private Button forward()
	{
		ItemStack icon = new ItemBuilder(Material.ARROW).setName(ChatColor.Reset + ChatColor.GOLD + "Next Page").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				pagination.nextPage();
				checkArrowButtons();
			}
		};
	}
	
	private Button backwards()
	{
		ItemStack icon = new ItemBuilder(Material.ARROW).setName(ChatColor.Reset + ChatColor.GOLD + "Previous Page").build();
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				
				pagination.prevPage();
				checkArrowButtons();
			}
		};
	}
	
	private void handleNextButtons(Config config, String path)
	{
		boolean added = false;
		removeButton(49);
		removeButton(53);
		if(config.getBoolean(path + ".customPath"))
		{
			addButton(49, addNew());
		}
		if(mainPath.equalsIgnoreCase("heads"))
		{
			config.getSection("Heads", false).forEach(s -> pagination.addPagedButton(BasePathButton(MobHeadData.getFirstValidConfigHeadData(config,
					"Heads." + s), s)));
			pagination.setPage(menuUtility.getLastPage());
			removeButton(45);
			checkArrowButtons();
			pagination.updatePage();
			return;
		} else
		{
			addButton(45, returnButton());
		}
		Set<String> pathList = config.getSection(path, false);
		pathList.remove("customPath");
		pagination.clear();
		for(String subPath : pathList)
		{
			String testPath = path + "." + subPath;
			if(MobHeadData.isValidHeadPath(config, testPath))
			{
				if(!added)
				{
					addButton(49, addNew());
					addButton(53, remove());
					added = true;
				}
				
				pagination.addPagedButton(HeadConfigButton(new MobHeadData(testPath, config, 1), subPath));
			} else
			{
				MobHeadData mobHead = MobHeadData.getFirstValidConfigHeadData(config, testPath);
				if(mobHead == null)
				{
					pagination.addPagedButton(SubPathButton(path, null, subPath));
					continue;
				}
				pagination.addPagedButton(SubPathButton(path, mobHead, subPath));
			}
		}
		checkArrowButtons();
		pagination.updatePage();
	}
	
	private Button SubPathButton(String pathInput, MobHeadData mobHeadData, String name)
	{
		pagination.setPage(1);
		ItemStack icon;
		if(mobHeadData == null)
		{
			icon = new ItemBuilder(Material.BEDROCK).setName("&r" + name)
													.addLoreLine(ChatColor.Reset + ChatColor.RED + "No Texture available")
													.build();
		} else
		{
			String capitalLetter = name.substring(0, 1).toUpperCase();
			icon = ItemUtil.createCustomHead(mobHeadData.getTexture(),
					ChatColor.Reset + ChatColor.LIGHT_PURPLE + capitalLetter + name.substring(1).replace("_", " "));
		}
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				mainPath = pathInput + "." + name;
				System.out.println("Get page: " + pagination.getPage());
				menuUtility.setLastPage(pagination.getPage());
				pagination.setPage(1);
				clear();
				handleNextButtons(config, mainPath);
			}
		};
	}
	
	private Button HeadConfigButton(MobHeadData mobHeadData, String fileName)
	{
		ItemStack icon = ItemUtil.addLore(mobHeadData.createHeadItemWithInfoDesc(), ChatColor.Reset + ChatColor.AQUA + "File name: " + fileName);
		pagination.setPage(1);
		return new Button(icon)
		{
			
			@Override
			public void onClick(InventoryClickEvent e)
			{
				if(e.getClick() == ClickType.SHIFT_LEFT)
				{
					mainPath = config.getParentPath(mobHeadData.getPath());
					selectedFile = fileName;
					setItem(ItemUtil.setName(icon, mobHeadData.getName() + " ~ Selected"));
					update();
					return;
				}
				mainPath = mobHeadData.getPath();
				menuUtility.setMobHeadData(mobHeadData);
				pagination.setPage(1);
				destroy();
				new ConfigurationPage(menuUtility, false).open();
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
					setItem(ItemUtil.setName(icon, "Confirm?"));
					update();
					confirmed = true;
					return;
				}
				config.set(mainPath + "." + selectedFile, null);
				config.silentSave();
				confirmed = false;
				destroy();
				getInventory().close();
				new MenuPage(menuUtility, config, mainPath, null, 1).open();
			}
		};
	}
	
	private Button BasePathButton(MobHeadData mobHeadData, String basePathName)
	{
		pagination.setPage(1);
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
			icon = ItemUtil.createCustomHead(mobHeadData.getTexture(),
					ChatColor.Reset + ChatColor.LIGHT_PURPLE + capitalLetter + baseName.substring(1).replace("_", " "));
		}
		
		String finalName = name;
		return new Button(icon)
		{
			@Override
			public void onClick(InventoryClickEvent e)
			{
				mainPath = mainPath + "." + finalName;
				menuUtility.setLastPage(pagination.getPage());
				destroy();
				new MenuPage(menuUtility, config, mainPath, finalName, 1).open();
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
				menuUtility.setDataVariables(MenuDataVariables.FILENAME);
				ChangeValueCommand.setPlayerDataChange(player, null);
				destroy();
				getInventory().close();
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
				mainPath = config.getParentPath(mainPath);
				clear();
				destroy();
				new MenuPage(menuUtility, config, mainPath, null, menuUtility.getLastPage()).open();
			}
		};
	}
	
	private void checkArrowButtons()
	{
		removeButton(47);
		removeButton(51);
		fill(0, 54, InventoryGUI.FILLER);
		if(pagination.getPage() < pagination.getMaxPage())
		{
			addButton(forward(), 51);
		}
		if(pagination.getPage() > 1)
		{
			addButton(backwards(), 47);
		}
		
		update();
	}
	
}