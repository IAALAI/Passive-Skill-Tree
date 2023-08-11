package daripher.skilltree.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import daripher.skilltree.SkillTreeMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = SkillTreeMod.MOD_ID, bus = Bus.MOD)
public class Config {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	private static final ConfigValue<Integer> MAX_SKILL_POINTS;
	private static final ConfigValue<Integer> DEFAULT_HELMET_SOCKETS;
	private static final ConfigValue<Integer> DEFAULT_CHESTPLATE_SOCKETS;
	private static final ConfigValue<Integer> DEFAULT_LEGGINGS_SOCKETS;
	private static final ConfigValue<Integer> DEFAULT_BOOTS_SOCKETS;
	private static final ConfigValue<Integer> DEFAULT_WEAPON_SOCKETS;
	private static final ConfigValue<Integer> DEFAULT_SHIELD_SOCKETS;
	private static final ConfigValue<Integer> DEFAULT_NECKLACE_SOCKETS;
	private static final ConfigValue<Integer> DEFAULT_RING_SOCKETS;
	private static final ConfigValue<Double> GEM_DROP_CHANCE;
	private static final ConfigValue<Double> AMNESIA_SCROLL_PENALTY;
	private static final ConfigValue<Double> GRINDSTONE_EXP_MULTIPLIER;
	private static final ConfigValue<Boolean> SHOW_CHAT_MESSAGES;
	private static final ConfigValue<Boolean> ENABLE_EXP_EXCHANGE;
	private static final ConfigValue<Boolean> DRAGON_DROPS_AMNESIA_SCROLL;
	private static final ConfigValue<List<? extends Integer>> LEVEL_UP_COSTS;
	private static final ConfigValue<List<? extends String>> SOCKET_BLACKLIST;
	private static final ConfigValue<List<? extends String>> FORCED_HELMETS;
	private static final ConfigValue<List<? extends String>> FORCED_CHESTPLATES;
	private static final ConfigValue<List<? extends String>> FORCED_LEGGINGS;
	private static final ConfigValue<List<? extends String>> FORCED_BOOTS;
	private static final ConfigValue<List<? extends String>> FORCED_SHIELDS;
	private static final ConfigValue<List<? extends String>> FORCED_MELEE_WEAPON;
	private static final ConfigValue<List<? extends String>> FORCED_RANGED_WEAPON;

	static {
		BUILDER.push("Skill Points");
		MAX_SKILL_POINTS = BUILDER.defineInRange("Maximum skill points", 85, 1, 1000);
		BUILDER.comment("This list's size must be equal to maximum skill points.");
		LEVEL_UP_COSTS = BUILDER.defineListAllowEmpty("Levelup costs", generateDefaultPointsCosts(50), o -> o instanceof Integer i && i > 0);
		BUILDER.comment("Disabling this will remove chat messages when you gain a skill point.");
		SHOW_CHAT_MESSAGES = BUILDER.define("Show chat messages", true);
		BUILDER.comment("Warning: If you disable this make sure you make alternative way of getting skill points.");
		ENABLE_EXP_EXCHANGE = BUILDER.define("Enable exprerience exchange for skill points", true);
		BUILDER.pop();

		BUILDER.push("Gems");
		GEM_DROP_CHANCE = BUILDER.defineInRange("Base drop chance", 0.05, 0, 1);
		BUILDER.pop();

		BUILDER.push("Equipment");
		DEFAULT_HELMET_SOCKETS = BUILDER.defineInRange("Default Helmets Sockets", 1, 0, 4);
		DEFAULT_CHESTPLATE_SOCKETS = BUILDER.defineInRange("Default Chestplates Sockets", 1, 0, 4);
		DEFAULT_LEGGINGS_SOCKETS = BUILDER.defineInRange("Default Leggings Sockets", 0, 0, 4);
		DEFAULT_BOOTS_SOCKETS = BUILDER.defineInRange("Default Boots Sockets", 1, 0, 4);
		DEFAULT_WEAPON_SOCKETS = BUILDER.defineInRange("Default Weapons Sockets", 1, 0, 4);
		DEFAULT_SHIELD_SOCKETS = BUILDER.defineInRange("Default Shields Sockets", 1, 0, 4);
		DEFAULT_RING_SOCKETS = BUILDER.defineInRange("Default Rings Sockets", 1, 0, 4);
		DEFAULT_NECKLACE_SOCKETS = BUILDER.defineInRange("Default Necklaces Sockets", 1, 0, 4);
		BUILDER.comment("You can remove sockets from items here");
		BUILDER.comment("Example: Blacklisting specific items: [\"minecraft:diamond_hoe\", \"minecraft:golden_hoe\"]");
		BUILDER.comment("Example: Blacklisting whole mod: [\"<mod_id>:*\"]");
		BUILDER.comment("Example: Blacklisting all items: [\"*:*\"]");
		SOCKET_BLACKLIST = BUILDER.defineListAllowEmpty("IDs of items that shouldn't have sockets", new ArrayList<>(), Config::validateItemName);
		BUILDER.comment("You can force items from other mods into equipment categories here");
		FORCED_HELMETS = BUILDER.defineListAllowEmpty("Force into helmets category", new ArrayList<>(), Config::validateItemName);
		FORCED_CHESTPLATES = BUILDER.defineListAllowEmpty("Force into chestplates category", new ArrayList<>(), Config::validateItemName);
		FORCED_LEGGINGS = BUILDER.defineListAllowEmpty("Force into leggings category", new ArrayList<>(), Config::validateItemName);
		FORCED_BOOTS = BUILDER.defineListAllowEmpty("Force into boots category", new ArrayList<>(), Config::validateItemName);
		FORCED_SHIELDS = BUILDER.defineListAllowEmpty("Force into shields category", new ArrayList<>(), Config::validateItemName);
		FORCED_MELEE_WEAPON = BUILDER.defineListAllowEmpty("Force into melee weapons category", new ArrayList<>(), Config::validateItemName);
		FORCED_RANGED_WEAPON = BUILDER.defineListAllowEmpty("Force into ranged weapons category", new ArrayList<>(), Config::validateItemName);
		BUILDER.pop();

		BUILDER.push("Amnesia Scroll");
		BUILDER.comment("How much levels (percentage) player lose using amnesia scroll");
		AMNESIA_SCROLL_PENALTY = BUILDER.defineInRange("Amnesia scroll penalty", 0.2D, 0D, 1D);
		DRAGON_DROPS_AMNESIA_SCROLL = BUILDER.define("Drop amnesia scrolls from the Ender Dragon", true);
		BUILDER.pop();

		BUILDER.push("Experience");
		GRINDSTONE_EXP_MULTIPLIER = BUILDER.defineInRange("Grindstone experience multiplier", 0.1D, 0D, 1D);
		BUILDER.pop();
	}

