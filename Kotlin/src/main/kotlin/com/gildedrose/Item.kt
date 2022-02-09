package com.gildedrose

open class Item(var name: String, var sellIn: Int, var quality: Int) {
    override fun toString(): String {
        return this.name + ", " + this.sellIn + ", " + this.quality
    }
}

fun Item.selectProcessor(): ItemProcessor =
    when(name) {
        "Aged Brie" -> AgedBrieProcessor
        "Sulfuras, Hand of Ragnaros" -> SulfurasProcessor
        "Backstage passes to a TAFKAL80ETC concert" -> BackstageProcessor
        "Conjured" -> ConjuredProcessor
        else -> DefaultProcessor()
    }

fun Item.process(): Item = this.selectProcessor().processItem(this)