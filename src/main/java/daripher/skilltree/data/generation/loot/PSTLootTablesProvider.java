package daripher.skilltree.data.generation.loot;

import java.util.List;
import java.util.Map;
import java.util.Set;

import daripher.skilltree.data.generation.PSTGemTypesProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

public class PSTLootTablesProvider extends LootTableProvider {

  public PSTLootTablesProvider(DataGenerator generator, PSTGemTypesProvider gemTypesProvider) {
    super(
        generator.getPackOutput(),
        Set.of(),
        List.of(
            new SubProviderEntry(
                () -> new PSTBlockLoot(gemTypesProvider), LootContextParamSets.BLOCK)));
  }

  @Override
  protected void validate(
      @NotNull Map<ResourceLocation, LootTable> map, @NotNull ValidationContext ctx) {}
}
