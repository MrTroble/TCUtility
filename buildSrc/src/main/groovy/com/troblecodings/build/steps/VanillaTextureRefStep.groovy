package com.troblecodings.build.steps

import com.troblecodings.build.AssetMigrationStep
import com.troblecodings.build.MigrationContext

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Schreibt {@code minecraft:blocks/X} und {@code minecraft:items/X} Texture-
 * Referenzen in JSON-Dateien (Blockstates, Models, ...) auf das Vanilla-
 * 1.14-Schema um. Das umfasst zwei Aenderungen:
 *
 * <ol>
 *   <li>Den Pfad von {@code blocks/} / {@code items/} (Plural) auf
 *       {@code block/} / {@code item/} (Singular).</li>
 *   <li>Ein Rename des Asset-Namens, falls Mojangs "Flattening" in 1.13 den
 *       Vanilla-Block / das Vanilla-Item umbenannt hat
 *       (z.B. {@code concrete_black} -> {@code black_concrete},
 *       {@code log_big_oak} -> {@code dark_oak_log},
 *       {@code stonebrick_mossy} -> {@code mossy_stone_bricks}).</li>
 * </ol>
 *
 * Die Tabelle deckt die Vanilla-Namen ab, die der Mod aktuell referenziert.
 * Sollte ein Content-Pack einen weiteren Vanilla-Namen verwenden, der hier
 * fehlt, wird der Pfad immer noch flattened (Plural -> Singular), aber der
 * Asset-Name bleibt unveraendert -- gibt dann eine "missing texture" Warnung
 * im Log und der Eintrag muss in {@link #VANILLA_RENAMES} ergaenzt werden.
 */
class VanillaTextureRefStep extends AssetMigrationStep {

    // 1.12 erlaubte sowohl explizite ({@code "minecraft:blocks/X"}) als auch
    // implizite ({@code "blocks/X"} -> default-Namespace minecraft) Vanilla-
    // Refs. Mod-eigene Refs wie {@code "tcutility:blocks/X"} werden vom
    // Pattern bewusst nicht erfasst.
    private static final Pattern BLOCKS_REF =
            Pattern.compile('"(?:minecraft:)?blocks/([A-Za-z0-9_./-]+)"')
    private static final Pattern ITEMS_REF =
            Pattern.compile('"(?:minecraft:)?items/([A-Za-z0-9_./-]+)"')

    /**
     * 1.12 Vanilla-Texture-Name -> 1.14 Vanilla-Texture-Name.
     * Eintraege deckten alle Renames ab, die der Mod im Test-Content-Pack
     * tatsaechlich anfasst; Erweiterungen sind unkritisch.
     */
    private static final Map<String, String> VANILLA_RENAMES = [
            'anvil_base'                       : 'anvil',
            'anvil_top_damaged_0'              : 'anvil_top',
            'brick'                            : 'bricks',
            'cobblestone_mossy'                : 'mossy_cobblestone',
            'concrete_black'                   : 'black_concrete',
            'concrete_blue'                    : 'blue_concrete',
            'concrete_brown'                   : 'brown_concrete',
            'concrete_cyan'                    : 'cyan_concrete',
            'concrete_gray'                    : 'gray_concrete',
            'concrete_green'                   : 'green_concrete',
            'concrete_light_blue'              : 'light_blue_concrete',
            'concrete_lime'                    : 'lime_concrete',
            'concrete_magenta'                 : 'magenta_concrete',
            'concrete_orange'                  : 'orange_concrete',
            'concrete_pink'                    : 'pink_concrete',
            'concrete_powder_black'            : 'black_concrete_powder',
            'concrete_powder_blue'             : 'blue_concrete_powder',
            'concrete_powder_brown'            : 'brown_concrete_powder',
            'concrete_powder_cyan'             : 'cyan_concrete_powder',
            'concrete_powder_gray'             : 'gray_concrete_powder',
            'concrete_powder_green'            : 'green_concrete_powder',
            'concrete_powder_light_blue'       : 'light_blue_concrete_powder',
            'concrete_powder_lime'             : 'lime_concrete_powder',
            'concrete_powder_magenta'          : 'magenta_concrete_powder',
            'concrete_powder_orange'           : 'orange_concrete_powder',
            'concrete_powder_pink'             : 'pink_concrete_powder',
            'concrete_powder_purple'           : 'purple_concrete_powder',
            'concrete_powder_red'              : 'red_concrete_powder',
            'concrete_powder_silver'           : 'light_gray_concrete_powder',
            'concrete_powder_white'            : 'white_concrete_powder',
            'concrete_powder_yellow'           : 'yellow_concrete_powder',
            'concrete_purple'                  : 'purple_concrete',
            'concrete_red'                     : 'red_concrete',
            'concrete_silver'                  : 'light_gray_concrete',
            'concrete_white'                   : 'white_concrete',
            'concrete_yellow'                  : 'yellow_concrete',
            'dirt_podzol_top'                  : 'podzol_top',
            'end_bricks'                       : 'end_stone_bricks',
            'glass_black'                      : 'black_stained_glass',
            'glass_blue'                       : 'blue_stained_glass',
            'glass_brown'                      : 'brown_stained_glass',
            'glass_cyan'                       : 'cyan_stained_glass',
            'glass_gray'                       : 'gray_stained_glass',
            'glass_green'                      : 'green_stained_glass',
            'glass_light_blue'                 : 'light_blue_stained_glass',
            'glass_lime'                       : 'lime_stained_glass',
            'glass_magenta'                    : 'magenta_stained_glass',
            'glass_orange'                     : 'orange_stained_glass',
            'glass_pink'                       : 'pink_stained_glass',
            'glass_purple'                     : 'purple_stained_glass',
            'glass_red'                        : 'red_stained_glass',
            'glass_silver'                     : 'light_gray_stained_glass',
            'glass_white'                      : 'white_stained_glass',
            'glass_yellow'                     : 'yellow_stained_glass',
            'grass_top'                        : 'grass_block_top',
            'hardened_clay_stained_black'      : 'black_terracotta',
            'hardened_clay_stained_blue'       : 'blue_terracotta',
            'hardened_clay_stained_brown'      : 'brown_terracotta',
            'hardened_clay_stained_cyan'       : 'cyan_terracotta',
            'hardened_clay_stained_gray'       : 'gray_terracotta',
            'hardened_clay_stained_green'      : 'green_terracotta',
            'hardened_clay_stained_light_blue' : 'light_blue_terracotta',
            'hardened_clay_stained_lime'       : 'lime_terracotta',
            'hardened_clay_stained_magenta'    : 'magenta_terracotta',
            'hardened_clay_stained_orange'     : 'orange_terracotta',
            'hardened_clay_stained_pink'       : 'pink_terracotta',
            'hardened_clay_stained_purple'     : 'purple_terracotta',
            'hardened_clay_stained_red'        : 'red_terracotta',
            'hardened_clay_stained_silver'     : 'light_gray_terracotta',
            'hardened_clay_stained_white'      : 'white_terracotta',
            'hardened_clay_stained_yellow'     : 'yellow_terracotta',
            'log_acacia'                       : 'acacia_log',
            'log_big_oak'                      : 'dark_oak_log',
            'log_birch'                        : 'birch_log',
            'log_jungle'                       : 'jungle_log',
            'log_oak'                          : 'oak_log',
            'log_spruce'                       : 'spruce_log',
            'nether_brick'                     : 'nether_bricks',
            'planks_acacia'                    : 'acacia_planks',
            'planks_big_oak'                   : 'dark_oak_planks',
            'planks_birch'                     : 'birch_planks',
            'planks_jungle'                    : 'jungle_planks',
            'planks_oak'                       : 'oak_planks',
            'planks_spruce'                    : 'spruce_planks',
            'red_sandstone_carved'             : 'chiseled_red_sandstone',
            'red_sandstone_smooth'             : 'red_sandstone_top',
            'sandstone_carved'                 : 'chiseled_sandstone',
            'sandstone_smooth'                 : 'sandstone_top',
            'stone_andesite'                   : 'andesite',
            'stone_andesite_smooth'            : 'polished_andesite',
            'stone_diorite'                    : 'diorite',
            'stone_diorite_smooth'             : 'polished_diorite',
            'stone_granite'                    : 'granite',
            'stone_granite_smooth'             : 'polished_granite',
            'stonebrick'                       : 'stone_bricks',
            'stonebrick_carved'                : 'chiseled_stone_bricks',
            'stonebrick_cracked'               : 'cracked_stone_bricks',
            'stonebrick_mossy'                 : 'mossy_stone_bricks',
    ]

    VanillaTextureRefStep() {
        super('vanilla-texture-ref-flatten')
    }

    @Override
    boolean matches(final String relPath) {
        return relPath ==~ ~/^assets\/[^\/]+\/(?:blockstates|models)\/.+\.json$/
    }

    @Override
    void apply(final MigrationContext ctx, final String relPath, final File file) {
        final String original = file.text
        String rewritten = rewrite(original, BLOCKS_REF, 'block', true)
        rewritten = rewrite(rewritten, ITEMS_REF, 'item', false)
        if (rewritten != original) {
            ctx.write(relPath, rewritten)
        }
    }

    private static String rewrite(final String content, final Pattern pattern,
            final String newPrefix, final boolean applyVanillaRenames) {
        final Matcher m = pattern.matcher(content)
        final StringBuffer out = new StringBuffer()
        while (m.find()) {
            final String name = m.group(1)
            final String renamed = applyVanillaRenames
                    ? VANILLA_RENAMES.getOrDefault(name, name)
                    : name
            m.appendReplacement(out, Matcher.quoteReplacement(
                    "\"minecraft:${newPrefix}/${renamed}\""))
        }
        m.appendTail(out)
        return out.toString()
    }
}
