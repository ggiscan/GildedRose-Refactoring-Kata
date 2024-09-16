package com.gildedrose

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class GildedRoseTest {
    private val dayRegex = """^-+ day (.*?) -+$""".toRegex()

    @Test
    fun test30Days() {
        val items = arrayOf(Item("+5 Dexterity Vest", 10, 20), //
            Item("Aged Brie", 2, 0), //
            Item("Elixir of the Mongoose", 5, 7), //
            Item("Sulfuras, Hand of Ragnaros", 0, 80), //
            Item("Sulfuras, Hand of Ragnaros", -1, 80),
            Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
            Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
            Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
            // this conjured item does not work properly yet
            Item("Conjured Mana Cake", 3, 6))

        val app = GildedRose(items)
        readResource("/stdout.gr")
            .drop(1)
            .chunked(12) { lines ->
                grabGildedRoseForDay(lines)
            }
            .forEach { (dailyGuildedRose, day) ->
                compare(dailyGuildedRose, app, day)
                app.updateQuality()
            }
    }

    private fun compare(dailyGuildedRose: GildedRose, app: GildedRose, day: Int) {
        dailyGuildedRose.items.zip(app.items).forEach{(expected, actual) ->
            //as we cannot transform the Item in data class, we need to do the hard work here
            Assertions.assertEquals(expected.name, actual.name, "name different for day $day")
            Assertions.assertEquals(expected.quality, actual.quality, "quality different for day $day for item '${expected.name}'")
            Assertions.assertEquals(expected.sellIn, actual.sellIn, "sellIn different for day $day for item '${expected.name}'")
        }
    }

    private fun grabGildedRoseForDay(lines: List<String>): Pair<GildedRose, Int> {
        val dayStr = dayRegex.find(lines[0])?.groupValues?.last()?: fail("Unexpected format of golden file")
        try {
            val day = dayStr.toInt()
            val items = lines.drop(2).dropLast(1).map { line ->
                val itemLine = line.split(',')
                when(itemLine.size) {
                    3 -> {
                        val (name, sellIn, quality) = itemLine
                        Item(name.trim(), sellIn.trim().toInt(), quality.trim().toInt())
                    }
                    4 -> {
                        //support name that has `,` such as `Sulfuras, Hand of Ragnaros`
                        val (name1, name2, sellIn, quality) = itemLine
                        Item(("$name1,$name2").trim(), sellIn.trim().toInt(), quality.trim().toInt())
                    }
                    else -> fail("Unexpected line format, found ${itemLine.size} items on a row")
                }

            }
            return GildedRose(items.toTypedArray()) to day
        } catch (e: NumberFormatException) {
            fail("Parse exception", e)
        }
    }

    private fun readResource(path: String): Sequence<String> {
        return object {}.javaClass.getResource(path)?.openStream()?.bufferedReader()?.lineSequence() ?: fail("Cannot open file")
    }
}