	static List<Integer> generateDefaultPointsCosts(int maximumPoints) {
		List<Integer> costs = new ArrayList<Integer>();
		costs.add(15);
		for (int i = 1; i < maximumPoints; i++) {
			int previousCost = costs.get(costs.size() - 1);
			int cost = previousCost + 3 + i;
			costs.add(cost);
		}
		return costs;
	}

	static boolean validateItemName(Object object) {
		return object instanceof String name && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(name));
	}

	public static final ForgeConfigSpec SPEC = BUILDER.build();

	public static int max_skill_points;
	public static int default_helmet_sockets;
	public static int default_chestplate_sockets;
	public static int default_leggings_sockets;
	public static int default_boots_sockets;
	public static int default_weapon_sockets;
	public static int default_shield_sockets;
	public static int default_necklace_sockets;
	public static int default_ring_sockets;
	public static double gem_drop_chance;
	public static double amnesia_scroll_penalty;
	public static double grindstone_exp_multiplier;
	public static boolean show_chat_messages;
	public static boolean enable_exp_exchange;
	public static boolean dragon_drops_amnesia_scroll;
	public static List<Integer> level_up_costs;
	public static List<? extends String> socket_blacklist;
	public static Set<Item> forced_helmets;
	public static Set<Item> forced_chestplates;
	public static Set<Item> forced_leggings;
	public static Set<Item> forced_boots;
	public static Set<Item> forced_shields;
	public static Set<Item> forced_melee_weapon;
	public static Set<Item> forced_ranged_weapon;

	@SubscribeEvent
	static void load(ModConfigEvent event) {
		max_skill_points = MAX_SKILL_POINTS.get();
		default_helmet_sockets = DEFAULT_HELMET_SOCKETS.get();
		default_chestplate_sockets = DEFAULT_CHESTPLATE_SOCKETS.get();
		default_leggings_sockets = DEFAULT_LEGGINGS_SOCKETS.get();
		default_boots_sockets = DEFAULT_BOOTS_SOCKETS.get();
		default_weapon_sockets = DEFAULT_WEAPON_SOCKETS.get();
		default_shield_sockets = DEFAULT_SHIELD_SOCKETS.get();
		default_necklace_sockets = DEFAULT_NECKLACE_SOCKETS.get();
		default_ring_sockets = DEFAULT_RING_SOCKETS.get();
		gem_drop_chance = GEM_DROP_CHANCE.get();
		amnesia_scroll_penalty = AMNESIA_SCROLL_PENALTY.get();
		grindstone_exp_multiplier = GRINDSTONE_EXP_MULTIPLIER.get();
		show_chat_messages = SHOW_CHAT_MESSAGES.get();
		enable_exp_exchange = ENABLE_EXP_EXCHANGE.get();
		dragon_drops_amnesia_scroll = DRAGON_DROPS_AMNESIA_SCROLL.get();
		level_up_costs = LEVEL_UP_COSTS.get().stream().collect(Collectors.toList());
		socket_blacklist = SOCKET_BLACKLIST.get();
		forced_helmets = getItems(FORCED_HELMETS.get());
		forced_chestplates = getItems(FORCED_CHESTPLATES.get());
		forced_leggings = getItems(FORCED_LEGGINGS.get());
		forced_boots = getItems(FORCED_BOOTS.get());
		forced_shields = getItems(FORCED_SHIELDS.get());
		forced_melee_weapon = getItems(FORCED_MELEE_WEAPON.get());
		forced_ranged_weapon = getItems(FORCED_RANGED_WEAPON.get());
	}

	static Set<Item> getItems(List<? extends String> names) {
		return names.stream().map(ResourceLocation::new).map(ForgeRegistries.ITEMS::getValue).collect(Collectors.toSet());
	}
}
